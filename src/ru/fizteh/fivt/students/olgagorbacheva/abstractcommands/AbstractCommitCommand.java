package ru.fizteh.fivt.students.olgagorbacheva.abstractcommands;

import ru.fizteh.fivt.students.olgagorbacheva.shell.State;

public abstract class AbstractCommitCommand<TableProvider> {
      private String name = "commit";
      private final int argNumber = 0;

      protected TableProvider provider;

      public AbstractCommitCommand(TableProvider provider) {
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
