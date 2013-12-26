package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
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
            StringBuilder typeString = new StringBuilder(join(args, " ", 2).trim());
            if (typeString.charAt(0) == '(' && typeString.charAt(typeString.length() - 1) == ')') {
                  typeString.deleteCharAt(0);
                  typeString.deleteCharAt(typeString.length() - 1);
            } else {
                  throw new IOException("Неверный формат ввода");
            }
            String[] typeList = typeString.toString().split("\\s+");
            try {
                  for (int i = 0; i < typeList.length; i++) {
                        types.add(StorableTypes.getClass(typeList[i]));
                  }
            } catch (ColumnFormatException e) {
                  System.err.println(e.getLocalizedMessage() + " in create command");
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

      public static String join(String[] objects, String separator, int begin) {

            StringBuilder argument = new StringBuilder();
            boolean first = true;
            for (int i = begin; i < objects.length; i++) {
                  if (!first) {
                        argument.append(separator);
                  } else {
                        first = false;
                  }
                  argument.append(objects[i]);
            }
            return argument.toString();
      }
}
