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
package majordodo.worker;

import majordodo.task.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import majordodo.utils.ReflectionUtils;

/**
 * configuration of the worker
 *
 * @author enrico.olivelli
 */
public class WorkerCoreConfiguration {

    private int maxThreads;
    private String workerId;
    private String location;
    private Map<String, Integer> maxThreadsByTaskType;
    private List<Integer> groups;
    private int tasksRequestTimeout = 60000;
    private Set<Integer> excludedGroups;
    private Map<String, Integer> resourcesLimits;
    private String codePoolsDirectory;
    private boolean enableCodePools;
    private int networkTimeout = 1000 * 60 * 10;

    public WorkerCoreConfiguration() {
        maxThreadsByTaskType = new HashMap<>();
        maxThreadsByTaskType.put(Task.TASKTYPE_ANY, 1);
        maxThreads = 20;
        location = "unknown";
        resourcesLimits = new HashMap<>();
        groups = new ArrayList<>();
        groups.add(0);
        excludedGroups = new HashSet<>();
    }

    public void read(Map<String, Object> properties) {
        ReflectionUtils.apply(properties, this);
    }

    /**
     * Maximum number of threads used to process tasks
     *
     * @return
     */
    public int getMaxThreads() {
        return maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.maxThreads = maxThreads;
    }

    /**
     * Directory for temporary directories for CodePools
     *
     * @return
     */
    public String getCodePoolsDirectory() {
        return codePoolsDirectory;
    }

    public void setCodePoolsDirectory(String codePoolsDirectory) {
        this.codePoolsDirectory = codePoolsDirectory;
    }

    /**
     * Enable CodePools
     *
     * @return
     */
    public boolean isEnableCodePools() {
        return enableCodePools;
    }

    public void setEnableCodePools(boolean enableCodePools) {
        this.enableCodePools = enableCodePools;
    }

    /**
     * Worker id (not the processId!)
     *
     * @return
     */
    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    /**
     * Description of the location, usually is something like
     * InetAddress.getLocalhost()...
     *
     * @return
     */
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Maximum number of active threads per tasktype
     *
     * @return
     */
    public Map<String, Integer> getMaxThreadsByTaskType() {
        return maxThreadsByTaskType;
    }

    public void setMaxThreadsByTaskType(Map<String, Integer> maxThreadsByTaskType) {
        this.maxThreadsByTaskType = maxThreadsByTaskType;
    }

    /**
     * User groups, in order of priority. Group = 0 measns "any group except
     * from excludedGroups"
     *
     * @return
     */
    public List<Integer> getGroups() {
        return groups;
    }

    public void setGroups(List<Integer> groups) {
        this.groups = groups;
    }

    /**
     * List of excluded groups from the "any group" special group
     *
     * @return
     */
    public Set<Integer> getExcludedGroups() {
        return excludedGroups;
    }

    public void setExcludedGroups(Set<Integer> excludedGroups) {
        this.excludedGroups = excludedGroups;
    }

    /**
     * Define a maximum number of tasks for resources.
     * Each task may be 'use' a set of resources, which are idendified by strings. 
     * Using this configuration you can limit the number of tasks which use the given resource on the worker.
     * An example of such a resource is a datasource, which a limited number of connections, and you don't want this worker to accept tasks which would lead to waits due due lack of available connections in the pool
     *
     * @return
     */
    public Map<String, Integer> getResourcesLimits() {
        return resourcesLimits;
    }

    public void setResourcesLimits(Map<String, Integer> resourcesLimits) {
        this.resourcesLimits = resourcesLimits;
    }

    /**
     * Maximum timeout while waiting for new tasks from the broker
     *
     * @return
     */
    public int getTasksRequestTimeout() {
        return tasksRequestTimeout;
    }

    public void setTasksRequestTimeout(int tasksRequestTimeout) {
        this.tasksRequestTimeout = tasksRequestTimeout;
    }

    private long maxPendingFinishedTaskNotifications = 10;

    /**
     * Maximum number of tasks for which a "finished" notification is waiting in
     * queues
     *
     * @return
     */
    public long getMaxPendingFinishedTaskNotifications() {
        return maxPendingFinishedTaskNotifications;
    }

    public void setMaxPendingFinishedTaskNotifications(long maxPendingFinishedTaskNotifications) {
        this.maxPendingFinishedTaskNotifications = maxPendingFinishedTaskNotifications;
    }

    /**
     * Maximum time to be in the pendingFinishedTaskNotifications queue
     */
    private long maxWaitPendingFinishedTaskNotifications = 1000;

    public long getMaxWaitPendingFinishedTaskNotifications() {
        return maxWaitPendingFinishedTaskNotifications;
    }

    public void setMaxWaitPendingFinishedTaskNotifications(long maxWaitPendingFinishedTaskNotifications) {
        this.maxWaitPendingFinishedTaskNotifications = maxWaitPendingFinishedTaskNotifications;
    }

    /**
     * Maximum time to wait before issuing a ping (anche configuration change)
     * to the broker
     */
    private long maxKeepAliveTime = 10000;

    public long getMaxKeepAliveTime() {
        return maxKeepAliveTime;
    }

    public void setMaxKeepAliveTime(long maxKeepAliveTime) {
        this.maxKeepAliveTime = maxKeepAliveTime;
    }

    /**
     * Shared secret among all the brokers and workers. Provides minimum
     * security level
     */
    private String sharedSecret = "dodo";

    public String getSharedSecret() {
        return sharedSecret;
    }

    public void setSharedSecret(String sharedSecret) {
        this.sharedSecret = sharedSecret;
    }

    public int getNetworkTimeout() {
        return networkTimeout;
    }

    public void setNetworkTimeout(int networkTimeout) {
        this.networkTimeout = networkTimeout;
    }

}
