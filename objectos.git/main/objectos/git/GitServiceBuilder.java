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

import objectos.concurrent.CpuArray;
import objectos.concurrent.CpuWorker;
import objectos.concurrent.IoWorker;
import objectos.lang.object.Check;
import objectos.notes.NoOpNoteSink;
import objectos.notes.NoteSink;

/**
 * @since 3
 */
final class GitServiceBuilder {

  private int bufferSize = Git.DEFAULT_BUFFER_SIZE;

  private final CpuArray cpuArray;

  private int enginesPerWorker = 1;

  private final IoWorker ioWorker;

  private NoteSink logger = NoOpNoteSink.of();

  GitServiceBuilder(CpuArray cpuArray, IoWorker ioWorker) {
    this.cpuArray = Check.notNull(cpuArray, "cpuArray == null");

    this.ioWorker = Check.notNull(ioWorker, "ioWorker == null");
  }

  public final GitService build() {
    int arraySize;
    arraySize = cpuArray.size();

    GitWorker[] workers;
    workers = new GitWorker[arraySize];

    GitEngine.Option[] engineOptions = {
        GitEngine.bufferSize(bufferSize),

        GitEngine.logger(logger)
    };

    for (int i = 0; i < arraySize; i++) {
      CpuWorker cpuWorker;
      cpuWorker = cpuArray.get(i);

      workers[i] = GitWorker.create(cpuWorker, ioWorker, enginesPerWorker, engineOptions);
    }

    return new GitService(cpuArray, workers);
  }

  final void setBufferSize(int size) {
    this.bufferSize = size;
  }

  final void setEnginesPerWorker(int value) {
    enginesPerWorker = value;
  }

  final void setLogger(NoteSink logger) {
    this.logger = logger;
  }

}