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
package org.apache.airavata.registry.core.experiment.catalog.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.lang.*;
import java.sql.Timestamp;

@Entity
@Table(name = "TASK")
public class Task {
    private final static Logger logger = LoggerFactory.getLogger(Task.class);
    private String taskId;
    private String taskType;
    private String parentProcessId;
    private Timestamp creationTime;
    private Timestamp lastUpdateTime;
    private String taskDetail;
    private Byte taskInternalStore;
    private Process process;
    private TaskError taskError;
    private TaskStatus taskStatus;

    @Id
    @Column(name = "TASK_ID")
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Basic
    @Column(name = "TASK_TYPE")
    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    @Basic
    @Column(name = "PARENT_PROCESS_ID")
    public String getParentProcessId() {
        return parentProcessId;
    }

    public void setParentProcessId(String parentProcessId) {
        this.parentProcessId = parentProcessId;
    }

    @Basic
    @Column(name = "CREATION_TIME")
    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    @Basic
    @Column(name = "LAST_UPDATE_TIME")
    public Timestamp getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Timestamp lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Basic
    @Column(name = "TASK_DETAIL")
    public String getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(String taskDetail) {
        this.taskDetail = taskDetail;
    }

    @Basic
    @Column(name = "TASK_INTERNAL_STORE")
    public Byte getTaskInternalStore() {
        return taskInternalStore;
    }

    public void setTaskInternalStore(Byte taskInternalStore) {
        this.taskInternalStore = taskInternalStore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (creationTime != null ? !creationTime.equals(task.creationTime) : task.creationTime != null) return false;
        if (lastUpdateTime != null ? !lastUpdateTime.equals(task.lastUpdateTime) : task.lastUpdateTime != null)
            return false;
        if (parentProcessId != null ? !parentProcessId.equals(task.parentProcessId) : task.parentProcessId != null)
            return false;
        if (taskDetail != null ? !taskDetail.equals(task.taskDetail) : task.taskDetail != null) return false;
        if (taskId != null ? !taskId.equals(task.taskId) : task.taskId != null) return false;
        if (taskInternalStore != null ? !taskInternalStore.equals(task.taskInternalStore) : task.taskInternalStore != null)
            return false;
        if (taskType != null ? !taskType.equals(task.taskType) : task.taskType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = taskId != null ? taskId.hashCode() : 0;
        result = 31 * result + (taskType != null ? taskType.hashCode() : 0);
        result = 31 * result + (parentProcessId != null ? parentProcessId.hashCode() : 0);
        result = 31 * result + (creationTime != null ? creationTime.hashCode() : 0);
        result = 31 * result + (lastUpdateTime != null ? lastUpdateTime.hashCode() : 0);
        result = 31 * result + (taskDetail != null ? taskDetail.hashCode() : 0);
        result = 31 * result + (taskInternalStore != null ? taskInternalStore.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "PARENT_PROCESS_ID", referencedColumnName = "PROCESS_ID")
    public Process getProcess() {
        return process;
    }

    public void setProcess(Process processByParentProcessId) {
        this.process = processByParentProcessId;
    }

    @OneToOne(mappedBy = "task")
    public TaskError getTaskError() {
        return taskError;
    }

    public void setTaskError(TaskError taskErrorsByTaskId) {
        this.taskError = taskErrorsByTaskId;
    }

    @OneToOne(mappedBy = "task")
    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatusesByTaskId) {
        this.taskStatus = taskStatusesByTaskId;
    }
}