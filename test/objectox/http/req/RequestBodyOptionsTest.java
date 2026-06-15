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

import java.io.IOException;
import java.util.function.Consumer;
import objectos.http.RequestBodyOptions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RequestBodyOptionsTest {

  @Test
  public void testCase01() throws IOException {
    final RequestBodyOptionsPojo pojo;
    pojo = RequestBodyOptionsPojo.create(_ -> {});

    assertEquals(pojo.memoryMax(), 32 * 1024);
    assertEquals(pojo.sizeMax(), 10 * 1024 * 1024);
  }

  @Test
  public void testCase02() throws IOException {
    final RequestBodyOptionsPojo pojo;
    pojo = RequestBodyOptionsPojo.create(opts -> {
      opts.memoryMax(64 * 1024);

      opts.sizeMax(20 * 1024 * 1024);
    });

    assertEquals(pojo.memoryMax(), 64 * 1024);
    assertEquals(pojo.sizeMax(), 20 * 1024 * 1024);
  }

  @DataProvider
  public Object[][] testCase03Provider() {
    return new Object[][] {
        {opts(opts -> opts.memoryMax(-1)), "memoryMax value must not be negative"},
        {opts(opts -> opts.sizeMax(-1)), "sizeMax value must not be negative"}
    };
  }

  @Test(dataProvider = "testCase03Provider")
  public void testCase03(Consumer<? super RequestBodyOptions> opts, String message) {
    try {
      RequestBodyOptionsPojo.create(opts);

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), message);
    }
  }

  private Consumer<? super RequestBodyOptions> opts(Consumer<? super RequestBodyOptions> opts) {
    return opts;
  }

}
