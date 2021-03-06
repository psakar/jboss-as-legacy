/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2013, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.legacy.jnp;

import javax.xml.stream.XMLStreamException;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.dmr.ModelNode;
import org.jboss.legacy.jnp.connector.JNPServerConnectorModel;
import org.jboss.legacy.jnp.infinispan.DistributedTreeManagerModel;
import org.jboss.legacy.jnp.remoting.RemotingModel;
import org.jboss.legacy.jnp.server.JNPServerModel;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

/**
 * @author baranowb
 * @author <a href="mailto:ehugonne@redhat.com">Emmanuel Hugonnet</a> (c) 2013 Red Hat, inc.
 */
public class JNPSubsystemXMLPersister implements XMLElementWriter<SubsystemMarshallingContext> {

    public static final JNPSubsystemXMLPersister INSTANCE = new JNPSubsystemXMLPersister();

    @Override
    public void writeContent(XMLExtendedStreamWriter xmlExtendedStreamWriter,
            SubsystemMarshallingContext subsystemMarshallingContext) throws XMLStreamException {
        subsystemMarshallingContext.startSubsystemElement(JNPSubsystemNamespace.LEGACY_JNP_1_0.getUriString(), false);

        writeElements(xmlExtendedStreamWriter, subsystemMarshallingContext);

        // write the subsystem end element
        xmlExtendedStreamWriter.writeEndElement();
    }

    private void writeElements(XMLExtendedStreamWriter xmlExtendedStreamWriter,
            SubsystemMarshallingContext subsystemMarshallingContext) throws XMLStreamException {
        final ModelNode model = subsystemMarshallingContext.getModelNode();

        if (model.hasDefined(JNPServerModel.SERVICE_NAME)) {
            writeJNPServer(xmlExtendedStreamWriter);
        }

        if (model.hasDefined(JNPServerConnectorModel.SERVICE_NAME)) {
            writeConnector(xmlExtendedStreamWriter, subsystemMarshallingContext);
        }

        final ModelNode treeModel = subsystemMarshallingContext.getModelNode().get(DistributedTreeManagerModel.SERVICE_NAME);
        if (model.hasDefined(DistributedTreeManagerModel.CACHE_CONTAINER) && treeModel.hasDefined(DistributedTreeManagerModel.CACHE_REF)) {
            writeDistributedCache(xmlExtendedStreamWriter, treeModel);
        }

    }

    /**
     * @param xmlExtendedStreamWriter
     * @param subsystemMarshallingContext
     */
    private void writeConnector(XMLExtendedStreamWriter xmlExtendedStreamWriter,
            SubsystemMarshallingContext subsystemMarshallingContext) throws XMLStreamException {
        final ModelNode model = subsystemMarshallingContext.getModelNode().get(JNPServerConnectorModel.SERVICE_NAME);

        xmlExtendedStreamWriter.writeStartElement(JNPSubsystemXMLElement.JNP_CONNECTOR.getLocalName());
        if (model.hasDefined(JNPServerConnectorModel.SOCKET_BINDING)) {
            xmlExtendedStreamWriter.writeAttribute(JNPSubsystemXMLAttribute.SOCKET_BINDING.getLocalName(), model.get(JNPServerConnectorModel.SOCKET_BINDING)
                    .asString());
        }

        if (model.hasDefined(JNPServerConnectorModel.RMI_SOCKET_BINDING)) {
            xmlExtendedStreamWriter.writeAttribute(JNPSubsystemXMLAttribute.RMI_SOCKET_BINDING.getLocalName(), model.get(JNPServerConnectorModel.RMI_SOCKET_BINDING)
                    .asString());
        }

        if (model.hasDefined(RemotingModel.SERVICE_NAME)) {
            writeRemoting(xmlExtendedStreamWriter, subsystemMarshallingContext);
        }

        xmlExtendedStreamWriter.writeEndElement();
    }

    private void writeJNPServer(XMLExtendedStreamWriter xmlExtendedStreamWriter) throws XMLStreamException {
        xmlExtendedStreamWriter.writeStartElement(JNPSubsystemXMLElement.JNP_SERVER.getLocalName());
        xmlExtendedStreamWriter.writeEndElement();
    }

    private void writeRemoting(XMLExtendedStreamWriter xmlExtendedStreamWriter,
            SubsystemMarshallingContext subsystemMarshallingContext) throws XMLStreamException {
        final ModelNode model = subsystemMarshallingContext.getModelNode().get(RemotingModel.SERVICE_NAME);

        xmlExtendedStreamWriter.writeStartElement(JNPSubsystemXMLElement.REMOTING.getLocalName());
        if (model.hasDefined(RemotingModel.SOCKET_BINDING)) {
            xmlExtendedStreamWriter.writeAttribute(JNPSubsystemXMLAttribute.SOCKET_BINDING.getLocalName(),
                    model.get(RemotingModel.SOCKET_BINDING).asString());
        }
        xmlExtendedStreamWriter.writeEndElement();
    }

    private void writeDistributedCache(XMLExtendedStreamWriter xmlExtendedStreamWriter, ModelNode treeModel) throws XMLStreamException {
        xmlExtendedStreamWriter.writeStartElement(JNPSubsystemXMLElement.DISTRIBUTED_CACHE.getLocalName());
        xmlExtendedStreamWriter.writeAttribute(JNPSubsystemXMLAttribute.CACHE_CONTAINER.getLocalName(), treeModel.get(
                DistributedTreeManagerModel.CACHE_CONTAINER).asString());
        xmlExtendedStreamWriter.writeAttribute(JNPSubsystemXMLAttribute.CACHE_REF.getLocalName(), treeModel.get(
                DistributedTreeManagerModel.CACHE_REF).asString());
        xmlExtendedStreamWriter.writeEndElement();
    }
}
