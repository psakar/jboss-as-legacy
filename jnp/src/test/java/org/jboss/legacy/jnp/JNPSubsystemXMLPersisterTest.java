package org.jboss.legacy.jnp;

import static org.jboss.legacy.jnp.JNPSubsystemModel.*;
import static org.junit.Assert.*;

import java.io.StringWriter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.dmr.ModelNode;
import org.jboss.legacy.jnp.connector.JNPServerConnectorModel;
import org.jboss.legacy.jnp.remoting.RemotingModel;
import org.jboss.legacy.jnp.server.JNPServerModel;
import org.jboss.staxmapper.XMLExtendedStreamWriter;
import org.jboss.staxmapper.XMLExtendedStreamWriterFactory;
import org.junit.Test;



public class JNPSubsystemXMLPersisterTest {

  private static final String EOL = "\n";

  @Test
  public void testWriteXml() throws Exception {
    JNPSubsystemXMLPersister persister = new JNPSubsystemXMLPersister();

    XMLOutputFactory factory = XMLOutputFactory.newInstance();
    StringWriter writer = new StringWriter();
    XMLStreamWriter streamWriter = factory.createXMLStreamWriter(writer);
    XMLExtendedStreamWriter xmlExtendedStreamWriter = new XMLExtendedStreamWriterFactory().create(streamWriter);
    ModelNode modelNode = createModelNode();
    SubsystemMarshallingContext subsystemMarshallingContext = new SubsystemMarshallingContext(modelNode, xmlExtendedStreamWriter);
    persister.writeContent(xmlExtendedStreamWriter, subsystemMarshallingContext);
    String expected = EOL +
          "<subsystem xmlns=\"urn:jboss:domain:legacy-jnp:1.0\">" + EOL +
          "    <jnp-server/>" + EOL +
          "    <jnp-connector socket-binding=\"jnp\" rmi-socket-binding=\"rmi-jnp\"/>" + EOL +
          "    <remoting socket-binding=\"rmi\"/>" + EOL +
          "</subsystem>";
    assertEquals(expected, writer.getBuffer().toString());
  }

  private ModelNode createModelNode() {

    ModelNode rootNode = createRootNode();
    ModelNode serviceNode = createChildNodeOf(rootNode, SERVICE);

    ModelNode jnpServerService = serviceNode.get(JNPServerModel.SERVICE_NAME);
    jnpServerService.set("x");

    ModelNode jnpServerConnectorService = serviceNode.get(JNPServerConnectorModel.SERVICE_NAME);
    ModelNode socketBindingAttribute = jnpServerConnectorService.get(JNPServerConnectorModel.SOCKET_BINDING);
    socketBindingAttribute.set("jnp");

    setAttributeValue(jnpServerConnectorService, JNPServerConnectorModel.RMI_SOCKET_BINDING, "rmi-jnp");

    ModelNode remotingService = serviceNode.get(RemotingModel.SERVICE_NAME);
    setAttributeValue(remotingService, RemotingModel.SOCKET_BINDING, "rmi");

    return rootNode;
  }

  private void setAttributeValue(ModelNode jnpServerConnectorService, String attributeName, String value) {
    ModelNode rmiSocketBindingAttribute = jnpServerConnectorService.get(attributeName);
    rmiSocketBindingAttribute.set(value);
  }

  private ModelNode createRootNode() {
    ModelNode rootNode = new ModelNode();
    rootNode.setEmptyObject();
    return rootNode;
  }

  private ModelNode createChildNodeOf(ModelNode rootNode, String name) {
    ModelNode serviceNode = rootNode.get(name); //, JNPExtension.SUBSYSTEM_NAME);
    serviceNode.setEmptyObject();
    return serviceNode;
  }

}