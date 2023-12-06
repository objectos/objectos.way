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

import objectos.lang.object.ToString;

final class WriteCommitTask extends AbstractGitTask<ObjectId> {

  private final MutableCommit commit;

  private final Repository repository;

  WriteCommitTask(GitEngine engine, Repository repository, MutableCommit commit) {
    super(engine);

    this.repository = repository;

    this.commit = commit;
  }

  @Override
  public final void formatToString(StringBuilder toString, int level) {
    ToString.format(
        toString, level, this,
        "repository", repository,
        "commit", commit
    );
  }

  @Override
  final WriteCommit executeSetInputImpl() {
    WriteCommit writeCommit;
    writeCommit = engine.getWriteCommit();

    writeCommit.setInput(repository, commit);

    return writeCommit;
  }

}