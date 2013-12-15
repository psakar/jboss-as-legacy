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

package org.jboss.legacy.jnp.server;

import static org.jboss.legacy.jnp.JNPLogger.*;

import org.jboss.as.controller.AbstractRemoveStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.dmr.ModelNode;
import org.jboss.legacy.jnp.server.clustered.HAServerService;
import org.jboss.legacy.jnp.server.simple.SingleServerService;
import org.jboss.msc.service.ServiceName;

/**
 * @author baranowb
 *
 */
class JNPServerServiceRemoveStepHandler extends AbstractRemoveStepHandler {

    static final JNPServerServiceRemoveStepHandler INSTANCE = new JNPServerServiceRemoveStepHandler();

    private JNPServerServiceRemoveStepHandler() {
    }

    @Override
    protected void performRuntime(OperationContext context, ModelNode operation, ModelNode model) throws OperationFailedException {
        if (context.isResourceServiceRestartAllowed()) {
            removeRuntimeService(context, operation);
        } else {
            context.reloadRequired();
        }
    }

    @Override
    protected void recoverServices(OperationContext context, ModelNode operation, ModelNode model) throws OperationFailedException {
        if (context.isResourceServiceRestartAllowed()) {
            //TODO: not null verification handler?
            JNPServerServiceAddStepHandler.INSTANCE.installRuntimeServices(context, operation,model, null);
        } else {
            context.revertReloadRequired();
        }
    }

    void removeRuntimeService(OperationContext context, ModelNode operation) throws OperationFailedException {
        ROOT_LOGGER.deactivatingLegacyJnpServer();
        boolean isHA = JNPServerResourceDefinition.HA.resolveModelAttribute(context, operation).asBoolean(false);
        ServiceName serviceName = isHA ? HAServerService.SERVICE_NAME : SingleServerService.SERVICE_NAME;
        context.removeService(serviceName);
    }
}
