package ru.fizteh.fivt.students.olgagorbacheva.multyfilehashmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import ru.fizteh.fivt.students.olgagorbacheva.filemap.FileMapException;
import ru.fizteh.fivt.students.olgagorbacheva.filemap.Storage;

public abstract class AbstractTable<KeyType, ValueType> {

      protected String dataBaseName;
      protected Storage<KeyType, ValueType> dataStorage;
      protected Storage<KeyType, ValueType> newKeys;
      protected Storage<KeyType, ValueType> removedKeys;
      protected File dataBaseDir;

      public AbstractTable(String name, File directory) {
            dataBaseName = name;
            dataBaseDir = directory;
            dataStorage = new Storage<KeyType, ValueType>();
            newKeys = new Storage<KeyType, ValueType>();
            removedKeys = new Storage<KeyType, ValueType>();
      }

      public File getWorkingDirectory() {
            return dataBaseDir;
      }

      public String getName() {
            return dataBaseName;
      }

      public ValueType get(KeyType key) throws IllegalArgumentException {
            if (key == null) {
                  throw new IllegalArgumentException("Неверное значение ключа");
            }
            ValueType value = newKeys.get(key);
            if (value != null) {
                  return value;
            } else {
                  ValueType dataStorageValue = dataStorage.get(key);
                  if (removedKeys.get(key) != null) {
                        return null;
                  } else {
                        return dataStorageValue;
                  }
            }
      }

      public ValueType put(KeyType key, ValueType value) {
            if (key == null || value == null || key.toString().trim().equals("") || value.toString().trim().equals("")) {
                  throw new IllegalArgumentException("Неверное значение ключа или значения");
            }
            if (dataStorage.get(key) == null && newKeys.get(key) == null || removedKeys.get(key) != null) {
                  if (removedKeys.get(key) != null) {
                        if (removedKeys.get(key) != value) {
                              newKeys.put(key, value);
                        }
                        removedKeys.remove(key);
                  } else {
                        newKeys.put(key, value);
                  }
                  return null;
            } else {
                  if (newKeys.get(key) == null) {
                        ValueType oldValue = dataStorage.get(key);
                        if (!oldValue.equals(value)) {
                              newKeys.put(key, value);
                        }
                        return oldValue;
                  }
                  ValueType oldValue = newKeys.get(key);
                  newKeys.set(key, value);
                  return oldValue;
            }
      }

      public ValueType remove(KeyType key) {
            if (key == null) {
                  throw new IllegalArgumentException("Неверное значение ключа");
            }
            ValueType value = newKeys.get(key);
            if (value != null) {
                  newKeys.remove(key);
                  if (dataStorage.get(key) != null) {
                        removedKeys.put(key, dataStorage.get(key));
                  }
                  return value;
            } else {
                  ValueType valueFromDataStorage = dataStorage.get(key);
                  if (valueFromDataStorage != null && removedKeys.get(key) == null) {
                        removedKeys.put(key, valueFromDataStorage);
                        return valueFromDataStorage;
                  }
                  return null;
            }
      }

      public int size() {
            int size = dataStorage.getSize() - removedKeys.getSize();
            Set<KeyType> keys = newKeys.keySet();
            for (KeyType key : keys) {
                  if (dataStorage.get(key) == null) {
                        size++;
                  }
            }
            return size;
      }

      public int commit() {
            int size = 0;
            if (newKeys.getSize() != 0) {
                  Set<KeyType> keys = newKeys.keySet();
                  for (KeyType key : keys) {
                        if (!newKeys.get(key).equals(dataStorage.get(key))) {
                              size++;
                        }
                        if (!dataStorage.put(key, newKeys.get(key))) {
                              dataStorage.set(key, newKeys.get(key));
                        }
                  }
                  newKeys.clear();
            }
            if (removedKeys.getSize() != 0) {
                  for (KeyType key : removedKeys.keySet()) {
                        size++;
                        dataStorage.remove(key);
                  }
                  removedKeys.clear();
            }
            return size;
      }

      public int sizeChangesCommit() {
            return removedKeys.getSize() + newKeys.getSize();
      }

      public int rollback() {
            int size = 0;
            if (newKeys.getSize() != 0) {
                  Set<KeyType> keys = newKeys.keySet();
                  for (KeyType key : keys) {
                        if (!newKeys.get(key).equals(dataStorage.get(key))) {
                              size++;
                        }
                  }
            }
            newKeys.clear();
            size += removedKeys.getSize();
            removedKeys.clear();
            return size;
      }


      public abstract void readFile() throws IOException;
      
      public abstract void writeFile() throws FileNotFoundException, IOException, FileMapException;
      
}