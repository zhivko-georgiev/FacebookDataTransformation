import java.util.TimeZone;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class EETRule extends TestWatcher {

	private TimeZone origDefault = TimeZone.getDefault();

	@Override
	protected void starting(Description description) {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Sofia"));
	}

	@Override
	protected void finished(Description description) {
		TimeZone.setDefault(origDefault);
	}
}
