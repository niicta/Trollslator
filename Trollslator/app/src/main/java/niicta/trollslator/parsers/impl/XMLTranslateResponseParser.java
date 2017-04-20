package niicta.trollslator.parsers.impl;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by niict on 09.04.2017.
 */
//данный парсер на основе XML выдает перевод
public class XMLTranslateResponseParser {

    public String parseResponse(InputStream inputStream) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new InputSource(inputStream));
        doc.getDocumentElement().normalize();

        Element element = (Element) doc.getElementsByTagName("text").item(0);
        return element.getTextContent();
    }
}
