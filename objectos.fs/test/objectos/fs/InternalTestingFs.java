/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.fs;

import java.io.IOException;
import java.util.List;
import objectos.util.list.GrowableList;

final class InternalTestingFs {

  private static final InternalTestingFs INSTANCE;

  static {
    InternalTestingFs tempDir;
    tempDir = new InternalTestingFs();

    var rt = Runtime.getRuntime();

    rt.addShutdownHook(
        new Thread(
            tempDir::executeShutdownHookTask, "hook-test-fs"
        )
    );

    INSTANCE = tempDir;
  }

  private final List<Directory> tempDirectories = new GrowableList<>();

  private InternalTestingFs() {}

  public static Directory nextDirectory() throws IOException {
    return INSTANCE.createTempDir();
  }

  public final Directory createTempDir() throws IOException {
    Directory tempDirectory;
    tempDirectory = JavaIoTmpdir.get();

    String suffix;
    suffix = Next.string(10);

    Directory directory;
    directory = tempDirectory.createDirectory("objectos-fs-" + suffix);

    tempDirectories.add(directory);

    return directory;
  }

  final void executeShutdownHookTask() {
    for (Directory directory : tempDirectories) {
      try {
        directory.deleteContents();
      } catch (Throwable ignored) {}

      try {
        directory.delete();
      } catch (Throwable ignored) {}
    }
  }

}