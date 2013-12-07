package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.io.IOException;

import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.students.olgagorbacheva.shell.Command;
import ru.fizteh.fivt.students.olgagorbacheva.shell.State;

public class GetCommand implements Command {

      private String name = "get";
      private final int argNumber = 1;

      StorableTableProvider provider;

      public GetCommand(StorableTableProvider provider) {
            this.provider = provider;
      }

      public void execute(String[] args, State state) throws IOException, IllegalArgumentException{
            if (provider.currentDataBase == null) {
                  throw new IllegalArgumentException("get: Таблица не выбрана");
            }
            Storeable value = provider.currentDataBase.get(args[1]);
            if (value == null) {
                  System.out.println("not found");
            } else {
                  System.out.println("found" + "\n");
                  for (int i = 0; i < provider.currentDataBase.getColumnsCount(); i++) {
                        System.out.print(value.getColumnAt(i).toString() + " ");
                  }
            }
      }

      public String getName() {
            return name;
      }

      public int getArgNumber() {
            return argNumber;
      }
}
