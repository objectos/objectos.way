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

final class UpdateRefTask extends AbstractGitTask<MaybeObjectId> {

  private final ObjectId newValue;

  private final RefName ref;

  private final Repository repository;

  private UpdateRef updateRef;

  UpdateRefTask(GitEngine engine, Repository repository, RefName ref, ObjectId newValue) {
    super(engine);

    this.repository = repository;

    this.ref = ref;

    this.newValue = newValue;
  }

  @Override
  public final void formatToString(StringBuilder toString, int level) {
    ToString.format(
        toString, level, this,
        "repository", repository,
        "ref", ref,
        "newValue", newValue
    );
  }

  @Override
  final UpdateRef executeSetInputImpl() {
    updateRef = engine.getUpdateRef();

    updateRef.setInput(repository, ref, newValue);

    return updateRef;
  }

}