package ru.fizteh.fivt.students.nadezhdakaratsapova.tableutils;

import ru.fizteh.fivt.students.nadezhdakaratsapova.filemap.DataTable;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class UniversalDataTable<ValueType> {
    public static final int DIR_COUNT = 16;
    public static final int FILE_COUNT = 16;

    protected File dataBaseDirectory;
    protected String tableName;
    private Map<String, ValueType> dataStorage = new HashMap<String, ValueType>();

    private Map<String, ValueType> putKeys = new HashMap<String, ValueType>();
    private Set<String> removeKeys = new HashSet<String>();
    public ValueConverter<ValueType> valueConverter;

    public UniversalDataTable() {

    }

    public UniversalDataTable(String name) {
        tableName = name;
    }

    public UniversalDataTable(String name, File dir, ValueConverter converter) {
        tableName = name;
        dataBaseDirectory = dir;
        valueConverter = converter;
    }

    public String getName() {
        return tableName;
    }

    protected ValueType putSimple(String key, ValueType value) {
        ValueType oldValue = null;
        if (!removeKeys.contains(key)) {
            if ((oldValue = putKeys.get(key)) == null) {
                oldValue = dataStorage.get(key);
                if (oldValue == null) {
                    putKeys.put(key, value);
                } else {
                    if (!oldValue.equals(value)) {
                        putKeys.put(key, value);
                    }
                }
            } else {
                ValueType dataValue = dataStorage.get(key);
                if (dataValue == null) {
                    putKeys.put(key, value);
                } else {
                    if (!dataStorage.get(key).equals(value)) {
                        putKeys.put(key, value);
                    } else {
                        putKeys.remove(key);
                    }
                }

            }
        } else {
            ValueType dataValue = dataStorage.get(key);
            if (!dataValue.equals(value)) {
                putKeys.put(key, value);
            }
            removeKeys.remove(key);
        }
        return oldValue;
    }

    public Set<String> getKeys() {
        return dataStorage.keySet();
    }

    public ValueType get(String key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("Not correct key");
        }
        ValueType value = null;
        if (!putKeys.isEmpty()) {
            if (putKeys.containsKey(key)) {
                return putKeys.get(key);
            }
        }
        if (!removeKeys.contains(key)) {
            value = dataStorage.get(key);
            if (value == null) {
                value = putKeys.get(key);
            }
        }
        return value;
    }

    public ValueType remove(String key) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("Not correct key");
        }
        if (!putKeys.isEmpty()) {
            if (putKeys.get(key) != null) {
                if (dataStorage.get(key) != null) {
                    removeKeys.add(key);
                }
                return putKeys.remove(key);
            }
        }
        if (!removeKeys.isEmpty()) {
            if (removeKeys.contains(key)) {
                return null;
            }
        }
        ValueType value;
        if ((value = dataStorage.get(key)) != null) {
            removeKeys.add(key);
        }
        return value;
    }

    public boolean isEmpty() {
        return dataStorage.isEmpty();
    }

    public int size() {
        int size = dataStorage.size();
        Set<String> keysToCommit = putKeys.keySet();
        for (String key : keysToCommit) {
            if (!dataStorage.containsKey(key)) {
                ++size;
            }
        }
        size -= removeKeys.size();
        return size;
    }

    public int commit() {
        int commitSize = 0;
        if (!putKeys.isEmpty()) {
            Set<String> putKeysToCommit = putKeys.keySet();
            for (String key : putKeysToCommit) {
                dataStorage.put(key, putKeys.get(key));
                ++commitSize;
            }
            putKeys.clear();
        }
        if (!removeKeys.isEmpty()) {
            for (String key : removeKeys) {
                dataStorage.remove(key);
                ++commitSize;
            }
            removeKeys.clear();
        }
        return commitSize;
    }

    public int rollback() {
        int rollbackSize = 0;
        if (!putKeys.isEmpty()) {
            rollbackSize += putKeys.size();
            Set<String> putKeysToRollback = putKeys.keySet();
            for (String key : putKeysToRollback) {
                dataStorage.containsKey(key);
            }
            putKeys.clear();
        }
        if (!removeKeys.isEmpty()) {
            rollbackSize += removeKeys.size();
            removeKeys.clear();
        }
        return rollbackSize;
    }

    public int commitSize() {
        return putKeys.size() + removeKeys.size();
    }

    public File getWorkingDirectory() {
        return dataBaseDirectory;
    }

    protected void universalLoad() throws IOException, ParseException {
        File curTable = new File(dataBaseDirectory, tableName);
        curTable = curTable.getCanonicalFile();
        File[] dirs = curTable.listFiles();
        if (dirs.length > DIR_COUNT) {
            throw new IOException("The table includes more than " + DIR_COUNT + " directories");
        }
        for (File d : dirs) {
            if (!d.isDirectory() && (!d.getName().equals("signature.tsv"))) {
                throw new IOException(tableName + " should include only directories");
            }
            if (!d.getName().equals("signature.tsv")) {
                File[] files = d.listFiles();
                if ((files.length - 1) > FILE_COUNT) {
                    throw new IOException("The directory includes more than " + FILE_COUNT + " files");
                }
                String dirName = d.getName();
                char firstChar = dirName.charAt(0);
                char secondChar;
                int dirNumber;
                if (dirName.length() > 1) {
                    secondChar = dirName.charAt(1);
                } else {
                    throw new IllegalArgumentException("Not allowed name of directory in table");
                }
                if (Character.isDigit(firstChar)) {
                    if (Character.isDigit(secondChar)) {
                        dirNumber = Integer.parseInt(dirName.substring(0, 2));
                    } else {
                        dirNumber = Integer.parseInt(dirName.substring(0, 1));
                    }
                } else {
                    throw new IllegalArgumentException("Not allowed name of directory in table");
                }
                if (!dirName.equals(new String(dirNumber + ".dir"))) {
                    throw new IllegalArgumentException("Not allowed name of directory in table");
                }
                for (File f : files) {
                    if (!f.isFile()) {
                        throw new IOException("Unexpected directory");
                    }
                    String fileName = f.getName();
                    char firstFileChar = fileName.charAt(0);
                    char secondFileChar;
                    int fileNumber;
                    if (fileName.length() > 1) {
                        secondFileChar = fileName.charAt(1);
                    } else {
                        throw new IllegalArgumentException("Not allowed name of file in table");
                    }
                    if (Character.isDigit(firstFileChar)) {
                        if (Character.isDigit(secondFileChar)) {
                            fileNumber = Integer.parseInt(fileName.substring(0, 2));
                        } else {
                            fileNumber = Integer.parseInt(fileName.substring(0, 1));
                        }
                    } else {
                        throw new IllegalArgumentException("Not allowed name of file in table");
                    }
                    if (!fileName.equals(new String(fileNumber + ".dat"))) {
                        throw new IllegalArgumentException("Not allowed name of file in table");
                    }
                    FileReader fileReader = new FileReader(f, this);
                    while (fileReader.checkingLoadingConditions()) {
                        String key = fileReader.getNextKey();
                        int hashByte = Math.abs(key.getBytes()[0]);
                        int ndirectory = hashByte % DIR_COUNT;
                        int nfile = (hashByte / DIR_COUNT) % FILE_COUNT;
                        if (ndirectory != dirNumber) {
                            throw new IllegalArgumentException("Wrong key in " + dirName);
                        }
                        if (fileNumber != nfile) {
                            throw new IllegalArgumentException("Wrong key in" + fileName);
                        }
                    }
                    while (fileReader.valuesToReadExists()) {
                        fileReader.putValueToTable(valueConverter.convertStringToValueType(fileReader.getNextValue()));
                    }
                    fileReader.closeResources();
                }
            }
        }
    }

    protected void writeToDataBaseWithoutSignature() throws IOException {
        rollback();
        Set<String> keys = getKeys();
        if (!keys.isEmpty()) {
            for (int i = 0; i < DIR_COUNT; ++i) {
                File dir = new File(new File(dataBaseDirectory, tableName), new String(i + ".dir"));
                for (int j = 0; j < FILE_COUNT; ++j) {
                    DataTable keysToFile = new DataTable();
                    File file = new File(dir, new String(j + ".dat"));
                    for (String key : keys) {
                        int hashByte = Math.abs(key.getBytes(StandardCharsets.UTF_8)[0]);
                        int ndirectory = hashByte % DIR_COUNT;
                        int nfile = (hashByte / DIR_COUNT) % FILE_COUNT;
                        if ((ndirectory == i) && (nfile == j)) {
                            if (!dir.getCanonicalFile().exists()) {
                                dir.getCanonicalFile().mkdir();
                            }
                            if (!file.getCanonicalFile().exists()) {
                                file.getCanonicalFile().createNewFile();
                            }
                            keysToFile.put(key, valueConverter.convertValueTypeToString(get(key)));
                            keysToFile.commit();
                        }
                    }

                    if (!keysToFile.isEmpty()) {
                        FileWriter fileWriter = new FileWriter();
                        fileWriter.writeDataToFile(file.getCanonicalFile(), keysToFile);
                    } else {
                        if (file.getCanonicalFile().exists()) {
                            file.getCanonicalFile().delete();
                        }
                    }
                }
                if (dir.getCanonicalFile().listFiles() == null) {
                    dir.delete();
                }
            }
        }
    }

    public abstract ValueType put(String key, ValueType value);

    public abstract void load() throws IOException, ParseException;

    public abstract void writeToDataBase() throws IOException;
}
