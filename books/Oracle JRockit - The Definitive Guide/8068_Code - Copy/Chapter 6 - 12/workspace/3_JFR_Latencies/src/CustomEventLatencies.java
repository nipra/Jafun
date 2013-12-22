import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.Producer;

/**
 * Demonstrates how to create custom events.
 * 
 * @author Marcus Hirt
 */
public class CustomEventLatencies {
	static EventToken LOG_EVENT_TOKEN;
	static EventToken WORK_EVENT_TOKEN;
	static Producer PRODUCER;
	private final static String PRODUCER_URI = "http://www.example.com/logdemo/";
	static {
		registerProducer();
		// Uncomment next line to register a recording with our events enabled by default.
		// enableAll();
	}

	/**
	 * Registers our producers, and our different events.
	 */
	private static void registerProducer() {
		try {
			PRODUCER = new Producer("Log Producer (Demo)",
					"A demo event producer for the demo logger.", PRODUCER_URI);
			LOG_EVENT_TOKEN = PRODUCER.addEvent(LogEvent.class);
			WORK_EVENT_TOKEN = PRODUCER.addEvent(WorkEvent.class);
			PRODUCER.register();
		} catch (Exception e) {
			// Add proper exception handling.
			e.printStackTrace();
		}
	}

	/**
	 * An example on how to creates a continuous recording and enable all the
	 * events for our producer. This can be safely skipped, and the events
	 * enabled in our template instead.
	 */
	/*
	private static void enableAll() {
		try {
			FlightRecorderClient fr = new FlightRecorderClient();
			FlightRecordingClient rec = fr.createRecordingObject("tmp");
			for (CompositeData pd : fr.getProducers()) {
				if (!PRODUCER_URI.equals(pd.get("uri")))
					continue;
				CompositeData events[] = (CompositeData[]) pd.get("events");
				// Go through all registered events and enable them
				for (CompositeData d : events) {
					int id = (Integer) d.get("id");
					rec.setEventEnabled(id, true);
					rec.setStackTraceEnabled(id, true);
					rec.setThreshold(id, 200);
					rec.setPeriod(id, 5);
					System.out.println("Enabled event " + d.get("name"));
				}
			}
		} catch (Exception e) {
			// Add proper exception handling.
			e.printStackTrace();
		}
	}
	*/
	
	/**
	 * Main program entry.
	 */
	public static void main(String[] args) throws InterruptedException {
		Thread.sleep(2000);
		Thread[] threads;
		threads = new Thread[20];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new WorkerThread(10000000);
			threads[i].start();
		}
	}
}
