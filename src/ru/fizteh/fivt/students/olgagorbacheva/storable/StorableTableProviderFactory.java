package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.io.File;
import java.io.IOException;

import ru.fizteh.fivt.storage.structured.TableProviderFactory;

public class StorableTableProviderFactory implements TableProviderFactory {

      @Override
      public StorableTableProvider create(String dir) throws IllegalArgumentException, IOException {
            if (dir == null || dir.trim().equals("") || dir.isEmpty()) {
                  throw new IllegalArgumentException("Недопустимое имя хранилища базы данных");
            }
//            System.err.println(dir);
            File directory = new File(dir);
            if (!directory.exists()) {
                  throw new IOException("Директории с данным именем не существует\n" + dir);
            }
            if (!directory.isDirectory()) {
                  throw new IllegalArgumentException("Файл с данным именем не является директорией");
            }
            if (!directory.canRead() || !directory.canWrite()) {
                  throw new RuntimeException("Директория недоступна");
            }
            StorableTableProvider tableProvider = new StorableTableProvider(dir);
            return tableProvider;
      }
}
