package ru.fizteh.fivt.students.olgagorbacheva.storable.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.students.olgagorbacheva.storable.StorableTableProvider;
import ru.fizteh.fivt.students.olgagorbacheva.storable.StorableTableProviderFactory;

public class StorableTableTest {

      static Table table;
      static StorableTableProvider provider;
      static StorableTableProviderFactory factory;
      static String dir = System.getProperty("fizteh.db.dir");
      
      @BeforeClass
      public static void setUpBeforeClass() throws Exception {
            factory = new StorableTableProviderFactory();
            provider = factory.create(dir);
            List<Class<?>> types = new ArrayList<>();
            types.add(String.class);
            types.add(Integer.class);
            types.add(Boolean.class);
            types.add(Double.class);
            table = provider.createTable("table", types);
            provider.setTable("table");
      }

      @AfterClass
      public static void tearDownAfterClass() throws Exception {
            provider.removeTable("table");
      }

      @Test
      public void testGetColumnsCount() {
            Assert.assertEquals(table.getColumnsCount(), 4);
      }

      @Test
      public void testGetColumnType() {
            Assert.assertEquals(table.getColumnType(0), String.class);
            Assert.assertEquals(table.getColumnType(1), Integer.class);
            Assert.assertEquals(table.getColumnType(2), Boolean.class);
            Assert.assertEquals(table.getColumnType(3), Double.class);
      }

      @Test
      public void testPutStringStoreable() {
            List<Object> list = new ArrayList<>();
            list.add("Все очень плохо");
            list.add(10);
            list.add(null);
            list.add(3.1415);
            Storeable st1 = provider.createFor(table, list);
            Storeable st = table.put("1", st1);
            Assert.assertNull(st);
            list.set(0, "Жить будем");
            st1 = provider.createFor(table, list);
            st = table.put("1", st1);
            for (int i = 0; i < provider.currentDataBase.getColumnsCount(); i++) {
                  Assert.assertEquals(table.get("1").getColumnAt(i), list.get(i));
            }
            list.set(0, "Все очень плохо");
            for (int i = 0; i < provider.currentDataBase.getColumnsCount(); i++) {
                  Assert.assertEquals(st.getColumnAt(i), list.get(i));
            }
      }

}
