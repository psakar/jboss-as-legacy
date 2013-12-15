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

package org.jboss.legacy.common;

import java.util.HashMap;

import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ejb3.component.EJBComponentDescription;
import org.jboss.as.server.deployment.AttachmentKey;
import org.jboss.msc.service.ServiceName;

/**
 * @author baranowb
 */
public class DeploymentEJBDataProxyMap extends HashMap<ServiceName, EJBDataProxy> {

    private static final long serialVersionUID = -6901611321639488361L;
    public static final ServiceName SERVICE_NAME_BASE = ServiceName.of("jboss", "legacy");
    public static final AttachmentKey<DeploymentEJBDataProxyMap> ATTACHMENT_KEY = AttachmentKey
            .create(DeploymentEJBDataProxyMap.class);

    public static ServiceName getServiceName(final EEModuleDescription moduleDescription,
            final EJBComponentDescription ejbComponentDescription) {
        // TODO: what about ear/war/jar/ejb ?
        if (moduleDescription.getEarApplicationName() == null) {
            return ServiceName.of(SERVICE_NAME_BASE, moduleDescription.getModuleName(),
                    ejbComponentDescription.getComponentName());
        } else {
            return ServiceName.of(SERVICE_NAME_BASE, moduleDescription.getEarApplicationName(),
                    moduleDescription.getModuleName(), ejbComponentDescription.getComponentName());
        }
    }
}
