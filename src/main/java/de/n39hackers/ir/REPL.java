package de.n39hackers.ir;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by n39hackers on 28/11/14.
 *
 * The REPL (read-eval-print loop) is the main user interface. It prints out
 * helpful instruction how to use the program and parses the user input to call
 * the appropriate commands.
 */
public class REPL {

    private final List<Command> commands;
    private final List<ReutersArticle> reutersDocuments;
    private final QueryIndexList queryIndexList;

    public REPL(List<Command> options) {
        this.commands = options;
        this.queryIndexList = new QueryIndexList();
        this.reutersDocuments = new ArrayList<>();

        new XMLParser("reuters.xml", this.reutersDocuments);
    }

    private static void printOptions(final List<Command> commands) {
        System.out.println("Your options are:");

        int i = 0;
        for (Command command : commands) {
            System.out.printf("[%d] %s\n", i, command.getName());
            ++i;
        }
    }

    public void run() {
        System.out.println("Welcome to n39hacker's awesome search engine!");
        System.out.println();

        while (true) {
            try {
                printOptions(this.commands);
                System.out.println(queryIndexList);

                // Wait for user's input
                System.out.print(">> ");
                int actionInputOption = UIScanner.getInstance().nextInt();

                if (actionInputOption >= commands.size()) {
                    System.err.println("This is not a valid action.");
                } else {
                    Command toCall = commands.get(actionInputOption);
                    toCall.run(queryIndexList, reutersDocuments);
                }
            } catch (NumberFormatException e) {
                System.err.println("This is not a valid action.");
            }
        }
    }

    /*
        We use a list of Commands to map the user's choice with an action.
        Every Command has a run() method that gets a State object with shared
        context. The user enters a number that stands for an action and that
        number is exactly the position in our command list.

        Some Commands are very short and have thus been created as anonymous
        instances. More complex Commands like building the index or Querying
        with a given string are implemented in separate classes.

        This approach has two benefits: It is very easy to add new Commands and
        we don't have to write a parser for string input by the user. We can simply map
        a number to an array index.
     */
    public static void main(String[] args) {
        final List<Command> commands = new ArrayList<>();

        final Command quitCommand = new Command() {
            @Override
            public String getName() {
                return "Quit";
            }

            @Override
            public void run(QueryIndexList queryIndexList, List<ReutersArticle> articles) {
                System.out.println("Bye!");
                System.exit(0);
            }
        };

        final Command helpCommand = new Command() {
            @Override
            public String getName() {
                return "Print help";
            }

            @Override
            public void run(QueryIndexList queryIndexList, List<ReutersArticle> articles) {
                printOptions(commands);
            }
        };


        final Command selectIndexCommand = new Command() {
            @Override
            public String getName() {
                return "Select index";
            }

            @Override
            public void run(QueryIndexList queryIndexList, List<ReutersArticle> articles) {
                System.out.println(queryIndexList);

                try {
                    int indexChoice = UIScanner.getInstance().nextInt();

                    if (indexChoice < 0 || indexChoice >= queryIndexList.size()) {
                        System.err.println("Wrong choice!");
                        return;
                    }

                    queryIndexList.setCurrentIndex(indexChoice);
                } catch (NumberFormatException e) {
                    System.err.println("This is not a valid search index!");
                }
            }
        };

        commands.add(quitCommand);
        commands.add(helpCommand);
        commands.add(new IndexBuilderCommand());
        commands.add(selectIndexCommand);
        commands.add(new QueryCommand());

        REPL repl = new REPL(commands);
        repl.run();
    }
}
