package edu.upenn.cit594.logging;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

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

    public static void writeLogs (List<String> logs) {
        Path file = Paths.get(filename);
        try {
            Files.write(file, logs, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("Unable to write logs into the log file. Check the filename and permission");
            System.exit(0);
        }
    }
}
