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

import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.EnumSet;
import java.util.Set;

class PosixJava7 extends PosixJavaAny {

  static final FileAttribute<?>[] EMPTY_FILE_ATTRIBUTES = new FileAttribute<?>[0];

  static FileAttribute<?>[] single(PosixFilePermission p) {
    EnumSet<PosixFilePermission> set;
    set = EnumSet.of(p);

    FileAttribute<Set<PosixFilePermission>> a;
    a = PosixFilePermissions.asFileAttribute(set);

    return new FileAttribute<?>[] {a};
  }

  @Override
  public final PosixFileModeOption ownerExecutable0() {
    return PosixFileModeOptionJava7.OWNER_EXECUTABLE;
  }

  @Override
  public final PosixFileModeOption ownerReadable0() {
    return PosixFileModeOptionJava7.OWNER_READABLE;
  }

  @Override
  public final PosixFileModeOption ownerWritable0() {
    return PosixFileModeOptionJava7.OWNER_WRITABLE;
  }

}
