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
package objectox.http.media;

import static org.testng.Assert.assertEquals;

import java.nio.file.Path;
import org.testng.annotations.Test;

public class StaticFilesExtensionTest {

  @Test
  public void get01() {
    final StaticFilesExtension subject;
    subject = new StaticFilesExtension("*");

    final Path file;
    file = Path.of("foo.html");

    assertEquals(subject.get(file), ".html");
  }

  @Test
  public void get02() {
    final StaticFilesExtension subject;
    subject = new StaticFilesExtension("*");

    final Path file;
    file = Path.of("foo");

    assertEquals(subject.get(file), "*");
  }

  @Test
  public void get03() {
    final StaticFilesExtension subject;
    subject = new StaticFilesExtension("*");

    final Path file;
    file = Path.of("foo.");

    assertEquals(subject.get(file), "*");
  }

}
