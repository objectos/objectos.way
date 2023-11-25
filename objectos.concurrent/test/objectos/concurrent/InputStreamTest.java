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
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import objectos.fs.Directory;
import objectos.fs.RegularFile;
import objectos.fs.testing.TmpDir;
import org.testng.annotations.Test;

public class InputStreamTest {

  private final Random random = new Random();

  @Test(timeOut = 5000)
  public void interrupt() throws IOException, InterruptedException {
    int size;
    size = 50 * 1024 * 1024;

    byte[] buffer;
    buffer = new byte[size];

    Directory root;
    root = TmpDir.create();

    RegularFile test;
    test = root.createRegularFile("test");

    OutputStream out;
    out = test.openOutputStream();

    try {
      random.nextBytes(buffer);

      out.write(buffer);
    } finally {
      out.close();
    }

    class Runner extends Thread {

      private final byte[] buffer;

      private boolean closed;

      private boolean finished;

      private final InputStream inputStream;

      private IOException ioException;

      private long readCount;

      private boolean started;

      Runner(int size, InputStream inputStream) {
        this.buffer = new byte[size];
        this.inputStream = inputStream;
      }

      @Override
      public final void run() {
        try {
          started = true;

          Thread.sleep(20);

          signal();

          readCount = inputStream.read(buffer);

          finished = true;
        } catch (IOException e) {
          ioException = e;
        } catch (InterruptedException e) {
          return;
        } finally {
          try {
            inputStream.close();
          } catch (IOException e) {}

          closed = true;
        }
      }

    }

    InputStream inputStream;
    inputStream = test.openInputStream();

    try {
      Runner runner;
      runner = new Runner(size, inputStream);

      runner.start();

      waitFor();

      runner.interrupt();

      assertTrue(runner.isInterrupted());

      assertTrue(runner.started);

      assertFalse(runner.finished);

      assertEquals(runner.readCount, 0);

      assertNull(runner.ioException);

      assertFalse(runner.closed);
    } finally {
      inputStream.close();
    }
  }

  private void signal() {
    synchronized (this) {
      notifyAll();
    }
  }

  private void waitFor() throws InterruptedException {
    synchronized (this) {
      wait();
    }
  }

}