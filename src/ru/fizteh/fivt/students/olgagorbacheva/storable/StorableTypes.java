package ru.fizteh.fivt.students.olgagorbacheva.storable;

import java.util.HashMap;
import java.util.Map;

//import ru.fizteh.fivt.storage.structured.ColumnFormatException;
//import ru.fizteh.fivt.storage.structured.Storeable;

public enum StorableTypes {

      
      INT("int", Integer.class),

      LONG("long", Long.class) {
//            public Long parse(String s) {
//                  return Long.parseLong(s);
//            }
//
//            public Object tryToGetStoreableField(int columnIndex, Storeable value) throws ColumnFormatException,
//                        IndexOutOfBoundsException {
//                  return value.getLongAt(columnIndex);
//            }
      },
      BYTE("byte", Byte.class) {
//            public Byte parse(String s) {
//                  return Byte.parseByte(s);
//            }
//
//            public Object tryToGetStoreableField(int columnIndex, Storeable value) throws ColumnFormatException,
//                        IndexOutOfBoundsException {
//                  return value.getByteAt(columnIndex);
//            }
      },
      FLOAT("float", Float.class) {
//            public Float parse(String s) {
//                  return Float.parseFloat(s);
//            }
//
//            public Object tryToGetStoreableField(int columnIndex, Storeable value) throws ColumnFormatException,
//                        IndexOutOfBoundsException {
//                  return value.getFloatAt(columnIndex);
//            }
      },
      DOUBLE("double", Double.class) {
//            public Double parse(String s) {
//                  return Double.parseDouble(s);
//            }
//
//            public Object tryToGetStoreableField(int columnIndex, Storeable value) throws ColumnFormatException,
//                        IndexOutOfBoundsException {
//                  return value.getDoubleAt(columnIndex);
//            }
      },
      BOOLEAN("boolean", Boolean.class) {
//            public Boolean parse(String s) {
//                  return Boolean.parseBoolean(s);
//            }
//
//            public Object tryToGetStoreableField(int columnIndex, Storeable value) throws ColumnFormatException,
//                        IndexOutOfBoundsException {
//                  return value.getBooleanAt(columnIndex);
//            }
      },
      STRING("String", String.class) {
//            public String parse(String s) {
//                  return s;
//            }
//
//            public Object tryToGetStoreableField(int columnIndex, Storeable value) throws ColumnFormatException,
//                        IndexOutOfBoundsException {
//                  return value.getStringAt(columnIndex);
//            }
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
      
      public static Class<?> getClass(String name) {
            StorableTypes type = nameToTypes.get(name);
            if (type == null) {
                  throw new IllegalArgumentException("Данного типа не существует");
            }
            return type.clazz;
      }

      public static String getName(Class<?> clazz) {
            StorableTypes type = classToTypes.get(clazz);
            if (type == null) {
                  throw new IllegalArgumentException("Данного типа не существует");
            }
            return type.name;
      }
      
//      public abstract Object parse(String type);
//
//      public abstract Object tryToGetStoreableField(int columnIndex, Storeable value) throws ColumnFormatException,
//                  IndexOutOfBoundsException;
//
//      public static Object getStoreableField(int columnIndex, Storeable value, Class<?> columnType)
//                  throws ColumnFormatException, IndexOutOfBoundsException {
//            StoreableColumnType storeableColumnType = classToColumnType.get(columnType);
//            if (storeableColumnType == null) {
//                  throw new ColumnFormatException("Not allowed type of signature");
//            }
//            return storeableColumnType.tryToGetStoreableField(columnIndex, value);
//      }
//
//      public static Object convertStringToAnotherObject(String value, Class<?> cls) {
//            try {
//                  StoreableColumnType storeableColumnType = classToColumnType.get(cls);
//                  if (storeableColumnType == null) {
//                        throw new IllegalArgumentException("Not allowed type of signature");
//                  }
//                  Object ret = storeableColumnType.parse(value);
//                  return ret;
//            } catch (NumberFormatException e) {
//                  throw new IllegalArgumentException("The column required for another type of value");
//            }
//      }
//
//      public static String getPrimitive(Class<?> cls) {
//            StoreableColumnType storeableColumnType = classToColumnType.get(cls);
//            if (storeableColumnType == null) {
//                  throw new IllegalArgumentException("Not allowed type of signature");
//            }
//            return storeableColumnType.primitiveColumnType;
//      }
//
//      public static Class<?> getClassFromPrimitive(String primitiveType) {
//            StoreableColumnType storeableColumnType = primitiveToColumnType.get(primitiveType);
//            if (storeableColumnType == null) {
//                  throw new IllegalArgumentException("wrong type " + primitiveType);
//            }
//            return storeableColumnType.clsColumnType;
//
//      }

}
