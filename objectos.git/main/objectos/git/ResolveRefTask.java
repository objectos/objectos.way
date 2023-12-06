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

final class ResolveRefTask extends AbstractGitTask<MaybeObjectId> {

  private final RefName ref;

  private final Repository repository;

  private ResolveRef resolveRef;

  public ResolveRefTask(GitEngine engine, Repository repository, RefName ref) {
    super(engine);

    this.repository = repository;

    this.ref = ref;
  }

  @Override
  public final void formatToString(StringBuilder toString, int level) {
    ToString.format(
        toString, level, this,
        "repository", repository,
        "ref", ref
    );
  }

  @Override
  final void executeFinally() {
    resolveRef = engine.putResolveRef(resolveRef);
  }

  @Override
  final AbstractGitEngineTask executeSetInputImpl() {
    resolveRef = engine.getResolveRef();

    resolveRef.setInput(repository, ref);

    return resolveRef;
  }

}