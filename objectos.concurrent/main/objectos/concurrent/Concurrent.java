/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package objectos.concurrent;

import java.util.concurrent.atomic.AtomicInteger;
import objectos.lang.object.Check;

/**
 * Provides {@code static} utility methods.
 *
 * @since 1
 */
public final class Concurrent {

  /*

  @startuml

  ' config

  left to right direction
  skinparam shadowing false

  ' actors

  :Job:

  :Server:

  ' usecases

  usecase SubmitIoTask as "Run blocking I/O task
  on a separate thread"

  usecase SubmitJob as "Run a Job (state machine)
  on a separate thread"

  usecase WatchDirectory as "Watch a Directory and
  dispatch jobs based on
  the modification observed"

  :Job: --> SubmitIoTask

  :Server: --> SubmitJob
  :Server: --> WatchDirectory

  @enduml

   */

  /*

  @startuml

  ' config

  hide empty members
  ' left to right direction
  skinparam genericDisplay old
  ' skinparam monochrome true
  skinparam shadowing false
  ' skinparam style strictuml

  class ArrayWorkerService implements WorkerService {
    - Job[] jobs
    - int jobsSize
  }

  class Concurrent {
    + {static} IoExecutorService\lcreateIoExecutorService(\lIoExecutorServiceOption... options)
    + {static} WorkerService\lcreateWorkerService(\lWorkerServiceOption... options)
    + {static} void\lstartServices(\lService... services)
    + {static} Option activeCount(int count)
    + {static} Option logger(Logger l)
    + {static} Option queueSize(int size)
    + {static} Option singleThread()
    + {static} Option threadCount(int)
  }

  interface IoExecutor {
    + IoRunner\lsubmit(IoTask task)
    + IoRunner\lsubmit(IoTask task, long timeOut, TimeUnit unit)
  }

  interface IoExecutorService extends IoExecutor, Service {
    + void setLogger(Logger l)
    + void shutdown()
    + void start()
  }

  interface IoRunner {
    + boolean isActive()
    + boolean isException()\lthrows IllegalStateException
    + Exception getException()\lthrows IllegalStateException
  }

  interface IoTask {
    + void executeIo()\lthrows Exception
  }

  interface Job {
    + void executeOne()
    + boolean isActive()
  }

  interface Service {
    + void startService()
    + void stopService()
  }

  interface Worker {
    + void execute(Job j)
  }

  interface WorkerService extends Service, Worker {
    + void start()
    + void shutdown()
  }

  ' rels

  Concurrent -[hidden]d- Service

  IoTask -[hidden]r- IoRunner

  IoTask -[hidden]d- Job

  Job -[hidden]d- Worker

  @enduml

   */

  private static final AtomicInteger IO_COUNTER = new AtomicInteger();

  private static final AtomicInteger WORKER_COUNTER = new AtomicInteger();

  private Concurrent() {}

  /**
   * Executes the specified task to its completion.
   *
   * @param task
   *        the task to be executed to its completion
   */
  public static void exhaust(CpuTask task) {
    Check.notNull(task, "task == null");

    while (task.isActive()) {
      task.executeOne();
    }
  }

  static String nextIoName() {
    return "Io-" + IO_COUNTER.getAndIncrement();
  }

  static String nextWorkerName() {
    return "Worker-" + WORKER_COUNTER.getAndIncrement();
  }

}
