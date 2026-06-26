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
import static org.testng.Assert.assertSame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.function.Consumer;
import objectos.http.RequestBodyFiles;
import objectos.lang.Throwables;
import objectos.way.Y;
import objectos.y.PathY;
import objectos.y.SocketY;
import objectox.http.HttpClientException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class RequestBodyDataParserTest {

  private static final class Cfg implements RequestBodyFiles {
    Path bodyDirectory;

    int bodyMemoryMax = 512;

    long bodySizeMax = 1024;

    long id = 0;

    RequestInputStream input;

    RequestBodyMeta.Data meta;

    static Consumer<? super Cfg> of(Consumer<? super Cfg> config) {
      return config;
    }

    @Override
    public final Path get() throws IOException {
      final String name;
      name = Long.toString(id);

      return bodyDirectory.resolve(name);
    }

    final RequestBodyDataParser build() {
      final RequestBodyConfig config;
      config = new RequestBodyConfig(this, bodyMemoryMax, bodySizeMax);

      final RequestBodySupport support;
      support = new RequestBodySupport(config);

      return new RequestBodyDataParser(support, input, meta);
    }
  }

  private RequestInputStream input(Object... data) {
    try {
      final Socket socket;
      socket = SocketY.of(data);

      return RequestInputStream.of(512, socket);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private RequestBodyData parse(Consumer<? super Cfg> config) throws IOException {
    final Cfg c;
    c = new Cfg();

    config.accept(c);

    final RequestBodyDataParser parser;
    parser = c.build();

    return parser.parse();
  }

  @DataProvider
  public Object[][] validProvider() throws IOException {
    final Path directory;
    directory = PathY.nextDir();

    final String data1;
    data1 = "1".repeat(64);

    final String data2;
    data2 = "2".repeat(64);

    return new Object[][] {
        {
            Cfg.of(cfg -> {
              cfg.meta = RequestBodyMeta.DataKind.EMPTY;
            }),

            RequestBodyData.ofNull(),
            "",
            "empty"
        },
        {
            Cfg.of(cfg -> {
              cfg.bodyMemoryMax = data1.length();

              cfg.input = input(data1 + data2);

              cfg.meta = new RequestBodyMeta.Fixed(data1.length());
            }),

            RequestBodyData.of(iso8859(data1)),
            data1,
            "memory: happy-path"
        },
        {
            Cfg.of(cfg -> {
              cfg.bodyMemoryMax = data1.length();

              cfg.input = input(data1.substring(0, 32), data1.substring(32, 64));

              cfg.meta = new RequestBodyMeta.Fixed(data1.length());
            }),

            RequestBodyData.of(iso8859(data1)),
            data1,
            "memory: split input"
        },
        {
            Cfg.of(cfg -> {
              cfg.bodyMemoryMax = data1.length();

              cfg.input = input(Y.slowStream(1, data1 + data2));

              cfg.meta = new RequestBodyMeta.Fixed(data1.length());
            }),

            RequestBodyData.of(iso8859(data1)),
            data1,
            "memory: slow stream"
        },
        {
            Cfg.of(cfg -> {
              cfg.bodyDirectory = directory;

              cfg.bodyMemoryMax = data1.length() - 1;

              cfg.id = 123L;

              cfg.input = input(data1 + data2);

              cfg.meta = new RequestBodyMeta.Fixed(data1.length());
            }),

            RequestBodyData.of(directory.resolve("123")),
            data1,
            "file: happy-path"
        }
    };
  }

  @SuppressWarnings("exports")
  @Test(dataProvider = "validProvider")
  public void valid(
      Consumer<? super Cfg> config,
      RequestBodyData expected,
      String contents,
      String description) throws IOException {
    final RequestBodyData res;
    res = parse(config);

    assertEquals(res, expected);

    final ByteArrayOutputStream out;
    out = new ByteArrayOutputStream();

    try (InputStream in = res.open()) {
      in.transferTo(out);
    }

    assertEquals(new String(out.toByteArray()), contents);
  }

  @DataProvider
  public Object[][] invalidProvider() {
    final String data1;
    data1 = "1".repeat(64);

    final IOException iex;
    iex = Throwables.trimStackTrace(new IOException(), 1);

    return new Object[][] {
        {
            Cfg.of(cfg -> {
              cfg.bodySizeMax = 100;

              cfg.meta = new RequestBodyMeta.Fixed(100 + 1);
            }),

            HttpClientException.Kind.CONTENT_TOO_LARGE,
            "The request message body exceeds the server's maximum allowed limit: 101 > 100"
        },
        {
            Cfg.of(cfg -> {
              cfg.bodyMemoryMax = 128;

              cfg.input = input(data1, iex);

              cfg.meta = new RequestBodyMeta.Fixed(128);
            }),

            iex,
            "memory: IOException during read"
        },
        {
            Cfg.of(cfg -> {
              cfg.bodyMemoryMax = 128;

              cfg.input = input(data1);

              cfg.meta = new RequestBodyMeta.Fixed(128);
            }),

            HttpClientException.Kind.INCOMPLETE_REQUEST_BODY,
            "EOF while reading request body"
        },
        {
            Cfg.of(cfg -> {
              cfg.bodyDirectory = PathY.nextDir();

              cfg.bodyMemoryMax = 32;

              cfg.input = input(data1, iex);

              cfg.meta = new RequestBodyMeta.Fixed(128);
            }),

            iex,
            "file: IOException during read"
        }
    };
  }

  @SuppressWarnings("exports")
  @Test(dataProvider = "invalidProvider")
  public void invalid(
      Consumer<? super Cfg> config,
      Object foo,
      String msg) throws IOException {
    try {
      parse(config);

      Assert.fail("It should have thrown");
    } catch (HttpClientException expected) {
      assertEquals(expected.getMessage(), msg);

      assertEquals(expected.kind, foo);
    } catch (IOException expected) {
      assertSame(expected, foo);
    }
  }

  private byte[] iso8859(String s) {
    return s.getBytes(StandardCharsets.ISO_8859_1);
  }

}
