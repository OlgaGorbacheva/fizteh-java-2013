package ru.fizteh.fivt.students.vlmazlov.storeable;

import ru.fizteh.fivt.storage.structured.Storeable;
import ru.fizteh.fivt.students.vlmazlov.shell.Shell;
import ru.fizteh.fivt.students.vlmazlov.shell.WrongCommandException;
import ru.fizteh.fivt.students.vlmazlov.shell.CommandFailException;
import ru.fizteh.fivt.students.vlmazlov.shell.Command;
import ru.fizteh.fivt.students.vlmazlov.shell.UserInterruptionException;
import ru.fizteh.fivt.students.vlmazlov.shell.ExitCommand;
import ru.fizteh.fivt.students.vlmazlov.multifilemap.ValidityCheckFailedException;
import ru.fizteh.fivt.students.vlmazlov.multifilemap.PutCommand;
import ru.fizteh.fivt.students.vlmazlov.multifilemap.GetCommand;
import ru.fizteh.fivt.students.vlmazlov.multifilemap.DropCommand;
import ru.fizteh.fivt.students.vlmazlov.multifilemap.UseCommand;
import ru.fizteh.fivt.students.vlmazlov.multifilemap.RemoveCommand;
import ru.fizteh.fivt.students.vlmazlov.multifilemap.DataBaseState;
import ru.fizteh.fivt.students.vlmazlov.multifilemap.CommitCommand;
import ru.fizteh.fivt.students.vlmazlov.multifilemap.RollBackCommand;
import ru.fizteh.fivt.students.vlmazlov.multifilemap.SizeCommand;
import java.io.IOException;
import java.io.FileNotFoundException;		

public class Main {
	public static void main(String[] args) {

		StoreableTableProviderFactory factory = new StoreableTableProviderFactory();
		DataBaseState<Storeable, StoreableTable> state = null;

		try {
			state = new DataBaseState(factory.create(System.getProperty("fizteh.db.dir")));
		} catch (IllegalArgumentException ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
			System.exit(2);
		}
		
		try {
			state.getProvider().read();
		} catch (IOException ex) {
			System.err.println("Unable to retrieve database: " + ex.getMessage());
			System.exit(3);
		} catch (ValidityCheckFailedException ex) {
			System.err.println("Validity check failed: " + ex.getMessage());
			System.exit(4);
		}

		Command[] commands = {
			new GetCommand(), new PutCommand(), 
			new RemoveCommand(), new ExitCommand(),
			new UseCommand(), new CreateStoreableCommand(),
			new DropCommand(), new CommitCommand(),
			new RollBackCommand(), new SizeCommand()
		};

		Shell<DataBaseState> shell = new Shell<DataBaseState>(commands, state);

		try {
			shell.process(args);
		} catch (WrongCommandException ex) {
			System.err.println(ex.getMessage());
			System.exit(5);
		} catch (CommandFailException ex) {
			System.err.println(ex.getMessage());
			System.exit(6);
		} catch (IllegalArgumentException ex) {
			System.err.println(ex.getMessage());
			System.exit(7);
		} catch (UserInterruptionException ex) {
		}

		try {
			state.getProvider().write();
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
			System.exit(8);
		} catch (ValidityCheckFailedException ex) {
			System.err.println("Validity check failed: " + ex.getMessage());
			System.exit(9);
		}
	}
}