package ru.fizteh.fivt.students.nadezhdakaratsapova.shell;

import java.io.IOException;

public class ExitCommand implements Command {

    public String getName() {
        return "exit";
    }

    public void execute(String[] args) {
        System.exit(0);
    }

    public boolean compareArgsCount(int inputArgsCount) {
        return (inputArgsCount == 0);
    }
}
