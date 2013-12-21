package com.practicalHadoop.lucene.cache;

import com.practicalHadoop.lucene.initializer.LuceneHBaseInitializer;


public class CacheUpdater implements Runnable {

	private boolean _completed = false;

	@Override
	public void run() {
		while (!_completed) {
			try {
				Thread.sleep(LuceneHBaseInitializer.getUpdaterTimeout());
				LuceneIndexCache.refresh();
				LuceneDocumentCache.refresh();
				LuceneDocumentNormsCache.refresh();
			} catch (Throwable e) {}
		}
	}
	
	public void complete(){
		_completed = true;
	}

}
