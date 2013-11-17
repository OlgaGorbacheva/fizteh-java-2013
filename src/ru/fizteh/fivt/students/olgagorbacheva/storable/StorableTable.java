package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.students.olgagorbacheva.filemap.FileMapException;
import ru.fizteh.fivt.students.olgagorbacheva.multyfilehashmap.AbstractTable;

public class StorableTable extends AbstractTable<String, Storeable> implements Table {

      private List<Class<?>> columnTypes;

      public StorableTable(String name, File directory) {
            super(name, directory);
      }

      @Override
      public int getColumnsCount() {
            return columnTypes.size();
      }

      @Override
      public Class<?> getColumnType(int columnIndex) throws IndexOutOfBoundsException {
            if (columnIndex < 0 || columnIndex >= columnTypes.size()) {
                  throw new IndexOutOfBoundsException("Выход за границы");
            }
            return columnTypes.get(columnIndex);
      }

      @Override
      public void readFile() throws IOException {
            // TODO Auto-generated method stub

      }

      @Override
      public void writeFile() throws FileNotFoundException, IOException, FileMapException {
            // TODO Auto-generated method stub

      }

      @Override
      public Storeable put(String key, Storeable value) throws ColumnFormatException {
            if (key == null || value == null || key.toString().trim().equals("") || value.toString().trim().equals("")) {
                  throw new IllegalArgumentException("Неверное значение ключа или значения");
            }
            StorableUtils.equalsType(value, columnTypes);
            if (dataStorage.get(key) == null && newKeys.get(key) == null || removedKeys.get(key) != null) {
                  if (removedKeys.get(key) != null) {
                        if (removedKeys.get(key) != value) {
                              newKeys.put(key, value);
                        }
                        removedKeys.remove(key);
                  } else {
                        newKeys.put(key, value);
                  }
                  return null;
            } else {
                  if (newKeys.get(key) == null) {
                        Storeable oldValue = dataStorage.get(key);
                        if (!oldValue.equals(value)) {
                              newKeys.put(key, value);
                        }
                        return oldValue;
                  }
                  Storeable oldValue = newKeys.get(key);
                  newKeys.set(key, value);
                  return oldValue;
            }

      }


}
