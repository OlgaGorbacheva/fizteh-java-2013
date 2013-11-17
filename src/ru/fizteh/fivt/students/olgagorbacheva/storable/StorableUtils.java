package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.util.List;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;

public class StorableUtils {
      private static void typeCheck(Class<?> value1, Class<?> value2) throws ColumnFormatException{
            if (!value1.equals(value1)) {
                  throw new ColumnFormatException("Несовпадение типов столбца");
            }
      }
      
      public static void equalFormat(Storeable struct, List<Class<?>> list) throws ColumnFormatException{
            for (int i = 0; i < list.size(); i++) {
                  try {
                        typeCheck(list.get(i), struct.getColumnAt(i).getClass());
                  } catch (IndexOutOfBoundsException e) {
                        throw new ColumnFormatException("Несовпадение типов столбца");
                  }
            }
      }
      
      public static void columnListCheck(List<Class<?>> list) throws IllegalArgumentException{
            for (int i = 0; i < list.size(); i++) {
                  if (list.get(i) == null) {
                        throw new IllegalArgumentException("Недопустимые типы колонок таблицы");
                  }
            }
      }
}
