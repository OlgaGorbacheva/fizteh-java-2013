package ru.fizteh.fivt.students.olgagorbacheva.storable.test;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.students.olgagorbacheva.storable.Storable;
import ru.fizteh.fivt.students.olgagorbacheva.storable.StorableTableProvider;
import ru.fizteh.fivt.students.olgagorbacheva.storable.StorableTableProviderFactory;

public class StorableTableProviderTest {

      static StorableTableProvider provider;
      static StorableTableProviderFactory factory;
      static String dir = System.getProperty("fizteh.db.dir");

      @BeforeClass
      public static void setUpBeforeClass() throws Exception {
            factory = new StorableTableProviderFactory();
            provider = factory.create(dir);
      }

      @AfterClass
      public static void tearDownAfterClass() throws Exception {
            provider.removeTable("table");
      }

      @Test (expected = IllegalArgumentException.class)
      public void testCreateWithNullTypeTable() throws IOException {
            List<Class<?>> types = new ArrayList<>();
            types.add(String.class);
            types.add(Integer.class);
            types.add(null);
            types.add(Boolean.class);
            types.add(Double.class);
            provider.createTable("table", types);
      }
      
      @Test (expected = IllegalArgumentException.class)
      public void testCreateNullTable() throws IOException {
            List<Class<?>> types = new ArrayList<>();
            types.add(String.class);
            types.add(Integer.class);
            types.add(Boolean.class);
            types.add(Double.class);
            provider.createTable(null, types);
      }
      
      @Test
      public void testCreateTable() throws IOException {
            List<Class<?>> types = new ArrayList<>();
            types.add(String.class);
            types.add(Integer.class);
            types.add(Boolean.class);
            types.add(Double.class);
            provider.createTable("table", types);
      }

      @Test(expected = IllegalArgumentException.class)
      public void testBadCreateTable() throws IOException {
            List<Class<?>> types = new ArrayList<>();
            types.add(String.class);
            types.add(Integer.class);
            types.add(Storable.class);
            types.add(Boolean.class);
            types.add(Double.class);
            provider.createTable("badTable", types);
      }

      @Test
      public void testRepeatedCreateTable() throws IOException {
            List<Class<?>> types = new ArrayList<>();
            types.add(String.class);
            types.add(Integer.class);
            types.add(Boolean.class);
            types.add(Double.class);
            Assert.assertEquals(provider.createTable("table", types), null);
      }

      @Test
      public void testDeserialize() throws ParseException {
            provider.setTable("table");
            Storeable st = new Storable(provider.currentDataBase);
            st.setColumnAt(0, "Все очень плохо");
            st.setColumnAt(1, 10);
            st.setColumnAt(2, true);
            st.setColumnAt(3, null);
            String storable = "<row><col>Все очень плохо</col><col>10</col><col>true</col><null/></row>";
            Storeable newSt = provider.deserialize(provider.currentDataBase, storable);
            for (int i = 0; i < provider.currentDataBase.getColumnsCount(); i++) {
                  Assert.assertEquals(newSt.getColumnAt(i), st.getColumnAt(i));
            }
      }

      @Test
      public void testSerialize() {
            provider.setTable("table");
            Storable st = new Storable(provider.currentDataBase);
            st.setColumnAt(0, "Все очень плохо");
            st.setColumnAt(1, 10);
            st.setColumnAt(2, true);
            st.setColumnAt(3, null);
            String storable = "<row><col>Все очень плохо</col><col>10</col><col>true</col><null/></row>";
            Assert.assertEquals(provider.serialize(provider.currentDataBase, st), storable);
      }

      // Я сомневаюсь в необходимости тестить createFor...

      @Test
      public void testCreateForTableList() {
            Storeable st = new Storable(provider.currentDataBase);
            st.setColumnAt(0, "Все очень плохо");
            st.setColumnAt(1, 10);
            st.setColumnAt(2, true);
            st.setColumnAt(3, null);
            List<Object> list = new ArrayList<>();
            list.add("Все очень плохо");
            list.add(10);
            list.add(true);
            list.add(null);
            Storeable newSt = provider.createFor(provider.currentDataBase, list);
            for (int i = 0; i < provider.currentDataBase.getColumnsCount(); i++) {
                  Assert.assertEquals(newSt.getColumnAt(i), st.getColumnAt(i));
            }
      }

}
