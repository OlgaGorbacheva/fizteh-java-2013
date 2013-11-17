package ru.fizteh.fivt.students.kislenko.storeable.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.fizteh.fivt.students.kislenko.junit.test.Cleaner;
import ru.fizteh.fivt.students.kislenko.storeable.MyTableProvider;
import ru.fizteh.fivt.students.kislenko.storeable.MyTableProviderFactory;

import java.io.File;
import java.util.ArrayList;

public class MyTableProviderTest {
    private static MyTableProvider provider;
    private static File databaseDir = new File("database");
    private static ArrayList<Class<?>> typeList = new ArrayList<Class<?>>();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        MyTableProviderFactory factory = new MyTableProviderFactory();
        databaseDir.mkdir();
        provider = factory.create("database");
        typeList.add(Integer.class);
        typeList.add(Integer.class);
        typeList.add(Integer.class);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        Cleaner.clean(databaseDir);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEmptyNameTable() throws Exception {
        provider.createTable("", typeList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTableWithDot() throws Exception {
        provider.createTable(".", typeList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTableWithDots() throws Exception {
        provider.createTable("..", typeList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNullNameTable() throws Exception {
        provider.createTable(null, typeList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNullTypeListTable() throws Exception {
        provider.createTable("goodTable", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithBadTypeListTable() throws Exception {
        ArrayList<Class<?>> badTypeList = new ArrayList<Class<?>>();
        badTypeList.add(StringBuilder.class);
        provider.createTable("tableBetterThanTheLastOne", badTypeList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEmptyTypeListTable() throws Exception {
        ArrayList<Class<?>> empty = new ArrayList<Class<?>>();
        provider.createTable("theBestTable", empty);
    }

    @Test
    public void testCreateNormalTable() throws Exception {
        provider.createTable("definitelyTheBestTable", typeList);
    }

    @Test
    public void testCreateExistingTable() throws Exception {
        provider.createTable("definitelyTheBestTable", typeList);
    }
}
