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

import java.util.List;
import objectos.way.Html;
import objectox.http.Header;
import objectox.http.resp.ResponseEntity;
import objectox.http.resp.ResponsePojo;
import objectox.http.resp.StatusEnum;
import org.testng.annotations.Test;

public class ResponseTest {

  @Test
  public void create01() {
    final Content content;
    content = Content.of(MediaType.TEXT_PLAIN, "test");

    assertEquals(
        Response.create(opts -> {
          opts.status(Status.BAD_REQUEST);

          opts.header(HeaderName.CONNECTION, "close");

          opts.send(content);
        }),

        new ResponsePojo(
            StatusEnum.BAD_REQUEST,

            List.of(new Header(HeaderName.CONNECTION, "close")),

            new ResponseEntity.OfContent(content),

            true
        )
    );
  }

  @Test
  public void of01() {
    final Content content;
    content = Content.of(MediaType.TEXT_PLAIN, "test");

    assertEquals(
        Response.of(Status.NOT_FOUND, content),

        new ResponsePojo(
            StatusEnum.NOT_FOUND,

            List.of(Header.DATE),

            new ResponseEntity.OfContent(content),

            false
        )
    );
  }

  @Test
  public void of02() {
    final Content content;
    content = Content.of(MediaType.TEXT_PLAIN, "test");

    final ContentProvider provider;
    provider = () -> content;

    assertEquals(
        Response.of(Status.NOT_FOUND, provider),

        new ResponsePojo(
            StatusEnum.NOT_FOUND,

            List.of(Header.DATE),

            new ResponseEntity.OfContent(content),

            false
        )
    );
  }

  @Test(description = "empty string if content is not testable")
  public void toTestableText01() {
    final Content content;
    content = Content.of(MediaType.TEXT_PLAIN, "test");

    final Response subject;
    subject = Response.create(opts -> {
      opts.send(content);
    });

    assertEquals(
        subject.toTestableText(),

        ""
    );
  }

  @Test(description = "testable text if content is testable")
  public void toTestableText02() {
    final ContentProvider html;
    html = new Html.Template() {
      @Override
      protected void render() {
        testableH1("Heading 1");

        testableField("foo", "bar");
      }
    };

    final Response subject;
    subject = Response.create(opts -> {
      opts.send(html);
    });

    assertEquals(
        subject.toTestableText(),

        """
        # Heading 1

        foo: bar
        """
    );
  }

}
