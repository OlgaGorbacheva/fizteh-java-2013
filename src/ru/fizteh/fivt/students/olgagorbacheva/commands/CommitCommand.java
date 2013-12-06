package ru.fizteh.fivt.students.olgagorbacheva.commands;

import java.io.IOException;

public class CommitCommand implements TableCommand{
      private String name = "commit";
      private final int argNumber = 0;

      public void execute(String[] args, TableState state) throws IOException {
            if (state.currentDataBase == null) {
                  throw new IllegalArgumentException("Таблица не выбрана");
            }
            System.out.println(state.currentDataBase.commit());
      }

      public String getName() {
            return name;
      }

      public int getArgNumber() {
            return argNumber;
      }
}
