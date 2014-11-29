package de.n39hackers.ir;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.util.List;

/**
 * Created by n39hackers on 28/11/14.
 *
 * This is JAXP callback class used for XML parsing. During the parsing
 * process we generate a list of articles that we can later use for indexing.
 */
public class ReutersContentHandler extends DefaultHandler {
    private final ReutersArticleBuilder articleBuilder;
    private final List<ReutersArticle> articleList;

    private String currentTag;

    public ReutersContentHandler(List<ReutersArticle> articleList) {
        this.articleBuilder = new ReutersArticleBuilder();
        this.articleList = articleList;
        this.currentTag = "";
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentTag = localName.toLowerCase();

        if (currentTag.equals("reuters")) {
            final String id = attributes.getValue("NEWID");
            articleBuilder.setId(id);
            currentTag = "";
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equalsIgnoreCase("reuters")) try {
            this.articleList.add(this.articleBuilder.build());
            this.articleBuilder.reset();
        } catch (ParseException e) {
            throw new SAXException(e);
        }

        currentTag = "";
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        switch (currentTag) {
            case "":
                // most common case, so break early
                break;

            case "title":
                articleBuilder.appendTitle(new String(ch, start, length));
                break;

            case "body":
                articleBuilder.appendBody(new String(ch, start, length));
                break;

            case "date":
                articleBuilder.appendDate(new String(ch, start, length));
                break;
        }
    }
}
