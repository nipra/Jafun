package com.practicalHadoop.s3client;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class FolderListing {
	
	private int nfiles;
	private List<FileDescriptor> files;

	public FolderListing(){
		nfiles = 0;
		files = new LinkedList<FileDescriptor>();
	}

	public int getNfiles() {
		return nfiles;
	}

	public void addFileCount(int nfiles) {
		this.nfiles += nfiles;
	}

	public List<FileDescriptor> getFiles() {
		return files;
	}

	public void addFiles(List<FileDescriptor> files) {
		files.addAll(files);
	}

	public void addFile(FileDescriptor file) {
		files.add(file);
	}
	
	public static class FileDescriptor{
		
		private String name;
		private long size;
		private Date modified;
		
		public FileDescriptor(String name, long size, Date modified)
		{
			this.name = name;
			this.size = size;
			this.modified = modified;
		}

		public String getName() {
			return name;
		}

		public long getSize() {
			return size;
		}

		public Date getModified() {
			return modified;
		}
	}
}
