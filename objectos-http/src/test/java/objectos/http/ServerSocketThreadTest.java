/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.http;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import org.testng.annotations.Test;

public class ServerSocketThreadTest extends AbstractHttpTest implements ServerSocketThreadAdapter {

  private volatile boolean accepted;

  @Override
  public final void acceptSocket(Socket socket) {
    synchronized (this) {
      try {
        accepted = true;

        socket.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      } finally {
        notifyAll();
      }
    }
  }

  @Test(description = TestCase0001.DESCRIPTION)
  public void testCase01() throws IOException, InterruptedException {
    InetSocketAddress loopback;
    loopback = nextLoopbackSocketAddress();

    ServerSocketThread thread;
    thread = ServerSocketThread.create(this, loopback);

    thread.start();

    assertTrue(thread.isAlive());

    assertTrue(thread.isOpen());

    Thread.sleep(2);

    try (SocketChannel open = SocketChannel.open(loopback)) {
      synchronized (this) {
        wait();
      }
    } finally {
      thread.interrupt();
    }

    assertTrue(accepted);
  }

}