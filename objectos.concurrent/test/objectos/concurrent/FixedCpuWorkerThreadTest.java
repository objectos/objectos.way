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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import objectos.concurrent.log.Event0Log;
import objectos.concurrent.log.Event1Log;
import objectos.concurrent.log.Log;
import objectos.notes.Note0;
import objectos.notes.Note1;
import objectos.util.list.GrowableList;
import objectos.util.list.UnmodifiableList;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class FixedCpuWorkerThreadTest {

  private MutableAdapter adapter;

  private FixedCpuWorkerThread thread;

  @BeforeClass
  public void beforeClass() {
    adapter = new MutableAdapter();

    thread = new FixedCpuWorkerThread(adapter);
  }

  @BeforeMethod
  public void beforeMethod() {
    adapter.reset();
  }

  @Test(description = TestCase004.DESCRIPTION)
  public void testCase004() throws InterruptedException {
    CpuTask task;
    task = TestCase004.takeTask();

    assertTrue(adapter.offer(task));

    thread.run0();

    adapter.assertLogCount(3);

    adapter.assertLog(0, FixedCpuWorker.UNPARKED);

    adapter.assertLog(1, FixedCpuWorker.JOB_STARTED, task);

    adapter.assertLog(2, FixedCpuWorker.JOB_COMPLETED, task);

    adapter.assertQueueEmpty();

    adapter.assertTasksEmpty();
  }

  @Test(description = TestCase005.DESCRIPTION)
  public void testCase005() throws InterruptedException {
    CpuTask t1;
    t1 = TestCase005.takeTask();

    CpuTask t2;
    t2 = TestCase005.pollTask();

    assertTrue(adapter.offer(t1));

    assertTrue(adapter.offer(t2));

    thread.run0();

    adapter.assertLogCount(5);

    adapter.assertLog(0, FixedCpuWorker.UNPARKED);

    adapter.assertLog(1, FixedCpuWorker.JOB_STARTED, t1);

    adapter.assertLog(2, FixedCpuWorker.JOB_STARTED, t2);

    adapter.assertLog(3, FixedCpuWorker.JOB_COMPLETED, t1);

    adapter.assertLog(4, FixedCpuWorker.JOB_COMPLETED, t2);

    adapter.assertQueueEmpty();

    adapter.assertTasksEmpty();
  }

  @Test(description = TestCase006.DESCRIPTION)
  public void testCase006() throws InterruptedException {
    CpuTask t1;
    t1 = TestCase006.fixedTask(20);

    CpuTask t2;
    t2 = TestCase006.fixedTask(2);

    CpuTask t3;
    t3 = TestCase006.fixedTask(2);

    CpuTask t4;
    t4 = TestCase006.fixedTask(20);

    assertTrue(adapter.offer(t1));

    assertTrue(adapter.offer(t2));

    assertTrue(adapter.offer(t3));

    assertTrue(adapter.offer(t4));

    thread.run0();

    adapter.assertLogCount(9);

    adapter.assertLog(0, FixedCpuWorker.UNPARKED);

    adapter.assertLog(1, FixedCpuWorker.JOB_STARTED, t1);

    adapter.assertLog(2, FixedCpuWorker.JOB_STARTED, t2);

    adapter.assertLog(3, FixedCpuWorker.JOB_STARTED, t3);

    adapter.assertLog(4, FixedCpuWorker.JOB_STARTED, t4);

    adapter.assertLog(5, FixedCpuWorker.JOB_COMPLETED, t2);

    adapter.assertLog(6, FixedCpuWorker.JOB_COMPLETED, t3);

    adapter.assertLog(7, FixedCpuWorker.JOB_COMPLETED, t1);

    adapter.assertLog(8, FixedCpuWorker.JOB_COMPLETED, t4);

    adapter.assertQueueEmpty();

    adapter.assertTasksEmpty();
  }

  @Test(description = TestCase007.DESCRIPTION)
  public void testCase007() throws InterruptedException {
    CpuTask t1;
    t1 = TestCase007.fixedTask(20);

    CpuTask t2;
    t2 = TestCase007.fixedTask(2);

    CpuTask t3;
    t3 = TestCase007.fixedTask(2);

    CpuTask t4;
    t4 = TestCase007.fixedTask(20);

    CpuTask t5;
    t5 = TestCase007.fixedTask(20);

    assertTrue(adapter.offer(t1));

    assertTrue(adapter.offer(t2));

    assertTrue(adapter.offer(t3));

    assertTrue(adapter.offer(t4));

    assertTrue(adapter.offer(t5));

    thread.run0();

    adapter.assertLogCount(11);

    adapter.assertLog(0, FixedCpuWorker.UNPARKED);

    adapter.assertLog(1, FixedCpuWorker.JOB_STARTED, t1);

    adapter.assertLog(2, FixedCpuWorker.JOB_STARTED, t2);

    adapter.assertLog(3, FixedCpuWorker.JOB_STARTED, t3);

    adapter.assertLog(4, FixedCpuWorker.JOB_STARTED, t4);

    adapter.assertLog(5, FixedCpuWorker.JOB_STARTED, t5);

    adapter.assertLog(6, FixedCpuWorker.JOB_COMPLETED, t2);

    adapter.assertLog(7, FixedCpuWorker.JOB_COMPLETED, t3);

    adapter.assertLog(8, FixedCpuWorker.JOB_COMPLETED, t1);

    adapter.assertLog(9, FixedCpuWorker.JOB_COMPLETED, t4);

    adapter.assertLog(10, FixedCpuWorker.JOB_COMPLETED, t5);

    adapter.assertQueueEmpty();

    adapter.assertTasksEmpty();
  }

  final static class MutableAdapter extends FixedCpuWorkerThreadAdapter {

    final GrowableList<Log> logs = new GrowableList<>();

    final BlockingQueue<CpuTask> queue = new ArrayBlockingQueue<CpuTask>(20);

    final CpuTask[] tasks = new CpuTask[10];

    public final void assertLog(int index, Note0 event) {
      Log l;
      l = logs.get(index);

      assertTrue(l.isEvent0(event));
    }

    public final <T1> void assertLog(int index, Note1<T1> event, T1 value) {
      Log l;
      l = logs.get(index);

      assertTrue(l.isEvent1(event, value));
    }

    public final void assertLogCount(int expected) {
      assertEquals(logs.size(), expected);
    }

    public final void assertQueueEmpty() {
      assertTrue(queue.isEmpty());
    }

    public final boolean offer(CpuTask task) {
      return queue.offer(task);
    }

    public final MutableAdapter reset() {
      logs.clear();

      queue.clear();

      return this;
    }

    final void assertLogs(Log... expected) {
      assertEquals(logs, UnmodifiableList.copyOf(expected));
    }

    @Override
    final void assertNull(int index) {
      CpuTask t;
      t = get(index);

      Assert.assertNull(t);
    }

    final void assertTasksEmpty() {
      for (int i = 0; i < tasks.length; i++) {
        assertNull(i);
      }
    }

    @Override
    final CpuTask get(int index) {
      return tasks[index];
    }

    @Override
    final boolean hasSlot(int index) {
      return index >= 0
          && index < tasks.length
          && tasks[index] == null;
    }

    @Override
    final boolean hasTask() {
      return !queue.isEmpty();
    }

    @Override
    final boolean interrupted() {
      return false;
    }

    @Override
    final void log(Note0 event) {
      Event0Log log;
      log = new Event0Log(event);

      logs.add(log);
    }

    @Override
    final <T1> void log(Note1<T1> event, T1 t1) {
      Event1Log<T1> log;
      log = new Event1Log<T1>(event, t1);

      logs.add(log);
    }

    @Override
    final CpuTask poll() {
      return queue.poll();
    }

    @Override
    final void set(int index, CpuTask task) {
      tasks[index] = task;
    }

    @Override
    boolean shutdown() {
      return false;
    }

    @Override
    final CpuTask take() throws InterruptedException {
      return queue.take();
    }

  }

}