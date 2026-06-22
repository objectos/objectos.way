/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.http.srv;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.ServerSocket;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ServerSocketBuilderTest {

  @Test(description = "reject negative")
  public void port01() {
    final ServerSocketBuilder subject;
    subject = new ServerSocketBuilder();

    try {
      subject.port(-1);

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "Invalid port: value must be in the interval 0 <= value < 65536 but found -1");
    }
  }

  @Test(description = "reject above 0xffff")
  public void port02() {
    final ServerSocketBuilder subject;
    subject = new ServerSocketBuilder();

    try {
      subject.port(0xFFFF + 1);

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "Invalid port: value must be in the interval 0 <= value < 65536 but found 65536");
    }
  }

  @Test
  public void port03() throws IOException {
    final ServerSocketBuilder subject;
    subject = new ServerSocketBuilder();

    try (ServerSocket res = subject.build()) {
      assertTrue(res.getLocalPort() > 0);

      assertTrue(res.getLocalPort() < 0xFFFF);
    }
  }

  @Test
  public void port04() throws IOException {
    final ServerSocketBuilder subject;
    subject = new ServerSocketBuilder();

    int port = 0;

    try (ServerSocket socket = new ServerSocket(0)) {
      port = socket.getLocalPort();
    }

    subject.port(port);

    try (ServerSocket res = subject.build()) {
      assertEquals(res.getLocalPort(), port);
    }
  }

}
