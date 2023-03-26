import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * * Event handler of this logging output component.
 * * We can use both Logger and BufferedWriter to achieve this function.
 * Output.log is the result of Logger, and Output2.log is the result of BufferedWriter.
 * * Write all the output into a log file
 */

public class LoggingOutput implements Observer {
    FileHandler fileHandler;
    Logger logger;

    // Create a FileWriter object to write in the file
    BufferedWriter bufferedWriter;
    /**
     * Constructs a logging output component. A new client output component subscribes to show events
     * at the time of creation.
     * Then, when we output something in console, we can also write into a log file.
     */
    public LoggingOutput() throws IOException {
        // Subscribe to SHOW Event, just as in ClientOutPut
        EventBus.subscribeTo(EventBus.EV_SHOW, this);
        fileHandler = new FileHandler("Output.log");
        this.fileHandler.setFormatter(new SimpleFormatter());
        this.logger = Logger.getLogger(getClass().toString());
        this.logger.setUseParentHandlers(false);
        this.logger.addHandler(this.fileHandler);

        // Or use bufferedWriter
        bufferedWriter = new BufferedWriter(new FileWriter("Output2.log"));
    }

    /**
     * Write the output into the log file
     * @param event an event object. (caution: not to be directly referenced)
     * @param param a parameter object of the event. (to be cast to appropriate data type)
     */
    public void update(Observable event, Object param) {
        logger.info(param.toString());
        try {
            bufferedWriter.flush();
            bufferedWriter.write(param.toString() + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
