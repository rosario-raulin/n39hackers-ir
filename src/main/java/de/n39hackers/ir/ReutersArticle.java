package de.n39hackers.ir;

import java.util.Date;

/**
 * Created by Rosario on 28/11/14.
 */
public class ReutersArticle {
    private final int id;
    private final Date date;
    private final String title;
    private final String body;

    public ReutersArticle(int id, Date date, String title, String body) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return String.format("%d - %s (from %s): %s", id, title, date, body);
    }
}
