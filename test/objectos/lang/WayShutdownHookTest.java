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
import java.util.ArrayList;
import java.util.List;
import objectos.notes.Note1;
import objectos.notes.Note2;
import objectos.way.TestingNoteSink;
import org.testng.Assert;
import org.testng.annotations.Test;

public class WayShutdownHookTest {

  @Test
  public void getShutdownHook() {
    WayShutdownHook hook;
    hook = new WayShutdownHook();

    ThisNoteSink noteSink;
    noteSink = new ThisNoteSink();

    hook.noteSink(noteSink);

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

    List<Object> hooks;
    hooks = noteSink.hooks;

    assertEquals(hooks.size(), 3);
    assertSame(hooks.get(0), cleanClosable);
    assertSame(hooks.get(1), dirtyCloseable);
    assertSame(hooks.get(2), someThread);

    List<Object> ignored;
    ignored = noteSink.ignored;
    
    assertEquals(ignored.size(), 0);
    
    try {
      Thread thread;
      thread = hook.startAndJoinThread();

      assertTrue(cleanClosable.closed);

      assertTrue(dirtyCloseable.closed);

      assertTrue(someThread.intCalled);

      List<Throwable> exceptions;
      exceptions = noteSink.exceptions;

      assertEquals(exceptions.size(), 1);

      assertSame(exceptions.get(0), ioException);

      Runtime runtime;
      runtime = Runtime.getRuntime();

      assertTrue(runtime.removeShutdownHook(thread));
    } catch (InterruptedException e) {
      Assert.fail("InterruptedException", e);
    }
  }
  
  @Test
  public void registerIfPossible() {
    WayShutdownHook hook;
    hook = new WayShutdownHook();

    ThisNoteSink noteSink;
    noteSink = new ThisNoteSink();

    hook.noteSink(noteSink);

    CloseableImpl closable;
    closable = new CloseableImpl();

    hook.registerIfPossible(closable);
    
    Thread thread;
    thread = new Thread();
    
    hook.registerIfPossible(thread);

    NotCloseable notCloseable;
    notCloseable = new NotCloseable();

    hook.registerIfPossible(notCloseable);

    List<Object> hooks;
    hooks = noteSink.hooks;

    assertEquals(hooks.size(), 2);
    assertSame(hooks.get(0), closable);
    assertSame(hooks.get(1), thread);

    List<Object> ignored;
    ignored = noteSink.ignored;

    assertEquals(ignored.size(), 1);
    assertSame(ignored.get(0), notCloseable);
  }
  
  private static class NotCloseable {
    @SuppressWarnings("unused")
    public final void close() throws IOException {}
  }

  private static class ThisNoteSink extends TestingNoteSink {

    final List<Throwable> exceptions = new ArrayList<>();

    final List<Object> hooks = new ArrayList<>();

    final List<Object> ignored = new ArrayList<>();
    
    @Override
    public <T1> void send(Note1<T1> note, T1 v1) {
      super.send(note, v1);

      if (note == ShutdownHook.REGISTRATION) {
        hooks.add(v1);
      } else if (note == ShutdownHook.IGNORED) {
        ignored.add(v1);
      }
    }

    @Override
    public <T1, T2> void send(Note2<T1, T2> note, T1 v1, T2 v2) {
      super.send(note, v1, v2);

      if (v2 instanceof Throwable t) {
        exceptions.add(t);
      }
    }

  }

}
