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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private final Set<String> fields;

    private QueryIndex(final Directory directory, final Set<String> fields) {
        this.directory = directory;
        this.fields = fields;
    }

    public String[] getFieldsAsArray() {
        return fields.toArray(new String[0]);
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

    // Two query indices are considered equal if and only if they index the same fields
    @Override
    public boolean equals(Object other) {
        if (other == null) return false;
        if (other == this) return true;
        if (other instanceof QueryIndex) {
            QueryIndex otherIndex = (QueryIndex)other;
            return otherIndex.fields.equals(this.fields);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hashValue = 0;

        for (String field : fields) {
            hashValue += field.hashCode();
        }

        return 23 * hashValue;
    }

    // Factory method to create an index from a given set of index fields
    public static QueryIndex buildIndex(Set<String> toIndex, List<ReutersArticle> articles) throws IOException {
        Analyzer analyzer = new StandardAnalyzer();

        Directory directory = new RAMDirectory();
        IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, analyzer);

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

        return new QueryIndex(directory, toIndex);
    }
}
