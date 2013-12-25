package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.io.IOException;
import java.text.ParseException;

import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.students.olgagorbacheva.shell.Command;
import ru.fizteh.fivt.students.olgagorbacheva.shell.State;

public class PutCommand implements Command {

      private String name = "put";
      private final int argNumber = -1;

      StorableTableProvider provider;

      public PutCommand(StorableTableProvider provider) {
            this.provider = provider;
      }

      public void execute(String[] args, State state) throws IOException, IllegalArgumentException {            
            if (provider.currentDataBase == null) {
                  throw new IllegalArgumentException("put: Таблица не выбрана");
            }
            try {
                  Storeable line = provider.deserialize(provider.currentDataBase, args[2]);
                  Storeable value = provider.currentDataBase.put(args[1], line);
                  if (value == null) {
                        System.out.println("new");
                  } else {
                        System.out.println("overwrite");
                        for (int i = 0; i < provider.currentDataBase.getColumnsCount(); i++) {
                              System.out.print(value.getColumnAt(i).toString() + " ");
                        }
                  }
            } catch (ParseException e) {
                  System.out.println(e.getLocalizedMessage());
                  
            }
      }

      public String getName() {
            return name;
      }

      public int getArgNumber() {
            return argNumber;
      }

}
