package de.n39hackers.ir;

import java.util.Scanner;

/**
 * Created by n39hackers on 29/11/14.
 *
 * A simple wrapper class for java.util.Scanner. We basically introduce blocking for nextLine().
 *
 */
public class UIScanner {

    private static UIScanner instance;

    public static UIScanner getInstance() {
        if (instance == null) {
            instance = new UIScanner();
        }
        return instance;
    }

    private final Scanner scanner;

    private UIScanner() {
        this.scanner = new Scanner(System.in);
    }

    public String nextLine() {
        while (!scanner.hasNextLine()) {
            // block until input is available
        }
        return scanner.nextLine();
    }

    public int nextInt() throws NumberFormatException {
        return Integer.parseInt(nextLine());
    }
}
