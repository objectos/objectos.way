/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

final class CssEngineScanner {

  interface Adapter extends Lang.ClassReader.StringConstantProcessor {

    void sourceName(String value);

  }

  private record Notes(
      Note.Ref1<String> classNotFound,
      Note.Ref2<String, IOException> classIoError,
      Note.Ref1<String> classLoaded,

      Note.Ref2<Path, IOException> directoryIoError,

      Note.Ref2<Class<?>, Throwable> jarFileException,
      Note.Ref2<Class<?>, String> jarFileNull,

      Note.Ref1<String> skipped
  ) {

    static Notes get() {
      Class<?> s;
      s = CssEngineScanner.class;

      return new Notes(
          Note.Ref1.create(s, "Class file not found", Note.ERROR),
          Note.Ref2.create(s, "Class file I/O error", Note.ERROR),
          Note.Ref1.create(s, "REA", Note.DEBUG),

          Note.Ref2.create(s, "Directory I/O error", Note.ERROR),

          Note.Ref2.create(s, "JarFile Exception", Note.ERROR),
          Note.Ref2.create(s, "JarFile No Value", Note.ERROR),

          Note.Ref1.create(s, "SKI", Note.DEBUG)
      );
    }

  }

  private final Notes notes = Notes.get();

  private final Note.Sink noteSink;

  private final Lang.ClassReader reader;

  public CssEngineScanner(Note.Sink noteSink) {
    this.noteSink = noteSink;

    reader = Lang.createClassReader(noteSink);
  }

  public final void scan(Class<?> clazz, Adapter adapter) {
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

  public final void scanDirectory(Path directory, Adapter adapter) {
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

        scanBytes(adapter, fileName, bytes);
      }

    } catch (IOException e) {
      noteSink.send(notes.directoryIoError, directory, e);
    }
  }

  public final void scanJarFile(Class<?> clazz, Adapter adapter) {
    ProtectionDomain domain;

    try {
      domain = clazz.getProtectionDomain();
    } catch (SecurityException e) {
      noteSink.send(notes.jarFileException, clazz, e);

      return;
    }

    CodeSource source;
    source = domain.getCodeSource();

    if (source == null) {
      noteSink.send(notes.jarFileNull, clazz, "CodeSource");

      return;
    }

    URL location;
    location = source.getLocation();

    if (location == null) {
      noteSink.send(notes.jarFileNull, clazz, "Location");

      return;
    }

    File file;

    try {
      URI uri;
      uri = location.toURI();

      file = new File(uri);
    } catch (URISyntaxException e) {
      noteSink.send(notes.jarFileException, clazz, e);

      return;
    }

    try (JarFile jar = new JarFile(file)) {
      Enumeration<JarEntry> entries;
      entries = jar.entries();

      while (entries.hasMoreElements()) {
        JarEntry entry;
        entry = entries.nextElement();

        String entryName;
        entryName = entry.getName();

        if (!entryName.endsWith(".class")) {
          continue;
        }

        long size;
        size = entry.getSize();

        int intSize;
        intSize = Math.toIntExact(size);

        byte[] bytes;
        bytes = new byte[intSize];

        int bytesRead;

        try (InputStream in = jar.getInputStream(entry)) {
          bytesRead = in.read(bytes, 0, bytes.length);
        }

        if (bytesRead != bytes.length) {
          continue;
        }

        scanBytes(adapter, entryName, bytes);
      }
    } catch (ArithmeticException | IOException e) {
      noteSink.send(notes.jarFileException, clazz, e);
    }
  }

  private void scanBytes(Adapter adapter, String fileName, byte[] bytes) {
    reader.init(fileName, bytes);

    if (!reader.isAnnotationPresent(Css.Source.class)) {
      noteSink.send(notes.skipped, fileName);

      return;
    }

    noteSink.send(notes.classLoaded, fileName);

    adapter.sourceName(fileName);

    reader.processStringConstants(adapter);
  }

}