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
package objectox.http.req;

import static org.testng.Assert.assertEquals;

import objectos.http.HttpHeaderName;
import objectos.http.Request;
import objectox.http.RequestMethodEnum;
import org.testng.annotations.Test;

public class RequestTest {

  @Test
  public void create01() {
    final Request r;
    r = Request.create(_ -> {});

    assertEquals(r.header(HttpHeaderName.WAY_REQUEST), null);
    assertEquals(r.method(), RequestMethodEnum.GET);
    assertEquals(r.path(), "/");
  }

  @Test
  public void create02() {
    final Request r;
    r = Request.create(opts -> {
      opts.header(HttpHeaderName.WAY_REQUEST, "foo");

      opts.method(RequestMethodEnum.PATCH);

      opts.path("/test/123");
    });

    assertEquals(r.header(HttpHeaderName.WAY_REQUEST), "foo");
    assertEquals(r.method(), RequestMethodEnum.PATCH);
    assertEquals(r.path(), "/test/123");
  }

}
