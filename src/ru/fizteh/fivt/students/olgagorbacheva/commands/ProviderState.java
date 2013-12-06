package ru.fizteh.fivt.students.olgagorbacheva.commands;

import ru.fizteh.fivt.storage.structured.TableProvider;

public abstract class ProviderState {
      
      public TableProvider currentTableProvider;
      
      ProviderState(TableProvider currentProvider) {
            currentTableProvider = currentProvider;
      }
      
      ProviderState() {
            currentTableProvider = null;
      }
}
