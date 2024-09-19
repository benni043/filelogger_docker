import java.io.*;
import java.nio.file.*;

public class Main {

    public static void main(String[] args) throws Exception {
        File f = new File(args[0]);
        WatchService ws = FileSystems.getDefault().newWatchService();
        f.toPath().getParent().register(ws, StandardWatchEventKinds.ENTRY_MODIFY);

        while (true) {
            for (WatchEvent<?> e : ws.take().pollEvents()) {
                if (e.context().toString().equals(f.getName())) {
                    try (FileWriter w = new FileWriter("file.log", true)) {
                        w.write(f.length() + " bytes\n");
                    }
                }
            }
            ws.take().reset();
        }
    }
}
