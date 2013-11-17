package ru.fizteh.fivt.students.chernigovsky.multifilehashmap;

import ru.fizteh.fivt.storage.strings.Table;
import ru.fizteh.fivt.storage.strings.TableProvider;

public class State {
    private Table currentTable;
    private TableProvider currentTableProvider;

    public State(Table newTable, TableProvider newTableProvider) {
        currentTable = newTable;
        currentTableProvider = newTableProvider;
    }

    public Table getCurrentTable() {
        return currentTable;
    }

    public void changeCurrentTable(Table newCurrentTable) {
        currentTable = newCurrentTable;
    }

    public TableProvider getCurrentTableProvider() {
        return currentTableProvider;
    }
}
