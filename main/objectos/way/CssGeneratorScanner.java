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
import java.util.function.Consumer;
import java.util.stream.Stream;
import objectos.notes.Note1;
import objectos.notes.Note2;
import objectos.notes.NoteSink;

final class CssGeneratorScanner {

  private static final Note1<String> CLASS_NOT_FOUND;

  private static final Note2<String, IOException> CLASS_IO_ERROR;

  private static final Note1<String> CLASS_LOADED;

  private static final Note2<Path, IOException> DIR_IO_ERROR;

  static {
    Class<?> s;
    s = Css.Generator.class;

    CLASS_NOT_FOUND = Note1.error(s, "Class file not found");

    CLASS_IO_ERROR = Note2.error(s, "Class file I/O error");

    CLASS_LOADED = Note1.debug(s, "Class file loaded");

    DIR_IO_ERROR = Note2.error(s, "Directory I/O error");
  }

  private final NoteSink noteSink;

  private final Lang.ClassReader reader;

  public CssGeneratorScanner(NoteSink noteSink) {
    this.noteSink = noteSink;

    reader = Lang.createClassReader(noteSink);
  }

  public final void scan(Class<?> clazz, Consumer<String> stringProcessor) {
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
      noteSink.send(CLASS_NOT_FOUND, binaryName);

      return;
    }

    byte[] bytes;

    try (in; ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      in.transferTo(out);

      bytes = out.toByteArray();

      noteSink.send(CLASS_LOADED, binaryName);
    } catch (IOException e) {
      noteSink.send(CLASS_IO_ERROR, binaryName, e);

      return;
    }

    reader.init(binaryName, bytes);

    reader.processStringConstants(stringProcessor);
  }

  public final void scanDirectory(Path directory, Consumer<String> stringProcessor) {
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

        noteSink.send(CLASS_LOADED, fileName);

        reader.processStringConstants(stringProcessor);
      }

    } catch (IOException e) {
      noteSink.send(DIR_IO_ERROR, directory, e);
    }
  }

}