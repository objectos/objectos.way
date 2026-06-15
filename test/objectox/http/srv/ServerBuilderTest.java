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
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.function.Consumer;
import objectos.http.HostOptions;
import objectox.http.host.HostMap;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ServerBuilderTest {

  @Test(description = "reject null")
  public void host01() {
    final ServerBuilder subject;
    subject = new ServerBuilder();

    try {
      subject.host(null);

      Assert.fail("It should have thrown");
    } catch (NullPointerException expected) {}
  }

  @Test(description = "accept")
  public void host02() throws IOException {
    final ServerBuilder subject;
    subject = new ServerBuilder();

    final Consumer<? super HostOptions> opts;
    opts = host -> {
      host.name("www.example.com");
    };

    subject.host(opts);

    final ServerPojo pojo;
    pojo = subject.build();

    final HostMap res;
    res = pojo.hosts();

    assertEquals(res.size(), 1);
    assertNotNull(res.get("www.example.com:" + pojo.port()));
  }

  @Test(description = "reject host duplicate name")
  public void host03() {
    final ServerBuilder subject;
    subject = new ServerBuilder();

    try {
      subject.host(host -> {
        host.name("duplicate.example.com");
      });

      subject.host(host -> {
        host.name("duplicate.example.com");
      });

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "A host with the same name was already registered: duplicate.example.com");
    }
  }

  @Test(description = "reject negative")
  public void port01() {
    final ServerBuilder subject;
    subject = new ServerBuilder();

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
    final ServerBuilder subject;
    subject = new ServerBuilder();

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
    final ServerBuilder subject;
    subject = new ServerBuilder();

    try (ServerPojo res = subject.build()) {
      assertTrue(res.port() > 0);

      assertTrue(res.port() < 0xFFFF);
    }
  }

  @Test
  public void port04() throws IOException {
    final ServerBuilder subject;
    subject = new ServerBuilder();

    int port = 0;

    try (ServerSocket socket = new ServerSocket(0)) {
      port = socket.getLocalPort();
    }

    subject.port(port);

    try (ServerPojo res = subject.build()) {
      assertEquals(res.port(), port);
    }
  }

}
