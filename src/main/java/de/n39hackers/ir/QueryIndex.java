package de.n39hackers.ir;

import org.apache.lucene.store.Directory;

import java.util.Collections;
import java.util.List;

/**
 * Created by Rosario on 29/11/14.
 */
public class QueryIndex {

    private final Directory directory;
    private final List<String> fields;

    public QueryIndex(final Directory directory, final List<String> fields) {
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
}
