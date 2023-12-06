/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
import objectos.concurrent.Computation;
import objectos.fs.Directory;
import objectos.fs.testing.TmpDir;

/**
 * Support reading and parsing loose objects (iter. 3).
 *
 * 0) Materialize use-case
 * 1) bare repo
 * 2) loose objects only
 * 3) commit with single parent (no merge)
 * 4) same as tc01 + 'deep tree'
 *
 * @since 1
 */
final class TestCase02 {

  public static final String DESCRIPTION = "Support loose objects (iter 3)";

  public static Computation<Directory> acceptGitServiceTest(GitService git) throws IOException {
    Directory target;
    target = TmpDir.create();

    MaterializeCommand command;
    command = new MaterializeCommand(getDirectory(), getCommit(), target);

    return git.submit(command);
  }

  public static ObjectId getCommit() throws InvalidObjectIdFormatException {
    return ObjectId.parse("293db19c76f6645343bfdcaf9ae54d47951ddf6a");
  }

  public static Directory getDirectory() throws IOException {
    return TestingGit.repo00();
  }

}