package ru.fizteh.fivt.students.olgagorbacheva.multyfilehashmap;

import ru.fizteh.fivt.students.olgagorbacheva.abstractcommands.AbstractGetCommand;
import ru.fizteh.fivt.students.olgagorbacheva.shell.Command;
import ru.fizteh.fivt.students.olgagorbacheva.shell.State;

public class GetCommand extends AbstractGetCommand<MultyFileMapTableProvider> implements Command {

      public GetCommand(MultyFileMapTableProvider provider) {
            super(provider);
      }

      public void execute(String[] args, State state) {
            if (provider.currentDataBase == null) {
                  throw new IllegalArgumentException("Таблица не выбрана");
            }
            String value = provider.currentDataBase.get(args[1]);
            if (value == null) {
                  System.out.println("not found");
            } else {
                  System.out.println("found" + "\n" + value);
            }
      }

}
