package ru.fizteh.fivt.students.olgagorbacheva.multyfilehashmap;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractTableProvider<TableType> {

      public static final String TABLE_NAME = "[a-zA-Zа-яА-Я0-9]+";
      protected File directory;
      public TableType currentDataBase = null;
      protected Map<String, TableType> tables = new HashMap<String, TableType>();

      public TableType getTable(String name) {
            if (name == null || name.isEmpty()) {
                  throw new IllegalArgumentException("Недопустимое название таблицы");
            }
            if (!name.matches(TABLE_NAME)) {
                  throw new RuntimeException("Недопустимое имя файла");
            }
            return tables.get(name);
      }

      public TableType setTable(String name) {
            if (name == null || name.isEmpty()) {
                  currentDataBase = null;
            }
            if (tables.get(name) == null) {
                  currentDataBase = null;
                  return null;
            }
            currentDataBase = tables.get(name);
            return currentDataBase;
      }

      private void deleteFiles(File f) {
            if (f.isDirectory()) {
                  File[] incFiles = f.listFiles();
                  for (File i : incFiles) {
                        deleteFiles(i);
                  }
            }
            f.delete();
      }

      public void removeTable(String name) throws IllegalArgumentException {
            if (name == null || name.isEmpty()) {
                  throw new IllegalArgumentException("Недопустимое название таблицы");
            }
            if (!name.matches(TABLE_NAME)) {
                  throw new IllegalArgumentException("Недопустимое имя файла");
            }
            if (tables.get(name) == null) {
                  throw new IllegalStateException("Данной таблицы не существует");
            }
            deleteFiles(new File(directory, name));
            tables.remove(name);
      }

}