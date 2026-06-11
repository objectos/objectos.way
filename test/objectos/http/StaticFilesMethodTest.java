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

import java.util.EnumSet;
import java.util.Iterator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class StaticFilesMethodTest {

  private final StaticFilesMethod subject = new StaticFilesMethod();

  @DataProvider
  public Iterator<HttpMethod> validProvider() {
    return EnumSet.of(HttpMethod.GET, HttpMethod.HEAD).iterator();
  }

  @Test(dataProvider = "validProvider")
  public void valid(HttpMethod method) throws StaticFilesErrMethod {
    final Request request;
    request = Request.create(opts -> {
      opts.method(method);
    });

    subject.validate(request);
  }

  @DataProvider
  public Iterator<HttpMethod> invalidProvider() {
    return EnumSet.complementOf(EnumSet.of(HttpMethod.GET, HttpMethod.HEAD)).iterator();
  }

  @Test(dataProvider = "invalidProvider")
  public void invalid(HttpMethod method) {
    final Request request;
    request = Request.create(opts -> {
      opts.method(method);
    });

    try {
      subject.validate(request);

      Assert.fail("It should have thrown");
    } catch (StaticFilesErrMethod e) {
      assertSame(e.method, method);
    }
  }

}
