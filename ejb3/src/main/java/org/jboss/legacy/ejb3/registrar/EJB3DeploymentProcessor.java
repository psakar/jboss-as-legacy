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

package org.jboss.legacy.ejb3.registrar;

import org.jboss.as.core.security.ServerSecurityManager;
import org.jboss.as.ee.component.Attachments;
import org.jboss.as.ee.component.Component;
import org.jboss.as.ee.component.ComponentConfiguration;
import org.jboss.as.ee.component.ComponentConfigurator;
import org.jboss.as.ee.component.ComponentDescription;
import org.jboss.as.ee.component.ComponentView;
import org.jboss.as.ee.component.EEModuleDescription;
import org.jboss.as.ejb3.component.EJBComponentDescription;
import org.jboss.as.ejb3.deployment.DeploymentRepository;
import org.jboss.as.security.service.SimpleSecurityManagerService;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.as.server.deployment.EjbDeploymentMarker;
import org.jboss.legacy.common.DeploymentEJBDataProxyMap;
import org.jboss.legacy.common.EJBDataProxy;
import org.jboss.legacy.ejb3.registrar.dynamic.DynamicInvocationService;
import org.jboss.legacy.ejb3.registrar.dynamic.stateful.StatefulDynamicInvokeService;
import org.jboss.legacy.ejb3.registrar.dynamic.stateles.StatelesDynamicInvokeService;
import org.jboss.msc.service.ServiceBuilder;
import org.jboss.msc.service.ServiceName;
import org.jboss.msc.service.ServiceTarget;

/**
 * Processor setup a service which will do AOP/JNP magic
 * @author baranowb
 */
public class EJB3DeploymentProcessor implements DeploymentUnitProcessor {

    public static final EJB3DeploymentProcessor INSTANCE = new EJB3DeploymentProcessor();

    public EJB3DeploymentProcessor() {
        super();
    }

    @Override
    public void deploy(final DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        if (!EjbDeploymentMarker.isEjbDeployment(deploymentUnit)) {
            return;
        }

        final EEModuleDescription moduleDescription = deploymentUnit.getAttachment(Attachments.EE_MODULE_DESCRIPTION);
        if (moduleDescription != null) {
            for (final ComponentDescription componentDescription : moduleDescription.getComponentDescriptions()) {
                if (componentDescription instanceof EJBComponentDescription) {
                    try {
                        final EJBComponentDescription ejbComponentDescription = (EJBComponentDescription) componentDescription;
                        ejbComponentDescription.getConfigurators().add(new ComponentConfigurator() {
                            @Override
                            public void configure(DeploymentPhaseContext context, ComponentDescription description,
                                    ComponentConfiguration configuration) throws DeploymentUnitProcessingException {
                                final DeploymentEJBDataProxyMap dataMap = deploymentUnit.getAttachment(DeploymentEJBDataProxyMap.ATTACHMENT_KEY);
                                ServiceName key = DeploymentEJBDataProxyMap.getServiceName(moduleDescription, ejbComponentDescription);
                                if (dataMap != null && dataMap.get(key)!=null) {
                                    final EJBDataProxy data = dataMap.get(key);
                                    // create servuce
                                    if (data.isStateful()) {
                                        StatefulDynamicInvokeService service = new StatefulDynamicInvokeService(data,
                                                moduleDescription, ejbComponentDescription);
                                        final ServiceName serviceName = service.getServiceName();
                                        final ServiceTarget serviceTarget = phaseContext.getServiceTarget();
                                        final ServiceBuilder<DynamicInvocationService> serviceBuilder = serviceTarget
                                                .addService(serviceName, service);
                                        serviceBuilder.addDependency(SimpleSecurityManagerService.SERVICE_NAME,
                                                ServerSecurityManager.class, service.getServerSecurityManagerInjectedValue());
                                        serviceBuilder.addDependency(EJB3Registrar.SERVICE_NAME, EJB3Registrar.class,
                                                service.getEJB3RegistrarInjectedValue());
                                        serviceBuilder.addDependency(data.getViewServiceName(), ComponentView.class,
                                                service.getViewInjectedValue());
                                        serviceBuilder.addDependency(DeploymentRepository.SERVICE_NAME,
                                                DeploymentRepository.class, service.getDeploymentRepositoryInjectedValue());
                                        serviceBuilder.addDependency(ejbComponentDescription.getCreateServiceName(),
                                                Component.class, service.getComponentCreateInjectedValue());
                                        serviceBuilder.install();
                                    } else {
                                        StatelesDynamicInvokeService service = new StatelesDynamicInvokeService(data,
                                                moduleDescription, ejbComponentDescription);
                                        final ServiceName serviceName = service.getServiceName();
                                        final ServiceTarget serviceTarget = phaseContext.getServiceTarget();
                                        final ServiceBuilder<DynamicInvocationService> serviceBuilder = serviceTarget
                                                .addService(serviceName, service);
                                        serviceBuilder.addDependency(SimpleSecurityManagerService.SERVICE_NAME,
                                                ServerSecurityManager.class, service.getServerSecurityManagerInjectedValue());
                                        serviceBuilder.addDependency(EJB3Registrar.SERVICE_NAME, EJB3Registrar.class,
                                                service.getEJB3RegistrarInjectedValue());
                                        serviceBuilder.addDependency(data.getViewServiceName(), ComponentView.class,
                                                service.getViewInjectedValue());
                                        serviceBuilder.addDependency(DeploymentRepository.SERVICE_NAME,
                                                DeploymentRepository.class, service.getDeploymentRepositoryInjectedValue());
                                        serviceBuilder.install();
                                    }
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    @Override
    public void undeploy(DeploymentUnit deploymentUnit) {
        // what about undeploy?
    }

}
