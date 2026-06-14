
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
package objectox.http.handler;

import static org.testng.Assert.assertSame;

import java.util.Iterator;
import java.util.List;
import objectos.http.Content;
import objectos.http.Handler;
import objectos.http.HttpStatus;
import objectos.http.MediaType;
import objectos.http.Redirect;
import objectos.http.Response;
import objectos.http.Result;
import objectos.http.StaticFile;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HandlerResultTest {

  @DataProvider
  public Iterator<Result> handleProvider() {
    return List.<Result> of(
        Content.of(MediaType.TEXT_PLAIN, "foo"),

        Redirect.found("/login"),

        Response.create(_ -> {}),

        StaticFile.of(Content.of(MediaType.TEXT_PLAIN, "static")),

        HttpStatus.NOT_FOUND
    ).iterator();
  }

  @Test(dataProvider = "handleProvider")
  public void handle(Result result) {
    final Handler subject;
    subject = new HandlerResult(result);

    final Result res;
    res = subject.handle(null);

    assertSame(res, result);
  }

}
