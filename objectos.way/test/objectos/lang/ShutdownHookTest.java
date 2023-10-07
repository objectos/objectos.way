/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.lang;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ShutdownHookTest {

  @Test
  public void getShutdownHook() {
    final ShutdownHook hook;
    hook = ShutdownHook.of();

    ShutdownHookLogger logger;
    logger = new ShutdownHookLogger();

    hook.noteSink(logger);

    CloseableImpl cleanClosable;
    cleanClosable = new CloseableImpl();

    hook.addAutoCloseable(cleanClosable);

    IOException ioException;
    ioException = new IOException();

    CloseableImpl dirtyCloseable;
    dirtyCloseable = new CloseableImpl(ioException);

    hook.addAutoCloseable(dirtyCloseable);

    class SomeThread extends Thread {
      private boolean intCalled;

      @Override
      public final void interrupt() {
        super.interrupt();

        intCalled = true;
      }

      @Override
      public final void run() {
        try {
          while (!isInterrupted()) {
            Thread.sleep(100);
          }
        } catch (InterruptedException e) {

        }
      }

      @Override
      public final synchronized void start() {
        super.start();

        hook.addThread(this);
      }
    }

    SomeThread someThread;
    someThread = new SomeThread();

    someThread.start();

    try {
      Thread thread;
      thread = hook.startAndJoinThread();

      assertTrue(cleanClosable.closed);

      assertTrue(dirtyCloseable.closed);

      assertTrue(someThread.intCalled);

      List<Exception> exceptions;
      exceptions = logger.exceptions;

      assertEquals(exceptions.size(), 1);

      assertSame(exceptions.get(0), ioException);

      Runtime runtime;
      runtime = Runtime.getRuntime();

      assertTrue(runtime.removeShutdownHook(thread));
    } catch (InterruptedException e) {
      Assert.fail("InterruptedException", e);
    }
  }

}
