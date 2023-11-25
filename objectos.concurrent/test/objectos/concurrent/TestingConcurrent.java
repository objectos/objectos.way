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

final class TestingConcurrent {

  /*
  
  @startmindmap
  
  *_ Test cases
  
  **:**Test case 04**
  ----
  SubmitJob use-case. For the case
  when the job is successful.
  ----
  # use worker1
  # submit CountingJob
  # signal job done
  # verify job;
  *** Job
  *** Worker
  *** WorkerService
  
  **:**Test case 03**
  ----
  SubmitIoTask use-case. For the case
  when the task times out.
  Also verifies that SingleThreadIoExecutor
  recovers from worker thread interruption.
  ----
  # use singleThreadIoExecutorService
  # submit CountingTask w/ timeout
  # assert runner.isActive()
  # assert task started
  # assert !runner.isActive()
  # assert runner.isExceptional()
  # assert runner.getException() ==
        TimeOutException;
  *** IoExecutor
  *** IoExecutorService
  *** IoRunner
  *** IoTask
  
  **:**Test case 02**
  ----
  SubmitIoTask use-case. For the case
  when the task throws an Exception.
  ----
  # use singleThreadIoExecutorService
  # submit CountingTask
  # assert runner.isActive()
  # assert task started
  # create IOException
  # signal task exception
  # assert !runner.isActive()
  # assert runner.isExceptional()
  # assert runner exception;
  *** IoExecutor
  *** IoExecutorService
  *** IoRunner
  *** IoTask

  **:**Test case 01**
  ----
  SubmitIoTask use-case. For the case
  when the task is successful.
  ----
  # use singleThreadIoExecutorService
  # submit CountingTask
  # assert runner.isActive()
  # assert task started
  # signal task done
  # assert !runner.isActive()
  # assert !runner.isExceptional();
  *** IoExecutor
  *** IoExecutorService
  *** IoRunner
  *** IoTask
  
  @endmindmap
  
   */

  private TestingConcurrent() {}

  public static CountingIoTask createCountingTask() {
    return new CountingIoTask();
  }

}