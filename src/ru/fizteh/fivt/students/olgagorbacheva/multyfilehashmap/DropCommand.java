package ru.fizteh.fivt.students.olgagorbacheva.multyfilehashmap;

import ru.fizteh.fivt.students.olgagorbacheva.abstractcommands.AbstractDropCommand;
import ru.fizteh.fivt.students.olgagorbacheva.shell.Command;
import ru.fizteh.fivt.students.olgagorbacheva.shell.State;

public class DropCommand extends AbstractDropCommand<MultyFileMapTableProvider> implements Command {

      public DropCommand(MultyFileMapTableProvider provider) {
            super(provider);
      }

      public void execute(String[] args, State state) {
            if (provider.getTable(args[1]) == null) {
                  System.out.println("tablename not exists");
            } else {
                  if (provider.currentDataBase != null) {
                        if (args[1].equals(provider.currentDataBase.getName())) {
                              provider.setTable(null);
                        }
                  }
                  provider.removeTable(args[1]);
                  System.out.println("droped");
            }

      }
}
