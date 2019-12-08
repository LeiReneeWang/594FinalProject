package edu.upenn.cit594.logging;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author atu
 */

public class Logger {

    private static String filename;

    private Logger(String filename) {
        this.filename = filename;
    }

    private static Logger instance;

    public static synchronized Logger getInstance() {
        if(instance == null) {
            throw new AssertionError("You need to call init first");
        }
        return instance;
    }

    public static synchronized void init(String filename) {
        instance = new Logger(filename);
    }

    public static void writeLog (String log) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filename, true));
            out.write(log + "\n");
            out.close();
        } catch (IOException e) {
            System.out.println("Unable to write logs into the log file. Check the filename and permission");
            System.exit(0);
        }
    }
}
