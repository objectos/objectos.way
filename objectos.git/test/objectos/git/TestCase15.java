/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.git;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Commit tree entry support
 */
public final class TestCase15 {

  private TestCase15() {}

  public static void repositoryTo(Path root) throws IOException {
    Path path;
    path = TestingGit2.repo03();

    TestingGit2.copyRecursively(path, root);
  }

  public static ObjectId treeId() throws IOException {
    return ObjectId.parse("400be3b2d3e8ba872ba980e93a3d533b16367732");
  }

}