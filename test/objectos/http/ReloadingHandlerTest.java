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

import objectos.way.Y;
import org.testng.annotations.Test;

public class ReloadingHandlerTest {

  @Test
  public void testCase01() throws Exception {
    try (var y = ReloadingHandlerY.of()) {
      y.javaFile("module-info.java", """
      module test.way {
        exports test;

        requires objectos.way;
      }
      """);

      y.javaFile("test/Subject.java", """
      package test;

      public final class Subject implements objectos.http.Handler {
        @Override
        public objectos.http.Result handle(objectos.http.Request req) {
          return objectos.http.Status.NOT_FOUND;
        }
      }
      """);

      assertEquals(y.compile(), true);

      final ClassLoader boot;
      boot = y.bootstrap();

      final Class<?> context;
      context = boot.loadClass("test.Subject");

      try (var subject = ReloadingHandler.create(opts -> {
        opts.directory(y.classOutput());

        opts.moduleOf(context);

        opts.noteSink(Y.noteSink());

        opts.reloadingFunction(loader -> y.handler(loader));
      })) {
        final Object res0;
        res0 = subject.handle(null);

        assertEquals(res0.toString(), "Status[404=Not Found]");

        y.javaFile("test/Subject.java", """
        package test;

        public final class Subject implements objectos.http.Handler {
          @Override
          public objectos.http.Result handle(objectos.http.Request req) {
            return objectos.http.Status.FOUND;
          }
        }
        """);

        assertEquals(y.compile(), true);

        final Object res1;
        res1 = subject.handle(null);

        assertEquals(res1.toString(), "Status[302=Found]");
      }
    }
  }

}