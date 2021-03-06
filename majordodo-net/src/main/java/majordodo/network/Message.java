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
package majordodo.network;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A message (from broker to worker or from worker to broker)
 *
 * @author enrico.olivelli
 */
public final class Message {

    public static Message DOWNLOAD_CODEPOOL(String workerProcessId, String codePoolId) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("codePoolId", codePoolId);
        return new Message(workerProcessId, TYPE_DOWNLOAD_CODEPOOL, parameters);
    }

    public static Message DOWNLOAD_CODEPOOL_RESPONSE(byte[] data) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("data", data);
        return new Message(null, TYPE_DOWNLOAD_CODEPOOL_RESPONSE, parameters);
    }

    public static Message WORKER_SHUTDOWN(String workerProcessId) {
        return new Message(workerProcessId, TYPE_WORKER_SHUTDOWN, null);
    }

    public static Message SNAPSHOT_DOWNLOAD_REQUEST() {
        return new Message(null, TYPE_SNAPSHOT_DOWNLOAD_REQUEST, null);
    }

    public static Message SNAPSHOT_DOWNLOAD_RESPONSE(byte[] data) {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("data", data);
        return new Message(null, TYPE_SNAPSHOT_DOWNLOAD_RESPONSE, parameters);
    }

    public static Message TYPE_TASK_ASSIGNED(String workerProcessId, Map<String, Object> taskParameters) {
        return new Message(workerProcessId, TYPE_TASK_ASSIGNED, taskParameters);
    }

    public static Message KILL_WORKER(String workerProcessId) {
        return new Message(workerProcessId, TYPE_KILL_WORKER, null);
    }

    public static Message ERROR(String workerProcessId, Throwable error) {
        Map<String, Object> params = new HashMap<>();
        params.put("error", error + "");
        StringWriter writer = new StringWriter();
        error.printStackTrace(new PrintWriter(writer));
        params.put("stackTrace", writer.toString());
        return new Message(workerProcessId, TYPE_ERROR, params);
    }

    public static Message ACK(String workerProcessId) {
        return new Message(workerProcessId, TYPE_ACK, new HashMap<>());
    }

    public static Message SASL_TOKEN_MESSAGE_REQUEST(String saslMech, byte[] firstToken) {
        HashMap<String, Object> data = new HashMap<>();
        String ts = System.currentTimeMillis() + "";
        data.put("ts", ts);
        data.put("mech", saslMech);
        data.put("token", firstToken);
        return new Message(null, TYPE_SASL_TOKEN_MESSAGE_REQUEST, data);
    }

    public static Message SASL_TOKEN_SERVER_RESPONSE(byte[] saslTokenChallenge) {
        HashMap<String, Object> data = new HashMap<>();
        String ts = System.currentTimeMillis() + "";
        data.put("ts", ts);
        data.put("token", saslTokenChallenge);
        return new Message(null, TYPE_SASL_TOKEN_SERVER_RESPONSE, data);
    }

    public static Message SASL_TOKEN_MESSAGE_TOKEN(byte[] token) {
        HashMap<String, Object> data = new HashMap<>();
        String ts = System.currentTimeMillis() + "";
        data.put("ts", ts);
        data.put("token", token);
        return new Message(null, TYPE_SASL_TOKEN_MESSAGE_TOKEN, data);
    }

    public static Message CONNECTION_REQUEST(String workerId,
        String processId,
        String location,
        String sharedSecret,
        Set<Long> actualRunningTasks,
        int maxThreads,
        Map<String, Integer> maxThreadsByTaskType,
        List<Integer> groups,
        Set<Integer> excludedGroups,
        Map<String, Integer> resources,
        String clientType) {
        Map<String, Object> params = new HashMap<>();
        params.put("workerId", workerId);
        params.put("clientType", clientType);
        params.put("processId", processId);
        params.put("actualRunningTasks", actualRunningTasks);
        params.put("location", location);
        params.put("secret", sharedSecret);
        params.put("maxThreads", maxThreads);
        params.put("maxThreadsByTaskType", maxThreadsByTaskType);
        params.put("groups", groups);
        params.put("resources", resources);
        params.put("excludedGroups", excludedGroups);
        return new Message(processId, TYPE_CONNECTION_REQUEST, params);
    }

    public static Message TASK_FINISHED(String processId, List<Map<String, Object>> tasksData) {
        Map<String, Object> params = new HashMap<>();
        params.put("processId", processId);
        params.put("tasksData", tasksData);;

        return new Message(processId, TYPE_TASK_FINISHED, params);
    }

    public static Message WORKER_PING(String processId, List<Integer> groups, Set<Integer> excludedGroups, Map<String, Integer> maxThreadsByTaskType, int max, Map<String, Integer> resources) {
        Map<String, Object> params = new HashMap<>();

        params.put("processId", processId);
        params.put("groups", groups);
        params.put("maxThreadsByTaskType", maxThreadsByTaskType);
        params.put("maxThreads", max);
        params.put("excludedGroups", excludedGroups);
        params.put("resources", resources);
        return new Message(processId, TYPE_WORKER_PING, params);
    }

    public final String workerProcessId;
    public final int type;
    public final Map<String, Object> parameters;
    public String messageId;
    public String replyMessageId;

    @Override
    public String toString() {
        return typeToString(type) + ", " + parameters;
    }

    public static final int TYPE_TASK_FINISHED = 1;
    public static final int TYPE_KILL_WORKER = 2;
    public static final int TYPE_ERROR = 3;
    public static final int TYPE_ACK = 4;
    public static final int TYPE_WORKER_SHUTDOWN = 5;
    public static final int TYPE_CONNECTION_REQUEST = 6;
    public static final int TYPE_TASK_ASSIGNED = 7;
    public static final int TYPE_WORKER_PING = 8;
    public static final int TYPE_SNAPSHOT_DOWNLOAD_REQUEST = 9;
    public static final int TYPE_SNAPSHOT_DOWNLOAD_RESPONSE = 10;
    public static final int TYPE_DOWNLOAD_CODEPOOL = 11;
    public static final int TYPE_DOWNLOAD_CODEPOOL_RESPONSE = 12;

    public static final int TYPE_SASL_TOKEN_MESSAGE_REQUEST = 100;
    public static final int TYPE_SASL_TOKEN_SERVER_RESPONSE = 101;
    public static final int TYPE_SASL_TOKEN_MESSAGE_TOKEN = 102;

    public static String typeToString(int type) {
        switch (type) {
            case TYPE_TASK_FINISHED:
                return "TYPE_TASK_FINISHED";
            case TYPE_SNAPSHOT_DOWNLOAD_REQUEST:
                return "TYPE_SNAPSHOT_DOWNLOAD_REQUEST";
            case TYPE_SNAPSHOT_DOWNLOAD_RESPONSE:
                return "TYPE_SNAPSHOT_DOWNLOAD_RESPONSE";
            case TYPE_KILL_WORKER:
                return "TYPE_KILL_WORKER";
            case TYPE_ERROR:
                return "TYPE_ERROR";
            case TYPE_ACK:
                return "TYPE_ACK";
            case TYPE_WORKER_SHUTDOWN:
                return "TYPE_WORKER_SHUTDOWN";
            case TYPE_CONNECTION_REQUEST:
                return "TYPE_CONNECTION_REQUEST";
            case TYPE_TASK_ASSIGNED:
                return "TYPE_TASK_ASSIGNED";
            case TYPE_WORKER_PING:
                return "TYPE_WORKER_PING";
            case TYPE_DOWNLOAD_CODEPOOL:
                return "TYPE_DOWNLOAD_CODEPOOL";
            case TYPE_DOWNLOAD_CODEPOOL_RESPONSE:
                return "TYPE_DOWNLOAD_CODEPOOL_RESPONSE";
            case TYPE_SASL_TOKEN_MESSAGE_REQUEST:
                return "SASL_TOKEN_MESSAGE_REQUEST";
            case TYPE_SASL_TOKEN_SERVER_RESPONSE:
                return "SASL_TOKEN_SERVER_RESPONSE";
            case TYPE_SASL_TOKEN_MESSAGE_TOKEN:
                return "SASL_TOKEN_MESSAGE_TOKEN";
            default:
                return "?" + type;
        }
    }

    public Message(String workerProcessId, int type, Map<String, Object> parameters) {
        this.workerProcessId = workerProcessId;
        this.type = type;
        this.parameters = parameters;
    }

    public String getMessageId() {
        return messageId;
    }

    public Message setMessageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public String getReplyMessageId() {
        return replyMessageId;
    }

    public Message setReplyMessageId(String replyMessageId) {
        this.replyMessageId = replyMessageId;
        return this;
    }

    public Message setParameter(String key, Object value) {
        this.parameters.put(key, value);
        return this;
    }
}
