package ru.fizteh.fivt.students.olgagorbacheva.storable.test;

import java.io.File;
import java.io.IOException;

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
      public void createTableProviderInBadDirectory() throws IllegalArgumentException, IOException {
            factory.create("/dev");
      }
      
      @Test (expected = IllegalArgumentException.class)
      public void createTableProviderInNotExistingDirectory() throws IllegalArgumentException, IOException {
            factory.create("/home/ololo");
      }
      
      @Test (expected = IllegalArgumentException.class)
      public void createNullNameTableProvider() throws IllegalArgumentException, IOException {
            factory.create(null);
      }
      
      @Test (expected = IllegalArgumentException.class)
      public void createNlNameTableProvider() throws IllegalArgumentException, IOException {
            factory.create("");
      }
      
      @Test (expected = IllegalArgumentException.class)
      public void createNlNameTableProvider2() throws IllegalArgumentException, IOException {
            factory.create("              ");
      }
      
      @Test (expected = RuntimeException.class)
      public void createUnreadableSignature() throws IllegalArgumentException, IOException {
            new File(dir, "dir").mkdir();
            factory.create(dir);
      }
      
      @Test
      public void createTableProvider() throws IllegalArgumentException, IOException {
            new File(dir, "dir").delete();
            factory.create(dir);
      }

}
