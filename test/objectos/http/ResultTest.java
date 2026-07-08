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

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import objectos.lang.Throwables;
import objectox.http.StatusThrowable;
import org.testng.annotations.Test;

public class ResultTest {

  private final IOException exception = Throwables.trimStackTrace(new IOException(), 1);

  @Test(
      description = "reject non-error status",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Status does not represent a client error nor a server error")
  public void error01() {
    Result.error(Status.OK, exception);
  }

  @Test(
      description = "reject null cause",
      expectedExceptions = NullPointerException.class,
      expectedExceptionsMessageRegExp = "cause == null")
  public void error02() {
    Result.error(Status.INTERNAL_SERVER_ERROR, null);
  }

  @Test(description = "accept")
  public void error03() {
    final Result res;
    res = Result.error(Status.INTERNAL_SERVER_ERROR, exception);

    final StatusThrowable pojo;
    pojo = (StatusThrowable) res;

    assertEquals(pojo.status(), Status.INTERNAL_SERVER_ERROR);
    assertEquals(pojo.cause(), exception);
  }

}
