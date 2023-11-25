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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFileAttributeView;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

final class PosixFileModeOptionJava7 implements PosixFileModeOption {

  static final PosixFileModeOptionJava7 OWNER_EXECUTABLE = new PosixFileModeOptionJava7(
      PosixFilePermission.OWNER_EXECUTE
  );

  static final PosixFileModeOptionJava7 OWNER_READABLE = new PosixFileModeOptionJava7(
      PosixFilePermission.OWNER_READ
  );

  static final PosixFileModeOptionJava7 OWNER_WRITABLE = new PosixFileModeOptionJava7(
      PosixFilePermission.OWNER_WRITE
  );

  private final PosixFilePermission permission;

  private final FileAttribute<?>[] single;

  PosixFileModeOptionJava7(PosixFilePermission permission) {
    single = PosixJava7.single(permission);

    this.permission = permission;
  }

  @Override
  public final Object createRegularFileGetValue() {
    return single;
  }

  @Override
  public final void createRegularFileSetValue(Object o) {
    FileAttributeBuilder builder;
    builder = (FileAttributeBuilder) o;

    builder.add(permission);
  }

  @Override
  public final boolean is(Object o) throws IOException {
    Path path;
    path = (Path) o;

    PosixFileAttributeView view;
    view = Files.getFileAttributeView(path, PosixFileAttributeView.class);

    if (view == null) {
      return false;
    }

    PosixFileAttributes attributes;
    attributes = view.readAttributes();

    Set<PosixFilePermission> set;
    set = attributes.permissions();

    return set.contains(permission);
  }

}
