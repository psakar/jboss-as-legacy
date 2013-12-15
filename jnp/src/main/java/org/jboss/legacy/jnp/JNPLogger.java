/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
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

import static org.jboss.logging.Logger.Level.*;

import org.jboss.logging.BasicLogger;
import org.jboss.logging.Logger;
import org.jboss.logging.annotations.LogMessage;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageLogger;

@MessageLogger(projectCode = "JBAS")
public interface JNPLogger extends BasicLogger {

    JNPLogger ROOT_LOGGER = Logger.getMessageLogger(JNPLogger.class, "org.jboss.legacy.jnp");

    //FIXME message id
    @LogMessage(level = INFO)
    @Message(id = 99999, value = "Activating Legacy JNP Extension")
    void activatingLegacyJnpExtension();

    //FIXME message id
    @LogMessage(level = INFO)
    @Message(id = 99998, value = "Deactivating Legacy JNP Extension")
    void deactivatingLegacyJnpExtension();

    //FIXME message id
    @LogMessage(level = DEBUG)
    @Message(id = 99997, value = "Activating Legacy JNP Server")
    void activatingLegacyJnpServer();

    //FIXME message id
    @LogMessage(level = DEBUG)
    @Message(id = 99996, value = "Deactivating Legacy JNP Server")
    void deactivatingLegacyJnpServer();

    //FIXME message id
    @LogMessage(level = DEBUG)
    @Message(id = 99995, value = "Activating Legacy HA JNP Naming Service")
    void activatingHANamingService();

    //FIXME message id
    @LogMessage(level = DEBUG)
    @Message(id = 99994, value = "Activating Legacy JNP Naming Service")
    void activatingNamingService();

    //FIXME message id
    @LogMessage(level = DEBUG)
    @Message(id = 99993, value = "Activating Legacy JNP Remoting Service")
    void activatingRemotingService();

    //FIXME message id
    @LogMessage(level = DEBUG)
    @Message(id = 99992, value = "Deactivating Legacy JNP Remoting Service")
    void deactivatingRemotingService();

    @LogMessage(level = DEBUG)
    @Message(id = 99991, value = "Started Legacy JNP HA Connector Service")
    void hAConnectorServiceStarted();

    @LogMessage(level = DEBUG)
    @Message(id = 99990, value = "Stopped Legacy JNP HA Connector Service")
    void hAConnectorServiceStopped();

    @LogMessage(level = DEBUG)
    @Message(id = 99989, value = "Started Legacy JNP Single Connector Service")
    void singleConnectorServiceStarted();

    @LogMessage(level = DEBUG)
    @Message(id = 99988, value = "Stopped Legacy JNP Single Connector Service")
    void singleConnectorServiceStopped();

    @LogMessage(level = DEBUG)
    @Message(id = 99987, value = "Started Legacy JNP Distributed Tree Manager Service")
    void distributedTreeManagerServiceStarted();

    @LogMessage(level = DEBUG)
    @Message(id = 99986, value = "Stopped Legacy JNP Distributed Tree Manager Service")
    void distributedTreeManagerServiceStopped();

    @LogMessage(level = DEBUG)
    @Message(id = 99985, value = "Started Legacy JNP Remote Connector Service")
    void remotingConnectorServiceStarted();

    @LogMessage(level = DEBUG)
    @Message(id = 99984, value = "Stopped Legacy JNP Remote Connector Service")
    void remotingConnectorServiceStopped();

    @LogMessage(level = DEBUG)
    @Message(id = 99983, value = "Started Legacy JNP Single Server Service")
    void singleServerServiceStarted();

    @LogMessage(level = DEBUG)
    @Message(id = 99982, value = "Stopped Legacy JNP Single Server Service")
    void singleServerServiceStopped();

    @LogMessage(level = DEBUG)
    @Message(id = 99981, value = "Started Legacy JNP HA Server Service")
    void hAServerServiceStarted();

    @LogMessage(level = DEBUG)
    @Message(id = 99980, value = "Stopped Legacy JNP HA Server Service")
    void hAServerServiceStopped();
    /*
    @LogMessage(level = TRACE)
    @Message(id = 15538, value = "Installing aspect %s")
    void installingAspect(String aspectName);
*/


}
