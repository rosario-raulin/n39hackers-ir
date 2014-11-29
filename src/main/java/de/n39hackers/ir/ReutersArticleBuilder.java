package de.n39hackers.ir;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by n39hackers on 28/11/14.
 *
 * This class follows the Builder pattern to create new instances of ReutersArticle. It is used
 * during XML parsing.
 */
public class ReutersArticleBuilder {

    private String currId;
    private StringBuilder currDate;
    private StringBuilder currTitle;
    private StringBuilder currBody;

    public ReutersArticleBuilder() {
        this.currDate = new StringBuilder();
        this.currTitle = new StringBuilder();
        this.currBody = new StringBuilder();
    }

    public ReutersArticleBuilder setId(final String input) {
        this.currId = input;
        return this;
    }

    public ReutersArticleBuilder appendBody(final String input) {
        this.currBody.append(input);
        return this;
    }

    public ReutersArticleBuilder appendTitle(final String input) {
        this.currTitle.append(input);
        return this;
    }

    public ReutersArticleBuilder appendDate(final String input) {
        this.currDate.append(input);
        return this;
    }

    public ReutersArticle build() throws ParseException {
        // Sample date format: 26-FEB-1987 15:02:20.00
        SimpleDateFormat dateParser = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss.SS");
        Date date = dateParser.parse(currDate.toString());
        int id = Integer.parseInt(currId);

        return new ReutersArticle(id, date, currTitle.toString(), currBody.toString());
    }

    public ReutersArticleBuilder reset() {
        currId = null;
        currBody.setLength(0);
        currTitle.setLength(0);
        currDate.setLength(0);
        return this;
    }
}
