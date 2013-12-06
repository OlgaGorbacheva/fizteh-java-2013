package ru.fizteh.fivt.students.olgagorbacheva.commands;

import ru.fizteh.fivt.students.olgagorbacheva.multyfilehashmap.AbstractTable;
import ru.fizteh.fivt.students.olgagorbacheva.multyfilehashmap.AbstractTableProvider;

public class CreateCommand implements ProviderCommand {

      private String name = "create";
      private final int argNumber = 1;

      public void execute(String[] args, ProviderState state) {
            if (state.currentTableProvider.createTable(args[1]) == null) {
                  System.out.println(args[1] + " exists");
            } else {
                  System.out.println("created");
            }
      }
      
      public String getName() {
            return name;
      }

      public int getArgNumber() {
            return argNumber;
      }
}
