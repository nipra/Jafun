package com.packtpub.java7.concurrency.chapter5.recipe03.task;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * Task that process a folder. Throw a new FolderProcesor task for each
 * subfolder. For each file in the folder, it checks if the file has the extension
 * it's looking for. If it's the case, it add the file name to the list of results.
 *
 */
public class FolderProcessor extends RecursiveTask<List<String>> {

	/**
	 * Serial Version of the class. You have to add it because the 
	 * ForkJoinTask class implements the Serializable interfaces
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Path of the folder this task is going to process
	 */
	private String path;
	
	/**
	 * Extension of the file the task is looking for
	 */
	private String extension;
	
	/**
	 * Constructor of the class
	 * @param path Path of the folder this task is going to process
	 * @param extension Extension of the files this task is looking for
	 */
	public FolderProcessor (String path, String extension) {
		this.path=path;
		this.extension=extension;
	}
	
	/**
	 * Main method of the task. It throws an additional FolderProcessor task
	 * for each folder in this folder. For each file in the folder, it compare
	 * its extension with the extension it's looking for. If they are equals, it
	 * add the full path of the file to the list of results
	 */
	@Override
	protected List<String> compute() {
		List<String> list=new ArrayList<>();
		List<FolderProcessor> tasks=new ArrayList<>();
		
		File file=new File(path);
		File content[] = file.listFiles();
		if (content != null) {
			for (int i = 0; i < content.length; i++) {
				if (content[i].isDirectory()) {
					// If is a directory, process it. Execute a new Task
					FolderProcessor task=new FolderProcessor(content[i].getAbsolutePath(), extension);
					task.fork();
					tasks.add(task);
				} else {
					// If is a file, process it. Compare the extension of the file and the extension
					// it's looking for
					if (checkFile(content[i].getName())){
						list.add(content[i].getAbsolutePath());
					}
				}
			}
			
			// If the number of tasks thrown by this tasks is bigger than 50, we write a message
			if (tasks.size()>50) {
				System.out.printf("%s: %d tasks ran.\n",file.getAbsolutePath(),tasks.size());
			}
			
			// Include the results of the tasks
			addResultsFromTasks(list,tasks);
		}
		return list;
	}

	/**
	 * Add the results of the tasks thrown by this task to the list this
	 * task will return. Use the join() method to wait for the finalization of
	 * the tasks
	 * @param list List of results
	 * @param tasks List of tasks
	 */
	private void addResultsFromTasks(List<String> list,
			List<FolderProcessor> tasks) {
		for (FolderProcessor item: tasks) {
			list.addAll(item.join());
		}
	}

	/**
	 * Checks if a name of a file has the extension the task is looking for
	 * @param name name of the file
	 * @return true if the name has the extension or false otherwise
	 */
	private boolean checkFile(String name) {
		if (name.endsWith(extension)) {
			return true;
		}
		return false;
	}

	
}
