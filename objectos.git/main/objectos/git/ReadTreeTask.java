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
import objectos.notes.Note1;

final class ReadTreeTask extends AbstractGitTask<Tree> {

  static final Note1<ObjectId> ESET_INPUT;

  static {
    Class<?> source;
    source = ReadTreeTask.class;

    ESET_INPUT = Note1.debug(source, "Set input");
  }

  private final ObjectId id;

  private final Repository repository;

  ReadTreeTask(GitEngine engine, Repository repository, ObjectId id) {
    super(engine);

    this.repository = repository;

    this.id = id;
  }

  @Override
  public final void formatToString(StringBuilder toString, int level) {
    ToString.format(
        toString, level, this,
        "repository", repository,
        "id", id
    );
  }

  @Override
  final ObjectReader executeSetInputImpl() {
    ObjectReader reader;
    reader = engine.getObjectReader();

    ReadTree readTree;
    readTree = engine.getReadTree();

    readTree.set(repository, id);

    reader.set(readTree);

    send(ESET_INPUT, id);

    return reader;
  }

}