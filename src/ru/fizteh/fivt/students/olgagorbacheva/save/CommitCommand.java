package ru.fizteh.fivt.students.olgagorbacheva.multyfilehashmap;

import ru.fizteh.fivt.students.olgagorbacheva.abstractcommands.AbstractCommitCommand;

public class CommitCommand extends AbstractCommitCommand<MultyFileMapTableProvider> implements Command {

      public CommitCommand(MultyFileMapTableProvider provider) {
            super(provider);
      }

      public void execute(String[] args, State state) {
            if (provider.currentDataBase == null) {
                  throw new IllegalArgumentException("Таблица не выбрана");
            }
            System.out.println(provider.currentDataBase.commit());
      }
}