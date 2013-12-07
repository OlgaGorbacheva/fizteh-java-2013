package ru.fizteh.fivt.students.olgagorbacheva.storable.test;

import static org.junit.Assert.*;

import java.awt.geom.Arc2D.Double;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import ru.fizteh.fivt.students.olgagorbacheva.storable.StorableTable;
import ru.fizteh.fivt.students.olgagorbacheva.storable.Storable;

public class StorableTest {

      @BeforeClass
      public static void setUpBeforeClass() throws Exception {
            String dir = System.getProperty("fizteh.db.dir");
            new File(dir, "tableName").mkdir();
            List<Class<?>> types = new ArrayList<>();
            types.add(String.class);
            types.add(Integer.class);
            types.add(Long.class);
            types.add(Boolean.class);
            types.add(Double.class);
            StorableTable table = new StorableTable("tableName", new File(dir, "tableName"), types, null);
            Storable tests_st= new Storable(); 
      }

      @Test
      public void testTypeCheck() {
            fail("Not yet implemented");
      }

      @Test
      public void testIndexCheck() {
            fail("Not yet implemented");
      }

      @Test
      public void testSetColumnAt() {
            fail("Not yet implemented");
      }

      @Test
      public void testGetColumnAt() {
            fail("Not yet implemented");
      }

      @Test
      public void testGetIntAt() {
            fail("Not yet implemented");
      }

      @Test
      public void testGetLongAt() {
            fail("Not yet implemented");
      }

      @Test
      public void testGetByteAt() {
            fail("Not yet implemented");
      }

      @Test
      public void testGetFloatAt() {
            fail("Not yet implemented");
      }

      @Test
      public void testGetDoubleAt() {
            fail("Not yet implemented");
      }

      @Test
      public void testGetBooleanAt() {
            fail("Not yet implemented");
      }

      @Test
      public void testGetStringAt() {
            fail("Not yet implemented");
      }

}
