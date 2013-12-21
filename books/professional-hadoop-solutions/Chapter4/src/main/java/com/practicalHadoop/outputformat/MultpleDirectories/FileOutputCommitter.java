/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.practicalHadoop.outputformat.MultpleDirectories;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.JobStatus;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.TaskAttemptID;
import org.apache.hadoop.util.StringUtils;

public class FileOutputCommitter extends org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter {

	private static final Log LOG = LogFactory.getLog(FileOutputCommitter.class);
	private static Map<String,FileOutputCommitter> committers = new HashMap<String, FileOutputCommitter>();

	/**
	 * Temporary directory name 
	 */
	protected static final String TEMP_DIR_NAME = "_temporary";
	public static final String SUCCEEDED_FILE_NAME = "_SUCCESS";
	static final String SUCCESSFUL_JOB_OUTPUT_DIR_MARKER =
		"mapreduce.fileoutputcommitter.marksuccessfuljobs";
	private static final String MULTIPLE_OUTPUTS = "mapreduce.multipleoutputs";

	private FileSystem outputFileSystem = null;
	private Path outputPath = null;
	private Path workPath = null;
	private List<String> pathNames = new ArrayList<String>();
	private boolean fake = true;

	/**
	 * Create a file output committer
	 * @param outputPath the job's output path
	 * @param context the task's context
	 * @throws IOException
	 */
	public FileOutputCommitter(Path outputPath, 
			TaskAttemptContext context) throws IOException {
		super(outputPath, context);
		Job job = new Job(context.getConfiguration());
		String outputDirectories = job.getConfiguration().get(MULTIPLE_OUTPUTS, "");
		if (outputDirectories != null) {
			StringTokenizer st = new StringTokenizer(outputDirectories, " ");
			while (st.hasMoreTokens()) {
				pathNames.add(st.nextToken());
			}
		}
		if (outputPath != null) {
			this.outputPath = outputPath;
			outputFileSystem = outputPath.getFileSystem(context.getConfiguration());
			workPath = new Path(outputPath,
					(FileOutputCommitter.TEMP_DIR_NAME + Path.SEPARATOR +
							"_" + context.getTaskAttemptID().toString()
					)).makeQualified(outputFileSystem);
			for(String p : pathNames){
				if(outputPath.toString().endsWith(p)){
					committers.put(p, this);
					fake = false;
					break;
				}
			}
		}
	}

	/**
	 * Create the temporary directory that is the root of all of the task 
	 * work directories.
	 * @param context the job's context
	 */
	public void setupJob(JobContext context) throws IOException {
		if (outputPath != null) {
			Path tmpDir = new Path(outputPath, FileOutputCommitter.TEMP_DIR_NAME);
			FileSystem fileSys = tmpDir.getFileSystem(context.getConfiguration());
			if (!fileSys.mkdirs(tmpDir)) {
				LOG.error("Mkdirs failed to create " + tmpDir.toString());
			}
		}
	}

	private static boolean shouldMarkOutputDir(Configuration conf) {
		return conf.getBoolean(SUCCESSFUL_JOB_OUTPUT_DIR_MARKER, 
				true);
	}

	// Mark the output dir of the job for which the context is passed.
	private void markOutputDirSuccessful(JobContext context, String path)
	throws IOException {
		if (outputPath != null) {
			Path p = (path == null) ? outputPath : new Path(outputPath, path);
			LOG.warn("Mark Output success " + p);
			FileSystem fileSys = outputPath.getFileSystem(context.getConfiguration());
			if (fileSys.exists(outputPath)) {
				// create a file in the folder to mark it
				Path filePath = new Path(p, SUCCEEDED_FILE_NAME);
				fileSys.create(filePath).close();
			}
		}
	}

	/**
	 * Delete the temporary directory, including all of the work directories.
	 * This is called for all jobs whose final run state is SUCCEEDED
	 * @param context the job's context.
	 */
	public void commitJob(JobContext context) throws IOException {
		// This is invoked in cleanup phase once need to run it for all directories
		pathNames.add(null);
		for (String path : pathNames) {
			LOG.warn("Cleaning up context " + path + " ...");
			// delete the _temporary folder
			cleanupJob(context, path);
			// check if the o/p dir should be marked
			if (shouldMarkOutputDir(context.getConfiguration())) {
				// create a _success file in the o/p folder
				LOG.warn("Marking output directory successful...");
				markOutputDirSuccessful(context, path);
			}
		}
	}

	public void cleanupJob(JobContext context, String path) throws IOException {
		if (outputPath != null) {
			Path p = (path == null) ? outputPath : new Path(outputPath, path);
			Path tmpDir = new Path(p, FileOutputCommitter.TEMP_DIR_NAME);
			LOG.warn("Cleanup on " + tmpDir);
			FileSystem fileSys = tmpDir.getFileSystem(context
					.getConfiguration());
			if (fileSys.exists(tmpDir)) {
				fileSys.delete(tmpDir, true);
			}
		} else {
			LOG.warn("Output path is null in cleanup");
		}
	}

	/**
	 * Delete the temporary directory, including all of the work directories.
	 * @param context the job's context
	 * @param state final run state of the job, should be FAILED or KILLED
	 */
	@Override
	public void abortJob(JobContext context, JobStatus.State state)
	throws IOException {
		// This is invoked in cleanup phase once need to run it for all directories
		pathNames.add(null);
		for (String path : pathNames) {
			cleanupJob(context, path);
		}
	}

	/**
	 * No task setup required.
	 */
	@Override
	public void setupTask(TaskAttemptContext context) throws IOException {
		// FileOutputCommitter's setupTask doesn't do anything. Because the
		// temporary task directory is created on demand when the 
		// task is writing.
	}

	/**
	 * Move the files from the work directory to the job output directory
	 * @param context the task context
	 */
	public void commitTask(TaskAttemptContext context) throws IOException {
		if (!fake || (committers.size() == 0)) {
			TaskAttemptID attemptId = context.getTaskAttemptID();
			if (workPath != null) {
				context.progress();
				if (outputFileSystem.exists(workPath)) {
					// Move the task outputs to their final place
					moveTaskOutputs(context, outputFileSystem, outputPath, workPath);
					// Delete the temporary task-specific output directory
					if (!outputFileSystem.delete(workPath, true)) {
						LOG.warn("Failed to delete the temporary output"
								+ " directory of task: " + attemptId + " - "
								+ workPath);
					}
					LOG.info("Saved output of task '" + attemptId + "' to "
							+ outputPath);
				}
			}
			//		  commitJob(context);
		}
		else{
			for(FileOutputCommitter c : committers.values()){
				c.commitTask(context);
			}    	    	
		}
	}

	/**
	 * Move all of the files from the work directory to the final output
	 * @param context the task context
	 * @param fs the output file system
	 * @param jobOutputDir the final output direcotry
	 * @param taskOutput the work path
	 * @throws IOException
	 */
	private void moveTaskOutputs(TaskAttemptContext context,
			FileSystem fs,
			Path jobOutputDir,
			Path taskOutput) 
	throws IOException {
		TaskAttemptID attemptId = context.getTaskAttemptID();
		context.progress();
		if (fs.isFile(taskOutput)) {
			Path finalOutputPath = getFinalPath(jobOutputDir, taskOutput, 
					workPath);
			if (!fs.rename(taskOutput, finalOutputPath)) {
				if (!fs.delete(finalOutputPath, true)) {
					throw new IOException("Failed to delete earlier output of task: " + 
							attemptId);
				}
				if (!fs.rename(taskOutput, finalOutputPath)) {
					throw new IOException("Failed to save output of task: " + 
							attemptId);
				}
			}
			LOG.debug("Moved " + taskOutput + " to " + finalOutputPath);
		} else if(fs.getFileStatus(taskOutput).isDir()) {
			FileStatus[] paths = fs.listStatus(taskOutput);
			Path finalOutputPath = getFinalPath(jobOutputDir, taskOutput, workPath);
			fs.mkdirs(finalOutputPath);
			if (paths != null) {
				for (FileStatus path : paths) {
					moveTaskOutputs(context, fs, jobOutputDir, path.getPath());
				}
			}
		}
	}

	/**
	 * Delete the work directory
	 */
	@Override
	public void abortTask(TaskAttemptContext context) {
		if (!fake || (committers.size() == 0)) {
			try {
				if (workPath != null) {
					context.progress();
					outputFileSystem.delete(workPath, true);
				}
			} catch (IOException ie) {
				LOG.warn("Error discarding output"
						+ StringUtils.stringifyException(ie));
			}
		}
		else{
			for(FileOutputCommitter c : committers.values()){
				c.abortTask(context);
			}    	    	
		}
	}

	/**
	 * Find the final name of a given output file, given the job output directory
	 * and the work directory.
	 * @param jobOutputDir the job's output directory
	 * @param taskOutput the specific task output file
	 * @param taskOutputPath the job's work directory
	 * @return the final path for the specific output file
	 * @throws IOException
	 */
	private Path getFinalPath(Path jobOutputDir, Path taskOutput, 
			Path taskOutputPath) throws IOException {
		URI taskOutputUri = taskOutput.toUri();
		URI relativePath = taskOutputPath.toUri().relativize(taskOutputUri);
		if (taskOutputUri == relativePath) {
			throw new IOException("Can not get the relative path: base = " + 
					taskOutputPath + " child = " + taskOutput);
		}
		if (relativePath.getPath().length() > 0) {
			return new Path(jobOutputDir, relativePath.getPath());
		} else {
			return jobOutputDir;
		}
	}

	/**
	 * Did this task write any files in the work directory?
	 * @param context the task's context
	 */
	@Override
	public boolean needsTaskCommit(TaskAttemptContext context
	) throws IOException {
		return workPath != null && outputFileSystem.exists(workPath);
	}

	/**
	 * Get the directory that the task should write results into
	 * @return the work directory
	 * @throws IOException
	 */
	public Path getWorkPath() throws IOException {
		return workPath;
	}
}

