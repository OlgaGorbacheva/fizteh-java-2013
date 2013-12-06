package ru.fizteh.fivt.students.olgagorbacheva.storable;

import ru.fizteh.fivt.students.olgagorbacheva.shell.Command;
import ru.fizteh.fivt.students.olgagorbacheva.shell.State;

public class SizeCommand implements Command {

      private String name = "size";
      private final int argNumber = 0;

      StorableTableProvider provider;

      public SizeCommand(StorableTableProvider provider) {
            this.provider = provider;
      }

      public void execute(String[] args, State state) {
            if (provider.currentDataBase == null) {
                  throw new IllegalArgumentException("Таблица не выбрана");
            }
            System.out.println(provider.currentDataBase.size());
      }

      public String getName() {
            return name;
      }

      public int getArgNumber() {
            return argNumber;
      }

}
