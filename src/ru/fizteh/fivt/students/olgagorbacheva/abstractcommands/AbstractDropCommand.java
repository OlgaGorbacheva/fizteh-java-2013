package ru.fizteh.fivt.students.olgagorbacheva.abstractcommands;

import ru.fizteh.fivt.students.olgagorbacheva.shell.State;

public abstract class AbstractDropCommand<TableProvider> {

      private String name = "drop";
      private final int argNumber = 1;

      protected TableProvider provider;

      public AbstractDropCommand(TableProvider provider) {
            this.provider = provider;
      }

      public abstract void execute(String[] args, State state);
      
      public String getName() {
            return name;
      }

      public int getArgNumber() {
            return argNumber;
      }
}
