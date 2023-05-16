package org.geekbang.time;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.Iterator;

public class ClassPathXmlResource implements Resource {
    private Document document;
    private Element rootElement;
    private Iterator<Element> elementIterator;

    public ClassPathXmlResource(String fileName) throws Exception {
        SAXReader saxReader = new SAXReader();
        URL xmlPath = this.getClass().getClassLoader().getResource(fileName);
        this.document = saxReader.read(xmlPath);
        this.rootElement = document.getRootElement();
        this.elementIterator = this.rootElement.elementIterator();
    }

    @Override
    public boolean hasNext() {
        return this.elementIterator.hasNext();
    }

    @Override
    public Object next() {
        return this.elementIterator.next();
    }
}
