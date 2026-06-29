/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.dev;

import java.io.IOException;
import java.lang.classfile.Annotation;
import java.lang.classfile.Attributes;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.lang.classfile.attribute.RuntimeInvisibleAnnotationsAttribute;
import java.lang.classfile.constantpool.Utf8Entry;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import objectos.dev.ReloadingFunction;
import objectos.dev.ReloadingHandlerOptions;
import objectos.internal.NoOpSinkSingleton;
import objectos.way.Note;

public final class ReloadingHandlerBuilder implements ReloadingHandlerOptions {

  private Predicate<? super String> binaryNameFilter;

  private Predicate<? super byte[]> classFileFilter;

  private Set<Path> directories = Set.of();

  private Note.Sink noteSink = NoOpSinkSingleton.INSTANCE;

  private ReloadingFunction reloadingFunction;

  private ReloadingModule reloadingModule;

  public final ReloadingHandlerPojo build() throws IOException {
    if (reloadingFunction == null) {
      throw new IllegalStateException("No reloading function was specified");
    }

    if (reloadingModule == null) {
      throw new IllegalStateException("No module was specified");
    }

    if (classFileFilter == null) {
      classFileFilter = bytes -> {
        final ClassFile cf;
        cf = ClassFile.of();

        final ClassModel cm;
        cm = cf.parse(bytes);

        final Optional<RuntimeInvisibleAnnotationsAttribute> maybe;
        maybe = cm.findAttribute(Attributes.runtimeInvisibleAnnotations());

        if (maybe.isEmpty()) {
          return true;
        }

        final RuntimeInvisibleAnnotationsAttribute attr;
        attr = maybe.get();

        final List<Annotation> annotations;
        annotations = attr.annotations();

        for (Annotation annotation : annotations) {
          final Utf8Entry className;
          className = annotation.className();

          if (className.equalsString("Lobjectos/way/App$DoNotReload;")) {
            return false;
          }
        }

        return true;
      };
    }

    return new ReloadingHandlerPojo(
        binaryNameFilter != null ? binaryNameFilter : _ -> true,

        classFileFilter,

        noteSink,

        reloadingFunction,

        reloadingModule,

        new ReloadingWatcher(
            ReloadingFs.of(noteSink, directories)
        )
    );
  }

  @Override
  public final void directory(Path value) {
    if (!Files.isDirectory(value)) {
      throw new IllegalArgumentException("Path does not represent a directory: " + value);
    }

    if (directories.isEmpty()) {
      directories = new HashSet<>();
    }

    directories.add(value);
  }

  @Override
  public final void moduleOf(Class<?> value) {
    if (reloadingModule != null) {
      throw new IllegalStateException("module has already been set");
    }

    final Class<?> ctx;
    ctx = Objects.requireNonNull(value, "value == null");

    final ReloadingModuleBuilder builder;
    builder = new ReloadingModuleBuilder(ctx);

    reloadingModule = builder.build();
  }

  @Override
  public final void noteSink(Note.Sink value) {
    noteSink = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void reloadingFunction(ReloadingFunction value) {
    reloadingFunction = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void withBinaryName(Predicate<? super String> value) {
    binaryNameFilter = Objects.requireNonNull(value, "value == null");
  }

}