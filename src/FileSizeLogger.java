import java.io.IOException;
import java.nio.file.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FileSizeLogger {

    private static final Logger logger = Logger.getLogger("FileLogger");

    public static void main(String[] args) {
        System.out.println("started");

        if (args.length != 2) {
            System.out.println("Usage: java FileWatcher <file-to-watch> <log-file>");
            return;
        }

        String filePath = args[0];
        String logFilePath = args[1];

        System.out.println(filePath);
        System.out.println(logFilePath);

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
            System.out.println(watchService);
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                WatchKey key;

                System.out.println("hugo-1");

                try {
                    key = watchService.take();
                } catch (InterruptedException ex) {
                    System.out.println("hugo-2");
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    System.out.println("huffi");
                    WatchEvent.Kind<?> kind = event.kind();
                    System.out.println("hugo0");
                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();
                    System.out.println("hugo");
                    if (fileName.endsWith(Paths.get(filePath).getFileName())) {
                        System.out.println("hugo2");
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
            System.out.println("File size of " + filePath + ": " + fileSize + " bytes");
            logger.info("File size of " + filePath + ": " + fileSize + " bytes");
        } catch (IOException e) {
            System.err.println("Error getting file size: " + e.getMessage());
        }
    }
}
