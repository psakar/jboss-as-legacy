package org.jboss.staxmapper;

import javax.xml.stream.XMLStreamWriter;

public class XMLExtendedStreamWriterFactory {

  public XMLExtendedStreamWriter create(XMLStreamWriter streamWriter) {
    return new FormattingXMLStreamWriter(streamWriter);
  }

}
