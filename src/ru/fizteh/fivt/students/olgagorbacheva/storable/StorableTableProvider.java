package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.storage.structured.TableProvider;
import ru.fizteh.fivt.students.olgagorbacheva.multyfilehashmap.AbstractTableProvider;

public class StorableTableProvider extends AbstractTableProvider<Table> implements TableProvider{

      public StorableTableProvider(String dir) {
            directory = new File(dir);
            File[] tableList = directory.listFiles();
            if (tableList.length != 0) {
                  for (File f : tableList) {
                        if (f.isDirectory()) {
                              Table dataTable;
                              try {
                                    dataTable = new StorableTable(f.getName(), f, StorableUtils.getSignature(f), this);
                                    tables.put(f.getName(), dataTable);
                              } catch (IllegalArgumentException | IOException e) {
                                    throw new RuntimeException("Невозможно узнать сигнатуру таблицы", e);
                              }
                        }
                  }
            }
      }
      
      @Override
      public Table createTable(String name, List<Class<?>> columnTypes) throws IOException {
            if (name == null || name.isEmpty()) {
                  throw new IllegalArgumentException("Недопустимое название таблицы");
            }
            if (!name.matches(TABLE_NAME)) {
                  throw new IOException("Недопустимое имя файла");
            }
            if (tables.get(name) != null) {
                  return null;
            }
            File f = new File(directory, name);
            if (!f.mkdir()) {
                  throw new IllegalArgumentException("Создание директории невозможно");
            }
            Table newTable = new StorableTable(name, f, columnTypes, this);
            tables.put(name, newTable);
            StorableUtils.setSignature((StorableTable) newTable);
            return newTable;
      }

      @Override
      public Storeable deserialize(Table table, String value) throws ParseException {
            try {
                  return StorableUtils.writeToStorable(table, value);
            } catch (XMLStreamException e) {
                  throw new RuntimeException("Невозможно прочитать строку таблицы", e);
            }
      }

      @Override
      public String serialize(Table table, Storeable value) throws ColumnFormatException {
            try {
                  return StorableUtils.writeToString(table, value);
            } catch (XMLStreamException | IOException e) {
                  throw new RuntimeException("Невозможно представить строку таблицы символьной сторкой", e);
            }
      }

      @Override
      public Storeable createFor(Table table) {
            return new Storable(table);
      }

      @Override
      public Storeable createFor(Table table, List<?> values) throws ColumnFormatException, IndexOutOfBoundsException {
            return new Storable(table, values);
      }


}