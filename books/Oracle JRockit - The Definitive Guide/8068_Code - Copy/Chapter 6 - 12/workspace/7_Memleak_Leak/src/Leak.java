/*
 * Copyright (c) 2002 by BEA Systems, Inc. All Rights Reserved.
 */
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;

/**
 * Simple MemLeak demo.
 */
public class Leak {
	public static class DemoObject {
		private long position;
		private long myField1 = 1;
		private long myField2 = 2;
		private char[] chunk = new char[255];

		public DemoObject(int pos) {
			position = pos;
		}

		public int hashCode() {
			return (int) position;
		}

		public boolean equals(Object o) {
			return (o instanceof DemoObject) && (o.hashCode() == position);
		}

		public long getMyField1() {
			return myField1;
		}

		public long getMyField2() {
			return myField2;
		}

		public char[] getChunk() {
			return chunk;
		}
	}

	private static class AllocThread extends Thread {
		public void run() {
			while (true) {
				// Alloc transients
				List<Object> junkList = new ArrayList<Object>();
				for (int i = 0; i < 1000; i++) {
					junkList.add(new Object());
					for (int j = 0; j < 10; j++)
						// Keep busy yielding for a little
						// while...
						Thread.yield();
				}
			}
		}
	}

	private static class DemoThread extends Thread {
		private Hashtable<DemoObject, String> table;
		private int leakspeed;

		DemoThread(Hashtable<DemoObject, String> table, int leakspeed) {
			this.table = table;
			this.leakspeed = leakspeed;
		}

		public void run() {
			int total = 0;
			while (true) {
				for (int i = 0; i <= 100; i++)
					put(total + i);

				for (int i = 0; i < 101 - leakspeed; i++)
					remove(total + i);

				total += 101;

				for (int i = 0; i < 10; i++) {
					// Keep busy yielding for a little while...
					Thread.yield(); 
				}
				try {
					Thread.sleep(70);
				} catch (InterruptedException e) {
				}
			}
		}

		private void put(int n) {
			table.put(new DemoObject(n), "foo");
		}

		private String remove(int n) {
			return table.remove(new DemoObject(n));
		}
	}

	public static void main(String[] args) {
		Hashtable<DemoObject, String> h = new Hashtable<DemoObject, String>();
		Thread[] threads;
		int leakspeed = 1;

		if (args.length < 1 || args.length > 2) {
			threads = new Thread[2];
		} else {
			threads = new Thread[Integer.parseInt(args[0])];
		}
		if (args.length == 2) {
			leakspeed = Integer.parseInt(args[1]);
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new DemoThread(h, leakspeed);
			threads[i].start();
		}
		startAllocThread();
	}
	
	private static void startAllocThread() {
		new AllocThread().start();		
	}

	public static void startLeak() {
		Hashtable<DemoObject, String> h = new Hashtable<DemoObject, String>();
		Thread t = new DemoThread(h, 1);
		t.start();
	}
	
	public static void startNonLeak() {
		Hashtable<DemoObject, String> h = new Hashtable<DemoObject, String>();
		Thread t = new DemoThread(h, 0);
		t.start();		
	}
}