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

import static org.testng.Assert.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import objectos.http.HeaderValueOptions;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HeaderValueBuilderTest {

  @DataProvider
  public Object[][] buildProvider() {
    return new Object[][] {
        {
            opts(_ -> {

            }),
            ""
        },
        {
            opts(opts -> {
              opts.value("foo");
            }),
            "foo"
        },
        {
            opts(opts -> {
              opts.value("foo");
              opts.value("bar");
            }),
            "foo, bar"
        },
        {
            opts(opts -> {
              opts.value("attachment");
              opts.param("filename", "document.pdf");
            }),
            "attachment; filename=document.pdf"
        },
        {
            opts(opts -> {
              opts.value("attachment");
              opts.param("filename", "my report.pdf");
            }),
            "attachment; filename=\"my report.pdf\""
        },
        {
            opts(opts -> {
              opts.value("attachment");
              opts.param("filename*", StandardCharsets.UTF_8, "document.pdf");
            }),
            "attachment; filename*=UTF-8''document.pdf"
        },
        {
            opts(opts -> {
              opts.value("attachment");
              opts.param("filename*", StandardCharsets.UTF_8, "my report.pdf");
            }),
            "attachment; filename*=UTF-8''my%20report.pdf"
        }
    };
  }

  @Test(dataProvider = "buildProvider")
  public void build(Consumer<? super HeaderValueOptions> opts, String expected) {
    final HeaderValueBuilder subject;
    subject = new HeaderValueBuilder();

    opts.accept(subject);

    final String res;
    res = subject.build();

    assertEquals(res, expected);
  }

  @Test(description = "reject param w/ no value")
  public void build02() {
    final HeaderValueBuilder subject;
    subject = new HeaderValueBuilder();

    try {
      subject.param("foo", "bar");

      Assert.fail("It should have thrown");
    } catch (IllegalStateException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "Cannot add a parameter: there's no current value");
    }
  }

  @Test(description = "reject charset other than UTF-8")
  public void build03() {
    final HeaderValueBuilder subject;
    subject = new HeaderValueBuilder();

    subject.value("attachment");

    try {
      subject.param("foo", StandardCharsets.US_ASCII, "bar");

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "The UTF-8 charset MUST be used.");
    }
  }

  private Consumer<? super HeaderValueOptions> opts(Consumer<? super HeaderValueOptions> opts) {
    return opts;
  }

}
