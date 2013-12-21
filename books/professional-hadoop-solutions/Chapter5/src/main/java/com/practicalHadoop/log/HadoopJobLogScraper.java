package com.practicalHadoop.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

public class HadoopJobLogScraper {
	private String _trackerURL = null;

	public static void main(String[] args) throws IOException{
		if (args.length != 2){
			System.err.println("usage: <Job Tracker URL>, <job id>");
		}
		String jobId = args[1];
		String trackerURL = args[0];
		
		HadoopJobLogScraper scraper = new HadoopJobLogScraper(trackerURL);
		scraper.scrape(jobId, JobType.MAP);
		scraper.scrape(jobId, JobType.REDUCE);
		System.out.println("done");
	}

	public enum JobType{
		MAP("map"), REDUCE("reduce");
		private String urlName;
		private JobType(String urlName){
			this.urlName = urlName;
		}
		
		public String getUrlName(){
			return urlName;
		}
	}

	private Pattern taskDetailsUrlPattern = Pattern.compile("<a href=\"(taskdetails\\.jsp.*?)\">(.*?)</a>");
	private Pattern logUrlPattern = Pattern.compile("<a href=\"([^\"]*)\">All</a>");

	public HadoopJobLogScraper (String trackerURL){
		_trackerURL = trackerURL;
	}
	
	public void scrape(String jobId, JobType type) throws IOException{
		System.out.println("scraping " + jobId + " - " + type);

		String jobTasksUrl = _trackerURL  + "/jobtasks.jsp?jobid=" + jobId +
				"&type=" + type.getUrlName() + "&pagenum=1";
		String jobTasksHtml = IOUtils.toString(new URL(jobTasksUrl).openStream());
		Matcher taskDetailsUrlMatcher = taskDetailsUrlPattern.matcher(jobTasksHtml);
		
		File dir = new File(jobId);
		if (!dir.exists()){
			dir.mkdir();
		}
		
		File outFile = new File(dir, type.getUrlName());
		BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
		
		while (taskDetailsUrlMatcher.find()){
			out.write(taskDetailsUrlMatcher.group(2) + ":\n");
			String taskDetailsUrl = new String(_trackerURL  + "/" +  taskDetailsUrlMatcher.group(1));
			String taskDetailsHtml = IOUtils.toString(new URL(taskDetailsUrl).openStream());
			Matcher logUrlMatcher = logUrlPattern.matcher(taskDetailsHtml);
			while (logUrlMatcher.find()){
				String logUrl = logUrlMatcher.group(1) + "&plaintext=true&filter=stdout";
				out.write(IOUtils.toString(new URL(logUrl).openStream()));
			}
		}
		
		out.flush();
		out.close();
	}
}