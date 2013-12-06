package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.util.ArrayList;
import java.util.List;

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

      public void execute(String[] args, State state) throws IllegalArgumentException {
            if (provider.currentDataBase == null) {
                  throw new IllegalArgumentException("Таблица не выбрана");
            }
            List<Object> values = new ArrayList<>();
            for (int i = 2; i < args.length; i++) {
                  values.add(StorableTypes.getValueAt(args[i], provider.currentDataBase, i - 2));
            }
            Storeable line = provider.createFor(provider.currentDataBase, values);
            Storeable value = provider.currentDataBase.put(args[1], line);
            if (value == null) {
                  System.out.println("new");
            } else {
                  System.out.println("overwrite" + "\n");
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

