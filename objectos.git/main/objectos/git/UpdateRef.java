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

/**
 * @since 3
 */
final class UpdateRef extends AbstractGitEngineTask {

  private static final byte _RESOLVE_REF = 1;

  private static final byte IO_UPDATE_REF = 1;

  private ObjectId newValue;

  private MaybeObjectId previous;

  private RefName ref;

  private Repository repository;

  private ResolveRef resolveRef;

  UpdateRef(GitInjector injector) {
    super(injector);
  }

  public final void setInput(Repository repository, RefName ref, ObjectId newValue) {
    checkSetInput();

    this.repository = repository;

    this.ref = ref;

    this.newValue = newValue;
  }

  @Override
  final byte execute(byte state) {
    switch (state) {
      case _RESOLVE_REF:
        return executeResolveRef();
      default:
        return super.execute(state);
    }
  }

  @Override
  final byte executeFinally() {
    newValue = null;

    previous = null;

    ref = null;

    repository = null;

    resolveRef = injector.putResolveRef(resolveRef);

    return super.executeFinally();
  }

  @Override
  final void executeIo(byte task) throws IOException {
    switch (task) {
      case IO_UPDATE_REF:
        ioUpdateRef();
        break;
      default:
        super.executeIo(task);
        break;
    }
  }

  @Override
  final byte executeStart() {
    super.executeStart();

    resolveRef = injector.getResolveRef();

    resolveRef.setInput(repository, ref);

    return toSubTask(resolveRef, _RESOLVE_REF);
  }

  private byte executeResolveRef() {
    previous = getSubTaskResult();

    return toIo(IO_UPDATE_REF, _FINALLY, _FINALLY);
  }

  private void ioUpdateRef() throws IOException {
    repository.update(ref, newValue);

    setResult(previous);
  }

}
