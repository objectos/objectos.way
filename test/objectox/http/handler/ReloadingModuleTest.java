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

import java.io.IOException;
import objectos.way.Y;
import org.testng.annotations.Test;

public class ReloadingModuleTest {

  @Test
  public void testCase01() throws IOException, ClassNotFoundException {
    try (var y = ReloadingModuleY.of()) {
      y.javaFile("module-info.java", """
      module test.way {
        exports test;

        requires objectos.way;
      }
      """);

      y.javaFile("test/Subject.java", """
      package test;

      public class Subject implements objectos.http.Handler {
        public objectos.http.Result handle(objectos.http.Request req) {
          return null;
        }
      }
      """);

      assertEquals(y.compile(), true);

      final ClassLoader classLoader;
      classLoader = y.classLoader("test.way");

      final Class<?> context;
      context = classLoader.loadClass("test.Subject");

      final ReloadingModuleBuilder builder;
      builder = new ReloadingModuleBuilder(context);

      final ReloadingModule subject;
      subject = builder.build();

      final ClassLoader res;
      res = subject.findLoader(_ -> true, _ -> true, Y.noteSink());

      assertEquals(res instanceof ReloadingClassLoader, true);
    }
  }

}
