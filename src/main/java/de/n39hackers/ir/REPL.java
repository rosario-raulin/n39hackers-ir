package de.n39hackers.ir;

import java.util.*;

/**
 * Created by Rosario on 28/11/14.
 *
 * The REPL (read-eval-print loop) is the main user interface. It prints out
 * helpful instruction how to use the program and parses the user input to call
 * the appropriate commands.
 */
public class REPL {

    private final List<Command> options;
    private State state;

    public REPL(List<Command> options) {
        this.options = options;
        this.state = new State();
    }

    private static void printOptions() {
        System.out.println("Your options are:");
        System.out.println("[0] Quit");
        System.out.println("[1] Print help");
        System.out.println("[2] Build index");
        System.out.println("[3] Select index");
        System.out.println("[4] Query");
    }

    public void run() {
        System.out.println("Welcome to n39hacker's awesome search engine!");
        System.out.println();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                printOptions();
                int currentChoice = state.getCurrentChoice();

                if (currentChoice != State.NOT_INITIALIZED) {
                    System.out.println("Chosen index: " + state.getIndexedAttributes().get(currentChoice));
                }

                System.out.print("Available indices: ");
                if (state.getIndexedAttributes().size() == 0) {
                    System.out.println("none");
                } else {
                    for (String i : state.getIndexedAttributes()) {
                        System.out.print(i + " ");
                    }
                }

                // Wait for user's input
                System.out.print(">> ");
                int actionInputOption = scanner.nextInt();

                if (actionInputOption >= options.size()) {
                    System.err.println("This is not a valid action.");
                } else {
                    Command toCall = options.get(actionInputOption);
                    toCall.run(state);
                }
            } catch (InputMismatchException e) {
                System.err.println("This is not a valid action.");
                scanner.reset();
                scanner.nextLine();
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
        Command quitCommand = new Command() {
            @Override
            public void run(State state) {
                System.out.println("Bye!");
                System.exit(0);
            }
        };

        Command helpCommand = new Command() {
            @Override
            public void run(State state) {
                REPL.printOptions();
            }
        };


        Command selectIndexCommand = new Command() {
            @Override
            public void run(State state) {
                Scanner scanner = new Scanner(System.in);

                int i = 0;
                for (String attr : state.getIndexedAttributes()) {
                    System.out.println("[" + i + "] " + attr);
                    ++i;
                }

                int choice = scanner.nextInt();
                if (choice >= state.getIndexedAttributes().size()) {
                    System.err.println("Wrong choice!");
                    return;
                }

                state.setCurrentChoice(choice);
            }
        };

        List<Command> commands = new ArrayList<>();

        commands.add(quitCommand);
        commands.add(helpCommand);
        commands.add(new IndexBuilderCommand());
        commands.add(selectIndexCommand);
        commands.add(new QueryCommand());

        REPL repl = new REPL(commands);
        repl.run();
    }
}
