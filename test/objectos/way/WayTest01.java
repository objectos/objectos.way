/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
package objectos.way;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(description = "Builds an existing project")
public final class WayTest01 extends WayTest00Support {

  private Project project;

  @BeforeClass
  public void prepare() {
    project = project(opts -> {
      opts.addFile("main/module-info.java", """
      module objectos.test {
        exports objectos.test;

        requires objectos.way;
      }
      """);

      opts.addFile("main/objectos/test/Start.java", """
      package objectos.test;
      import objectos.way.App;
      public final class Start extends App.Bootstrap {
        @Override
        protected final void bootstrap() {
        }
      }
      """);
    });
  }

  public void testCase01() {
    assertEquals(project.ls(), """
    Way.java
    main/module-info.java
    main/objectos/test/Start.java
    """);

    project.way();

    project.waitFor();

    assertEquals(project.ls(), """
    .objectos/repository/e5a9574a4b58c9af8cc4c84e2f3e032786c32fa4.jar
    Way.java
    main/module-info.java
    main/objectos/test/Start.java
    """);
  }

}