package ru.fizteh.fivt.students.olgagorbacheva.storable.test;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ru.fizteh.fivt.students.olgagorbacheva.storable.StorableTableProviderFactory;

public class StorableTableProviderFactoryTest {

      static StorableTableProviderFactory factory;
      static String dir = System.getProperty("fizteh.db.dir");
      
      @BeforeClass
      public static void setUpBeforeClass() throws Exception {
            factory = new StorableTableProviderFactory();
      }

      @Test (expected = IllegalArgumentException.class)
      public void createTableProviderInBadDirectory() {
            factory.create("/dev");
      }
      
      @Test (expected = IllegalArgumentException.class)
      public void createTableProviderInNotExistingDirectory() {
            factory.create("/home/ololo");
      }
      
      @Test (expected = IllegalArgumentException.class)
      public void createNullNameTableProvider() {
            factory.create(null);
      }
      
      @Test (expected = IllegalArgumentException.class)
      public void createNlNameTableProvider() {
            factory.create("");
      }
      
      @Test (expected = IllegalArgumentException.class)
      public void createNlNameTableProvider2() {
            factory.create("              ");
      }
      
      @Test (expected = RuntimeException.class)
      public void createUnreadableSignature() {
            new File(dir, "dir").mkdir();
            factory.create(dir);
      }
      
      @Test
      public void createTableProvider() {
            factory.create(dir);
      }

      @AfterClass
      public static void tearDownAfterClass() throws Exception {
            new File(dir, "dir").delete();
      }

}
