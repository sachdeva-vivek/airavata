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

package org.apache.airavata.client.samples;

import org.apache.airavata.api.Airavata;
import org.apache.airavata.api.client.AiravataClientFactory;
import org.apache.airavata.client.AiravataAPIFactory;
import org.apache.airavata.client.api.AiravataAPI;
import org.apache.airavata.client.api.exception.AiravataAPIInvocationException;
import org.apache.airavata.client.tools.DocumentCreator;
import org.apache.airavata.common.exception.ApplicationSettingsException;
import org.apache.airavata.common.utils.AiravataUtils;
import org.apache.airavata.common.utils.ClientSettings;
import org.apache.airavata.model.error.*;
import org.apache.airavata.model.util.ExperimentModelUtil;
import org.apache.airavata.model.util.ProjectModelUtil;
import org.apache.airavata.model.workspace.Project;
import org.apache.airavata.model.workspace.experiment.*;
import org.apache.airavata.model.workspace.experiment.Experiment;
import org.apache.airavata.persistance.registry.jpa.model.*;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

public class TestCreateLaunchExperiment {

    //FIXME: Read from a config file
    public static final String THRIFT_SERVER_HOST = "localhost";
    public static final int THRIFT_SERVER_PORT = 8930;
    private final static Logger logger = LoggerFactory.getLogger(TestCreateLaunchExperiment.class);
    private static final String DEFAULT_USER = "default.registry.user";
    private static final String DEFAULT_GATEWAY = "default.registry.gateway";

    public static void main(String[] args) {
        try {
            AiravataUtils.setExecutionAsClient();
            final Airavata.Client airavata = AiravataClientFactory.createAiravataClient(THRIFT_SERVER_HOST, THRIFT_SERVER_PORT);
            System.out.println("API version is " + airavata.getAPIVersion());
//            addDescriptors();

//            final String expId = createExperimentForSSHHost(airavata);
//            final String expId = createExperimentForTrestles(airavata);
//            final String expId = createExperimentForStampede(airavata);
//            for (int i = 0 ; i < 100 ; i++){
//                final String expId = createExperimentForLocalHost(airavata);
//                System.out.println("Experiment ID : " + expId);
//                launchExperiment(airavata, expId);
//                Thread monitor = (new Thread(){
//                    public void run() {
//                        Map<String, JobStatus> jobStatuses = null;
//                        while (true) {
//                            try {
//                                jobStatuses = airavata.getJobStatuses(expId);
//                                Set<String> strings = jobStatuses.keySet();
//                                for (String key : strings) {
//                                    JobStatus jobStatus = jobStatuses.get(key);
//                                    if(jobStatus == null){
//                                        return;
//                                    }else {
//                                        if (JobState.COMPLETE.equals(jobStatus.getJobState())) {
//                                            System.out.println("Job completed Job ID: " + key);
//                                            return;
//                                        }else{
//                                            System.out.println("Job ID:" + key + jobStatuses.get(key).getJobState().toString());
//                                        }
//                                    }
//                                }
//                                System.out.println(airavata.getExperimentStatus(expId));
//                                Thread.sleep(5000);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                    }
//                });
//                monitor.start();
//                try {
//                    monitor.join();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                }
//                try {
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace(); // To change body of catch statement use
//                    // File | Settings | File Templates.
//                }
//            }

//            List<Experiment> experimentList = getExperimentsForUser(airavata, "admin");
//            Experiment experiment = experimentList.get(1);
//            ExperimentStatus experimentStatus = experiment.getExperimentStatus();
//            long timeOfStateChange = experimentStatus.getTimeOfStateChange();
//            System.out.println(timeOfStateChange);
//            Date date = new Date(timeOfStateChange);
//            Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
//            String time1 = format.format(date);
////                int count = i+1;
//            System.out.println(time1);

            for (int i = 0; i < 500 ; i++){
                long time = System.currentTimeMillis();
                List<Experiment> experiments = getExperimentsForUser(airavata, "admin");
                int count = i+1;
                System.out.println("Experiment count : " + experiments.size());
                System.out.println("iteration : " + String.valueOf(count));
                System.out.println(System.currentTimeMillis() - time);
            }
//            List<Experiment> experiments = getExperimentsForUser(airavata, "admin");
//            System.out.println("Experiment count : " + experiments.size());
//            for (Experiment ex : experiments){
//                System.out.println(ex.getExperimentID());
//            }

        } catch (Exception e) {
            logger.error("Error while connecting with server", e.getMessage());
            e.printStackTrace();
        }
    }

    public static void addDescriptors() throws AiravataAPIInvocationException,ApplicationSettingsException  {
        try {
            DocumentCreator documentCreator = new DocumentCreator(getAiravataAPI());
            documentCreator.createLocalHostDocs();
            documentCreator.createSSHHostDocs();
            documentCreator.createGramDocs();
            documentCreator.createPBSDocsForOGCE();
            documentCreator.createSlurmDocs();
            documentCreator.createSGEDocs();
            documentCreator.createEchoHostDocs();
        } catch (AiravataAPIInvocationException e) {
            logger.error("Unable to create airavata API", e.getMessage());
            throw new AiravataAPIInvocationException(e);
        } catch (ApplicationSettingsException e) {
            logger.error("Unable to create airavata API", e.getMessage());
            throw new ApplicationSettingsException(e.getMessage());
        }
    }

    private static AiravataAPI getAiravataAPI() throws AiravataAPIInvocationException, ApplicationSettingsException {
        AiravataAPI airavataAPI;
        try {
            String sysUser = ClientSettings.getSetting(DEFAULT_USER);
            String gateway = ClientSettings.getSetting(DEFAULT_GATEWAY);
            airavataAPI = AiravataAPIFactory.getAPI(gateway, sysUser);
        } catch (AiravataAPIInvocationException e) {
            logger.error("Unable to create airavata API", e.getMessage());
            throw new AiravataAPIInvocationException(e);
        } catch (ApplicationSettingsException e) {
            logger.error("Unable to create airavata API", e.getMessage());
            throw new ApplicationSettingsException(e.getMessage());
        }
        return airavataAPI;
    }

    public static String createExperimentForTrestles(Airavata.Client client) throws TException  {
        try{
            List<DataObjectType> exInputs = new ArrayList<DataObjectType>();
            DataObjectType input = new DataObjectType();
            input.setKey("echo_input");
            input.setType(DataType.STRING);
            input.setValue("echo_output=Hello World");
            exInputs.add(input);

            List<DataObjectType> exOut = new ArrayList<DataObjectType>();
            DataObjectType output = new DataObjectType();
            output.setKey("echo_output");
            output.setType(DataType.STRING);
            output.setValue("");
            exOut.add(output);

            Experiment simpleExperiment =
                    ExperimentModelUtil.createSimpleExperiment("default", "admin", "echoExperiment", "SimpleEcho2", "SimpleEcho2", exInputs);
            simpleExperiment.setExperimentOutputs(exOut);

            ComputationalResourceScheduling scheduling = ExperimentModelUtil.createComputationResourceScheduling("trestles.sdsc.edu", 1, 1, 1, "normal", 0, 0, 1, "sds128");
            UserConfigurationData userConfigurationData = new UserConfigurationData();
            userConfigurationData.setAiravataAutoSchedule(false);
            userConfigurationData.setOverrideManualScheduledParams(false);
            userConfigurationData.setComputationalResourceScheduling(scheduling);
            simpleExperiment.setUserConfigurationData(userConfigurationData);
            return client.createExperiment(simpleExperiment);
        } catch (AiravataSystemException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new AiravataSystemException(e);
        } catch (InvalidRequestException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new InvalidRequestException(e);
        } catch (AiravataClientException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new AiravataClientException(e);
        }catch (TException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new TException(e);
        }
    }

    public static String cloneExperiment(Airavata.Client client, String expId) throws TException  {
        try{
            return client.cloneExperiment(expId, "cloneExperiment1");
        }catch (TException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new TException(e);
        }
    }

    public static void updateExperiment(Airavata.Client client, String expId) throws TException  {
        try{
            Experiment experiment = client.getExperiment(expId);
            experiment.setDescription("updatedDescription");
            client.updateExperiment(expId, experiment );
        }catch (TException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new TException(e);
        }
    }


    public static String createExperimentForLocalHost(Airavata.Client client) throws TException  {
        try{
            List<DataObjectType> exInputs = new ArrayList<DataObjectType>();
            DataObjectType input = new DataObjectType();
            input.setKey("echo_input");
            input.setType(DataType.STRING);
            input.setValue("echo_output=Hello World");
            exInputs.add(input);

            List<DataObjectType> exOut = new ArrayList<DataObjectType>();
            DataObjectType output = new DataObjectType();
            output.setKey("echo_output");
            output.setType(DataType.STRING);
            output.setValue("");
            exOut.add(output);

            Project project = ProjectModelUtil.createProject("project1", "admin", "test project");
            String projectId = client.createProject(project);

            Experiment simpleExperiment =
                    ExperimentModelUtil.createSimpleExperiment(projectId, "admin", "echoExperiment", "Echo Test", "Echo", exInputs);
            simpleExperiment.setExperimentOutputs(exOut);

            ComputationalResourceScheduling scheduling = ExperimentModelUtil.createComputationResourceScheduling("localhost", 1, 1, 1, "normal", 0, 0, 1, "");
            UserConfigurationData userConfigurationData = new UserConfigurationData();
            userConfigurationData.setAiravataAutoSchedule(false);
            userConfigurationData.setOverrideManualScheduledParams(false);
            userConfigurationData.setComputationalResourceScheduling(scheduling);
            simpleExperiment.setUserConfigurationData(userConfigurationData);
            return client.createExperiment(simpleExperiment);
        } catch (AiravataSystemException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new AiravataSystemException(e);
        } catch (InvalidRequestException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new InvalidRequestException(e);
        } catch (AiravataClientException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new AiravataClientException(e);
        }catch (TException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new TException(e);
        }
    }
    
    public static String createExperimentForSSHHost(Airavata.Client client) throws TException  {
        try{
            List<DataObjectType> exInputs = new ArrayList<DataObjectType>();
            DataObjectType input = new DataObjectType();
            input.setKey("echo_input");
            input.setType(DataType.STRING);
            input.setValue("echo_output=Hello World");
            exInputs.add(input);

            List<DataObjectType> exOut = new ArrayList<DataObjectType>();
            DataObjectType output = new DataObjectType();
            output.setKey("echo_output");
            output.setType(DataType.STRING);
            output.setValue("");
            exOut.add(output);

            Project project = ProjectModelUtil.createProject("default", "admin", "test project");
            String projectId = client.createProject(project);

            Experiment simpleExperiment =
                    ExperimentModelUtil.createSimpleExperiment(projectId, "admin", "sshEchoExperiment", "SSHEcho1", "SSHEcho1", exInputs);
            simpleExperiment.setExperimentOutputs(exOut);

            ComputationalResourceScheduling scheduling = ExperimentModelUtil.createComputationResourceScheduling("gw111.iu.xsede.org", 1, 1, 1, "normal", 0, 0, 1, "sds128");
            scheduling.setResourceHostId("gw111.iu.xsede.org");
            UserConfigurationData userConfigurationData = new UserConfigurationData();
            userConfigurationData.setAiravataAutoSchedule(false);
            userConfigurationData.setOverrideManualScheduledParams(false);
            userConfigurationData.setComputationalResourceScheduling(scheduling);
            simpleExperiment.setUserConfigurationData(userConfigurationData);
            return client.createExperiment(simpleExperiment);
        } catch (AiravataSystemException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new AiravataSystemException(e);
        } catch (InvalidRequestException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new InvalidRequestException(e);
        } catch (AiravataClientException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new AiravataClientException(e);
        }catch (TException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new TException(e);
        }
    }
    public static String createExperimentForStampede(Airavata.Client client) throws TException  {
        try{
            List<DataObjectType> exInputs = new ArrayList<DataObjectType>();
            DataObjectType input = new DataObjectType();
            input.setKey("echo_input");
            input.setType(DataType.STRING);
            input.setValue("echo_output=Hello World");
            exInputs.add(input);

            List<DataObjectType> exOut = new ArrayList<DataObjectType>();
            DataObjectType output = new DataObjectType();
            output.setKey("echo_output");
            output.setType(DataType.STRING);
            output.setValue("");
            exOut.add(output);

            Project project = ProjectModelUtil.createProject("default", "admin", "test project");
            String projectId = client.createProject(project);

            Experiment simpleExperiment =
                    ExperimentModelUtil.createSimpleExperiment(projectId, "admin", "echoExperiment", "SimpleEcho3", "SimpleEcho3", exInputs);
            simpleExperiment.setExperimentOutputs(exOut);

            ComputationalResourceScheduling scheduling =
                    ExperimentModelUtil.createComputationResourceScheduling("stampede.tacc.xsede.org", 1, 1, 1, "normal", 0, 0, 1, "TG-STA110014S");
            UserConfigurationData userConfigurationData = new UserConfigurationData();
            userConfigurationData.setAiravataAutoSchedule(false);
            userConfigurationData.setOverrideManualScheduledParams(false);
            userConfigurationData.setComputationalResourceScheduling(scheduling);
            simpleExperiment.setUserConfigurationData(userConfigurationData);
            return client.createExperiment(simpleExperiment);
        } catch (AiravataSystemException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new AiravataSystemException(e);
        } catch (InvalidRequestException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new InvalidRequestException(e);
        } catch (AiravataClientException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new AiravataClientException(e);
        }catch (TException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new TException(e);
        }
    }
       public static String createExperimentForLonestar(Airavata.Client client) throws TException  {
        try{
            List<DataObjectType> exInputs = new ArrayList<DataObjectType>();
            DataObjectType input = new DataObjectType();
            input.setKey("echo_input");
            input.setType(DataType.STRING);
            input.setValue("echo_output=Hello World");
            exInputs.add(input);

            List<DataObjectType> exOut = new ArrayList<DataObjectType>();
            DataObjectType output = new DataObjectType();
            output.setKey("echo_output");
            output.setType(DataType.STRING);
            output.setValue("");
            exOut.add(output);

            Project project = ProjectModelUtil.createProject("default", "admin", "test project");
            String projectId = client.createProject(project);

            Experiment simpleExperiment =
                    ExperimentModelUtil.createSimpleExperiment(projectId, "admin", "echoExperiment", "SimpleEcho4", "SimpleEcho4", exInputs);
            simpleExperiment.setExperimentOutputs(exOut);

            ComputationalResourceScheduling scheduling =
                    ExperimentModelUtil.createComputationResourceScheduling("lonestar.tacc.utexas.edu", 1, 1, 1, "normal", 0, 0, 1, "TG-STA110014S");
            scheduling.setResourceHostId("lonestar-host");
            UserConfigurationData userConfigurationData = new UserConfigurationData();
            userConfigurationData.setAiravataAutoSchedule(false);
            userConfigurationData.setOverrideManualScheduledParams(false);
            userConfigurationData.setComputationalResourceScheduling(scheduling);
            simpleExperiment.setUserConfigurationData(userConfigurationData);
            return client.createExperiment(simpleExperiment);
        } catch (AiravataSystemException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new AiravataSystemException(e);
        } catch (InvalidRequestException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new InvalidRequestException(e);
        } catch (AiravataClientException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new AiravataClientException(e);
        } catch (LaunchValidationException e) {
            logger.error("Validation failed" + e.getErrorMessage());
            org.apache.airavata.model.error.ValidationResults validationResult = e.getValidationResult();
            for (org.apache.airavata.model.error.ValidatorResult vResult : validationResult.getValidationResultList()) {
                if (!vResult.isSetResult()) {
                    System.out.println("Error:" + vResult.getErrorDetails());
                }
            }
            throw e;
        }catch (TException e) {
            logger.error("Error occured while creating the experiment...", e.getMessage());
            throw new TException(e);
        }
    }
    public static void launchExperiment (Airavata.Client client, String expId)
            throws TException{
        try {
            client.launchExperiment(expId, "testToken");
        } catch (ExperimentNotFoundException e) {
            logger.error("Error occured while launching the experiment...", e.getMessage());
            throw new ExperimentNotFoundException(e);
        } catch (AiravataSystemException e) {
            logger.error("Error occured while launching the experiment...", e.getMessage());
            throw new AiravataSystemException(e);
        } catch (InvalidRequestException e) {
            logger.error("Error occured while launching the experiment...", e.getMessage());
            throw new InvalidRequestException(e);
        } catch (AiravataClientException e) {
            logger.error("Error occured while launching the experiment...", e.getMessage());
            throw new AiravataClientException(e);
        }catch (TException e) {
            logger.error("Error occured while launching the experiment...", e.getMessage());
            throw new TException(e);
        }
    }

    public static List<Experiment> getExperimentsForUser (Airavata.Client client, String user){
        try {
            return client.getAllUserExperiments(user);
        } catch (AiravataSystemException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (AiravataClientException e) {
            e.printStackTrace();
        }catch (TException e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Project> getAllUserProject (Airavata.Client client, String user){
        try {
            return client.getAllUserProjects(user);
        } catch (AiravataSystemException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (AiravataClientException e) {
            e.printStackTrace();
        }catch (TException e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Project> searchProjectsByProjectName (Airavata.Client client, String user, String projectName){
        try {
            return client.searchProjectsByProjectName(user, projectName);
        } catch (AiravataSystemException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (AiravataClientException e) {
            e.printStackTrace();
        }catch (TException e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Project> searchProjectsByProjectDesc (Airavata.Client client, String user, String desc){
        try {
            return client.searchProjectsByProjectDesc(user, desc);
        } catch (AiravataSystemException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (AiravataClientException e) {
            e.printStackTrace();
        }catch (TException e){
            e.printStackTrace();
        }
        return null;
    }


    public static List<ExperimentSummary> searchExperimentsByName (Airavata.Client client, String user, String expName){
        try {
            return client.searchExperimentsByName(user, expName);
        } catch (AiravataSystemException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (AiravataClientException e) {
            e.printStackTrace();
        }catch (TException e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<ExperimentSummary> searchExperimentsByDesc(Airavata.Client client, String user, String desc){
        try {
            return client.searchExperimentsByDesc(user, desc);
        } catch (AiravataSystemException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (AiravataClientException e) {
            e.printStackTrace();
        }catch (TException e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<ExperimentSummary> searchExperimentsByApplication(Airavata.Client client, String user, String app){
        try {
            return client.searchExperimentsByApplication(user, app);
        } catch (AiravataSystemException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (AiravataClientException e) {
            e.printStackTrace();
        }catch (TException e){
            e.printStackTrace();
        }
        return null;
    }
}