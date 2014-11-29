package de.n39hackers.ir;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 * QueryIndexList keeps track of all indices (instances of QueryIndex) and the current index used for querying.
 *
 * Created by n39hackers on 29/11/14.
 */
public class QueryIndexList {

    private final Set<QueryIndex> indices;
    private QueryIndex currentIndex;

    public QueryIndexList() {
        // A LinkedHashSet provides a deterministic iteration order:
        // The items are returned in the order they were inserted.
        // We need this to set the index later on (see setCurrentIndex(int)).
        this.indices = new LinkedHashSet<QueryIndex>();
    }

    public void add(QueryIndex index) {
        indices.add(index);
    }

    public Iterable<QueryIndex> getIndices() {
        return indices;
    }

    public QueryIndex getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int index) {
        this.currentIndex = indices.toArray(new QueryIndex[0])[index];
    }

    public int size() {
        return indices.size();
    }

    @Override
    public String toString() {
        StringBuffer outputBuffer = new StringBuffer();

        outputBuffer.append("Current index: ");
        if (currentIndex == null) {
            outputBuffer.append("none");
        } else {
            outputBuffer.append(currentIndex);
        }
        outputBuffer.append('\n');

        outputBuffer.append("Available indices: \n");
        int pos = 0;
        for (QueryIndex index : indices) {
            outputBuffer.append('[').append(pos).append("] ").append(index).append('\n');
            ++pos;
        }

        return outputBuffer.toString();
    }
}
