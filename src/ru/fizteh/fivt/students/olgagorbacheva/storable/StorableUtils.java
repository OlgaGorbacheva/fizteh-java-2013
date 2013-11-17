package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.util.List;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;

public class StorableUtils {
      void typeCheck(Class<?> value1, Class<?> value2) {
            if (!value1.equals(value1)) {
                  throw new ColumnFormatException("Несовпадение типов столбца");
            }
      }

      void indexCheck(Storeable struct, int columnIndex) {
            if (columnIndex < 0 || columnIndex >= struct.getSize()) {
                  throw new IndexOutOfBoundsException("Выход за границы");
            }
      }

      public void equalFormat(Storeable first, Storeable second) {
            for (int i = 0; i < first.getSize(); i++) {
                  if (i > second.getSize()) {
                        throw new ColumnFormatException("Несовпадение типов столбца");
                  }
                  typeCheck(first.getColumnAt(i).getClass(), second.getColumnAt(i).getClass());
            }
      }

      public void equalFormat(Storeable struct, List<Class<?>> list) {
            for (int i = 0; i < struct.getSize(); i++) {
                  if (i > list.size()) {
                        throw new ColumnFormatException("Несовпадение типов столбца");
                  }
                  typeCheck(list.get(i), struct.getColumnAt(i).getClass());
            }
      }
}
