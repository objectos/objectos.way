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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class WayTest00Support {

  WayTest00Support() {}

  // ##################################################################
  // # BEGIN: Project
  // ##################################################################

  private record Notes(
      Note.Ref1<String> stderr,
      Note.Ref1<String> stdout,
      Note.Ref1<IOException> ioException
  ) {

    static Notes get() {
      Class<?> s;
      s = WayTest00Support.class;

      return new Notes(
          Note.Ref1.create(s, "STE", Note.INFO),
          Note.Ref1.create(s, "STO", Note.INFO),
          Note.Ref1.create(s, "IOX", Note.ERROR)
      );
    }

  }

  static final class Project {

    static final class Builder {

      private final Path root = Y.nextTempDir();

      private Builder() {
        try {
          final Path originalWay;
          originalWay = Path.of("main", "objectos", "way", "Way.java");

          final String originalContents;
          originalContents = Files.readString(originalWay, StandardCharsets.UTF_8);

          final String contents;
          contents = originalContents.replace("package objectos.way;", "");

          addFile("Way.java", contents);
        } catch (IOException e) {
          throw new UncheckedIOException(e);
        }
      }

      public final void addFile(String relativePath, String contents) {
        try {
          final Path file;
          file = root.resolve(relativePath);

          final Path parent;
          parent = file.getParent();

          Files.createDirectories(parent);

          Files.writeString(file, contents, StandardOpenOption.CREATE_NEW);
        } catch (IOException e) {
          throw new UncheckedIOException(e);
        }
      }

      private Project build() {
        return new Project(this);
      }

    }

    private final Notes notes = Notes.get();

    private final Note.Sink noteSink = Y.noteSink();

    private Process process;

    private final Path root;

    private Project(Builder builder) {
      this.root = builder.root;
    }

    public final String ls() {
      try (Stream<Path> walk = Files.walk(root)) {
        return walk
            .filter(path -> !path.equals(root))
            .filter(Files::isRegularFile)
            .map(root::relativize)
            .sorted()
            .map(Object::toString)
            .collect(Collectors.joining("\n", "", "\n"));
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    public final void waitFor() {
      try {
        process.waitFor();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    public final void way(String... args) {
      final List<String> cmd;
      cmd = new ArrayList<>();

      cmd.add("java");

      cmd.add("Way.java");

      for (String arg : args) {
        cmd.add(arg);
      }

      try {
        final ProcessBuilder builder;
        builder = new ProcessBuilder(cmd);

        builder.directory(root.toFile());

        process = builder.start();

        Thread.ofVirtual().start(this::stderr);
        Thread.ofVirtual().start(this::stdout);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }

    private void stderr() {
      try (BufferedReader reader = process.errorReader()) {
        String line;
        while ((line = reader.readLine()) != null) {
          noteSink.send(notes.stderr, line);
        }
      } catch (IOException e) {
        noteSink.send(notes.ioException, e);
      }
    }

    private void stdout() {
      try (BufferedReader reader = process.inputReader()) {
        String line;
        while ((line = reader.readLine()) != null) {
          noteSink.send(notes.stdout, line);
        }
      } catch (IOException e) {
        noteSink.send(notes.ioException, e);
      }
    }

  }

  final Project project(Consumer<? super Project.Builder> opts) {
    final Project.Builder builder;
    builder = new Project.Builder();

    opts.accept(builder);

    return builder.build();
  }

  // ##################################################################
  // # END: Project
  // ##################################################################

}