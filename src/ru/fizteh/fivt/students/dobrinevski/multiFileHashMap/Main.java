package ru.fizteh.fivt.students.dobrinevski.multiFileHashMap;

import java.io.File;
import java.util.HashMap;
import ru.fizteh.fivt.students.dobrinevski.shell.Command;
import ru.fizteh.fivt.students.dobrinevski.shell.Shell;

public class Main {
    private static MyMultiHashMap dtb;
    private static HashMap<String, Command> cmdMap = new HashMap<String, Command>();

    static {
        dtb = new MyMultiHashMap();
        MultiFileHashMapCommand buf = new MultiFileHashMapCommands.Create(dtb);
        cmdMap.put("create", buf);
        buf =  new MultiFileHashMapCommands.Drop(dtb);
        cmdMap.put("drop", buf);
        buf = new MultiFileHashMapCommands.Use(dtb);
        cmdMap.put("use", buf);
        buf = new MultiFileHashMapCommands.Put(dtb);
        cmdMap.put("put", buf);
        buf = new MultiFileHashMapCommands.Get(dtb);
        cmdMap.put("get", buf);
        buf = new MultiFileHashMapCommands.Remove(dtb);
        cmdMap.put("remove", buf);
        buf = new MultiFileHashMapCommands.Exit(dtb);
        cmdMap.put("exit", buf);
        }

    public static void main(String[] args) throws Exception {
        try {
            String way = System.getProperty("fizteh.db.dir");
            if (way == null) {
                throw new Exception("Illegal table");
            }
            File dbsDir = new File(way);
            if (!dbsDir.isDirectory()) {
                throw new Exception(dbsDir + " doesn't exist or is not a directory");
            }
         /*  File[] files = dbsDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if(file.isFile()) {
                    throw new Exception("It's a file in a root directory.");
                    }
                }
            }  */
        } catch (Exception e) {
            System.out.println("Error while opening database: " + (e.getMessage()));
            System.exit(1);
        }
        Shell sl = new Shell(cmdMap, "fizteh.db.dir");
        if (args.length > 0) {
            StringBuilder builder = new StringBuilder();
            for (String arg : args) {
                builder.append(arg).append(' ');
            }
            try {
                sl.executeCommands(builder.toString());
            } catch (Exception e) {
                System.err.println(e);
                System.exit(1);
            }
        } else {
            sl.iMode();
        }
    }
}

