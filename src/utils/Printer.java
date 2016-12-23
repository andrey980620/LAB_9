package utils;

import java.io.*;
import java.util.*;

public class Printer extends Thread {
    private String outputFileName;
    private File outFile;
    private BufferedWriter out;
    static HashMap<String, Integer> toPrint = new HashMap<>();
    private static volatile Iterator<Map.Entry<String, Integer>> it;
    private static String command;

    private static void print(String task) {
        Printer.toPrint.put(task, 1);
    }

    public static void giveCommand(String message) {
        command = message;
    }

    public static String getLastCommand() {
        return command;
    }

    public static void updateMap(String source) throws FileNotFoundException {
        Scanner in = new Scanner(new File(source));
        while (in.hasNextLine()) {
            String temp = in.nextLine();
            if (!toPrint.containsKey(temp))
                Printer.print(temp);
        }
        it = toPrint.entrySet().iterator();
    }

    public Printer(String fileName) throws IOException {
        outputFileName = fileName;
        outFile = new File(outputFileName);
        outFile.getParentFile().mkdirs();
        outFile.createNewFile();
        out = new BufferedWriter(new FileWriter(outputFileName, true));
        out.write("Printing started " + System.currentTimeMillis());
        out.write("\r\n");
    }

    @Override
    public void run() {

        while (!Objects.equals(command, "stop")) {
            try {
                if (it.hasNext()) {
                    Map.Entry<String, Integer> temp = it.next();
                    if (temp.getValue() != 0) {
                        temp.setValue(0);
                        int writeTime = Math.abs(new Random().nextInt() % 7000 - 1);
                        System.out.printf("%s -> %s, %dms%n", outputFileName, temp.getKey(), writeTime);
                        out.write(temp.getKey());
                        out.write("\r\n");
                        sleep(writeTime);
                        if (writeTime >= 5000)
                            throw new Exception("\tPrinting of \"" + temp.getKey() + "\" took too long");

                    }
                } else {
                    sleep(1000);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }


    public void finish() throws InterruptedException, IOException {
        out.write("Printing finished " + System.currentTimeMillis());
        out.write("\r\n");
        out.close();
        super.join();
    }


}
