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
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import objectos.way.Lang;
import objectos.way.Media;
import objectos.way.Y;
import org.testng.annotations.Test;

public class HttpServerTaskTestASession {

  private final Media OK = Media.Bytes.textPlain("OK\n");

  @Test
  public void session01() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.sessionStore = HttpSessionStore.create(options -> {
            options.sessionGenerator(Y.randomGeneratorOfLongs(1L, 2L, 3L, 4L));
          });

          opts.socket = Y.socket(
              """
              GET /1 HTTP/1.1\r
              Host: www.example.com\r
              \r
              """,
              """
              GET /2 HTTP/1.1\r
              Host: www.example.com\r
              Connection: close\r
              Cookie: OBJECTOSWAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
              \r
              """
          );

          opts.handler = http -> {
            final String path;
            path = http.path();

            switch (path) {
              case "/1" -> {
                http.sessionAttr(String.class, "FOO\n");

                http.ok(Media.Bytes.textPlain("NEW\n"));
              }

              case "/2" -> {
                final String attr;
                attr = http.sessionAttr(String.class);

                http.ok(Media.Bytes.textPlain(attr));
              }

              default -> http.notFound(OK);
            }
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Set-Cookie: OBJECTOSWAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=; HttpOnly; Path=/; Secure\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        NEW
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        FOO
        """
    );
  }

  public class Subject {}

  @Test(description = "session should be resistant to class reloading (dev mode)")
  public void session02() throws ClassNotFoundException {
    final Class<Subject> class1;
    class1 = Subject.class;

    final Subject subject;
    subject = new Subject();

    final Class<Subject> class2;
    class2 = reload(class1);

    assertFalse(class1.equals(class2));

    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.sessionStore = HttpSessionStore.create(options -> {
            options.sessionGenerator(Y.randomGeneratorOfLongs(1L, 2L, 3L, 4L));
          });

          opts.socket = Y.socket(
              """
              GET /1 HTTP/1.1\r
              Host: www.example.com\r
              \r
              """,
              """
              GET /2 HTTP/1.1\r
              Host: www.example.com\r
              Connection: close\r
              Cookie: OBJECTOSWAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
              \r
              """
          );

          opts.handler = http -> {
            final String path;
            path = http.path();

            switch (path) {
              case "/1" -> {
                http.sessionAttr(class1, subject);

                http.ok(OK);
              }

              case "/2" -> {
                final Subject attr;
                attr = http.sessionAttr(class2);

                assertSame(attr, subject);

                http.ok(OK);
              }

              default -> http.notFound(OK);
            }
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Set-Cookie: OBJECTOSWAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=; HttpOnly; Path=/; Secure\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
  }

  @SuppressWarnings("unchecked")
  private <T> Class<T> reload(Class<T> original) {
    final String className;
    className = original.getName();

    final String pathName;
    pathName = className.replace('.', '/') + ".class";

    final ClassLoader originalLoader;
    originalLoader = original.getClassLoader();

    final byte[] bytes;

    try (
        InputStream in = originalLoader.getResourceAsStream(pathName);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
    ) {
      in.transferTo(out);

      bytes = out.toByteArray();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    final ClassLoader reloader;
    reloader = new ClassLoader(null) {
      @Override
      protected Class<?> findClass(String name) throws ClassNotFoundException {
        return defineClass(name, bytes, 0, bytes.length);
      }
    };

    try {
      return (Class<T>) reloader.loadClass(className);
    } catch (ClassNotFoundException e) {
      throw new AssertionError("Reload failed", e);
    }
  }

  private final Lang.Key<String> testKey = Lang.Key.of("TEST");

  @Test
  public void session03() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.sessionStore = HttpSessionStore.create(options -> {
            options.sessionGenerator(Y.randomGeneratorOfLongs(1L, 2L, 3L, 4L));
          });

          opts.socket = Y.socket(
              """
              GET /1 HTTP/1.1\r
              Host: www.example.com\r
              \r
              """,
              """
              GET /2 HTTP/1.1\r
              Host: www.example.com\r
              Connection: close\r
              Cookie: OBJECTOSWAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
              \r
              """
          );

          opts.handler = http -> {
            final String path;
            path = http.path();

            switch (path) {
              case "/1" -> {
                http.sessionAttr(testKey, "FOO\n");

                http.ok(Media.Bytes.textPlain("NEW\n"));
              }

              case "/2" -> {
                final String attr;
                attr = http.sessionAttr(testKey);

                http.ok(Media.Bytes.textPlain(attr));
              }

              default -> http.notFound(OK);
            }
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Set-Cookie: OBJECTOSWAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=; HttpOnly; Path=/; Secure\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        NEW
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        FOO
        """
    );
  }

  @Test(description = "support logout")
  public void session04() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.sessionStore = HttpSessionStore.create(options -> {
            options.sessionGenerator(Y.randomGeneratorOfLongs(1L, 2L, 3L, 4L));
          });

          opts.socket = Y.socket(
              """
              GET /1 HTTP/1.1\r
              Host: www.example.com\r
              \r
              """,
              """
              GET /2 HTTP/1.1\r
              Host: www.example.com\r
              Cookie: OBJECTOSWAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
              \r
              """,
              """
              GET /3 HTTP/1.1\r
              Host: www.example.com\r
              Connection: close\r
              Cookie: OBJECTOSWAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
              \r
              """
          );

          opts.handler = http -> {
            final String path;
            path = http.path();

            switch (path) {
              case "/1" -> {
                http.sessionAttr(String.class, "FOO\n");

                http.ok(Media.Bytes.textPlain("NEW\n"));
              }

              case "/2" -> {
                final String attr;
                attr = http.sessionAttr(String.class);

                http.sessionInvalidate();

                http.ok(Media.Bytes.textPlain(attr));
              }

              case "/3" -> {
                final String attr;
                attr = http.sessionAttr(String.class);

                assertNull(attr);

                http.ok(OK);
              }

              default -> http.notFound(OK);
            }
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Set-Cookie: OBJECTOSWAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=; HttpOnly; Path=/; Secure\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        NEW
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        FOO
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
  }

  @Test(description = "support unsetting a value")
  public void session05() {
    assertEquals(
        HttpServerTaskY.resp(opts -> {
          opts.sessionStore = HttpSessionStore.create(options -> {
            options.sessionGenerator(Y.randomGeneratorOfLongs(1L, 2L, 3L, 4L));
          });

          opts.socket = Y.socket(
              """
              GET /1 HTTP/1.1\r
              Host: www.example.com\r
              \r
              """,
              """
              GET /2 HTTP/1.1\r
              Host: www.example.com\r
              Cookie: OBJECTOSWAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
              \r
              """,
              """
              GET /3 HTTP/1.1\r
              Host: www.example.com\r
              Connection: close\r
              Cookie: OBJECTOSWAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
              \r
              """
          );

          opts.handler = http -> {
            final String path;
            path = http.path();

            switch (path) {
              case "/1" -> {
                http.sessionAttr(String.class, "FOO\n");

                http.ok(Media.Bytes.textPlain("NEW\n"));
              }

              case "/2" -> {
                final String attr;
                attr = http.sessionAttr(String.class);

                http.sessionAttr(String.class, null);

                http.ok(Media.Bytes.textPlain(attr));
              }

              case "/3" -> {
                final String attr;
                attr = http.sessionAttr(String.class);

                assertNull(attr);

                http.ok(OK);
              }

              default -> http.notFound(OK);
            }
          };
        }),

        """
        HTTP/1.1 200 OK\r
        Set-Cookie: OBJECTOSWAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=; HttpOnly; Path=/; Secure\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        NEW
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        FOO
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 3\r
        \r
        OK
        """
    );
  }

}