package ru.fizteh.fivt.students.kochetovnicolai.fileMap;

import ru.fizteh.fivt.storage.strings.Table;
import ru.fizteh.fivt.students.kochetovnicolai.shell.Executable;

public class TableCommandPut extends Executable {
    TableManager manager;

    @Override
    public boolean execute(String[] args) {
        Table table = manager.getCurrentTable();
        if (table == null) {
            manager.printMessage("no table");
            return false;
        }
        String oldValue = table.put(args[1], args[2]);
        if (oldValue == null) {
            manager.printMessage("new");
        } else {
            manager.printMessage("overwrite");
            manager.printMessage(oldValue);
        }
        return true;
    }

    public TableCommandPut(TableManager tableManager) {
        super("put", 3);
        manager = tableManager;
    }
}
