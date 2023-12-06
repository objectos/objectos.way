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

import objectos.concurrent.Computation;
import objectos.concurrent.CpuTask;

/**
 * A result producing low-level Git task. It encapsulates any Git operation
 * provided by a {@link GitEngine} such as opening a repository, reading a
 * commit object from a repository or copying a blob object from one repository
 * to another repository.
 *
 * <p>
 * Instances of this interface are bound to a single {@link GitEngine} and,
 * since engines are not thread-safe, tasks are not thread-safe either and must
 * be run in a single thread. Tasks from distinct engines can be executed
 * concurrently in the same thread. Note, however, that tasks coming from the
 * same engine instance, if executed concurrently, will in practice run
 * <em>sequentially</em>.
 *
 * @param <V> the type of result
 *
 * @since 3
 */
public interface GitTask<V> extends Computation<V>, CpuTask {}