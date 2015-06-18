/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.apache.airavata.registry.core.experiment.catalog.resources;

import org.apache.airavata.registry.core.experiment.catalog.ExpCatResourceUtils;
import org.apache.airavata.registry.core.experiment.catalog.ExperimentCatResource;
import org.apache.airavata.registry.core.experiment.catalog.ResourceType;
import org.apache.airavata.registry.core.experiment.catalog.model.Process;
import org.apache.airavata.registry.core.experiment.catalog.model.*;
import org.apache.airavata.registry.core.experiment.catalog.utils.QueryGenerator;
import org.apache.airavata.registry.cpi.RegistryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProcessResource extends AbstractExpCatResource {
    private static final Logger logger = LoggerFactory.getLogger(ProcessResource.class);
    private String processId;
    private String experimentId;
    private Timestamp creationTime;
    private Timestamp lastUpdateTime;
    private String processDetail;
    private String applicationInterfaceId;
    private String taskDag;

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getExperimentId() {
        return experimentId;
    }

    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getProcessDetail() {
        return processDetail;
    }

    public void setProcessDetail(String processDetail) {
        this.processDetail = processDetail;
    }

    public String getApplicationInterfaceId() {
        return applicationInterfaceId;
    }

    public void setApplicationInterfaceId(String applicationInterfaceId) {
        this.applicationInterfaceId = applicationInterfaceId;
    }

    public String getTaskDag() {
        return taskDag;
    }

    public void setTaskDag(String taskDag) {
        this.taskDag = taskDag;
    }

    public ExperimentCatResource create(ResourceType type) throws RegistryException{
       switch (type){
           case PROCESS_ERROR:
               ProcessErrorResource errorResource = new ProcessErrorResource();
               errorResource.setProcessId(processId);
               return errorResource;
           case PROCESS_STATUS:
               ProcessStatusResource statusResource = new ProcessStatusResource();
               statusResource.setProcessId(processId);
               return statusResource;
           case PROCESS_INPUT:
               ProcessInputResource processInputResource = new ProcessInputResource();
               processInputResource.setProcessId(processId);
               return processInputResource;
           case PROCESS_OUTPUT:
               ProcessOutputResource processOutputResource = new ProcessOutputResource();
               processOutputResource.setProcessId(processId);
               return processOutputResource;
           case PROCESS_RESOURCE_SCHEDULE:
               ProcessResourceScheduleResource processResourceScheduleResource = new ProcessResourceScheduleResource();
               processResourceScheduleResource.setProcessId(processId);
               return processResourceScheduleResource;
           case TASK:
               TaskResource taskResource = new TaskResource();
               taskResource.setParentProcessId(processId);
               return taskResource;
           default:
               logger.error("Unsupported resource type for process resource.", new IllegalArgumentException());
               throw new IllegalArgumentException("Unsupported resource type for process resource.");
       }
    }

    
    public void remove(ResourceType type, Object name) throws RegistryException{
        EntityManager em = null;
        try {
            em = ExpCatResourceUtils.getEntityManager();
            em.getTransaction().begin();
            Query q;
            QueryGenerator generator;
            switch (type) {
                case PROCESS_ERROR:
                    generator = new QueryGenerator(PROCESS_ERROR);
                    generator.setParameter(ProcessErrorConstants.PROCESS_ID, name);
                    q = generator.deleteQuery(em);
                    q.executeUpdate();
                    break;
                case PROCESS_STATUS:
                    generator = new QueryGenerator(PROCESS_STATUS);
                    generator.setParameter(ProcessStatusConstants.PROCESS_ID, name);
                    q = generator.deleteQuery(em);
                    q.executeUpdate();
                    break;
                case PROCESS_INPUT:
                    generator = new QueryGenerator(PROCESS_INPUT);
                    generator.setParameter(ProcessInputConstants.PROCESS_INPUT_ID, name);
                    q = generator.deleteQuery(em);
                    q.executeUpdate();
                    break;
                case PROCESS_OUTPUT:
                    generator = new QueryGenerator(PROCESS_OUTPUT);
                    generator.setParameter(ProcessOutputConstants.PROCESS_OUTPUT_ID, name);
                    q = generator.deleteQuery(em);
                    q.executeUpdate();
                    break;
                case PROCESS_RESOURCE_SCHEDULE:
                    generator = new QueryGenerator(PROCESS_RESOURCE_SCHEDULE);
                    generator.setParameter(ProcessResourceScheduleConstants.PROCESS_ID, processId);
                    q = generator.deleteQuery(em);
                    q.executeUpdate();
                    break;
                case TASK:
                    generator = new QueryGenerator(TASK);
                    generator.setParameter(TaskConstants.PROCESS_ID, processId);
                    generator.setParameter(TaskConstants.TASK_ID, name);
                    q = generator.deleteQuery(em);
                    q.executeUpdate();
                    break;
                default:
                    logger.error("Unsupported resource type for process detail resource.", new IllegalArgumentException());
                    break;
            }
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RegistryException(e);
        } finally {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive()){
                    em.getTransaction().rollback();
                }
                em.close();
            }
        }
    }

    
    public ExperimentCatResource get(ResourceType type, Object name) throws RegistryException{
        EntityManager em = null;
        try {
            em = ExpCatResourceUtils.getEntityManager();
            em.getTransaction().begin();
            QueryGenerator generator;
            Query q;
            switch (type) {
                case PROCESS_STATUS:
                    generator = new QueryGenerator(PROCESS_STATUS);
                    generator.setParameter(ProcessStatusConstants.PROCESS_ID, name);
                    q = generator.selectQuery(em);
                    ProcessStatus status = (ProcessStatus) q.getSingleResult();
                    ProcessStatusResource statusResource = (ProcessStatusResource) Utils.getResource(ResourceType.PROCESS_STATUS, status);
                    em.getTransaction().commit();
                    em.close();
                    return statusResource;
                case PROCESS_ERROR:
                    generator = new QueryGenerator(PROCESS_ERROR);
                    generator.setParameter(ProcessErrorConstants.PROCESS_ID, name);
                    q = generator.selectQuery(em);
                    ProcessError processError = (ProcessError) q.getSingleResult();
                    ProcessErrorResource processErrorResource = (ProcessErrorResource) Utils.getResource(ResourceType.PROCESS_ERROR, processError);
                    em.getTransaction().commit();
                    em.close();
                    return processErrorResource;
                case PROCESS_INPUT:
                    generator = new QueryGenerator(PROCESS_INPUT);
                    generator.setParameter(ProcessInputConstants.PROCESS_INPUT_ID, name);
                    generator.setParameter(ProcessInputConstants.PROCESS_ID, processId);
                    q = generator.selectQuery(em);
                    ProcessInput processInput = (ProcessInput) q.getSingleResult();
                    ProcessInputResource processInputResource = (ProcessInputResource) Utils.getResource(ResourceType.PROCESS_INPUT, processInput);
                    em.getTransaction().commit();
                    em.close();
                    return processInputResource;
                case PROCESS_OUTPUT:
                    generator = new QueryGenerator(PROCESS_OUTPUT);
                    generator.setParameter(ProcessOutputConstants.PROCESS_OUTPUT_ID, name);
                    generator.setParameter(ProcessInputConstants.PROCESS_ID, processId);
                    q = generator.selectQuery(em);
                    ProcessOutput processOutput = (ProcessOutput) q.getSingleResult();
                    ProcessOutputResource outputResource = (ProcessOutputResource) Utils.getResource(ResourceType.PROCESS_OUTPUT, processOutput);
                    em.getTransaction().commit();
                    em.close();
                    return outputResource;
                case PROCESS_RESOURCE_SCHEDULE:
                    generator = new QueryGenerator(PROCESS_RESOURCE_SCHEDULE);
                    generator.setParameter(ProcessResourceScheduleConstants.PROCESS_ID, name);
                    q = generator.selectQuery(em);
                    ProcessResourceSchedule processResourceSchedule = (ProcessResourceSchedule) q.getSingleResult();
                    ProcessResourceScheduleResource processResourceScheduleResource = (ProcessResourceScheduleResource)
                            Utils.getResource(ResourceType.PROCESS_RESOURCE_SCHEDULE, processResourceSchedule);
                    em.getTransaction().commit();
                    em.close();
                    return processResourceScheduleResource;
                case TASK:
                    generator = new QueryGenerator(TASK);
                    generator.setParameter(TaskConstants.TASK_ID, name);
                    generator.setParameter(TaskConstants.PROCESS_ID, processId);
                    q = generator.selectQuery(em);
                    Task task = (Task) q.getSingleResult();
                    TaskResource taskResource = (TaskResource) Utils.getResource(ResourceType.TASK, task);
                    em.getTransaction().commit();
                    em.close();
                    return taskResource;
                default:
                    em.getTransaction().commit();
                    em.close();
                    logger.error("Unsupported resource type for process resource.", new IllegalArgumentException());
                    throw new IllegalArgumentException("Unsupported resource type for process resource.");
            }
        } catch (Exception e) {
            throw new RegistryException(e);
        } finally {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive()){
                    em.getTransaction().rollback();
                }
                em.close();
            }
        }
    }

    
    public List<ExperimentCatResource> get(ResourceType type) throws RegistryException{
        List<ExperimentCatResource> resourceList = new ArrayList<ExperimentCatResource>();
        EntityManager em = null;
        try {
            em = ExpCatResourceUtils.getEntityManager();
            em.getTransaction().begin();
            Query q;
            QueryGenerator generator;
            List results;
            switch (type) {
                case PROCESS_INPUT:
                    generator = new QueryGenerator(PROCESS_INPUT);
                    generator.setParameter(ProcessInputConstants.PROCESS_ID, processId);
                    q = generator.selectQuery(em);
                    results = q.getResultList();
                    if (results.size() != 0) {
                        for (Object result : results) {
                            ProcessInput processInput = (ProcessInput) result;
                            ProcessInputResource processInputResource =
                                    (ProcessInputResource) Utils.getResource(ResourceType.PROCESS_INPUT, processInput);
                            resourceList.add(processInputResource);
                        }
                    }
                    break;
                case PROCESS_OUTPUT:
                    generator = new QueryGenerator(PROCESS_OUTPUT);
                    generator.setParameter(ProcessOutputConstants.PROCESS_ID, processId);
                    q = generator.selectQuery(em);
                    results = q.getResultList();
                    if (results.size() != 0) {
                        for (Object result : results) {
                            ProcessOutput processOutput = (ProcessOutput) result;
                            ProcessOutputResource processOutputResource
                                    = (ProcessOutputResource) Utils.getResource(ResourceType.PROCESS_OUTPUT, processOutput);
                            resourceList.add(processOutputResource);
                        }
                    }
                    break;
                case TASK:
                    generator = new QueryGenerator(TASK);
                    generator.setParameter(TaskConstants.PROCESS_ID, processId);
                    q = generator.selectQuery(em);
                    results = q.getResultList();
                    if (results.size() != 0) {
                        for (Object result : results) {
                            Task task = (Task) result;
                            TaskResource taskResource =
                                    (TaskResource) Utils.getResource(ResourceType.TASK, task);
                            resourceList.add(taskResource);
                        }
                    }
                    break;
                default:
                    em.getTransaction().commit();
                    em.close();
                    logger.error("Unsupported resource type for task resource.", new UnsupportedOperationException());
                    throw new UnsupportedOperationException();
            }
            em.getTransaction().commit();
            em.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RegistryException(e);
        } finally {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive()){
                    em.getTransaction().rollback();
                }
                em.close();
            }
        }
        return resourceList;
    }

    
    public void save() throws RegistryException{
        EntityManager em = null;
        try {
            em = ExpCatResourceUtils.getEntityManager();
            Process process = em.find(Process.class, processId);
            em.close();
            em = ExpCatResourceUtils.getEntityManager();
            em.getTransaction().begin();
            if (process == null) {
            	process = new Process();
            }
            process.setProcessId(processId);
            process.setExperimentId(experimentId);
            process.setCreationTime(creationTime);
            process.setLastUpdateTime(lastUpdateTime);
            process.setProcessDetail(processDetail);
            process.setApplicationInterfaceId(applicationInterfaceId);
            process.setTaskDag(taskDag);
            em.persist(process);
            em.getTransaction().commit();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RegistryException(e);
        } finally {
            if (em != null && em.isOpen()) {
                if (em.getTransaction().isActive()){
                    em.getTransaction().rollback();
                }
                em.close();
            }
        }
    }

    public List<ProcessInputResource> getProcessInputs() throws RegistryException{
        List<ProcessInputResource> processInputResources = new ArrayList();
        List<ExperimentCatResource> resources = get(ResourceType.PROCESS_INPUT);
        for (ExperimentCatResource resource : resources) {
            ProcessInputResource inputResource = (ProcessInputResource) resource;
            processInputResources.add(inputResource);
        }
        return processInputResources;
    }

    public List<ProcessOutputResource> getProcessOutputs() throws RegistryException{
        List<ProcessOutputResource> outputResources = new ArrayList();
        List<ExperimentCatResource> resources = get(ResourceType.PROCESS_OUTPUT);
        for (ExperimentCatResource resource : resources) {
            ProcessOutputResource outputResource = (ProcessOutputResource) resource;
            outputResources.add(outputResource);
        }
        return outputResources;
    }

    public ProcessStatusResource getProcessStatus() throws RegistryException{
        ExperimentCatResource resource = get(ResourceType.PROCESS_STATUS, processId);
        return (ProcessStatusResource)resource;
    }

    public ProcessErrorResource getProcessError() throws RegistryException{
        ExperimentCatResource resource = get(ResourceType.PROCESS_ERROR, processId);
        return (ProcessErrorResource)resource;
    }

    public ProcessResourceScheduleResource getProcessResourceSchedule() throws RegistryException{
        ExperimentCatResource resource = get(ResourceType.PROCESS_RESOURCE_SCHEDULE, processId);
        return (ProcessResourceScheduleResource)resource;
    }

    public List<TaskResource> getTaskList() throws RegistryException{
        List<TaskResource> taskResources = new ArrayList<TaskResource>();
        List<ExperimentCatResource> resources = get(ResourceType.TASK);
        for (ExperimentCatResource resource : resources) {
            TaskResource taskResource = (TaskResource) resource;
            taskResources.add(taskResource);
        }
        return taskResources;
    }
}
