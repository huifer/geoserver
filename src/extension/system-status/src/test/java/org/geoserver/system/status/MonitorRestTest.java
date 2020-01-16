/* (c) 2017 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.system.status;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.geoserver.rest.RestBaseController;
import org.geoserver.system.status.collector.MetricInfo;
import org.geoserver.system.status.collector.MetricValue;
import org.geoserver.system.status.collector.Metrics;
import org.geoserver.test.GeoServerSystemTestSupport;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class MonitorRestTest extends GeoServerSystemTestSupport {

    @Test
    public void testDefaultCallback() throws Exception {
        MockHttpServletResponse response =
                getAsServletResponse(RestBaseController.ROOT_PATH + "/about/system-status");
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        XStream xs = new XStream(new JettisonMappedXmlDriver());
        xs.setMode(XStream.NO_REFERENCES);
        xs.alias("metric", MetricValue.class);
        xs.alias("metrics", Metrics.class);
        xs.addImplicitCollection(Metrics.class, "metrics");
        Metrics metrics = (Metrics) xs.fromXML(response.getContentAsString());
        assertTrue(metrics.getMetrics().size() >= MetricInfo.values().length);
    }

    @Test
    public void testXmlCallback() throws Exception {
        MockHttpServletResponse response =
                getAsServletResponse(RestBaseController.ROOT_PATH + "/about/system-status.xml");
        assertEquals(200, response.getStatus());
        assertEquals("application/xml", response.getContentType());
        XStream xs = new XStream();
        xs.alias("metric", MetricValue.class);
        xs.alias("metrics", Metrics.class);
        xs.addImplicitCollection(Metrics.class, "metrics");
        Metrics metrics = (Metrics) xs.fromXML(response.getContentAsString());
        assertTrue(metrics.getMetrics().size() >= MetricInfo.values().length);
    }

    @Test
    public void testJsonCallback() throws Exception {
        MockHttpServletResponse response =
                getAsServletResponse(RestBaseController.ROOT_PATH + "/about/system-status.json");
        assertEquals(200, response.getStatus());
        assertEquals("application/json", response.getContentType());
        XStream xs = new XStream(new JettisonMappedXmlDriver());
        xs.setMode(XStream.NO_REFERENCES);
        xs.alias("metric", MetricValue.class);
        xs.alias("metrics", Metrics.class);
        xs.addImplicitCollection(Metrics.class, "metrics");
        Metrics metrics = (Metrics) xs.fromXML(response.getContentAsString());
        assertTrue(metrics.getMetrics().size() >= MetricInfo.values().length);
    }

    @Test
    public void testHtmlCallback() throws Exception {
        Document doc = getAsDOM(RestBaseController.ROOT_PATH + "/about/system-status.html");
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList nodes =
                (NodeList) xpath.evaluate("/html/body/table/tr", doc, XPathConstants.NODESET);
        assertTrue(nodes.getLength() >= MetricInfo.values().length);
    }
}