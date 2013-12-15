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
package org.jboss.legacy.jnp.remoting;

import static org.jboss.legacy.jnp.JNPLogger.*;
import static org.jboss.legacy.jnp.JNPSubsystemModel.*;

import java.util.HashMap;
import java.util.Map;

import org.jboss.as.network.SocketBinding;
import org.jboss.aspects.remoting.AOPRemotingInvocationHandler;
import org.jboss.msc.service.Service;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jboss.remoting.ServerConfiguration;
import org.jboss.remoting.transport.Connector;

/**
 * @author baranowb
 *
 */
public class RemotingConnectorService implements Service<Connector> {

    private static final String INVOCATION_HANDLER_KEY = "AOP";
    private static final String INVOCATION_HANDLER_CLASS = AOPRemotingInvocationHandler.class.getName();
    private static final String TRANSPORT = "socket";

    public static final ServiceName SERVICE_NAME = ServiceName.JBOSS.append(LEGACY).append(RemotingModel.SERVICE_NAME);
    private Connector connector;

    private final InjectedValue<SocketBinding> binding = new InjectedValue<SocketBinding>();

    public RemotingConnectorService() {
        super();
    }

    public InjectedValue<SocketBinding> getBinding() {
        return binding;
    }

    @Override
    public Connector getValue() throws IllegalStateException, IllegalArgumentException {
        return this.connector;
    }

    @Override
    public void start(StartContext startContext) throws StartException {
        try {
            final ServerConfiguration serverConfiguration = new ServerConfiguration(TRANSPORT);
            Map<String, String> parameters = new HashMap<String, String>(6);
            parameters.put("serverBindAddress", this.getBinding().getValue().getAddress().getHostName());
            parameters.put("serverBindPort", String.valueOf(this.getBinding().getValue().getAbsolutePort()));
            parameters.put("dataType", "invocation");
            parameters.put("marshaller", "org.jboss.invocation.unified.marshall.InvocationMarshaller");
            parameters.put("unmarshaller", "org.jboss.invocation.unified.marshall.InvocationUnMarshaller");
            parameters.put("enableTcpNoDelay", "true");
            serverConfiguration.setInvokerLocatorParameters(parameters);
            final Map<String, String> invocationHandlers = new HashMap<String, String>(1);
            invocationHandlers.put(INVOCATION_HANDLER_KEY, INVOCATION_HANDLER_CLASS);
            serverConfiguration.setInvocationHandlers(invocationHandlers);
            this.connector = new Connector();
            this.connector.setServerConfiguration(serverConfiguration);
            this.connector.start();
            ROOT_LOGGER.remotingConnectorServiceStopped();
        } catch (Exception e) {
            throw new StartException(e);
        }
    }

    @Override
    public void stop(StopContext stopContext) {
        this.connector.stop();
        ROOT_LOGGER.remotingConnectorServiceStopped();
    }

}
