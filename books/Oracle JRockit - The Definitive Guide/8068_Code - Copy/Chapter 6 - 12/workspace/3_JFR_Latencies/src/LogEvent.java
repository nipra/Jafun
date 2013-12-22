import com.oracle.jrockit.jfr.EventDefinition;
import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.TimedEvent;
import com.oracle.jrockit.jfr.ValueDefinition;

@EventDefinition(path="log/logentry", name = "Log Entry", description="A log call in the custom logger.", stacktrace=true, thread=true)
public class LogEvent extends TimedEvent {
	@ValueDefinition(name="Message", description="The logged message.")
	private String text;

	public LogEvent(EventToken eventToken, String text) {
		super(eventToken);
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
}
