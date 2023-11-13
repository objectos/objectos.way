/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectox.lang;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchService;
import objectos.lang.ClassReloader;
import objectos.lang.ClassReloader.Builder;
import objectos.lang.object.Check;
import objectos.notes.NoteSink;
import objectos.lang.NoOpNoteSink;
import objectos.util.GrowableList;

public final class ClassReloaderBuilder implements ClassReloader.Builder {

  final GrowableList<ClassReloaderImpl.Directory> directories = new GrowableList<>();

  NoteSink noteSink = NoOpNoteSink.of();

  WatchService watchService;

  @Override
  public final Builder watch(Path directory, String prefix) {
    Check.argument(Files.isDirectory(directory), "Not a directory: ", directory);
    Check.notNull(prefix, "prefix == null");

    ClassReloaderImpl.Directory dir;
    dir = new ClassReloaderImpl.Directory(directory, prefix);

    directories.add(dir);

    return this;
  }

  @Override
  public final Builder noteSink(NoteSink noteSink) {
    this.noteSink = Check.notNull(noteSink, "noteSink == null");

    return this;
  }

  @Override
  public final Builder watchService(WatchService service) {
    watchService = Check.notNull(service, "service == null");

    return this;
  }

  @Override
  public final ClassReloader of(String binaryName) throws IOException {
    Check.notNull(binaryName, "binaryName == null");

    if (watchService == null) {
      FileSystem fileSystem;
      fileSystem = FileSystems.getDefault();

      watchService = fileSystem.newWatchService();
    }

    return new ClassReloaderImpl(binaryName, this);
  }

}
