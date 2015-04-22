package server;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
/**
 * A class to handle loading, saving and creating of parts of xml files.
 * I've tried to keep it as non-specific to this project as possible so that it can be re-used.
 *
 *
 */

public class XmlFile {
    private static final String SRC = "src";
    DocumentBuilderFactory docBuilder;
    DocumentBuilder dbuilder;


    public XmlFile() {
        try {
            docBuilder = DocumentBuilderFactory.newInstance();
            dbuilder = docBuilder.newDocumentBuilder();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Reads in the xml file and returns it as an XML document
     * @param filename the name of the file that you wish to load.
     * @return the read in file as an xml doc.
     */
    public Document readFile(String filename) {
        try {
            //Load up file
            File xmlFile = new File( SRC + "/" + filename + ".xml");
            //Parse the document to xml
            Document meetingData = dbuilder.parse(xmlFile);
            return meetingData;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    /**
     * Creates a node, populates it and adds it to it's parent
     * @param doc the xml doc that you are using.
     * @param name the name of the node that you are creating
     * @param value the value that you wish to place in the created node
     * @param parent the parent node that you wish to attach it to
     * @return the read in file as an xml doc.
     */
    public static void createNode(Document doc, String name, String value, Element parent ) {
        Element tmp = doc.createElement(name);
        tmp.appendChild(doc.createTextNode(value));
        parent.appendChild(tmp);
    }

    /**
     * Brings back a nodelist containing all nodes that have the name you request
     * @param doc the xml doc that you are using.
     * @param type the name of the type of node that you are looking for
     * @return the nodelist with all the matching nodes (by tagname)
     */
    public static NodeList getItems(String type, Document doc) {
        NodeList returnList = doc.getElementsByTagName(type);
        return returnList;
    }

    /**
     * Saves the xml doc to file
     * @param input the xml doc that you wish to save.
     * @param filename What you want to call your file
     * @return true if the success worked and false if it didn't
     */
    public boolean saveFile(String filename, Document input) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(input);
            StreamResult result = new StreamResult(SRC + "/" + filename + ".xml");
            transformer.transform(source, result);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

}
