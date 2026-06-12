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
package objectox.http;

import static org.testng.Assert.assertSame;

import java.util.Iterator;
import objectos.http.Handler;
import objectos.http.HttpStatus;
import objectos.http.RequestMethod;
import objectos.http.RequestMethodY;
import objectos.http.Response;
import objectos.http.Request;
import objectos.http.Result;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RoutingPojoTest {

  private final Response okResp = Response.create(opts -> {
    opts.status(HttpStatus.OK);
  });

  @Test(description = "empty => request")
  public void testCase01() {
    final Request request;
    request = Request.create(_ -> {});

    final Handler subject;
    subject = RoutingPojo.create0(_ -> {});

    final Result res;
    res = subject.handle(request);

    assertSame(res, request);
  }

  @DataProvider
  public Iterator<RequestMethod> methodProvider() {
    return RequestMethodY.iterator();
  }

  @Test(
      enabled = false,
      description = "Non-method HttpHandler should accept all methods",
      dataProvider = "methodProvider"
  )
  public void testCase02(RequestMethod method) {
    final Request request;
    request = Request.create(opts -> {
      opts.method(method);
    });

    final Handler subject;
    subject = RoutingPojo.create0(r -> {
      r.at("/test", okResp);
    });

    final Result res;
    res = subject.handle(request);

    assertSame(res, okResp);
  }

}
