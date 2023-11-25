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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import objectos.core.service.Services;
import objectos.notes.Level;
import objectos.notes.NoOpNoteSink;
import objectos.notes.NoteSink;
import objectos.notes.console.ConsoleNoteSink;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ConcurrentTest implements IoTask {

  private static final byte IO_COUNTER = 0;

  private volatile int counter;

  private volatile boolean counterActive;

  private IOException counterError;

  private CountingCpuTask countingJob;

  private CpuWorkerService fixedCpuWorker;

  private Throwable ioError;

  private volatile boolean ioRunning;

  private byte ioTask;

  private JamWorkerCpuTask jamWorkerJob;

  private NoteSink logger;

  private IoWorkerService singleThreadIoWorker;

  private volatile boolean waiting;

  @BeforeClass
  public void _beforeClass() throws Exception {
    logger = ConsoleNoteSink.of(Level.TRACE);

    countingJob = new CountingCpuTask();

    jamWorkerJob = new JamWorkerCpuTask();

    singleThreadIoWorker = new SingleThreadIoWorker(logger);

    fixedCpuWorker = new FixedCpuWorker(1, 2, logger);

    Services.start(
        singleThreadIoWorker,

        fixedCpuWorker
    );
  }

  @BeforeMethod
  public void _beforeMethod() {
    counter = 0;

    counterActive = true;

    counterError = null;

    countingJob.reset();

    ioError = null;

    ioRunning = false;

    jamWorkerJob.reset();

    //logger.clear();

    waiting = false;
  }

  @Override
  public final void executeIo() {
    try {
      executeIoTask(ioTask);
    } catch (Throwable t) {
      ioError = t;
    } finally {
      ioRunning = false;

      if (waiting) {
        signal();
      }
    }
  }

  @Test
  public void setLogger() {
    try {
      fixedCpuWorker.setLogger(NoOpNoteSink.of());

      Assert.fail("expected exception was not thrown");
    } catch (IllegalStateException expected) {
      String message;
      message = expected.getMessage();

      assertEquals(message, "Service already started");
    }

    try {
      singleThreadIoWorker.setLogger(NoOpNoteSink.of());

      Assert.fail("expected exception was not thrown");
    } catch (IllegalStateException expected) {
      String message;
      message = expected.getMessage();

      assertEquals(message, "Service already started");
    }
  }

  @Test(timeOut = 1000)
  public void testCase01() throws InterruptedException {
    assertFalse(ioRunning);

    toIo(IO_COUNTER);

    waitFor();

    assertTrue(ioRunning);

    int value;
    value = counter;

    assertTrue(value > 0);

    waitFor();

    assertTrue(counter > value);

    counterActive = false;

    waitFor();

    assertFalse(ioRunning);

    assertNull(ioError);
  }

  @Test(timeOut = 1000)
  public void testCase02() throws InterruptedException {
    assertFalse(ioRunning);

    toIo(IO_COUNTER);

    waitFor();

    assertTrue(ioRunning);

    int value;
    value = counter;

    assertTrue(value > 0);

    waitFor();

    assertTrue(counter > value);

    counterError = new IOException();

    waitFor();

    assertFalse(ioRunning);

    assertNotNull(ioError);

    assertSame(ioError, counterError);
  }

  @Test(timeOut = 1000)
  public void testCase03() throws InterruptedException {
    assertFalse(ioRunning);

    toIo(IO_COUNTER);

    waitFor();

    assertTrue(ioRunning);

    int value;
    value = counter;

    assertTrue(value > 0);

    waitFor();

    assertTrue(counter > value);

    assertTrue(singleThreadIoWorker.cancelOrInterrupt(this));

    value = counter;

    assertTrue(counter == value);
  }

  @Test(dependsOnMethods = "testCase03", description = ""
      + "verify that singleThreadExecutor recovers from a task cancellation.",
      timeOut = 1000)
  public void testCase03b() throws InterruptedException {
    testCase01();
  }

  @Test(timeOut = 1000)
  public void testCase04() throws InterruptedException {
    class ThisJob implements CpuTask {
      boolean active = true;

      volatile int i;

      volatile boolean started = false;

      private boolean stop;

      @Override
      public final void executeOne() {
        if (stop) {
          try {
            Thread.sleep(50);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          } finally {
            active = false;
          }
        }

        i++;
      }

      public final int get() {
        return i;
      }

      @Override
      public final boolean isActive() {
        if (!started) {
          signal();

          started = true;
        }

        if (!active) {
          signal();
        }

        return active;
      }

      public final void signalStop() {
        stop = true;
      }
    }

    assertTrue(fixedCpuWorker.offer(jamWorkerJob));

    ThisJob job;
    job = new ThisJob();

    assertTrue(fixedCpuWorker.offer(job));

    Thread.sleep(100);

    assertTrue(jamWorkerJob.get() > 0);

    jamWorkerJob.jam();

    waitFor();

    int before;
    before = job.get();

    job.signalStop();

    waitFor();

    assertTrue(before < job.get());
  }

  private void executeIoTask(byte task) throws InterruptedException, IOException {
    switch (task) {
      case IO_COUNTER:
        ioCountingTask();
        break;
      default:
        throw new UnsupportedOperationException("Implement me: task=" + task);
    }
  }

  private void ioCountingTask() throws InterruptedException, IOException {
    while (counterActive) {
      counter++;

      Thread.sleep(2);

      if (counterError != null) {
        throw counterError;
      }

      if (waiting) {
        signal();
      }
    }
  }

  private void signal() {
    synchronized (this) {
      if (waiting) {
        waiting = false;

        notifyAll();
      }
    }
  }

  private void toIo(byte task) {
    ioTask = task;

    ioRunning = true;

    singleThreadIoWorker.submit(this);
  }

  private void waitFor() throws InterruptedException {
    synchronized (this) {
      waiting = true;

      wait();
    }
  }

}