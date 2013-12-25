package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.students.olgagorbacheva.filemap.FileMapException;
import ru.fizteh.fivt.students.olgagorbacheva.multyfilehashmap.AbstractTable;

public class StorableTable extends AbstractTable<String, Storeable> implements Table {

      private List<Class<?>> columnTypes;
      private StorableTableProvider provider;

      public StorableTable(String name, File directory, List<Class<?>> columnTypes, StorableTableProvider provider)
                  throws IllegalArgumentException {
            super(name, directory);
            for (Class<?> clazz : columnTypes) {
                  if (!StorableTypes.check(clazz)) {
                        throw new IllegalArgumentException("Недопустимые типы колонк таблицы");
                  }
            }
            this.columnTypes = columnTypes;
            this.provider = provider;
      }

      @Override
      public int getColumnsCount() {
            return columnTypes.size();
      }

      @Override
      public Class<?> getColumnType(int columnIndex) throws IndexOutOfBoundsException {
            if (columnIndex < 0 || columnIndex >= columnTypes.size()) {
                  throw new IndexOutOfBoundsException("Выход за границы таблицы: " + columnIndex + "; размер таблицы:"
                              + columnTypes.size());
            }
            return columnTypes.get(columnIndex);
      }

      @Override
      public Storeable put(String key, Storeable value) throws ColumnFormatException {
            if (key == null || value == null || key.toString().trim().equals("") 
                        || value.toString().trim().equals("")) {
                  throw new IllegalArgumentException("Неверное значение ключа или значения");
            }
            if (key.split("\\s+").length != 0) {
                  throw new IllegalArgumentException("неподходящий ключ");
            }
            StorableUtils.equalFormat(value, columnTypes);
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

      @Override
      public void readFile() throws IOException {
            columnTypes = StorableUtils.getSignature(dataBaseDir);
            try {
                  StorableUtils.readTable(provider, this);
            } catch (ParseException e) {
                  throw new IOException("Ошибка чтения файла", e);
            }
      }

      @Override
      public void writeFile() throws FileNotFoundException, IOException, FileMapException {
            StorableUtils.writeTable(provider, this);

      }
}
