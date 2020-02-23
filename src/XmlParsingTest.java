package edu.ucsd.cs;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlParsingTest {

    public static void main(String args[]) {

        if (args.length != 1) {
            System.err.println("Usage: XmlParsingTest <xmlfile>");
            System.exit(1);
        }

        try {

            File fXmlFile = new File(args[0]);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            assert(doc.getDocumentElement().getNodeName() == "MPD");

            NodeList repList = doc.getElementsByTagName("Representation");
            for (int repnum = 0; repnum < repList.getLength(); repnum++) {

                Node rNode = repList.item(repnum);
                assert(rNode.getNodeName() == "Representation");
                assert(rNode.getNodeType() == Node.ELEMENT_NODE);
                
                Element representation = (Element) rNode;
                System.out.println("Representation " + repnum + " Bandwidth: " + representation.getAttribute("bandwidth"));

                NodeList segmentlists = rNode.getChildNodes();
                for (int i = 0; i < segmentlists.getLength(); i++) {
                    Node segmentlist = segmentlists.item(i);

                    if (segmentlist.getNodeType() == Node.ELEMENT_NODE) {
                        System.out.println("  " + segmentlist.getNodeName());

                        NodeList segments = segmentlist.getChildNodes();
                        for (int j = 0; j < segments.getLength(); j++) {
                            Node segmentNode = segments.item(j);

                            if (segmentNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element segment = (Element) segmentNode;
                                if (segment.getNodeName() == "Initialization") {
                                    System.out.println("    init: " + segment.getAttribute("sourceURL"));
                                } else {
                                    System.out.println("    m4s: " + segment.getAttribute("media"));
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
