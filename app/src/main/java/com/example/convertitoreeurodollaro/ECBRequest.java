package com.example.convertitoreeurodollaro;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ECBRequest {
    private String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";

    public float getRate(String currency) {

        float rate = 0.0F;

        try {
            // invoke WS
            URL server = new URL(url);
            HttpsURLConnection service = (HttpsURLConnection) server.openConnection();
            //service.setRequestProperty("Host", "https://www.ecb.europa.eu"); // ?
            //service.setRequestProperty("Accept", "text/xml");
            //service.setRequestProperty("Accept-Charset", "UTF-8");
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
                for (int i = 0; i < list.getLength(); i++) {
                    Element cube = (Element) list.item(i);
                    if (cube.hasAttribute("currency")) {
                        Attr _currency = cube.getAttributeNode("currency");
                        String currency = _currency.getValue();
                        if (currency.equalsIgnoreCase("USD")) {
                            Attr __rate = cube.getAttributeNode("rate");
                            String _rate = __rate.getValue();
                            try {
                                rate = Float.parseFloat(_rate);
                                return rate;
                            } catch (NumberFormatException exception) {
                                service.getInputStream().close();
                                return rate;
                            }
                        }
                    }
                }
            }
            service.getInputStream().close();
            return rate;
        } catch (IOException | ParserConfigurationException | SAXException exception) {
            return rate;
        }
    }
}