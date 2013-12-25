package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.util.HashMap;
import java.util.Map;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;

public enum StorableTypes {

      INT("int", Integer.class) {
            @Override
            public Integer get(Storeable value, Integer index) throws ColumnFormatException, IndexOutOfBoundsException {
                  return value.getIntAt(index);
            }

            @Override
            public Integer parse(String value) {
                  return Integer.parseInt(value);
            }
      },

      LONG("long", Long.class) {
            @Override
            public Long get(Storeable value, Integer index) throws ColumnFormatException, IndexOutOfBoundsException {
                  return value.getLongAt(index);
            }

            @Override
            public Long parse(String value) {
                  return Long.parseLong(value);
            }
      },

      BYTE("byte", Byte.class) {
            @Override
            public Byte get(Storeable value, Integer index) throws ColumnFormatException, IndexOutOfBoundsException {
                  return value.getByteAt(index);
            }

            @Override
            public Byte parse(String value) {
                  return Byte.parseByte(value);
            }
      },

      FLOAT("float", Float.class) {
            public Float get(Storeable value, Integer index) throws ColumnFormatException, IndexOutOfBoundsException {
                  return value.getFloatAt(index);
            }

            @Override
            public Float parse(String value) {
                  return Float.parseFloat(value);
            }
      },
      DOUBLE("double", Double.class) {
            public Double get(Storeable value, Integer index) throws ColumnFormatException, IndexOutOfBoundsException {
                  return value.getDoubleAt(index);
            }

            @Override
            public Double parse(String value) {
                  return Double.parseDouble(value);
            }
      },
      BOOLEAN("boolean", Boolean.class) {
            public Boolean get(Storeable value, Integer index) throws ColumnFormatException, IndexOutOfBoundsException {
                  return value.getBooleanAt(index);
            }

            @Override
            public Boolean parse(String value) {
                  return Boolean.parseBoolean(value);
            }
      },
      STRING("String", String.class) {
            public String get(Storeable value, Integer index) throws ColumnFormatException, IndexOutOfBoundsException {
                  return value.getStringAt(index);
            }

            @Override
            public String parse(String value) {
                  return value;
            }
      };

      private String name;
      private Class<?> clazz;

      StorableTypes(String name, Class<?> clazz) {
            this.clazz = clazz;
            this.name = name;
      }

      private static Map<String, StorableTypes> nameToTypes = new HashMap<>();
      private static Map<Class<?>, StorableTypes> classToTypes = new HashMap<>();
      static {
            for (StorableTypes currentType : StorableTypes.values()) {
                  nameToTypes.put(currentType.name, currentType);
                  classToTypes.put(currentType.clazz, currentType);
            }
      }

      public static Class<?> getClass(String name) throws ColumnFormatException {
            if (name.equals("null")) {
                  return null;
            }
            StorableTypes type = nameToTypes.get(name);
            if (type == null) {
                  throw new ColumnFormatException("wrong type " + name);
            }
            return type.clazz;
      }

      public static String getName(Class<?> clazz) throws ColumnFormatException {
            if (clazz.equals(null)) {
                  return null;
            }
            StorableTypes type = classToTypes.get(clazz);
            if (type == null) {
                  throw new ColumnFormatException("wrong type " + clazz.toString());
            }
            return type.name;
      }

      public static String getStringAt(Storeable value, Integer index) throws ColumnFormatException,
                  IndexOutOfBoundsException {
            StorableTypes type = classToTypes.get(value.getColumnAt(index).getClass());
            if (type == null) {
                  throw new ColumnFormatException("wrong type " + value.getColumnAt(index));
            }
            return type.get(value, index).toString();
      }

      public static Object getValueAt(String value, Table table, Integer index) throws ColumnFormatException,
                  IndexOutOfBoundsException {
            StorableTypes type = classToTypes.get(table.getColumnType(index));
            if (type == null) {
                  throw new ColumnFormatException("wrong type "
                                    + table.getColumnType(index).toString());
            }
            if (value.equals("null")) {
                  return null;
            }
            return type.parse(value);
      }
      
      public static boolean check(Class<?> clazz) {
            return (clazz != null) && (classToTypes.get(clazz) != null);
      }
      
      abstract Object parse(String value);

      abstract Object get(Storeable value, Integer index) throws ColumnFormatException,
                  IndexOutOfBoundsException;
}
