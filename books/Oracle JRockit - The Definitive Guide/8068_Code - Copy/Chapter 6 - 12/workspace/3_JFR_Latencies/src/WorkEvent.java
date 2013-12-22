import com.oracle.jrockit.jfr.EventDefinition;
import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.TimedEvent;

@EventDefinition(path="log/work", name = "Work", description="A piece of work executed.", stacktrace=true, thread=true)
public class WorkEvent extends TimedEvent {
	public WorkEvent(EventToken eventToken) {
		super(eventToken);
	}
}
