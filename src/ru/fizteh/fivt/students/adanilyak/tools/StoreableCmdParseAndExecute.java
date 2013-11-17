package ru.fizteh.fivt.students.adanilyak.tools;

import ru.fizteh.fivt.students.adanilyak.commands.Cmd;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * User: Alexander
 * Date: 04.11.13
 * Time: 13:48
 */
public class StoreableCmdParseAndExecute {
    public static List<String> splitByDelimiter(String cmd, String delimiter) {
        cmd.trim();
        String[] tokens = cmd.split(delimiter);
        List<String> result = new ArrayList<>();
        for (int i = 0; i < tokens.length; i++) {
            if (!tokens[i].equals("") && !tokens[i].matches("\\s+")) {
                result.add(tokens[i]);
            }
        }
        return result;
    }

    public static List<String> parseIt(String input, Map<String, Cmd> cmdList) throws IOException {
        List<String> cmdAndArgs = new ArrayList<>();
        Scanner cmdScanner = new Scanner(input);
        cmdAndArgs.add(cmdScanner.next());
        try {
            String commandName = cmdAndArgs.get(0);
            if (!cmdList.containsKey(commandName)) {
                throw new NoSuchElementException("wrong type (unknown command)");
            }

            Cmd command = cmdList.get(commandName);
            switch (commandName) {
                case "put":
                    try {
                        cmdAndArgs.add(cmdScanner.next());
                        cmdAndArgs.add(cmdScanner.findInLine(Pattern.compile("\\[.+\\]")));
                    } catch (NullPointerException exc) {
                        throw new IOException("wrong type (execute put: bad arguments)");
                    }
                    if (cmdScanner.hasNext()) {
                        throw new IOException("wrong type (put: wrong amount of arguments)");
                    }

                    break;
                case "create":
                    try {
                        cmdAndArgs.add(cmdScanner.next());
                        cmdAndArgs.add(cmdScanner.findInLine(Pattern.compile("\\(.+\\)")).replaceAll("[\\(|\\)]", ""));
                    } catch (NullPointerException exc) {
                        throw new IOException("wrong type (execute create: bad arguments)");
                    }
                    if (cmdScanner.hasNext()) {
                        throw new IOException("wrong type (create: wrong amount of arguments");
                    }
                    break;
                default:
                    if (cmdScanner.hasNext()) {
                        cmdAndArgs.addAll(splitByDelimiter(cmdScanner.nextLine(), " "));
                    }
                    if (cmdAndArgs.size() != command.getAmArgs() + 1) {
                        throw new IOException("wrong type (wrong amount of arguments)");
                    }
            }
        } finally {
            cmdScanner.close();
        }
        return cmdAndArgs;
    }

    public static void execute(String input, Map<String, Cmd> cmdList) {
        try {
            List<String> cmdAndArgs = parseIt(input, cmdList);
            String commandName = cmdAndArgs.get(0);
            Cmd command = cmdList.get(commandName);
            command.work(cmdAndArgs);
        } catch (IOException exc) {
            System.err.println(/**cmdAndArgs + ": " +*/exc.getMessage());
        }
    }
}
