package ru.fizteh.fivt.students.olgagorbacheva.commands;

import ru.fizteh.fivt.storage.structured.Table;

abstract public class TableState{
      
      public Table currentDataBase;
      
      TableState(Table currentTable) {
            currentDataBase = currentTable;
      }
      
      TableState() {
            currentDataBase = null;
      }
      
}
