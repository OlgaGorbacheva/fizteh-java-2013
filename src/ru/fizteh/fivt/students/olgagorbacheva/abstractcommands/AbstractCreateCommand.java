package ru.fizteh.fivt.students.olgagorbacheva.abstractcommands;

import ru.fizteh.fivt.students.olgagorbacheva.shell.State;


public abstract class AbstractCreateCommand<TableProvider> {

      private String name = "create";
      private final int argNumber = 1;

      protected TableProvider provider;

      public AbstractCreateCommand(TableProvider provider) {
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
