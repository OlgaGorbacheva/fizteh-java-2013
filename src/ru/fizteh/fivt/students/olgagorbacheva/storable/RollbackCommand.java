package ru.fizteh.fivt.students.olgagorbacheva.storable;

import ru.fizteh.fivt.students.olgagorbacheva.shell.Command;
import ru.fizteh.fivt.students.olgagorbacheva.shell.State;

public class RollbackCommand implements Command {

      private String name = "rollback";
      private final int argNumber = 0;

      StorableTableProvider provider;

      public RollbackCommand(StorableTableProvider provider) {
            this.provider = provider;
      }

      public void execute(String[] args, State state) {
            System.out.println(provider.currentDataBase.rollback());
      }

      public String getName() {
            return name;
      }

      public int getArgNumber() {
            return argNumber;
      }
}
