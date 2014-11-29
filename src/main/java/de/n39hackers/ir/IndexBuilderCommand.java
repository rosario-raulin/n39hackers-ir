package de.n39hackers.ir;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

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

    private static String[] ALL_FIELDS = {"id", "title", "body", "date"};

    private void printIndexOptions() {
        System.out.println("[0] Use Title");
        System.out.println("[1] Use Date");
        System.out.println("[2] Use Body");
        System.out.println("[3] All");
    }

    private Directory buildIndex(Set<String> toIndex, State state) {
        Analyzer analyzer = new StandardAnalyzer();

        Directory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);
        IndexWriter writer;

        Map<String, FieldType> types = new HashMap<>();
        for (String fieldName : ALL_FIELDS) {
            if (!toIndex.contains(fieldName)) {
                // don't index, just store
                types.put(fieldName, StoredField.TYPE);
            } else {
                // index attribute and store
                types.put(fieldName, TextField.TYPE_STORED);
            }
        }

        StringBuilder indexName = new StringBuilder();
        indexName.append("{ ");
        for (String s : toIndex) {
            indexName.append(s).append(" ");
        }
        indexName.append("}");
        state.getIndexedAttributes().add(indexName.toString());

        try {
            writer = new IndexWriter(directory, config);
            for (ReutersArticle article : state.getArticleList()) {
                Document doc = new Document();

                doc.add(new Field("id", article.getId() + "", types.get("id")));
                doc.add(new Field("title", article.getTitle(), types.get("title")));
                doc.add(new Field("body", article.getBody(), types.get("body")));
                doc.add(new Field("date", DateTools.dateToString(article.getDate(), DateTools.Resolution.DAY), types.get("date")));

                writer.addDocument(doc);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return directory;
    }

    @Override
    public void run(State state) {
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

            state.getIndices().add(buildIndex(toIndex, state));
        } catch (InputMismatchException e) {
            System.err.println("Illegal index option.");
        }
    }
}
