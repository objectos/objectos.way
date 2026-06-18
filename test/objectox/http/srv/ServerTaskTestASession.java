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
package objectox.http.srv;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertSame;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import objectos.http.Content;
import objectos.http.Status;
import objectos.http.MediaType;
import objectos.lang.Key;
import objectos.y.RandomGeneratorY;
import objectos.y.SocketY;
import org.testng.annotations.Test;

public class ServerTaskTestASession {

  private final Content ok = Content.of(MediaType.TEXT_PLAIN, "OK\n");

  @Test
  public void session01() {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.host(host -> {
            host.name = "www.example.com";

            host.handler = req -> {
              final String path;
              path = req.path();

              return switch (path) {
                case "/1" -> {
                  req.sessionAttr(String.class, "FOO\n");

                  yield Content.of(MediaType.TEXT_PLAIN, "NEW\n");
                }

                case "/2" -> {
                  final String attr;
                  attr = req.sessionAttr(String.class);

                  yield Content.of(MediaType.TEXT_PLAIN, attr);
                }

                default -> Status.NOT_FOUND;
              };
            };

            host.session = session -> {
              session.randomGenerator(RandomGeneratorY.ofLongs(1L, 2L, 3L, 4L));
            };
          });

          opts.socket(
              """
              GET /1 HTTP/1.1\r
              Host: www.example.com\r
              \r
              """,
              """
              GET /2 HTTP/1.1\r
              Host: www.example.com\r
              Connection: close\r
              Cookie: WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
              \r
              """
          );
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Set-Cookie: WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=; HttpOnly; Path=/; Secure\r
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
        ServerTaskY.resp(opts -> {
          opts.host(host -> {
            host.name = "www.example.com";

            host.handler = req -> {
              final String path;
              path = req.path();

              return switch (path) {
                case "/1" -> {
                  req.sessionAttr(class1, subject);

                  yield ok;
                }

                case "/2" -> {
                  final Subject attr;
                  attr = req.sessionAttr(class2);

                  assertSame(attr, subject);

                  yield ok;
                }

                default -> Status.NOT_FOUND;
              };
            };

            host.session = session -> {
              session.randomGenerator(RandomGeneratorY.ofLongs(1L, 2L, 3L, 4L));
            };
          });

          opts.socket(
              """
              GET /1 HTTP/1.1\r
              Host: www.example.com\r
              \r
              """,
              """
              GET /2 HTTP/1.1\r
              Host: www.example.com\r
              Connection: close\r
              Cookie: WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
              \r
              """
          );
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Set-Cookie: WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=; HttpOnly; Path=/; Secure\r
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

  private final Key<String> testKey = Key.of("TEST");

  @Test
  public void session03() {
    assertEquals(
        ServerTaskY.resp(opts -> {
          opts.host(host -> {
            host.name = "www.example.com";

            host.handler = req -> {
              final String path;
              path = req.path();

              return switch (path) {
                case "/1" -> {
                  req.sessionAttr(testKey, "FOO\n");

                  yield Content.of(MediaType.TEXT_PLAIN, "NEW\n");
                }

                case "/2" -> {
                  final String attr;
                  attr = req.sessionAttr(testKey);

                  yield Content.of(MediaType.TEXT_PLAIN, attr);
                }

                default -> Status.NOT_FOUND;
              };
            };

            host.session = session -> {
              session.randomGenerator(RandomGeneratorY.ofLongs(1L, 2L, 3L, 4L));
            };
          });

          opts.socket = SocketY.of(
              """
              GET /1 HTTP/1.1\r
              Host: www.example.com\r
              \r
              """,
              """
              GET /2 HTTP/1.1\r
              Host: www.example.com\r
              Connection: close\r
              Cookie: WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
              \r
              """
          );
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Set-Cookie: WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=; HttpOnly; Path=/; Secure\r
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
        ServerTaskY.resp(opts -> {
          opts.host(host -> {
            host.name = "www.example.com";

            host.handler = req -> {
              final String path;
              path = req.path();

              return switch (path) {
                case "/1" -> {
                  req.sessionAttr(String.class, "FOO\n");

                  yield Content.of(MediaType.TEXT_PLAIN, "NEW\n");
                }

                case "/2" -> {
                  final String attr;
                  attr = req.sessionAttr(String.class);

                  req.sessionInvalidate();

                  yield Content.of(MediaType.TEXT_PLAIN, attr);
                }

                case "/3" -> {
                  final String attr;
                  attr = req.sessionAttr(String.class);

                  assertNull(attr);

                  yield ok;
                }

                default -> Status.NOT_FOUND;
              };
            };

            host.session = session -> {
              session.randomGenerator(RandomGeneratorY.ofLongs(1L, 2L, 3L, 4L));
            };
          });

          opts.socket(
              """
              GET /1 HTTP/1.1\r
              Host: www.example.com\r
              \r
              """,
              """
              GET /2 HTTP/1.1\r
              Host: www.example.com\r
              Cookie: WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
              \r
              """,
              """
              GET /3 HTTP/1.1\r
              Host: www.example.com\r
              Connection: close\r
              Cookie: WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
              \r
              """
          );
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Set-Cookie: WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=; HttpOnly; Path=/; Secure\r
        Content-Type: text/plain; charset=utf-8\r
        Content-Length: 4\r
        \r
        NEW
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Set-Cookie: WAY=; HttpOnly; Max-Age=0; Path=/; Secure\r
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
        ServerTaskY.resp(opts -> {
          opts.host(host -> {
            host.name = "www.example.com";

            host.handler = req -> {
              final String path;
              path = req.path();

              return switch (path) {
                case "/1" -> {
                  req.sessionAttr(String.class, "FOO\n");

                  yield Content.of(MediaType.TEXT_PLAIN, "NEW\n");
                }

                case "/2" -> {
                  final String attr;
                  attr = req.sessionAttr(String.class);

                  req.sessionAttr(String.class, null);

                  yield Content.of(MediaType.TEXT_PLAIN, attr);
                }

                case "/3" -> {
                  final String attr;
                  attr = req.sessionAttr(String.class);

                  assertNull(attr);

                  yield ok;
                }

                default -> Status.NOT_FOUND;
              };
            };

            host.session = session -> {
              session.randomGenerator(RandomGeneratorY.ofLongs(1L, 2L, 3L, 4L));
            };
          });

          opts.socket = SocketY.of(
              """
              GET /1 HTTP/1.1\r
              Host: www.example.com\r
              \r
              """,
              """
              GET /2 HTTP/1.1\r
              Host: www.example.com\r
              Cookie: WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
              \r
              """,
              """
              GET /3 HTTP/1.1\r
              Host: www.example.com\r
              Connection: close\r
              Cookie: WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=\r
              \r
              """
          );
        }),

        """
        HTTP/1.1 200 OK\r
        Date: Wed, 28 Jun 2023 12:08:43 GMT\r
        Set-Cookie: WAY=AAAAAAAAAAEAAAAAAAAAAgAAAAAAAAADAAAAAAAAAAQ=; HttpOnly; Path=/; Secure\r
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