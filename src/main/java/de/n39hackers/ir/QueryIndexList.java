package de.n39hackers.ir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * QueryIndexList keeps track of all indices (instances of QueryIndex) and the current index used for querying.
 *
 * Created by n39hackers on 29/11/14.
 */
public class QueryIndexList {

    private final List<QueryIndex> indices;
    private QueryIndex currentIndex;

    public QueryIndexList() {
        this.indices = new ArrayList<QueryIndex>();
    }

    public void add(QueryIndex index) {
        indices.add(index);
    }

    public List<QueryIndex> getIndices() {
        return Collections.unmodifiableList(indices);
    }

    public QueryIndex getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int index) {
        this.currentIndex = indices.get(index);
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
