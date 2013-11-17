package ru.fizteh.fivt.students.surakshina.filemap;

import org.junit.*;
import ru.fizteh.fivt.storage.strings.TableProviderFactory;

public class TableProviderFactoryTest {
    @Test(expected = IllegalArgumentException.class)
    public void testCreateNull() {
        TableProviderFactory factory = new NewTableProviderFactory();
        factory.create(null);
    }

}
