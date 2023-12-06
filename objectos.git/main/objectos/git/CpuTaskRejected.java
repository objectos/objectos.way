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

import java.util.concurrent.ExecutionException;
import objectos.concurrent.Computation;
import objectos.concurrent.CpuTask;

final class CpuTaskRejected<V> implements Computation<V> {

  private final String simpleName;

  CpuTaskRejected(CpuTask task) {
    Class<?> t;
    t = task.getClass();

    simpleName = t.getSimpleName();
  }

  @Override
  public final V getResult() throws IllegalStateException, ExecutionException {
    throw new IllegalStateException(simpleName + " rejected");
  }

  @Override
  public final boolean isActive() {
    return false;
  }

}