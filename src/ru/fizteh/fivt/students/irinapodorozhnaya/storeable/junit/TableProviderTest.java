package ru.fizteh.fivt.students.irinapodorozhnaya.storeable.junit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.storage.structured.TableProvider;
import ru.fizteh.fivt.students.irinapodorozhnaya.shell.CommandRemove;
import ru.fizteh.fivt.students.irinapodorozhnaya.storeable.MyTableProviderFactory;
import ru.fizteh.fivt.students.irinapodorozhnaya.utils.Utils;

public class TableProviderTest {
    
    private static final String DATA_BASE_DIR = "./src/ru/fizteh/fivt/students/irinapodorozhnaya/test";
    private final File curDir = new File(DATA_BASE_DIR);
    private TableProvider provider;
    private List<Class<?>> list;
    
    @Before
    public void setUp() throws Exception {
        list  = new ArrayList<>();
        list.add(Integer.class);
        
        curDir.mkdirs();
        provider = new MyTableProviderFactory().create(DATA_BASE_DIR);
        
    }
    
    @After
    public void tearDown() {
        CommandRemove.deleteRecursivly(curDir);
    }

    @Test
    public void testCreateTable() throws Exception {
        Assert.assertNotNull(provider.createTable("createTable", list));
        Assert.assertNull(provider.createTable("createTable", list));
        provider.removeTable("createTable");
        Assert.assertNotNull(provider.createTable("createTable", list));
    }
    
    @Test
    public void testCreateSignature() throws Exception {
        
        list.add(Double.class);
        provider.createTable("createTable", list);        
        File sign = new File(new File(curDir, "createTable"), "signature.tsv");
        Assert.assertTrue(sign.isFile());
        
        Scanner sc = new Scanner(sign);
        List<Class<?>> columns = new ArrayList<>();
        
        while (sc.hasNextLine()) {
            columns.add(Utils.detectClass(sc.nextLine()));
        }
        sc.close();
        Assert.assertEquals(columns, list);
        
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTableNullList() throws Exception {
        provider.createTable("createTable", null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateTableIllegalType() throws Exception {
        list.add(provider.getClass());
        provider.createTable("createTable", list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTableIllegalSymbols() throws Exception {
        provider.createTable("%:^*(&^i", list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTableIllegalSymbols() throws Exception {
        provider.getTable("O*^//*"); 
    }

    @Test
    public void testGetCreateTableReference() throws Exception {
        Table table = provider.createTable("getCreateTableReference", list);
        Assert.assertNotNull(table);
        Assert.assertNotNull(provider.getTable("getCreateTableReference"));
        Assert.assertSame(provider.getTable("getCreateTableReference"), table);
        Assert.assertSame(provider.getTable("getCreateTableReference"), 
                          provider.getTable("getCreateTableReference"));
    }

    @Test
    public void testGetNonExistingTable() {
        Assert.assertNull(provider.getTable("nonExictingTable"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNullTable() throws Exception {
        provider.getTable(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNullTable() throws Exception {
        provider.createTable(null, list);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullTable() throws Exception {
        provider.removeTable(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveNotExistingTable() throws Exception {
        provider.removeTable("notExistingTable");
    }

}
