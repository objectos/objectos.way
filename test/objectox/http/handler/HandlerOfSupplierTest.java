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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.util.function.Supplier;
import objectos.http.Content;
import objectos.http.Handler;
import objectos.http.MediaType;
import objectos.http.Result;
import org.testng.Assert;
import org.testng.annotations.Test;

public class HandlerOfSupplierTest {

  @Test
  public void handle01() {
    final Content content;
    content = Content.of(MediaType.TEXT_PLAIN, "HandlerOfSupplier");

    final Supplier<? extends Result> supplier;
    supplier = () -> content;

    final Handler subject;
    subject = new HandlerOfSupplier(supplier);

    final Result res;
    res = subject.handle(null);

    assertSame(res, content);
  }

  @Test
  public void handle02() {
    final Content content;
    content = null;

    final Supplier<? extends Result> supplier;
    supplier = () -> content;

    final Handler subject;
    subject = new HandlerOfSupplier(supplier);

    try {
      subject.handle(null);

      Assert.fail("It should have thrown");
    } catch (NullPointerException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "Supplier provided a null result");
    }
  }

}
