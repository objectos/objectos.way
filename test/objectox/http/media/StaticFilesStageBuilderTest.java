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
import java.util.Set;
import objectos.y.PathY;
import org.testng.Assert;
import org.testng.annotations.Test;

public class StaticFilesStageBuilderTest {

  @Test(description = "reject non-directory")
  public void addDirectory01() {
    final StaticFilesStageBuilder subject;
    subject = new StaticFilesStageBuilder();

    final Path file;
    file = PathY.nextFile();

    try {
      subject.addDirectory(file);

      Assert.fail("It should have thrown");
    } catch (IllegalArgumentException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "Path %s does not represent a directory".formatted(file));
    }
  }

  @Test(description = "reject null")
  public void addDirectory02() {
    final StaticFilesStageBuilder subject;
    subject = new StaticFilesStageBuilder();

    final Path file;
    file = null;

    try {
      subject.addDirectory(file);

      Assert.fail("It should have thrown");
    } catch (NullPointerException expected) {
      final String msg;
      msg = expected.getMessage();

      assertEquals(msg, "directory == null");
    }
  }

  @Test(description = "accept directory proper")
  public void addDirectory03() {
    final StaticFilesStageBuilder subject;
    subject = new StaticFilesStageBuilder();

    final Path dir;
    dir = PathY.nextDir();

    subject.addDirectory(dir);

    final StaticFilesStage res;
    res = subject.build();

    assertEquals(res.directories(), Set.of(dir));
  }

}
