package de.n39hackers.ir;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

/**
 * Created by Rosario on 28/11/14.
 */
public class IR {

    public static void usage() {
        System.err.println("usage: java -jar n39hackers-ir.jar input-file.xml");
        System.exit(1);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            usage();
        } else {
            final String filePath = args[0];

            final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            saxParserFactory.setNamespaceAware(true);

            try {
                final SAXParser parser = saxParserFactory.newSAXParser();
                final XMLReader xmlReader = parser.getXMLReader();
                xmlReader.setContentHandler(new ReutersContentHandler());
                xmlReader.parse(filePath);

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }
    }
}
