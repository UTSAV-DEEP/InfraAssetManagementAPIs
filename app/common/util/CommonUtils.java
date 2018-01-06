package common.util;

import exceptions.ApplicationException;
import org.apache.http.HttpStatus;

import javax.inject.Inject;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.concurrent.CompletionException;

public class CommonUtils {

    public static Timestamp stringToTimestamp(String datetimeStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            Date parsedDate = dateFormat.parse(datetimeStr);
            return new Timestamp(parsedDate.getTime());
        } catch(Exception e) {
            throw new CompletionException(new ApplicationException(
                    HttpStatus.SC_BAD_REQUEST, "Invalid Datetime format. Expected format is 'yyyy-MM-dd hh:mm'"));
        }
    }
}
