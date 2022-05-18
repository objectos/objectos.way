/*
 * Copyright (C) 2022-2022 Objectos Software LTDA.
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
    ShutdownHookLogger logger;
    logger = new ShutdownHookLogger();

    ShutdownHook.setLogger(logger);

    CloseableImpl cleanClosable;
    cleanClosable = new CloseableImpl();

    ShutdownHook.register(cleanClosable);

    IOException ioException;
    ioException = new IOException();

    CloseableImpl dirtyCloseable;
    dirtyCloseable = new CloseableImpl(ioException);

    ShutdownHook.register(dirtyCloseable);

    ShutdownHookTaskImpl cleanListener;
    cleanListener = new ShutdownHookTaskImpl();

    ShutdownHook.register(cleanListener);

    Exception exception;
    exception = new Exception();

    ShutdownHookTaskImpl dirtyListener;
    dirtyListener = new ShutdownHookTaskImpl(exception);

    ShutdownHook.register(dirtyListener);

    try {
      ShutdownHook hook;
      hook = ShutdownHook.getHook();

      Thread thread;
      thread = hook.startAndJoinThread();

      assertTrue(cleanClosable.closed);

      assertTrue(dirtyCloseable.closed);

      assertTrue(cleanListener.called);

      assertTrue(dirtyListener.called);

      List<Exception> exceptions;
      exceptions = logger.exceptions;

      assertEquals(exceptions.size(), 2);

      assertSame(exceptions.get(0), exception);

      assertSame(exceptions.get(1), ioException);

      Runtime runtime;
      runtime = Runtime.getRuntime();

      assertTrue(runtime.removeShutdownHook(thread));
    } catch (InterruptedException e) {
      Assert.fail("InterruptedException", e);
    }
  }

}
