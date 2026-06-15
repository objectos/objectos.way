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
package objectox.http.media;

import static org.testng.Assert.assertSame;

import java.util.EnumSet;
import java.util.Iterator;
import objectos.http.Request;
import objectox.http.RequestMethodEnum;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@SuppressWarnings("exports")
public class StaticFilesMethodTest {

  private final StaticFilesMethod subject = new StaticFilesMethod();

  @DataProvider
  public Iterator<RequestMethodEnum> validProvider() {
    return EnumSet.of(RequestMethodEnum.GET, RequestMethodEnum.HEAD).iterator();
  }

  @Test(dataProvider = "validProvider")
  public void valid(RequestMethodEnum method) throws StaticFilesErrMethod {
    final Request request;
    request = Request.create(opts -> {
      opts.method(method);
    });

    subject.validate(request);
  }

  @DataProvider
  public Iterator<RequestMethodEnum> invalidProvider() {
    return EnumSet.complementOf(EnumSet.of(RequestMethodEnum.GET, RequestMethodEnum.HEAD)).iterator();
  }

  @Test(dataProvider = "invalidProvider")
  public void invalid(RequestMethodEnum method) {
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
