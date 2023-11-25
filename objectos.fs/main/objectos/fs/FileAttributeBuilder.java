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
import java.util.EnumSet;
import java.util.Set;

abstract class FileAttributeBuilder {

  private static final Noop NOOP = new Noop();

  public static FileAttributeBuilder create(OperatingSystem os) {
    return os.acceptOperatingSystemVisitor(ThisVisitor.INSTANCE, null);
  }

  public abstract FileAttribute<?>[] build();

  abstract void add(PosixFilePermission permission);

  private static class Noop extends FileAttributeBuilder {

    @Override
    public final FileAttribute<?>[] build() {
      return PosixJava7.EMPTY_FILE_ATTRIBUTES;
    }

    @Override
    final void add(PosixFilePermission permission) {}

  }

  private static class Posix extends FileAttributeBuilder
      implements FileAttribute<Set<PosixFilePermission>> {

    private final EnumSet<PosixFilePermission> set = EnumSet.noneOf(PosixFilePermission.class);

    @Override
    public final FileAttribute<?>[] build() {
      return new FileAttribute<?>[] {this};
    }

    @Override
    public final String name() {
      return "posix:permissions";
    }

    @Override
    public final Set<PosixFilePermission> value() {
      return set;
    }

    @Override
    final void add(PosixFilePermission permission) {
      set.add(permission);
    }

  }

  private static class ThisVisitor implements OperatingSystemVisitor<FileAttributeBuilder, Void> {

    static final ThisVisitor INSTANCE = new ThisVisitor();

    @Override
    public final FileAttributeBuilder visitLinux(Linux os, Void p) {
      return new Posix();
    }

    @Override
    public final FileAttributeBuilder visitUnsupportedOs(UnsupportedOperatingSystem os, Void p) {
      return NOOP;
    }

  }

}