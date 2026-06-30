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
package objectox.http.media;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import objectos.internal.IOFunction;
import objectos.lang.BinaryObject;
import objectos.way.Note;

final class StaticFilesAttributes {

  private static final Note.Ref1<Throwable> THROW = Note.Ref1.create(StaticFilesAttributes.class, "THR", Note.ERROR);

  private final Note.Sink noteSink;

  private final IOFunction<Path, BasicFileAttributes> reader;

  StaticFilesAttributes(Note.Sink noteSink, IOFunction<Path, BasicFileAttributes> reader) {
    this.noteSink = noteSink;

    this.reader = reader;
  }

  public final BasicFileAttributes readOrCreate(Path file, BinaryObject object) {
    try {
      final BasicFileAttributes attrs;
      attrs = reader.apply(file);

      if (attrs.isRegularFile()) {
        return attrs;
      } else {
        return null;
      }
    } catch (NoSuchFileException expected) {
      return object != null ? create(file, object) : null;
    } catch (IOException e) {
      noteSink.send(THROW, e);

      return null;
    }
  }

  private static final CopyOption[] COPY = {StandardCopyOption.ATOMIC_MOVE};

  private static final OpenOption[] OPEN = {StandardOpenOption.WRITE};

  private BasicFileAttributes create(Path file, BinaryObject object) {
    Path tmp = null;

    try {
      tmp = Files.createTempFile("http-static-file-", ".tmp");

      try (OutputStream out = Files.newOutputStream(tmp, OPEN)) {
        object.binaryTo(out);
      }

      Files.move(tmp, file, COPY);

      return reader.apply(file);
    } catch (IOException e) {
      noteSink.send(THROW, e);

      return null;
    } finally {
      if (tmp != null) {
        try {
          Files.deleteIfExists(tmp);
        } catch (IOException suppressed) {
          noteSink.send(THROW, suppressed);
        }
      }
    }
  }

}
