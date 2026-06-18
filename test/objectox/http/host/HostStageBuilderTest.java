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
package objectox.http.host;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.util.function.Consumer;
import objectos.http.Handler;
import objectos.http.HostOptions;
import objectos.http.Status;
import objectox.http.handler.HandlerNoop;
import objectox.http.handler.HandlerResult;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HostStageBuilderTest {

  private HostStage create(Consumer<? super HostOptions> opts) {
    final HostStageBuilder builder;
    builder = new HostStageBuilder();

    opts.accept(builder);

    return builder.build();
  }

  @Test
  public void defaults() {
    final HostStage res;
    res = create(_ -> {});

    assertEquals(res.handler(), HandlerNoop.INSTANCE);
    assertEquals(res.name(), "localhost");
    assertEquals(res.sessionSupport(), null);
    assertEquals(res.staticFiles(), null);
  }

  @Test
  public void handler01() {
    try {
      create(opts -> {
        opts.handler(null);
      });

      Assert.fail("It should have thrown");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "value == null");
    }
  }

  @Test
  public void handler02() {
    final Handler handler;
    handler = new HandlerResult(Status.NOT_FOUND);

    final HostStage res;
    res = create(opts -> {
      opts.handler(handler);
    });

    assertSame(res.handler(), handler);
  }

  @Test
  public void name01() {
    try {
      create(opts -> {
        opts.name(null);
      });

      Assert.fail("It should have thrown");
    } catch (NullPointerException expected) {
      assertEquals(expected.getMessage(), "value == null");
    }
  }

  @Test(description = "reject names with port")
  public void name02() {
    try {
      create(opts -> {
        opts.name("www.example.com:8080");
      });

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "Invalid host name: names must not include the port number 'www.example.com:8080'");
    }
  }

  @Test
  public void name03() {
    final HostStage res;
    res = create(opts -> {
      opts.name("www.example.com");
    });

    assertEquals(res.name(), "www.example.com");
  }

}
