package utils;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;

public class Watcher extends Thread {
    private final WatchService watcher = FileSystems.getDefault().newWatchService();

    private WatchKey key;
    private String fileName;
    private String dir;

    public Watcher(String dir, String fileName) throws IOException {
        this.dir = dir;
        final WatchKey watchKey = Paths.get(dir).register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
        this.fileName = fileName;
    }


    @Override
    public void run() {
        while (!Objects.equals(Printer.getLastCommand(), "stop")) {
            final WatchKey wk;
            try {
                wk = watcher.take();

                for (WatchEvent<?> event : wk.pollEvents()) {
                    final Path changed = (Path) event.context();
                    if (changed.endsWith(fileName)) {
                        System.out.println("\tInput file has changed");
                        Printer.updateMap(String.format("%s\\%s", dir, fileName));
                    }
                }
                wk.reset();
                sleep(2500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
