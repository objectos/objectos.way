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
package objectos.http;

import static org.testng.Assert.assertSame;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class HostHandlerTest {

  private final HttpHeaderName test = HttpHeaderName.of("Way-Test");

  private final Response mainResp = Response.create(opts -> {
    opts.header(test, "main");
  });

  private final Response filesResp = Response.create(opts -> {
    opts.header(test, "files");
  });

  private HostHandler handler;

  @BeforeClass
  public void beforeClass() {
    final Handler main = req -> {
      if (req.path().equals("/main")) {
        return mainResp;
      } else {
        return req;
      }
    };

    final Handler staticFiles = req -> {
      if (req.path().equals("/files")) {
        return filesResp;
      } else {
        return req;
      }
    };

    handler = new HostHandler(main, staticFiles);
  }

  @Test
  public void testCase01() {
    assertSame(
        handler.handle(Request.create(opts -> {
          opts.path("/main");
        })),

        mainResp
    );
  }

  @Test
  public void testCase02() {
    assertSame(
        handler.handle(Request.create(opts -> {
          opts.path("/files");
        })),

        filesResp
    );
  }

  @Test
  public void testCase03() {
    final Request req;
    req = Request.create(opts -> {
      opts.path("/other");
    });

    assertSame(
        handler.handle(req),

        req
    );
  }

}
