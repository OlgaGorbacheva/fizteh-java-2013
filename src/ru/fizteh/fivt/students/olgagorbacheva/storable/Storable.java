package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.util.List;
import java.util.ArrayList;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;

public class Storable implements Storeable {

      private List<Object> column;
      private Table table;

      public Storable(Table table) {
            column = new ArrayList<>();
            this.table = table;
            for (int i = 0; i < table.getColumnsCount(); i++) {
                  column.add(null);
            }
      }

      public Storable(Table table, List<?> values) throws IndexOutOfBoundsException, ColumnFormatException {
            column = new ArrayList<>();
            if (values.size() != values.size()) {
                  throw new IndexOutOfBoundsException("Создание объекта невозможно. Строка значений неверного размера");
            }
            this.table = table;
            for (int i = 0; i < table.getColumnsCount(); i++) {
                  if (values.get(i) == null) {
                        if (table.getColumnType(i) != null) {
                              throw new ColumnFormatException("Несовпадение типов столбца "
                                          + table.getColumnType(i) + " и значения " + values.get(i));
                        }
                  } else {
                        typeCheck(values.get(i).getClass(), i);
                  }
                  column.add(values.get(i));
            }
      }

      void typeCheck(Class<?> value, int columnIndex) throws ColumnFormatException {
            if (value != null && table.getColumnType(columnIndex) == null) {
                  throw new ColumnFormatException("Несовпадение типов столбца " + table.getColumnType(columnIndex)
                              + " и значения " + value);
            }
            if (!value.equals(table.getColumnType(columnIndex))) {
                  throw new ColumnFormatException("Несовпадение типов столбца " + table.getColumnType(columnIndex)
                              + " и значения " + value);
            }
            // что делать с long и short???
      }

      void indexCheck(int columnIndex) throws IndexOutOfBoundsException {
            if (columnIndex < 0 || columnIndex >= column.size()) {
                  throw new IndexOutOfBoundsException("Выход за границы таблицы");
            }
      }

      @Override
      public void setColumnAt(int columnIndex, Object value) throws ColumnFormatException, IndexOutOfBoundsException {
            indexCheck(columnIndex);
            if (value == null) {
                  if (table.getColumnType(columnIndex) != null) {
                        throw new ColumnFormatException("Несовпадение типов столбца "
                                    + table.getColumnType(columnIndex) + " и значения " + value);
                  }
            } else {
                  typeCheck(value.getClass(), columnIndex);
            }
            column.set(columnIndex, value);
      }

      @Override
      public Object getColumnAt(int columnIndex) throws IndexOutOfBoundsException {
            indexCheck(columnIndex);
            return column.get(columnIndex);
      }

      @Override
      public Integer getIntAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
            indexCheck(columnIndex);
            typeCheck(Integer.class, columnIndex);
            return (Integer) column.get(columnIndex);
      }

      @Override
      public Long getLongAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
            indexCheck(columnIndex);
            typeCheck(Long.class, columnIndex);
            return (Long) column.get(columnIndex);
      }

      @Override
      public Byte getByteAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
            indexCheck(columnIndex);
            typeCheck(Byte.class, columnIndex);
            return (Byte) column.get(columnIndex);
      }

      @Override
      public Float getFloatAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
            indexCheck(columnIndex);
            typeCheck(Float.class, columnIndex);
            return (Float) column.get(columnIndex);
      }

      @Override
      public Double getDoubleAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
            indexCheck(columnIndex);
            typeCheck(Double.class, columnIndex);
            return (Double) column.get(columnIndex);
      }

      @Override
      public Boolean getBooleanAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
            indexCheck(columnIndex);
            typeCheck(Boolean.class, columnIndex);
            return (Boolean) column.get(columnIndex);
      }

      @Override
      public String getStringAt(int columnIndex) throws ColumnFormatException, IndexOutOfBoundsException {
            indexCheck(columnIndex);
            typeCheck(String.class, columnIndex);
            return (String) column.get(columnIndex);
      }

      public Integer getSize() {
            return column.size();
      }

      public void equalFormat(Storable other) {
            for (int i = 0; i < column.size(); i++) {
                  if (i > other.getSize()) {
                        throw new ColumnFormatException("Несовпадение типов столбцов");
                  }
                  typeCheck(other.getColumnAt(i).getClass(), i);
            }
      }

      public void equalFormat(List<Class<?>> other) {
            for (int i = 0; i < column.size(); i++) {
                  if (i > other.size()) {
                        throw new ColumnFormatException("Несовпадение типов столбца");
                  }
                  typeCheck(other.get(i), i);
            }
      }

}
