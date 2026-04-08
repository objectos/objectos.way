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
import static org.testng.Assert.assertSame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import objectos.http.HttpRequestParser8BodyData.Invalid;
import objectos.way.Y;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class HttpRequestParser8BodyDataTest {

  private static final class Cfg extends HttpRequestBodySupport {
    Path bodyDirectory;

    int bodyMemoryMax = 512;

    long bodySizeMax = 1024;

    private Path file;

    long id = 0;

    HttpRequestParser0Input input;

    HttpRequestBodyMeta.Data meta;

    static Consumer<? super Cfg> of(Consumer<? super Cfg> config) {
      return config;
    }

    final HttpRequestParser8BodyData build() {
      return new HttpRequestParser8BodyData(this, input, meta);
    }

    @Override
    public final void close() throws IOException {
      if (file != null) {
        Files.delete(file);
      }
    }

    @Override
    final Path file() {
      if (file == null) {
        final String fileName;
        fileName = Long.toString(id);

        file = bodyDirectory.resolve(fileName);
      }

      return file;
    }

    @Override
    final int memoryMax() { return bodyMemoryMax; }

    @Override
    final long sizeMax() { return bodySizeMax; }
  }

  private HttpRequestParser0Input input(Object... data) {
    try {
      final Socket socket;
      socket = Y.socket(data);

      return HttpRequestParser0Input.of(512, socket);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private HttpRequestBodyData parse(Consumer<? super Cfg> config) throws IOException {
    final Cfg c;
    c = new Cfg();

    config.accept(c);

    final HttpRequestParser8BodyData parser;
    parser = c.build();

    return parser.parse();
  }

  @DataProvider
  public Object[][] validProvider() throws IOException {
    final Path directory;
    directory = Y.nextTempDir();

    final String data1;
    data1 = "1".repeat(64);

    final String data2;
    data2 = "2".repeat(64);

    return new Object[][] {
        {
            Cfg.of(cfg -> {
              cfg.meta = HttpRequestBodyMeta.DataKind.EMPTY;
            }),

            HttpRequestBodyData.ofNull(),
            "",
            "empty"
        },
        {
            Cfg.of(cfg -> {
              cfg.bodyMemoryMax = data1.length();

              cfg.input = input(data1 + data2);

              cfg.meta = new HttpRequestBodyMeta.Fixed(data1.length());
            }),

            HttpRequestBodyData.of(iso8859(data1)),
            data1,
            "memory: happy-path"
        },
        {
            Cfg.of(cfg -> {
              cfg.bodyMemoryMax = data1.length();

              cfg.input = input(data1.substring(0, 32), data1.substring(32, 64));

              cfg.meta = new HttpRequestBodyMeta.Fixed(data1.length());
            }),

            HttpRequestBodyData.of(iso8859(data1)),
            data1,
            "memory: split input"
        },
        {
            Cfg.of(cfg -> {
              cfg.bodyMemoryMax = data1.length();

              cfg.input = input(Y.slowStream(1, data1 + data2));

              cfg.meta = new HttpRequestBodyMeta.Fixed(data1.length());
            }),

            HttpRequestBodyData.of(iso8859(data1)),
            data1,
            "memory: slow stream"
        },
        {
            Cfg.of(cfg -> {
              cfg.bodyDirectory = directory;

              cfg.bodyMemoryMax = data1.length() - 1;

              cfg.id = 123L;

              cfg.input = input(data1 + data2);

              cfg.meta = new HttpRequestBodyMeta.Fixed(data1.length());
            }),

            HttpRequestBodyData.of(directory.resolve(Long.toString(123L))),
            data1,
            "file: happy-path"
        }
    };
  }

  @SuppressWarnings("exports")
  @Test(dataProvider = "validProvider")
  public void valid(
      Consumer<? super Cfg> config,
      HttpRequestBodyData expected,
      String contents,
      String description) throws IOException {
    final HttpRequestBodyData res;
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
    iex = Y.trimStackTrace(new IOException(), 1);

    return new Object[][] {
        {
            Cfg.of(cfg -> {
              cfg.bodySizeMax = 100;

              cfg.meta = new HttpRequestBodyMeta.Fixed(100 + 1);
            }),

            Invalid.CONTENT_TOO_LARGE,
            "content-length exceeds bodySizeMax"
        },
        {
            Cfg.of(cfg -> {
              cfg.bodyMemoryMax = 128;

              cfg.input = input(data1, iex);

              cfg.meta = new HttpRequestBodyMeta.Fixed(128);
            }),

            iex,
            "memory: IOException during read"
        },
        {
            Cfg.of(cfg -> {
              cfg.bodyMemoryMax = 128;

              cfg.input = input(data1);

              cfg.meta = new HttpRequestBodyMeta.Fixed(128);
            }),

            Invalid.EOF,
            "memory: EOF"
        },
        {
            Cfg.of(cfg -> {
              cfg.bodyDirectory = Y.nextTempDir();

              cfg.bodyMemoryMax = 32;

              cfg.input = input(data1, iex);

              cfg.meta = new HttpRequestBodyMeta.Fixed(128);
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
      String description) throws IOException {
    try {
      parse(config);

      Assert.fail("It should have thrown");
    } catch (HttpClientException expected) {
      assertEquals(expected.kind, foo);
    } catch (IOException expected) {
      assertSame(expected, foo);
    }
  }

  private byte[] iso8859(String s) {
    return s.getBytes(StandardCharsets.ISO_8859_1);
  }

}
