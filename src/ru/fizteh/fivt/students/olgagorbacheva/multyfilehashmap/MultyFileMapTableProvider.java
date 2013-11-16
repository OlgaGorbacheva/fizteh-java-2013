package ru.fizteh.fivt.students.olgagorbacheva.multyfilehashmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Map;


import ru.fizteh.fivt.storage.strings.TableProvider;
import ru.fizteh.fivt.students.olgagorbacheva.filemap.FileMapException;

public class MultyFileMapTableProvider extends AbstractTableProvider<DataTable> implements TableProvider {

      public MultyFileMapTableProvider(String dir) {
            directory = new File(dir);
            File[] tableList = directory.listFiles();
            if (tableList.length != 0) {
                  for (File f : tableList) {
                        if (f.isDirectory()) {
                              DataTable dataTable = new DataTable(f.getName(), f);
                              tables.put(f.getName(), dataTable);
                        }
                  }
            }
      }

      public void writeToFile() throws FileNotFoundException, IOException {
            Iterator<Map.Entry<String, DataTable>> it = tables.entrySet().iterator();
            while (it.hasNext()) {
                  Entry<String, DataTable> elem = it.next();
                  try {
                        elem.getValue().writeFile();
                  } catch (IOException | FileMapException e) {
                        System.err.println(e.getLocalizedMessage());
                  }
            }
      }
      
      @Override
      public DataTable createTable(String name) {
            if (name == null || name.isEmpty()) {
                  throw new IllegalArgumentException("Недопустимое название таблицы");
            }
            if (!name.matches(TABLE_NAME)) {
                  throw new RuntimeException("Недопустимое имя файла");
            }
            if (tables.get(name) != null) {
                  return null;
            }
            File f = new File(directory, name);
            if (!f.mkdir()) {
                  throw new IllegalArgumentException("Создание директории невозможно");
            }
            DataTable newTable = new DataTable(name, f);
            tables.put(name, newTable);
            return newTable;
      }

}