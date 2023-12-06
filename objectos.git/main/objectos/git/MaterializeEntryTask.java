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

import objectos.fs.Directory;
import objectos.lang.object.ToString;

final class MaterializeEntryTask extends AbstractGitTask<MaterializedEntry> {

  private final Entry entry;

  private final Repository repository;

  private final Directory target;

  MaterializeEntryTask(GitEngine engine,
                       Repository repository,
                       Entry entry,
                       Directory target) {
    super(engine);

    this.repository = repository;

    this.entry = entry;

    this.target = target;
  }

  @Override
  public final void formatToString(StringBuilder toString, int level) {
    ToString.format(
        toString, level, this,
        "repository", repository,
        "entry", entry,
        "target", target
    );
  }

  @Override
  final MaterializeEntry executeSetInputImpl() {
    MaterializeEntry materializeEntry;
    materializeEntry = engine.getMaterializeEntry();

    materializeEntry.setInput(repository, entry, target);

    return materializeEntry;
  }

}