package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.io.IOException;

import ru.fizteh.fivt.students.olgagorbacheva.shell.Command;
import ru.fizteh.fivt.students.olgagorbacheva.shell.State;

public class RemoveCommand implements Command {

      private String name = "remove";
      private final int argNumber = 1;

      StorableTableProvider provider;

      public RemoveCommand(StorableTableProvider provider) {
            this.provider = provider;
      }

      public void execute(String[] args, State state) throws IOException, IllegalArgumentException {
            if (provider.currentDataBase == null) {
                  throw new IllegalArgumentException("remove: Таблица не выбрана");
            }
            if (provider.currentDataBase.remove(args[1]) == null) {
                  System.out.println("not found");
            } else {
                  System.out.println("removed");
            }

      }

      public String getName() {
            return name;
      }

      public int getArgNumber() {
            return argNumber;
      }

}
