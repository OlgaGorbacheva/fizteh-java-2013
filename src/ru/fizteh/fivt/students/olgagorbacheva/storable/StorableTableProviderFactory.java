package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.io.File;
import ru.fizteh.fivt.storage.structured.TableProviderFactory;

public class StorableTableProviderFactory implements TableProviderFactory{
      
      @Override
      public StorableTableProvider create(String dir) throws IllegalArgumentException {
            if (dir == null || dir.trim().equals("") || dir.isEmpty()) {
                  throw new RuntimeException("Недопустимое имя хранилища базы данных");
            }
            File directory = new File(dir);
            if (!directory.exists()) {
                  throw new RuntimeException("Директории с данным именем не существует");
            }
            if (!directory.isDirectory()) {
                  throw new RuntimeException("Файл с данным именем не является директорией");
            }
            if (!directory.canRead() || !directory.canWrite()) {
                  throw new RuntimeException("Директория недоступна");
            }
            StorableTableProvider tableProvider = new StorableTableProvider(dir);
            return tableProvider;
      }
}
