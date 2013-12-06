package ru.fizteh.fivt.students.olgagorbacheva.multyfilehashmap;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ru.fizteh.fivt.students.olgagorbacheva.commands.TableState;;

public abstract class AbstractTableProvider<TableType> {

      public static final String TABLE_NAME = "[a-zA-Zа-яА-Я0-9]+";
      protected File directory;
      public TableState<TableType> currentTable = new TableState<>();
      protected Map<String, TableType> tables = new HashMap<>();

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
                  state.currentDataBase = null;
            }
            if (tables.get(name) == null) {
                  state.currentDataBase = null;
                  return null;
            }
            state.currentDataBase = tables.get(name);
            return state.currentDataBase;
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

      public abstract TableType createTable(String name);
      
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