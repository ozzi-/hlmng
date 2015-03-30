package hlmng.resource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class FormatterS extends Formatter {

	@Override
	public String format(LogRecord record) {
		Date date = new Date(record.getMillis());
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		
        return "-----------------------\n"+formatter.format(date)+ "\n"
        		+ record.getLevel() + "  :  "
                + record.getMessage() + "\n";
	}
}
