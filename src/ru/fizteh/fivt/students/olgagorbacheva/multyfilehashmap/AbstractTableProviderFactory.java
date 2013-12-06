package ru.fizteh.fivt.students.olgagorbacheva.multyfilehashmap;

import ru.fizteh.fivt.students.olgagorbacheva.commands.ProviderState;

public abstract class AbstractTableProviderFactory<TableProviderType> {
      
      public ProviderState<TableProviderType> currentProvider = new ProviderState<>();   
      
      abstract TableProviderType create(String dir);
}
