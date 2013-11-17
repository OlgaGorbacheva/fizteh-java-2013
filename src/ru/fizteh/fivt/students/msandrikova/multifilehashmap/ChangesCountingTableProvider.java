package ru.fizteh.fivt.students.msandrikova.multifilehashmap;

import ru.fizteh.fivt.storage.strings.TableProvider;

public interface ChangesCountingTableProvider extends TableProvider {
	@Override
	ChangesCountingTable getTable(String name);

	@Override
	ChangesCountingTable createTable(String name);
}
