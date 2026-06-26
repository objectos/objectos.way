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

import java.io.IOException;
import java.time.Clock;
import objectos.way.Y;
import objectox.http.req.RequestBodyConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ServerLoopBuilderTest {

  @Test
  public void bufferSize01() {
    final ServerLoopBuilder builder;
    builder = new ServerLoopBuilder();

    try {
      builder.bufferSize(127);

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "Invalid buffer size: buffers must hold at least 128 bytes");
    }
  }

  @Test
  public void bufferSize02() throws IOException {
    final ServerLoopBuilder builder;
    builder = new ServerLoopBuilder();

    builder.bufferSize(128);

    try (var subject = builder.unstarted()) {
      assertEquals(subject.bufferSize, 128);
    }
  }

  @Test
  public void bufferSize03() throws IOException {
    final ServerLoopBuilder builder;
    builder = new ServerLoopBuilder();

    try (var subject = builder.unstarted()) {
      assertEquals(subject.bufferSize, 4096);
    }
  }

  @Test
  public void clock01() {
    final ServerLoopBuilder builder;
    builder = new ServerLoopBuilder();

    try {
      builder.clock(null);

      Assert.fail("It should have thrown");
    } catch (NullPointerException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "value == null");
    }
  }

  @Test
  public void clock02() throws IOException {
    final ServerLoopBuilder builder;
    builder = new ServerLoopBuilder();

    try (var subject = builder.unstarted()) {
      assertEquals(subject.clock, Clock.systemUTC());
    }
  }

  @Test
  public void clock03() throws IOException {
    final ServerLoopBuilder builder;
    builder = new ServerLoopBuilder();

    builder.clock(Y.clockFixed());

    try (var subject = builder.unstarted()) {
      assertEquals(subject.clock, Y.clockFixed());
    }
  }

  @Test
  public void requestBody01() {
    final ServerLoopBuilder builder;
    builder = new ServerLoopBuilder();

    try {
      builder.requestBody(null);

      Assert.fail("It should have thrown");
    } catch (NullPointerException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "Cannot invoke \"java.util.function.Consumer.accept(Object)\" because \"opts\" is null");
    }
  }

  @Test
  public void requestBody02() throws IOException {
    final ServerLoopBuilder builder;
    builder = new ServerLoopBuilder();

    try (var subject = builder.unstarted()) {
      final RequestBodyConfig config;
      config = subject.requestBodyConfig;

      assertEquals(config.memoryMax(), 32 * 1024);
      assertEquals(config.sizeMax(), 10 * 1024 * 1024);
    }
  }

  @Test
  public void requestBody03() throws IOException {
    final ServerLoopBuilder builder;
    builder = new ServerLoopBuilder();

    builder.requestBody(opts -> {
      opts.memoryMax(6789);
      opts.sizeMax(123456L);
    });

    try (var subject = builder.unstarted()) {
      final RequestBodyConfig config;
      config = subject.requestBodyConfig;

      assertEquals(config.memoryMax(), 6789);
      assertEquals(config.sizeMax(), 123456L);
    }
  }

}
