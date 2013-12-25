package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.fizteh.fivt.students.olgagorbacheva.shell.Command;
import ru.fizteh.fivt.students.olgagorbacheva.shell.State;

public class CreateCommand implements Command {

      private String name = "create";
      private final int argNumber = -1;

      StorableTableProvider provider;

      public CreateCommand(StorableTableProvider provider) {
            this.provider = provider;
      }

      public void execute(String[] args, State state) throws IOException {
            List<Class<?>> types = new ArrayList<>();
            for (int i = 2; i < args.length; i++) {
                  types.add(StorableTypes.getClass(args[i]));
            }
            if (provider.createTable(args[1], types) == null) {
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
