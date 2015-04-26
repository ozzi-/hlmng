package log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class FormatterS extends Formatter {
	private static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

	@Override
	public String format(LogRecord record) {
		Date date = new Date(record.getMillis());
        return "-----------------------\n"
        		+formatter.format(date)+ " | "
        		+ record.getLevel() + "  |  "
                + record.getMessage() + "\n";
	}
}
