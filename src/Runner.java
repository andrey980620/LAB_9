import utils.Printer;
import utils.Watcher;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

/**
 * Created by andrey on 16.12.2016.
 */
public class Runner {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner in = new Scanner(System.in);

        Printer primary = new Printer("src\\out1.txt");
        Printer secondary = new Printer("src\\out2.txt");
        Watcher watcher = new Watcher("src", "Tasks.txt");

        watcher.start();
        Printer.updateMap("src\\Tasks.txt");
        primary.start();
        secondary.start();

        for (; ; ) {
            String command = in.nextLine();
            Printer.giveCommand(command);
            if (Objects.equals(command, "stop")) {
                primary.finish();
                secondary.finish();
                break;
            }
        }
        watcher.join();
    }
}
