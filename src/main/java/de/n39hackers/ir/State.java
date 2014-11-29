package de.n39hackers.ir;

import org.apache.lucene.store.Directory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rosario on 28/11/14.
 *
 * An object that represents a State and is shared between different Commands.
 */
public class State {
    public final static int NOT_INITIALIZED = -1;
    public final static int ALL_ATTRIBUTES = -2;

    private final List<String> indexedAttributes;
    private final List<Directory> indices;
    private final List<ReutersArticle> articleList;

    private int currentChoice;

    public State() {
       this.articleList = new ArrayList<>();
        XMLParser parser = new XMLParser("reuters.xml", articleList);

        this.indexedAttributes = new ArrayList<>();
        this.indices = new ArrayList<>();
        this.currentChoice = NOT_INITIALIZED;
    }

    public List<String> getIndexedAttributes() {
        return indexedAttributes;
    }

    public List<Directory> getIndices() {
        return indices;
    }

    public int getCurrentChoice() {
        return currentChoice;
    }

    public List<ReutersArticle> getArticleList() {
        return articleList;
    }

    public Directory getCurrentDirectory() {
        if (currentChoice == NOT_INITIALIZED) {
          throw new RuntimeException("currentChoice not initialized!");
        } else if (currentChoice == ALL_ATTRIBUTES) {
            // use first index/directory
            return indices.get(0);
        } else {
            return indices.get(currentChoice);
        }
    }

    public void setCurrentChoice(int currentChoice) {
        this.currentChoice = currentChoice;
    }

    public String getIndexAttribute() {
        if (currentChoice == NOT_INITIALIZED || currentChoice == ALL_ATTRIBUTES) {
            throw new RuntimeException("currentChoice set to all or not initialized!");
        }
        return indexedAttributes.get(currentChoice);
    }
}
