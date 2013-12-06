package ru.fizteh.fivt.students.olgagorbacheva.multyfilehashmap;

import ru.fizteh.fivt.students.olgagorbacheva.abstractcommands.AbstractCreateCommand;
import ru.fizteh.fivt.students.olgagorbacheva.shell.Command;
import ru.fizteh.fivt.students.olgagorbacheva.shell.State;

public class CreateCommand extends AbstractCreateCommand<MultyFileMapTableProvider> implements Command {

      public CreateCommand(MultyFileMapTableProvider provider) {
            super(provider);
      }

      public void execute(String[] args, State state) {
            if (provider.createTable(args[1]) == null) {
                  System.out.println(args[1] + " exists");
            } else {
                  System.out.println("created");
            }
      }
      
}