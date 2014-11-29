package de.n39hackers.ir;

import java.io.IOException;
import java.util.*;

/**
 * Created by Rosario on 28/11/14.
 *
 * This Command builds an index for a given set of attributes using Apache Lucene.
 * We always store all attributes of ReutersArticle instances (to retrieve them later on)
 * but only use the specified attributes for indexing.
 */
public class IndexBuilderCommand implements Command {

    @Override
    public String getName() {
        return "Build index";
    }

    private void printIndexOptions() {
        System.out.println("[0] Use Title");
        System.out.println("[1] Use Date");
        System.out.println("[2] Use Body");
        System.out.println("[3] All");
    }

    @Override
    public void run(QueryIndexList queryIndexList, List<ReutersArticle> articles) {
        printIndexOptions();

        try {
            Scanner scanner = new Scanner(System.in);
            int index = scanner.nextInt();
            Set<String> toIndex = new HashSet<>();

            switch (index) {
                case 0:
                    toIndex.add("title");
                    break;
                case 1:
                    toIndex.add("date");
                    break;
                case 2:
                    toIndex.add("body");
                    break;
                case 3:
                    toIndex.add("title");
                    toIndex.add("date");
                    toIndex.add("body");
                    break;
                default:
                    System.err.println("Illegal index option.");
                    return;
            }

            QueryIndex newIndex = QueryIndex.buildIndex(toIndex, articles);
            queryIndexList.add(newIndex);
        } catch (InputMismatchException e) {
            System.err.println("Illegal index option.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
