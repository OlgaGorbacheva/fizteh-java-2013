package ru.fizteh.fivt.students.dubovpavel.multifilehashmap;

public class DataBaseMultiFileHashMapBuilder extends DataBaseBuilder<FileRepresentativeDataBase> {
    public DataBaseMultiFileHashMap construct() {
        assert(dir != null);
        return new DataBaseMultiFileHashMap(dir);
    }
}
