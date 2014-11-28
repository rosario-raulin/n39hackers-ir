package de.n39hackers.ir;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.List;

/**
 * Created by Rosario on 28/11/14.
 */
public class XMLParser {

    private final String filePath;
    private final List<ReutersArticle> articleList;

    public XMLParser(String filePath, List<ReutersArticle> articleList) {
        this.filePath = filePath;
        this.articleList = articleList;
        run();
    }

    public void run() {
        final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware(true);

        try {
            final SAXParser parser = saxParserFactory.newSAXParser();
            final XMLReader xmlReader = parser.getXMLReader();
            xmlReader.setContentHandler(new ReutersContentHandler(articleList));
            xmlReader.parse(filePath);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }
}