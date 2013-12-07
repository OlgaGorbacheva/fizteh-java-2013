package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.io.IOException;

import ru.fizteh.fivt.students.olgagorbacheva.shell.Command;
import ru.fizteh.fivt.students.olgagorbacheva.shell.State;

public class DropCommand implements Command {

      private String name = "drop";
      private int argNumber = 1;
      
      private StorableTableProvider provider;
      
      public DropCommand(StorableTableProvider provider) {
            this.provider = provider;
      }

      public void execute(String[] args, State state) throws IOException{
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

      @Override
      public String getName() {
            return name;
      }

      @Override
      public int getArgNumber() {
            return argNumber;
      }
}

