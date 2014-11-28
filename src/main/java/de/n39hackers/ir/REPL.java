package de.n39hackers.ir;

import org.apache.lucene.store.Directory;

import java.util.*;

/**
 * Created by Rosario on 28/11/14.
 */
public class REPL {

    private final List<Callable> options;
    private State state;

    public REPL(List<Callable> options) {
        this.options = options;
        this.state = new State();
    }

    private static void printOptions() {
        System.out.println("Welcome to n39hacker's awesome search engine!");
        System.out.println("");
        System.out.println("Your options are:");
        System.out.println("[0] Quit");
        System.out.println("[1] Print help");
        System.out.println("[2] Build index");
        System.out.println("[3] Select index");
        System.out.println("[4] Query");
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        printOptions();

        while (true) {
            try {
                int currentChoice = state.getCurrentChoice();
                if (currentChoice >= 0) {
                    System.out.println("Current Index: " + state.getIndexedAttributes().get(currentChoice));
                }
                System.out.print("Indicies: ");
                for (String i : state.getIndexedAttributes()) {
                    System.out.print(i + ", ");
                }
                System.out.println();

                System.out.print(">> ");
                int index = scanner.nextInt();

                if (index >= options.size()) {
                    System.err.println("This is not a valid option.");
                } else {
                    Callable toCall = options.get(index);
                    toCall.run(state);
                }
            } catch (InputMismatchException e) {
                System.err.println("This is not a valid option.");
                scanner.reset();
                scanner.nextLine();
            }
        }
    }

    public static void main(String[] args) {
        Callable quitCommand = new Callable() {
            @Override
            public void run(State state) {
                System.out.println("Bye!");
                System.exit(0);
            }
        };

        Callable helpCommand = new Callable() {
            @Override
            public void run(State state) {
                REPL.printOptions();
            }
        };


        Callable selectIndexCommand = new Callable() {
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


        List<Directory> indicies = new ArrayList<>();
        List<Callable> commands = new ArrayList<>();
        List<String> indexed = new ArrayList<>();

        commands.add(quitCommand);
        commands.add(helpCommand);
        commands.add(new IndexBuilderCommand());
        commands.add(selectIndexCommand);
        commands.add(new QueryCommand());

        REPL repl = new REPL(commands);
        repl.run();
    }
}
