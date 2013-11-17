package ru.fizteh.fivt.students.nadezhdakaratsapova.storeable.junittests;

import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.students.nadezhdakaratsapova.shell.CommandUtils;
import ru.fizteh.fivt.students.nadezhdakaratsapova.storeable.StoreableDataValue;
import ru.fizteh.fivt.students.nadezhdakaratsapova.storeable.StoreableTableProvider;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class StoreableTableProviderTest {
    private static final String TESTED_DIRECTORY = "JavaTests";
    private static final String TESTED_TABLE = "MyFavouriteTable";
    private StoreableTableProvider tableProvider;
    private File testedFile = new File(TESTED_DIRECTORY);
    List<Class<?>> types;

    @Before
    public void setUp() throws Exception {
        if (!testedFile.exists()) {
            testedFile.mkdir();
        }
        tableProvider = new StoreableTableProvider(testedFile);
        types = new ArrayList<Class<?>>();
        types.add(Integer.class);
        types.add(String.class);
        types.add(Boolean.class);

    }

    @After
    public void tearDown() throws Exception {
        if (tableProvider.getTable(TESTED_TABLE) != null) {
            tableProvider.removeTable(TESTED_TABLE);
        }
        CommandUtils.recDeletion(testedFile);
    }

    @Test
    public void getValidTable() throws Exception {
        tableProvider.createTable(TESTED_TABLE, types);
        Assert.assertEquals(tableProvider.getTable(TESTED_TABLE).getName(), TESTED_TABLE);
        Assert.assertEquals(tableProvider.getTable(TESTED_TABLE).getName(), TESTED_TABLE);
        tableProvider.removeTable(TESTED_TABLE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNullTableShouldFail() throws Exception {
        tableProvider.getTable(null);
    }

    @Test(expected = RuntimeException.class)
    public void getNotValidNameTableShouldFail() throws Exception {
        tableProvider.getTable("((((()))))))))");
    }

    @Test
    public void createValidTable() throws Exception {
        Assert.assertEquals(tableProvider.createTable(TESTED_TABLE, types).getName(), TESTED_TABLE);
        tableProvider.removeTable(TESTED_TABLE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullNameTableShouldFail() throws Exception {
        tableProvider.createTable(null, types);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createNullColumnsTableShouldFail() throws Exception {
        tableProvider.createTable(TESTED_TABLE, null);
    }

    @Test
    public void removeValidTable() throws Exception {
        tableProvider.createTable(TESTED_TABLE, types);
        tableProvider.removeTable(TESTED_TABLE);
        Assert.assertNull(tableProvider.getTable(TESTED_TABLE));
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullNameTable() throws Exception {
        tableProvider.removeTable(null);
    }

    @Test(expected = IllegalStateException.class)
    public void removeNotExistingTable() throws Exception {
        tableProvider.removeTable(TESTED_TABLE);
    }

    @Test
    public void testDeserialize() throws Exception {
        Table table = tableProvider.createTable(TESTED_TABLE, types);
        Assert.assertEquals(tableProvider.deserialize(table, "<row><col>5</col><col>qwerty</col><col>true</col></row>").getIntAt(0).intValue(), 5);
        Assert.assertEquals(tableProvider.deserialize(table, "<row><col>5</col><col>qwerty</col><col>true</col></row>").getBooleanAt(2).booleanValue(), true);
        Assert.assertEquals(tableProvider.deserialize(table, "<row><col>5</col><col>qwerty</col><col>true</col></row>").getStringAt(1), "qwerty");
        tableProvider.removeTable(TESTED_TABLE);
    }

    @Test(expected = ParseException.class)
    public void testBadDeserialize() throws Exception {
        Table table = tableProvider.createTable(TESTED_TABLE, types);
        Assert.assertEquals(tableProvider.deserialize(table, "<row><col>5</col><col></col><col>true</col></row>").getIntAt(0).intValue(), 5);
    }

    @Test
    public void testSerialize() throws Exception {
        Table table = tableProvider.createTable(TESTED_TABLE, types);
        Storeable storeable = new StoreableDataValue(types);
        storeable.setColumnAt(0, 89);
        storeable.setColumnAt(1, "cat");
        storeable.setColumnAt(2, false);
        Assert.assertEquals(tableProvider.serialize(table, storeable), "<row><col>89</col><col>cat</col><col>false</col></row>");
    }

    @Test(expected = ColumnFormatException.class)
    public void testBadSerialize() throws Exception {
        Table table = tableProvider.createTable(TESTED_TABLE, types);
        List<Class<?>> newTypes = new ArrayList<Class<?>>();
        newTypes.add(String.class);
        newTypes.add(Double.class);
        newTypes.add(Byte.class);
        Storeable storeable = new StoreableDataValue(newTypes);
        storeable.setColumnAt(0, "fox");
        storeable.setColumnAt(1, 78.45);
        storeable.setColumnAt(2, true);
        tableProvider.serialize(table, storeable);
    }

    @Test
    public void testCreateFor() throws Exception {
        Table table = tableProvider.createTable(TESTED_TABLE, types);
        Storeable storeable = tableProvider.createFor(table);
        Assert.assertNull(storeable.getIntAt(0));
        Assert.assertNull(storeable.getStringAt(1));
        Assert.assertNull(storeable.getBooleanAt(2));
        tableProvider.removeTable(TESTED_TABLE);
    }

    @Test
    public void createForValid() throws Exception {
        Table table = tableProvider.createTable(TESTED_TABLE, types);
        List<Object> values = new ArrayList<Object>();
        values.add(new Integer(56));
        values.add(new String("moo"));
        values.add(new Boolean(true));
        Storeable storeable = tableProvider.createFor(table, values);
        Assert.assertEquals(storeable.getIntAt(0).intValue(), 56);
        Assert.assertEquals(storeable.getStringAt(1), "moo");
        Assert.assertEquals(storeable.getBooleanAt(2).booleanValue(), true);
        tableProvider.removeTable(TESTED_TABLE);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void createForNotValidColumnsCountShouldFail() throws Exception {
        Table table = tableProvider.createTable(TESTED_TABLE, types);
        List<Object> values = new ArrayList<Object>();
        values.add(new Integer(56));
        values.add(new String("moo"));
        values.add(new Boolean(true));
        values.add(new Integer(125));
        Storeable storeable = tableProvider.createFor(table, values);
    }

    @Test(expected = ColumnFormatException.class)
    public void createForNotValidColumnsFormatShouldFail() throws Exception {
        Table table = tableProvider.createTable(TESTED_TABLE, types);
        List<Object> values = new ArrayList<Object>();
        values.add(new Integer(56));
        values.add(new String("moo"));
        values.add(new String("true"));
        Storeable storeable = tableProvider.createFor(table, values);
    }
}
