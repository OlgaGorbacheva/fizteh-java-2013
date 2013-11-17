package ru.fizteh.fivt.students.adanilyak.tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.fizteh.fivt.storage.strings.Table;
import ru.fizteh.fivt.students.adanilyak.multifilehashmap.MultiFileTableProvider;

import java.io.File;
import java.io.IOException;

/**
 * User: Alexander
 * Date: 26.10.13
 * Time: 18:32
 */
public class MultiFileTableUnitTest {
    MultiFileTableProvider tableProvider;
    Table testTableEng;
    Table testTableRus;

    @Before
    public void setUpTestObject() throws IOException {
        tableProvider = new MultiFileTableProvider(new File("/Users/Alexander/Documents/JavaDataBase/Tests"));
        testTableEng = tableProvider.createTable("testTable9");
        testTableRus = tableProvider.createTable("тестоваяТаблица10");
    }

    @After
    public void tearDownTestObject() {
        tableProvider.removeTable("testTable9");
        tableProvider.removeTable("тестоваяТаблица10");
    }

    /**
     * TEST BLOCK
     * GET NAME TESTS
     */

    @Test
    public void getNameTest() {
        Assert.assertEquals("testTable9", testTableEng.getName());
        Assert.assertEquals("тестоваяТаблица10", testTableRus.getName());
    }

    /**
     * TEST BLOCK
     * GET TESTS
     */

    @Test
    public void getTest() {
        testTableEng.put("key", "value");
        Assert.assertEquals("value", testTableEng.get("key"));
        Assert.assertNull(testTableEng.get("nonExictingKey"));
        testTableEng.remove("key");

        testTableRus.put("ключ", "значение");
        Assert.assertEquals("значение", testTableRus.get("ключ"));
        Assert.assertNull(testTableRus.get("несуществующийКлюч"));
        testTableRus.remove("ключ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNullTest() {
        testTableEng.get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getEmptyTest() {
        testTableEng.get("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNlTest() {
        testTableEng.get("    ");
    }

    /**
     * TEST BLOCK
     * PUT TESTS
     */

    @Test
    public void putTest() {
        Assert.assertNull(testTableEng.put("key", "value"));
        Assert.assertEquals("value", testTableEng.put("key", "value"));
        Assert.assertEquals("value", testTableEng.put("key", "anotherValue"));
        testTableEng.remove("key");

        Assert.assertNull(testTableRus.put("ключ", "значение"));
        Assert.assertEquals("значение", testTableRus.put("ключ", "значение"));
        Assert.assertEquals("значение", testTableRus.put("ключ", "другоеЗначение"));
        testTableRus.remove("ключ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void putNullKeyTest() {
        testTableEng.put(null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void putNullValueTest() {
        testTableEng.put("key", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void putNullBothTest() {
        testTableEng.put(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void putEmptyKeyTest() {
        testTableEng.put("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void putNlKeyTest() {
        testTableEng.put("    ", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void putEmptyValueTest() {
        testTableEng.put("key", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void putNlValueTest() {
        testTableEng.put("key", "    ");
    }

    /**
     * TEST BLOCK
     * REMOVE TESTS
     */

    @Test
    public void removeTest() {
        testTableEng.put("key", "value");
        Assert.assertNull(testTableEng.remove("nonExictingKey"));
        Assert.assertEquals("value", testTableEng.remove("key"));

        testTableEng.put("ключ", "значение");
        Assert.assertNull(testTableEng.remove("несуществующийКлюч"));
        Assert.assertEquals("значение", testTableEng.remove("ключ"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNullTest() {
        testTableEng.remove(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeEmptyTest() {
        testTableEng.remove("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeNlTest() {
        testTableEng.remove("    ");
    }

    /**
     * TEST BLOCK
     * SIZE TESTS
     */

    @Test
    public void sizeTest() {
        Assert.assertEquals(0, testTableEng.size());
        testTableEng.put("key1", "value1");
        Assert.assertEquals(1, testTableEng.size());
        testTableEng.put("key2", "value2");
        testTableEng.put("key3", "value3");
        Assert.assertEquals(3, testTableEng.size());
        testTableEng.put("key4", "value4");
        testTableEng.put("key5", "value5");
        Assert.assertEquals(5, testTableEng.size());
        testTableEng.commit();
        Assert.assertEquals(5, testTableEng.size());

        for (int i = 1; i <= 5; ++i) {
            testTableRus.put("ключ" + i, "значение" + i);
        }
        Assert.assertEquals(5, testTableRus.size());
        testTableRus.rollback();
        Assert.assertEquals(0, testTableRus.size());

        for (int i = 1; i <= 5; ++i) {
            testTableEng.remove("key" + i);
        }

        for (int i = 1; i <= 5; ++i) {
            testTableRus.remove("ключ" + i);
        }
    }

    /**
     * TEST BLOCK
     * COMMIT TESTS
     */

    @Test
    public void commitTest() {
        Assert.assertEquals(0, testTableEng.commit());
        for (int i = 1; i <= 5; ++i) {
            testTableEng.put("key" + i, "value" + i);
        }
        Assert.assertEquals(5, testTableEng.commit());
        for (int i = 1; i <= 5; ++i) {
            testTableEng.remove("key" + i);
        }
    }

    @Test
    public void commitHardTest() {
        testTableEng.put("key1", "value1");
        testTableEng.put("key2", "value2");
        Assert.assertEquals(2, testTableEng.commit());
        testTableEng.put("key1", "anotherValue1");
        testTableEng.put("key3", "value3");
        testTableEng.remove("key2");
        Assert.assertEquals(3, testTableEng.commit());
        testTableEng.remove("key1");
        testTableEng.remove("key3");
    }

    /**
     * TEST BLOCK
     * ROLLBACK TESTS
     */

    @Test
    public void rollbackTest() {
        Assert.assertEquals(0, testTableEng.rollback());
        for (int i = 1; i <= 5; ++i) {
            testTableEng.put("key" + i, "value" + i);
        }
        testTableEng.commit();
        testTableEng.put("key2", "anotherValue2");
        testTableEng.put("key4", "anotherValue4");
        Assert.assertEquals(2, testTableEng.rollback());
        Assert.assertEquals("value2", testTableEng.get("key2"));
        Assert.assertEquals("value4", testTableEng.get("key4"));
        for (int i = 1; i <= 5; ++i) {
            testTableEng.remove("key" + i);
        }
    }

    @Test
    public void rollbackHardTest() {
        testTableEng.put("key1", "value1");
        testTableEng.put("key2", "value2");
        Assert.assertEquals(2, testTableEng.commit());
        testTableEng.remove("key2");
        testTableEng.put("key2", "value2");
        Assert.assertEquals(0, testTableEng.rollback());
        testTableEng.remove("key1");
        testTableEng.remove("key2");
    }
}
