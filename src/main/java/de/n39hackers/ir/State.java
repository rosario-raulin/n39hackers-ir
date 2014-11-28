package de.n39hackers.ir;

import org.apache.lucene.store.Directory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rosario on 28/11/14.
 */
public class State {
    private final List<String> indexedAttributes;
    private final List<Directory> indices;
    private final List<ReutersArticle> articleList;

    private int currentChoice;

    public State() {
       this.articleList = new ArrayList<>();
        XMLParser parser = new XMLParser("reuters.xml", articleList);

        this.indexedAttributes = new ArrayList<>();
        this.indices = new ArrayList<>();
        this.currentChoice = -1;
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
        if (currentChoice < 0) {
            return indices.get(0);
        } else {
            return indices.get(currentChoice);
        }
    }

    public void setCurrentChoice(int currentChoice) {
        this.currentChoice = currentChoice;
    }

    public String getIndexAttribute() {
        if (currentChoice < 0) {
            System.err.println("YOU SHOULD NEVER CALL THIS METHOD!!!");
            return null;
        }
        return indexedAttributes.get(currentChoice);
    }
}
