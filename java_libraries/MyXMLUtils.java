package utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

public class MyXMLUtils {

    public static List<XMLRecord> parseXML(String xmlString) {
        List<XMLRecord> records = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputSource is = new InputSource(new StringReader(xmlString));
            Document document = builder.parse(is);

            Element root = document.getDocumentElement();

            NodeList recordNodes = root.getElementsByTagName("Record");
            for (int i = 0; i < recordNodes.getLength(); i++) {
                Node recordNode = recordNodes.item(i);
                if (recordNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element recordElement = (Element) recordNode;
                    XMLRecord record = new XMLRecord();
                    parseNode(recordElement, record);
                    records.add(record);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return records;
    }

    private static void parseNode(Node node, XMLRecord record) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            NodeList nodeList = element.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node currentNode = nodeList.item(i);
                if (currentNode.getNodeType() == Node.ELEMENT_NODE) {
                    String key = currentNode.getNodeName();
                    String value = currentNode.getTextContent();
                    record.addAttribute(key, value);
                }
            }
        }
    }

    public static String generateXML(List<XMLRecord> records) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Records");
            doc.appendChild(rootElement);

            for (XMLRecord record : records) {
                Element recordElement = doc.createElement("Record");
                for (String key : record.getAttributeNames()) {
                    String value = record.getAttribute(key);
                    Element attribute = doc.createElement(key);
                    attribute.appendChild(doc.createTextNode(value));
                    recordElement.appendChild(attribute);
                }
                rootElement.appendChild(recordElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));

            return writer.getBuffer().toString();
        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
