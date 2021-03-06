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
package majordodo.task;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class TasksHeapBenchTest {

    private static final String TASKTYPE_MYTASK1 = "mytask1";
    private static final String TASKTYPE_MYTASK2 = "mytask2";
    private static final String USERID1 = "myuser1";
    private static final String USERID2 = "myuser2";
    private static final int GROUPID1 = 9713;
    private static final int GROUPID2 = 972;

    private final TaskPropertiesMapperFunction DEFAULT_FUNCTION = new TaskPropertiesMapperFunction() {
        @Override
        public TaskProperties getTaskProperties(long taskid, String taskType, String userid) {
            int groupId;
            switch (userid) {
                case USERID1:
                    groupId = GROUPID1;
                    break;
                case USERID2:
                    groupId = GROUPID2;
                    break;
                default:
                    groupId = -1;
            }
            return new TaskProperties(groupId, null);
        }

    };

    @Test
    public void monoThreadTests() throws Exception {
        TasksHeap instance = new TasksHeap(1000000, DEFAULT_FUNCTION);
        instance.setMaxFragmentation(Integer.MAX_VALUE);

        {
            long _start = System.currentTimeMillis();
            for (int i = 0; i < 100000; i++) {
                instance.insertTask(i + 1, TASKTYPE_MYTASK1, USERID1);
            }
            long _stop = System.currentTimeMillis();
            System.out.println("Time: " + (_stop - _start) + " ms");
        }

//        instance.scan(entry -> {
//            System.out.println("entry:" + entry);
//        });
//        System.out.println("after:");
        {
            long _start = System.currentTimeMillis();
            Map<String, Integer> availableSpace = new HashMap<>();
            availableSpace.put(TASKTYPE_MYTASK1, 1);
            for (int i = 0; i < 1000; i++) {
                instance.takeTasks(1, Arrays.asList(Task.GROUP_ANY), Collections.emptySet(), availableSpace, Collections.emptyMap(), new ResourceUsageCounters(), Collections.emptyMap(), new ResourceUsageCounters());
//                instance.scan(entry -> {
//                    System.out.println("entry:" + entry);
//                });
            }

            long _stop = System.currentTimeMillis();
            System.out.println("Time: " + (_stop - _start) + " ms");
        }

    }

}
