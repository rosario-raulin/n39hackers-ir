package de.n39hackers.ir;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Rosario on 28/11/14.
 *
 * We use QueryCommand to retrieve documents with a query entered by the user. We support
 * date range queries and return the top n results including their score.
 */
public class QueryCommand implements Command {

    private String getQuery() {
        System.out.print("query >> ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private void printDoc(int rank, Document document, float score) {
        String id = document.get("id");
        String title = document.get("title");


        System.out.printf("%d\t\t%s\t%.2f\t%s\n", rank, id, score, title);
    }

    @Override
    public void run(State state) {
        Analyzer analyzer = new StandardAnalyzer();
        try {
            DirectoryReader reader = DirectoryReader.open(state.getCurrentDirectory());
            IndexSearcher searcher = new IndexSearcher(reader);

            String[] fields;
            if (state.getCurrentChoice() == State.ALL_ATTRIBUTES) {
                fields = state.getIndexedAttributes().toArray(new String[0]);
            } else {
                fields = new String[]{state.getIndexAttribute()};
            }

            QueryParser qparser = new MultiFieldQueryParser(fields, analyzer);
            String queryString = getQuery();

            System.out.println("Using search query \"" + queryString + "\"");

            Query query = qparser.parse(queryString);

            // TODO: let the user decide how many results should be returned
            ScoreDoc[] hits = searcher.search(query, 1000).scoreDocs;

            System.out.println(hits.length + " matches:");
            System.out.printf("rank\tid\tscore\ttitle\n");
            for (int i = 0; i < hits.length; ++i) {
                Document hitDoc = searcher.doc(hits[i].doc);
                printDoc(i, hitDoc, hits[i].score);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
