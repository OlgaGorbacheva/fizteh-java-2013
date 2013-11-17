package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.util.List;
import java.util.ArrayList;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;

public class Storable implements Storeable {

      private List<Object> column;
      private Table table;

      Storable(Table table) {
            column = new ArrayList<>();
            this.table = table;
            for (int i = 0; i < table.getColumnsCount(); i++) {
                  column.add(null);
            }
      }

      Storable(Table table, List<?> values) {
            column = new ArrayList<>();
            if (column.size() != values.size()) {
                  throw new IndexOutOfBoundsException("Количество колонок в таблице не совдает со строкой значений");
            }
            this.table = table;
            for (int i = 0; i < table.getColumnsCount(); i++) {
                  typeCheck(values.get(i).getClass(), i);
                  column.add(values.get(i));
            }
      }

      void typeCheck(Class<?> value, int columnIndex) {
            if (!value.equals(table.getColumnType(columnIndex))) {
                  throw new ColumnFormatException("Несовпадение типов столбца");
            }
      }

      void indexCheck(int columnIndex) {
            if (columnIndex < 0 || columnIndex >= column.size()) {
                  throw new IndexOutOfBoundsException("Выход за границы");
            }
      }

      @Override
      public void setColumnAt(int columnIndex, Object value) throws ColumnFormatException, IndexOutOfBoundsException {

            indexCheck(columnIndex);
            typeCheck(value.getClass(), columnIndex);
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
                        throw new ColumnFormatException("Несовпадение типов столбца");
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
