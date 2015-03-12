/*
 Licensed to Diennea S.r.l. under one
 or more contributor license agreements. See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership. Diennea S.r.l. licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.

 */
package dodo.worker;

import dodo.executors.TaskExecutor;
import dodo.executors.TaskExecutorStatus;
import java.util.Map;

/**
 * Real execution of the task
 *
 * @author enrico.olivelli
 */
public class ExecutorRunnable implements Runnable {

    private WorkerCore core;
    private String taskId;
    private Map<String, Object> parameters;
    private TaskExecutionCallback callback;

    public ExecutorRunnable(WorkerCore core, String taskId, Map<String, Object> parameters, TaskExecutionCallback callback) {
        this.core = core;
        this.taskId = taskId;
        this.parameters = parameters;
        this.callback = callback;
    }

    public static interface TaskExecutionCallback {

        public void taskStatusChanged(String taskId, Map<String, Object> parameters, String finalStatus, Throwable error);
    }

    @Override
    public void run() {
        try {
            this.taskId = (String) parameters.get("taskid");
            String taskType = (String) parameters.get("tasktype");
            callback.taskStatusChanged(taskId, parameters, TaskExecutorStatus.RUNNING, null);
            TaskExecutor executor = core.createTaskExecutor(taskType);
            executor.executeTask(parameters);
            callback.taskStatusChanged(taskId, parameters, TaskExecutorStatus.FINISHED, null);
        } catch (Throwable t) {
            callback.taskStatusChanged(taskId, parameters, TaskExecutorStatus.ERROR, t);
        }
    }
}