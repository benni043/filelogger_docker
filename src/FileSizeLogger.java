import java.io.IOException;
import java.nio.file.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FileSizeLogger {

    private static final Logger logger = Logger.getLogger("FileLogger");
    private static long lastLoggedFileSize = -1;  // Track the last logged file size

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java FileWatcher <file-to-watch> <log-file>");
            return;
        }

        String filePath = args[0];
        String logFilePath = args[1];

        setupLogger(logFilePath);
        watchFile(filePath);
    }

    private static void setupLogger(String logFilePath) {
        try {
            FileHandler fileHandler = new FileHandler(logFilePath, true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            System.err.println("Failed to set up logger: " + e.getMessage());
        }
    }

    private static void watchFile(String filePath) {
        Path path = Paths.get(filePath).getParent();
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                WatchKey key;

                try {
                    key = watchService.take();
                } catch (InterruptedException ex) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();
                    if (fileName.endsWith(Paths.get(filePath).getFileName())) {
                        // Add a small delay to allow the file write to finish
                        try {
                            Thread.sleep(100);  // 100ms delay to debounce the event
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        logFileSize(filePath);
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error watching file: " + e.getMessage());
        }
    }

    private static void logFileSize(String filePath) {
        try {
            long fileSize = Files.size(Paths.get(filePath));

            // Only log if the file size has changed to prevent duplicate logging
            if (fileSize != lastLoggedFileSize) {
                logger.info("File size of " + filePath + ": " + fileSize + " bytes");
                lastLoggedFileSize = fileSize;
            }
        } catch (IOException e) {
            System.err.println("Error getting file size: " + e.getMessage());
        }
    }
}
