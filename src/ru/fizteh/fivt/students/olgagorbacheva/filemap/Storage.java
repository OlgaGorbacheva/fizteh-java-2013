package ru.fizteh.fivt.students.olgagorbacheva.filemap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Storage<KeyType, ValueType> {
      Map<KeyType, ValueType> storage;
      Boolean commited;

      public Storage() {
            storage = new HashMap<KeyType, ValueType>();
            commited = true;
      }

      public boolean put(KeyType key, ValueType value) {
            if (storage.get(key) != null) {
                  return false;
            }
            storage.put(key, value);
            return true;
      }

      public boolean set(KeyType key, ValueType value) {
            if (storage.get(key) == null) {
                  return false;
            }
            storage.put(key, value);
            return true;
      }

      public boolean remove(KeyType key) {
            if (storage.get(key) == null) {
                  return false;
            }
            storage.remove(key);
            return true;
      }

      public ValueType get(KeyType key) {
            if (storage.get(key) == null) {
                  return null;
            }
            return storage.get(key);
      }

      public Map<KeyType, ValueType> getMap() {
            return storage;
      }

      public Set<KeyType> keySet() {
            return storage.keySet();
      }

      public int getSize() {
            return storage.size();
      }

      public void clear() {
            storage.clear();
      }

}
