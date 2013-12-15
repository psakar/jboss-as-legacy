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
package org.jboss.legacy.jnp.connector.simple;

import static org.jboss.legacy.jnp.JNPLogger.*;

import org.jboss.as.network.SocketBinding;
import org.jboss.legacy.jnp.connector.JNPServerNamingConnectorService;
import org.jboss.legacy.jnp.server.JNPServer;
import org.jboss.msc.service.StartContext;
import org.jboss.msc.service.StartException;
import org.jboss.msc.service.StopContext;
import org.jboss.msc.value.InjectedValue;
import org.jnp.server.Main;

/**
 * @author baranowb
 */
public class SingleConnectorService implements JNPServerNamingConnectorService<Main> {


    private final InjectedValue<SocketBinding> binding = new InjectedValue<SocketBinding>();
    private final InjectedValue<SocketBinding> rmiBinding = new InjectedValue<SocketBinding>();
    private final InjectedValue<JNPServer> jnpServer = new InjectedValue<JNPServer>();
    private Main serverConnector;


    public SingleConnectorService() {
    }


    public InjectedValue<JNPServer> getJNPServer() {
        return jnpServer;
    }

    @Override
    public InjectedValue<SocketBinding> getBinding() {
        return binding;
    }

    @Override
    public InjectedValue<SocketBinding> getRmiBinding() {
        return rmiBinding;
    }

    @Override
    public Main getValue() throws IllegalStateException, IllegalArgumentException {
        return this.serverConnector;
    }

    @Override
    public void start(StartContext startContext) throws StartException {
        this.serverConnector = new Main();
        this.serverConnector.setNamingInfo(jnpServer.getValue().getNamingBean());
        try {
            if (this.getRmiBinding().getOptionalValue() != null) {
                this.serverConnector.setRmiBindAddress(this.getRmiBinding().getValue().getAddress().getHostName());
                this.serverConnector.setRmiPort(this.getRmiBinding().getValue().getAbsolutePort());
            }
            this.serverConnector.setBindAddress(this.getBinding().getValue().getAddress().getHostName());
            this.serverConnector.setPort(this.getBinding().getValue().getAbsolutePort());
            this.serverConnector.start();
            ROOT_LOGGER.singleConnectorServiceStarted();
        } catch (Exception e) {
            throw new StartException(e);
        }
    }

    @Override
    public void stop(StopContext stopContext) {
        this.serverConnector.stop();
        this.serverConnector = null;
        ROOT_LOGGER.singleConnectorServiceStopped();
    }
}
