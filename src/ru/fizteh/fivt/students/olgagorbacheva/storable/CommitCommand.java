package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.io.IOException;

import ru.fizteh.fivt.students.olgagorbacheva.shell.Command;
import ru.fizteh.fivt.students.olgagorbacheva.shell.State;

public class CommitCommand implements Command {

      private String name = "commit";
      private final int argNumber = 0;

      StorableTableProvider provider;

      public CommitCommand(StorableTableProvider provider) {
            this.provider = provider;
      }

      public void execute(String[] args, State state) throws IOException, IllegalArgumentException {
            if (provider.currentDataBase == null) {
                  throw new IllegalArgumentException("commit: Таблица не выбрана");
            }
            System.out.println(provider.currentDataBase.commit());
      }

      public String getName() {
            return name;
      }

      public int getArgNumber() {
            return argNumber;
      }
}