package com.example.convertitoreeurodollaro;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ECBRequest {
    private String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

    public Double getRate(String currency) {

        Double rate = 0.0;

        try {
            // invoke WS
            URL server = new URL(url);
            HttpsURLConnection service = (HttpsURLConnection) server.openConnection();
            service.setDoInput(true);
            service.setRequestMethod("GET");
            service.setReadTimeout(1000); // timeout: 1s
            service.setConnectTimeout(1000); // timeout: 1s
            service.connect();
            int status_code = service.getResponseCode();
            if (status_code != 200) {
                return rate;
            }
            // parse XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(service.getInputStream());
            Element root = document.getDocumentElement();
            NodeList list = root.getElementsByTagName("Cube");
            if (list != null) {
                try {
                    double convertRate = 0;
                    final NodeList node = root.getElementsByTagName("Cube");
                    for (int i = 0; i < node.getLength(); i++) {
                        final Element element = (Element) node.item(i);
                        if (element.getAttribute("currency").equalsIgnoreCase(currency)) {
                            convertRate = Double.parseDouble(element.getAttribute("rate"));
                        }
                    }
                    return convertRate;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
        return rate;
    }
}
