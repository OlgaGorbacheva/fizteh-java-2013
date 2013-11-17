package ru.fizteh.fivt.students.vlmazlov.multifilemap;

import ru.fizteh.fivt.students.vlmazlov.shell.AbstractCommand;
import ru.fizteh.fivt.students.vlmazlov.shell.CommandFailException;
import java.io.OutputStream;
import java.io.IOException;

public class UseCommand extends AbstractDataBaseCommand {
	public UseCommand() {
		super("use", 1);
	}

	public void execute(String[] args, DataBaseState state, OutputStream out) throws CommandFailException {
		String tablename = args[0];
		
		if ((state.getActiveTable() != null) && (state.getActiveTable().getDiffCount() != 0)) {
			displayMessage(state.getActiveTable().getDiffCount() + " unsaved changes" + SEPARATOR, out);
			return;
		}

		try {
			if (state.getProvider().getTable(tablename) == null) {
				displayMessage(tablename + " not exists" + SEPARATOR, out);
			} else {
				state.setActiveTable(state.getProvider().getTable(tablename));
		
				displayMessage("using " + tablename + SEPARATOR, out);
			}
		} catch (IllegalArgumentException ex) {
			displayMessage("operation failed: " + ex.getMessage() + SEPARATOR, out);
			return;
		}
	}
}