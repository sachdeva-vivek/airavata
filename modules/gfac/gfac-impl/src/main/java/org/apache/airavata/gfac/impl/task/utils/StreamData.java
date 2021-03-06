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

package org.apache.airavata.gfac.impl.task.utils;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.airavata.common.exception.AiravataException;
import org.apache.airavata.credential.store.credential.Credential;
import org.apache.airavata.credential.store.credential.impl.ssh.SSHCredential;
import org.apache.airavata.credential.store.store.CredentialReader;
import org.apache.airavata.credential.store.store.CredentialStoreException;
import org.apache.airavata.gfac.core.GFacException;
import org.apache.airavata.gfac.core.GFacUtils;
import org.apache.airavata.gfac.core.SSHApiException;
import org.apache.airavata.gfac.core.authentication.AuthenticationInfo;
import org.apache.airavata.gfac.core.authentication.SSHKeyAuthentication;
import org.apache.airavata.gfac.core.cluster.CommandInfo;
import org.apache.airavata.gfac.core.cluster.RawCommandInfo;
import org.apache.airavata.gfac.core.cluster.RemoteCluster;
import org.apache.airavata.gfac.core.cluster.ServerInfo;
import org.apache.airavata.gfac.core.context.TaskContext;
import org.apache.airavata.gfac.impl.Factory;
import org.apache.airavata.gfac.impl.SSHUtils;
import org.apache.airavata.model.commons.ErrorModel;
import org.apache.airavata.model.status.JobState;
import org.apache.airavata.model.status.JobStatus;
import org.apache.airavata.model.task.DataStagingTaskModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.TimerTask;

public class StreamData extends TimerTask  {
    private static final Logger log = LoggerFactory.getLogger(StreamData.class);
    private static final int DEFAULT_SSH_PORT = 22;
    private String publicKeyPath;
    private String passPhrase;
    private String privateKeyPath;
    private String userName;
    private String hostName;
    private String inputPath;
    private TaskContext taskContext;
    private DataStagingTaskModel subTaskModel;

    public StreamData(String userName, String hostName, String inputPath, TaskContext taskContext, DataStagingTaskModel subTaskModel) {
        this.userName = userName;
        this.hostName = hostName;
        this.inputPath = inputPath;
        this.taskContext = taskContext;
        this.subTaskModel = subTaskModel;
    }

    @Override
    public void run() {
        try {
            // output staging should start when the job is in active state
            JobStatus jobStatus = taskContext.getParentProcessContext().getJobModel().getJobStatus();
            if (jobStatus != null && jobStatus.getJobState().equals(JobState.ACTIVE)){
                runOutputStaging();
            }

            // output staging should end when the job is complete
            if (jobStatus != null && jobStatus.getJobState().equals(JobState.COMPLETE) || jobStatus.getJobState().equals(JobState.CANCELED) || jobStatus.getJobState().equals(JobState.FAILED)){
                this.cancel();
            }
        } catch (URISyntaxException e) {
            log.error("expId: {}, processId:{}, taskId: {}:- Couldn't stage file {} , Erroneous path specified",
                    taskContext.getExperimentId(), taskContext.getProcessId(), taskContext.getTaskId(),
                    taskContext.getProcessOutput().getName());
        } catch (IllegalAccessException | InstantiationException | AiravataException | IOException | JSchException | SSHApiException e) {
            log.error("expId: {}, processId:{}, taskId: {}:- Couldn't stage file {} , Error occurred while streaming data",
                    taskContext.getExperimentId(), taskContext.getProcessId(), taskContext.getTaskId(),
                    taskContext.getProcessOutput().getName());
        } catch (CredentialStoreException e) {
            log.error("expId: {}, processId:{}, taskId: {}:- Couldn't stage file {} , Error occurred while connecting with credential store",
                    taskContext.getExperimentId(), taskContext.getProcessId(), taskContext.getTaskId(),
                    taskContext.getProcessOutput().getName());
        }
    }

    public void runOutputStaging() throws URISyntaxException, IllegalAccessException, InstantiationException, CredentialStoreException, AiravataException, IOException, JSchException, SSHApiException {
        try {

            AuthenticationInfo authenticationInfo = null;
            URI sourceURI = new URI(subTaskModel.getSource());
            String fileName = sourceURI.getPath().substring(sourceURI.getPath().lastIndexOf(File.separator) + 1,
                    sourceURI.getPath().length());
            URI destinationURI = null;
            if (subTaskModel.getDestination().startsWith("dummy")) {
                destinationURI = getDestinationURI(taskContext, fileName);
                subTaskModel.setDestination(destinationURI.toString());
            } else {
                destinationURI = new URI(subTaskModel.getDestination());
            }

            if (sourceURI.getHost().equalsIgnoreCase(destinationURI.getHost())
                    && sourceURI.getUserInfo().equalsIgnoreCase(destinationURI.getUserInfo())) {
                localDataCopy(taskContext, sourceURI, destinationURI);
            }
            authenticationInfo = Factory.getStorageSSHKeyAuthentication(taskContext.getParentProcessContext());

            ServerInfo serverInfo = new ServerInfo(userName, hostName, DEFAULT_SSH_PORT);
            Session sshSession = Factory.getSSHSession(authenticationInfo, serverInfo);
            String targetPath = destinationURI.getPath().substring(0, destinationURI.getPath().lastIndexOf('/'));
            SSHUtils.makeDirectory(targetPath, sshSession);
            outputDataStaging(taskContext, sshSession, sourceURI, destinationURI);
        } catch (GFacException e) {
            log.error("expId: {}, processId:{}, taskId: {}:- Couldn't stage file {} , Error while output staging",
                    taskContext.getExperimentId(), taskContext.getProcessId(), taskContext.getTaskId(),
                    taskContext.getProcessOutput().getName());
            throw new AiravataException("Error while output staging",e);
        }
    }

    private void localDataCopy(TaskContext taskContext, URI sourceURI, URI destinationURI) throws SSHApiException {
        StringBuilder sb = new StringBuilder("rsync -cr ");
        sb.append(sourceURI.getPath()).append(" ").append(destinationURI.getPath());
        CommandInfo commandInfo = new RawCommandInfo(sb.toString());
        taskContext.getParentProcessContext().getDataMovementRemoteCluster().execute(commandInfo);
    }


    public URI getDestinationURI(TaskContext taskContext, String fileName) throws URISyntaxException {
        String filePath = (inputPath.endsWith(File.separator) ? inputPath : inputPath + File.separator) +
                taskContext.getParentProcessContext().getProcessId() + File.separator + fileName;
        return new URI("SCP", hostName, filePath, null);

    }

    private void outputDataStaging(TaskContext taskContext, Session sshSession, URI sourceURI, URI destinationURI)
            throws SSHApiException, AiravataException, IOException, JSchException, GFacException {

        /**
         * scp third party file transfer 'from' comute resource.
         */
        taskContext.getParentProcessContext().getDataMovementRemoteCluster().scpThirdParty(sourceURI.getPath(),
                destinationURI.getPath(), sshSession, RemoteCluster.DIRECTION.FROM, true);
        // update output locations
        GFacUtils.saveExperimentOutput(taskContext.getParentProcessContext(), taskContext.getProcessOutput().getName(), destinationURI.getPath());
        GFacUtils.saveProcessOutput(taskContext.getParentProcessContext(), taskContext.getProcessOutput().getName(), destinationURI.getPath());

    }

}
