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
 * QueryIndex captures Apache Lucene's index class (Directory) and the fields that have been used for index
 * generation.
 *
 * It provides a factory method to create new indices from a given set of documents and indexable fields.
 *
 * Created by n39hackers on 29/11/14.
 */
public class QueryIndex {

    private static String[] ALL_FIELDS = {"id", "title", "body", "date"};

    private final Directory directory;
    private final List<String> fields;

    private QueryIndex(final Directory directory, final List<String> fields) {
        this.directory = directory;
        this.fields = fields;
    }

    public List<String> getFields() {
        return Collections.unmodifiableList(fields);
    }

    public Directory getDirectory() {
        return directory;
    }

    @Override
    public String toString() {
        final StringBuilder outputBuilder = new StringBuilder();
        outputBuilder.append("{ ");
        for (String field : fields) {
            outputBuilder.append(field).append(" ");
        }
        outputBuilder.append("}");
        return outputBuilder.toString();
    }

    // Factory method to create an index from a given set of index fields
    public static QueryIndex buildIndex(Set<String> toIndex, List<ReutersArticle> articles) throws IOException {
        Analyzer analyzer = new StandardAnalyzer();

        Directory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);


        List<String> indexedFields = new ArrayList<>();

        Map<String, FieldType> types = new HashMap<>();
        for (String fieldName : ALL_FIELDS) {
            if (!toIndex.contains(fieldName)) {
                // don't index, just store
                types.put(fieldName, StoredField.TYPE);
            } else {
                // index attribute and store
                indexedFields.add(fieldName);
                types.put(fieldName, TextField.TYPE_STORED);
            }
        }

        IndexWriter writer = new IndexWriter(directory, config);

        for (ReutersArticle article : articles) {
            Document doc = new Document();

            doc.add(new Field("id", article.getId() + "", types.get("id")));
            doc.add(new Field("title", article.getTitle(), types.get("title")));
            doc.add(new Field("body", article.getBody(), types.get("body")));

            // allow range queries
            String dateString = DateTools.dateToString(article.getDate(), DateTools.Resolution.DAY);
            doc.add(new Field("date", dateString, types.get("date")));

            writer.addDocument(doc);
        }

        writer.close();

        return new QueryIndex(directory, indexedFields);
    }
}
