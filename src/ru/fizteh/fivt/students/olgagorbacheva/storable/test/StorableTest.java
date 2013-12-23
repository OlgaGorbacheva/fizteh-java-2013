package ru.fizteh.fivt.students.olgagorbacheva.storable.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ru.fizteh.fivt.storage.structured.ColumnFormatException;
import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.storage.structured.Table;
import ru.fizteh.fivt.students.olgagorbacheva.storable.StorableTableProvider;
import ru.fizteh.fivt.students.olgagorbacheva.storable.StorableTableProviderFactory;

public class StorableTest {

      static Storeable storable;
      static Table table;
      static StorableTableProvider provider;
      static StorableTableProviderFactory factory;
      static String dir = System.getProperty("fizteh.db.dir");
      
      @BeforeClass
      public static void setUpBeforeClass() throws Exception {
            List<Class<?>> types = new ArrayList<>();
            types.add(String.class);
            types.add(Integer.class);
            types.add(Boolean.class);
            types.add(String.class);
            types.add(Double.class);
            factory = new StorableTableProviderFactory();
            provider = factory.create(dir);
            table = provider.createTable("table", types);
            storable = provider.createFor(table);
      }

      @AfterClass
      public static void tearDownAfterClass() throws Exception {
            provider.removeTable("table");
      }
      
      @Test (expected = IndexOutOfBoundsException.class)
      public void testSetColumnAtOutOfBounds() {
            storable.setColumnAt(10, "какая-то хрень");
      }
      
      @Test (expected = ColumnFormatException.class)
      public void testSetColumnAtOtherType() {
            storable.setColumnAt(1, "какая-то хрень");
      }
      
      @Test 
      public void testSetColumnAt() {
            storable.setColumnAt(0, "какая-то хрень");
            storable.setColumnAt(1, 0);
            storable.setColumnAt(2, true);
            storable.setColumnAt(3, "новая хрень");
            storable.setColumnAt(4, null);
      }

      @Test (expected = IndexOutOfBoundsException.class)
      public void testGetColumnAtOutOfBounds() {
            storable.getColumnAt(10);
      }
      
      @Test
      public void testGetColumnAt() {
            Assert.assertNull(storable.getColumnAt(4));
            Assert.assertEquals(true, storable.getColumnAt(2));
      }

      @Test (expected = ColumnFormatException.class)
      public void testGetIntAt() {
            storable.getIntAt(0);
      }

      @Test
      public void testGetDoubleAt() {
            Assert.assertNull(storable.getDoubleAt(4));
      }


      @Test
      public void testGetStringAt() {
            Assert.assertEquals("какая-то хрень", storable.getStringAt(0));
      }

}
