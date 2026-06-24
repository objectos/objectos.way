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
package objectox.http.handler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.function.Predicate;
import objectos.way.Note;

final class ReloadingClassLoader extends ClassLoader {

  private static final Note.Ref1<Class<?>> LOADED = Note.Ref1.create(ReloadingClassLoader.class, "REL", Note.TRACE);

  private static final Note.Ref1<String> SKIPPED = Note.Ref1.create(ReloadingClassLoader.class, "SKI", Note.DEBUG);

  private final Predicate<? super String> binaryNameFilter;

  private final Predicate<? super byte[]> classFileFilter;

  private final Path moduleLocation;

  private final Note.Sink noteSink;

  private final ClassLoader parentLoader;

  ReloadingClassLoader(
      Predicate<? super String> binaryNameFilter,

      Predicate<? super byte[]> classFileFilter,

      Path moduleLocation,

      Note.Sink noteSink,

      ClassLoader parentLoader
  ) {
    super(null); // no parent

    this.binaryNameFilter = binaryNameFilter;

    this.classFileFilter = classFileFilter;

    this.moduleLocation = moduleLocation;

    this.noteSink = noteSink;

    this.parentLoader = parentLoader;
  }

  @Override
  protected final Class<?> findClass(String name) throws ClassNotFoundException {
    if (!binaryNameFilter.test(name)) {
      noteSink.send(SKIPPED, name);

      return fromParent(name);
    }

    final String sepName;
    sepName = name.replace('.', File.separatorChar);

    final String fileName;
    fileName = sepName + ".class";

    final Path file;
    file = moduleLocation.resolve(fileName);

    final byte[] bytes;

    try {
      bytes = Files.readAllBytes(file);
    } catch (NoSuchFileException expected) {
      return fromParent(name);
    } catch (IOException e) {
      throw new ClassNotFoundException(name, e);
    }

    if (!classFileFilter.test(bytes)) {
      noteSink.send(SKIPPED, name);

      return fromParent(name);
    }

    final Class<?> clazz;
    clazz = defineClass(name, bytes, 0, bytes.length);

    noteSink.send(LOADED, clazz);

    return clazz;
  }

  private Class<?> fromParent(String name) throws ClassNotFoundException {
    return parentLoader.loadClass(name);
  }

}
