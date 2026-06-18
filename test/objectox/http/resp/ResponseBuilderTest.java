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
package objectox.http.resp;

import static org.testng.Assert.assertEquals;

import java.nio.file.Path;
import java.util.List;
import objectos.http.Content;
import objectos.http.ContentProvider;
import objectos.http.HeaderName;
import objectos.http.MediaType;
import objectos.http.Status;
import objectos.y.PathY;
import objectox.http.Header;
import org.testng.annotations.Test;

public class ResponseBuilderTest {

  @Test(description = "empty => defaults")
  public void build01() {
    final ResponseBuilder builder;
    builder = new ResponseBuilder();

    assertEquals(
        builder.build(),

        new ResponsePojo(
            StatusEnum.OK,

            List.of(),

            ResponseEntity.OfEmpty.INSTANCE,

            false
        )
    );
  }

  @Test
  public void closeConnection01() {
    final ResponseBuilder builder;
    builder = new ResponseBuilder();

    builder.header(HeaderName.CONNECTION, "close");

    assertEquals(
        builder.build(),

        new ResponsePojo(
            StatusEnum.OK,

            List.of(new Header(HeaderName.CONNECTION, "close")),

            ResponseEntity.OfEmpty.INSTANCE,

            true
        )
    );
  }

  @Test
  public void closeConnection02() {
    final ResponseBuilder builder;
    builder = new ResponseBuilder();

    builder.header(HeaderName.CONNECTION, "ClOsE");

    assertEquals(
        builder.build(),

        new ResponsePojo(
            StatusEnum.OK,

            List.of(new Header(HeaderName.CONNECTION, "ClOsE")),

            ResponseEntity.OfEmpty.INSTANCE,

            true
        )
    );
  }

  @Test(description = "header(name, string)")
  public void header01() {
    final ResponseBuilder builder;
    builder = new ResponseBuilder();

    builder.header(HeaderName.ALLOW, "GET");

    assertEquals(
        builder.build(),

        new ResponsePojo(
            StatusEnum.OK,

            List.of(new Header(HeaderName.ALLOW, "GET")),

            ResponseEntity.OfEmpty.INSTANCE,

            false
        )
    );
  }

  @Test(description = "header(name, long)")
  public void header02() {
    final ResponseBuilder builder;
    builder = new ResponseBuilder();

    builder.header(HeaderName.CONTENT_LENGTH, 123456L);

    assertEquals(
        builder.build(),

        new ResponsePojo(
            StatusEnum.OK,

            List.of(new Header(HeaderName.CONTENT_LENGTH, "123456")),

            ResponseEntity.OfEmpty.INSTANCE,

            false
        )
    );
  }

  @Test(description = "header(name, builder)")
  public void header03() {
    final ResponseBuilder builder;
    builder = new ResponseBuilder();

    builder.header(HeaderName.CONTENT_DISPOSITION, v -> {
      v.value("test");

      v.param("foo", "bar");
    });

    assertEquals(
        builder.build(),

        new ResponsePojo(
            StatusEnum.OK,

            List.of(new Header(HeaderName.CONTENT_DISPOSITION, "test; foo=bar")),

            ResponseEntity.OfEmpty.INSTANCE,

            false
        )
    );
  }

  @Test(description = "header + header should keep insert order")
  public void header04() {
    final ResponseBuilder builder;
    builder = new ResponseBuilder();

    builder.header(HeaderName.CONTENT_TYPE, "image/jpeg");

    builder.header(HeaderName.CONTENT_LENGTH, 4780L);

    assertEquals(
        builder.build(),

        new ResponsePojo(
            StatusEnum.OK,

            List.of(
                new Header(HeaderName.CONTENT_TYPE, "image/jpeg"),

                new Header(HeaderName.CONTENT_LENGTH, "4780")
            ),

            ResponseEntity.OfEmpty.INSTANCE,

            false
        )
    );
  }

  @Test(description = "send(file)")
  public void send01() {
    final Path file;
    file = PathY.nextFile();

    final ResponseBuilder builder;
    builder = new ResponseBuilder();

    builder.send(file);

    assertEquals(
        builder.build(),

        new ResponsePojo(
            StatusEnum.OK,

            List.of(),

            new ResponseEntity.OfFile(file),

            false
        )
    );
  }

  @Test(description = "send(content)")
  public void send02() {
    final Content content;
    content = Content.of(MediaType.TEXT_PLAIN, "ok\n");

    final ResponseBuilder builder;
    builder = new ResponseBuilder();

    builder.send(content);

    assertEquals(
        builder.build(),

        new ResponsePojo(
            StatusEnum.OK,

            List.of(),

            new ResponseEntity.OfContent(content),

            false
        )
    );
  }

  @Test(description = "send(provider)")
  public void send03() {
    final Content content;
    content = Content.of(MediaType.TEXT_PLAIN, "ok\n");

    final ContentProvider provider;
    provider = () -> content;

    final ResponseBuilder builder;
    builder = new ResponseBuilder();

    builder.send(provider);

    assertEquals(
        builder.build(),

        new ResponsePojo(
            StatusEnum.OK,

            List.of(),

            new ResponseEntity.OfContent(content),

            false
        )
    );
  }

  @Test(description = "set status")
  public void status01() {
    final ResponseBuilder builder;
    builder = new ResponseBuilder();

    builder.status(Status.BAD_REQUEST);

    assertEquals(
        builder.build(),

        new ResponsePojo(
            StatusEnum.BAD_REQUEST,

            List.of(),

            ResponseEntity.OfEmpty.INSTANCE,

            false
        )
    );
  }

}
