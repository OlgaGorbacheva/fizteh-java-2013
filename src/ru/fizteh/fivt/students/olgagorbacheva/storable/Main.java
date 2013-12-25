package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.io.IOException;

import ru.fizteh.fivt.students.olgagorbacheva.shell.ExitCommand;
import ru.fizteh.fivt.students.olgagorbacheva.shell.Shell;
import ru.fizteh.fivt.students.olgagorbacheva.shell.State;

public class Main {

      /**
       * @param args
       */
      public static void main(String[] args) {
            if (System.getProperty("fizteh.db.dir") == null) {
                  System.exit(1);
            }
            String dir = System.getProperty("fizteh.db.dir");
            Shell storable = new Shell(new State(System.getProperty("fizteh.db.dir")));
            StorableTableProviderFactory providerFactory = new StorableTableProviderFactory();
            StorableTableProvider provider;
            try {
                  provider = providerFactory.create(dir);
                  storable.addCommand(new RemoveCommand(provider));
                  storable.addCommand(new RollbackCommand(provider));
                  storable.addCommand(new GetCommand(provider));
                  storable.addCommand(new CommitCommand(provider));
                  storable.addCommand(new CreateCommand(provider));
                  storable.addCommand(new DropCommand(provider));
                  storable.addCommand(new PutCommand(provider));
                  storable.addCommand(new SizeCommand(provider));
                  storable.addCommand(new UseCommand(provider));
                  storable.addCommand(new ExitCommand());

                  storable.execute(args);

                  provider.writeAll();
            } catch (IllegalArgumentException | IOException e) {
                  System.err.println(e.getMessage());
                  System.exit(1);
            }
      }

}
