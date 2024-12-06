/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.stream.Stream;

final class CssGeneratorScanner {

  private record Notes(
      Note.Ref1<String> classNotFound,
      Note.Ref2<String, IOException> classIoError,
      Note.Ref1<String> classLoaded,
      Note.Ref2<Path, IOException> directoryIoError
  ) {

    static Notes get() {
      Class<?> s;
      s = Css.Generator.class;

      return new Notes(
          Note.Ref1.create(s, "Class file not found", Note.ERROR),
          Note.Ref2.create(s, "Class file I/O error", Note.ERROR),
          Note.Ref1.create(s, "Class file loaded", Note.DEBUG),
          Note.Ref2.create(s, "Directory I/O error", Note.ERROR)
      );
    }

  }

  private final Notes notes = Notes.get();

  private final Note.Sink noteSink;

  private final Lang.ClassReader reader;

  public CssGeneratorScanner(Note.Sink noteSink) {
    this.noteSink = noteSink;

    reader = Lang.createClassReader(noteSink);
  }

  public final void scan(Class<?> clazz, CssGeneratorAdapter adapter) {
    String binaryName;
    binaryName = clazz.getName();

    // 0. load class file

    String resourceName;
    resourceName = binaryName.replace('.', '/');

    resourceName += ".class";

    ClassLoader loader;
    loader = ClassLoader.getSystemClassLoader();

    InputStream in;
    in = loader.getResourceAsStream(resourceName);

    if (in == null) {
      noteSink.send(notes.classNotFound, binaryName);

      return;
    }

    byte[] bytes;

    try (in; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      in.transferTo(out);

      bytes = out.toByteArray();

      noteSink.send(notes.classLoaded, binaryName);
    } catch (IOException e) {
      noteSink.send(notes.classIoError, binaryName, e);

      return;
    }

    reader.init(binaryName, bytes);

    adapter.sourceName(binaryName);

    reader.processStringConstants(adapter);
  }

  public final void scanDirectory(Path directory, CssGeneratorAdapter adapter) {
    try (Stream<Path> stream = Files.walk(directory, Integer.MAX_VALUE).filter(Files::isRegularFile)) {

      Iterator<Path> iterator;
      iterator = stream.iterator();

      while (iterator.hasNext()) {
        Path entry;
        entry = iterator.next();

        Path relative;
        relative = directory.relativize(entry);

        String fileName;
        fileName = relative.toString();

        if (!fileName.endsWith(".class")) {
          continue;
        }

        byte[] bytes;
        bytes = Files.readAllBytes(entry);

        reader.init(fileName, bytes);

        if (!reader.isAnnotationPresent(Css.Source.class)) {
          continue;
        }

        noteSink.send(notes.classLoaded, fileName);

        adapter.sourceName(fileName);

        reader.processStringConstants(adapter);
      }

    } catch (IOException e) {
      noteSink.send(notes.directoryIoError, directory, e);
    }
  }

}