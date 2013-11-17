package ru.fizteh.fivt.students.chernigovsky.junit;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MyTable implements ExtendedTable {
    private HashMap<String, String> hashMap;
    private HashMap<String, String> newEntries;
    private HashMap<String, String> changedEntries;
    private HashMap<String, String> removedEntries;
    boolean autoCommit;
    String tableName;

    public Set<Map.Entry<String, String>> getEntrySet() {
        return hashMap.entrySet();
    }

    public MyTable(String name, boolean flag) {
        tableName = name;
        autoCommit = flag;
        hashMap = new HashMap<String, String>();
        newEntries = new HashMap<String, String>();
        changedEntries = new HashMap<String, String>();
        removedEntries = new HashMap<String, String>();
    }

    public String getName(){
        return tableName;
    }

    public int getDiffCount() {
        return newEntries.size() + changedEntries.size() + removedEntries.size();
    }

    /**
     * Получает значение по указанному ключу.
     *
     * @param key Ключ.
     * @return Значение. Если не найдено, возвращает null.
     *
     * @throws IllegalArgumentException Если значение параметра key является null.
     */
    public String get(String key) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("key is null");
        }
        if (removedEntries.get(key) != null) {
            return null;
        }
        if (newEntries.get(key) != null) {
            return newEntries.get(key);
        }
        if (changedEntries.get(key) != null) {
            return changedEntries.get(key);
        }
        return hashMap.get(key);
    }

    /**
     * Устанавливает значение по указанному ключу.
     *
     * @param key Ключ.
     * @param value Значение.
     * @return Значение, которое было записано по этому ключу ранее. Если ранее значения не было записано,
     * возвращает null.
     *
     * @throws IllegalArgumentException Если значение параметров key или value является null.
     */

    private String putting(String key, String value) {
        if (hashMap.get(key) == null) {
            return newEntries.put(key, value);
        } else {
            if (hashMap.get(key).equals(value)) {
                if (changedEntries.get(key) != null) {
                    return changedEntries.remove(key);
                }
                if (removedEntries.get(key) != null) {
                    removedEntries.remove(key);
                    return null;
                }
                return value;
            }
            if (removedEntries.get(key) != null) {
                removedEntries.remove(key);
                changedEntries.put(key, value);
                return null;
            }
            if (changedEntries.get(key) == null) {
                changedEntries.put(key, value);
                return hashMap.get(key);
            } else {
                return changedEntries.put(key, value);
            }
        }

    }

    public String put(String key, String value) {
        if (key == null || key.trim().isEmpty()) {
            throw new IllegalArgumentException("key is null");
        }
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("value is null");
        }

        String ans = putting(key, value);
        if (autoCommit) {
            commit();
        }
        return ans;

    }

    /**
     * Удаляет значение по указанному ключу.
     *
     * @param key Ключ.
     * @return Значение. Если не найдено, возвращает null.
     *
     * @throws IllegalArgumentException Если значение параметра key является null.
     */

    private String removing(String key) {
        if (removedEntries.get(key) != null) {
            return null;
        }
        if (newEntries.get(key) != null) {
            return newEntries.remove(key);
        }
        if (changedEntries.get(key) != null) {
            removedEntries.put(key, hashMap.get(key));
            return changedEntries.remove(key);
        }
        if (hashMap.get(key) != null) {
            removedEntries.put(key, hashMap.get(key));
            return hashMap.get(key);
        }
        return null;
    }

    public String remove(String key) {
        if (key == null) {
            throw new IllegalArgumentException("key is null");
        }

        String ans = removing(key);
        if (autoCommit) {
            commit();
        }
        return ans;

    }

    /**
     * Возвращает количество ключей в таблице.
     *
     * @return Количество ключей в таблице.
     */
    public int size() {
        return hashMap.size() - removedEntries.size() + newEntries.size();
    }

    /**
     * Выполняет фиксацию изменений.
     *
     * @return Количество сохранённых ключей.
     */
    public int commit() {
        int changed = getDiffCount();

        for (Map.Entry<String, String> entry : newEntries.entrySet()) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        newEntries.clear();

        for (Map.Entry<String, String> entry : changedEntries.entrySet()) {
            hashMap.put(entry.getKey(), entry.getValue());
        }
        changedEntries.clear();

        for (Map.Entry<String, String> entry : removedEntries.entrySet()) {
            hashMap.remove(entry.getKey());
        }
        removedEntries.clear();

        return changed;
    }

    /**
     * Выполняет откат изменений с момента последней фиксации.
     *
     * @return Количество отменённых ключей.
     */
    public int rollback() {
        int changed = getDiffCount();

        newEntries.clear();
        changedEntries.clear();
        removedEntries.clear();

        return changed;
    }
}
