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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import objectos.http.Content;
import objectos.http.MediaType;
import objectos.internal.IOFunction;
import objectos.lang.Throwables;
import objectos.way.Note;
import objectos.way.Y;
import objectos.y.BasicFileAttributesY;
import objectos.y.PathY;
import org.testng.annotations.Test;

public class StaticFilesAttributesTest {

  private static final class ReaderY implements IOFunction<Path, BasicFileAttributes> {

    BasicFileAttributes attributes;

    IOException exception;

    Path path;

    @Override
    public final BasicFileAttributes apply(Path path) throws IOException {
      this.path = path;

      if (exception != null) {
        final IOException ex;
        ex = exception;

        exception = null;

        throw ex;
      }

      return attributes;
    }

  }

  private final Note.Sink noteSink = Y.noteSink();

  private final Path path = PathY.nextFile();

  @Test(description = "ignore non-regular file")
  public void read01() {
    final ReaderY reader;
    reader = new ReaderY();

    final BasicFileAttributes attributes;
    attributes = BasicFileAttributesY.create();

    reader.attributes = attributes;

    final StaticFilesAttributes subject;
    subject = new StaticFilesAttributes(noteSink, reader);

    final BasicFileAttributes res;
    res = subject.readOrCreate(path, null);

    assertEquals(res, null);
  }

  @Test(description = "ignore non-existing file (contents == null)")
  public void read02() {
    final ReaderY reader;
    reader = new ReaderY();

    final BasicFileAttributes attributes;
    attributes = BasicFileAttributesY.create(opts -> {
      opts.regularFile = true;
    });

    reader.attributes = attributes;

    reader.exception = new NoSuchFileException("");

    final StaticFilesAttributes subject;
    subject = new StaticFilesAttributes(noteSink, reader);

    final BasicFileAttributes res;
    res = subject.readOrCreate(path, null);

    assertEquals(res, null);
  }

  @Test(description = "ignore if IOException")
  public void read03() {
    final ReaderY reader;
    reader = new ReaderY();

    final BasicFileAttributes attributes;
    attributes = BasicFileAttributesY.create(opts -> {
      opts.regularFile = true;
    });

    reader.attributes = attributes;

    final IOException exception;
    exception = Throwables.trimStackTrace(new IOException(), 1);

    reader.exception = exception;

    final StaticFilesAttributes subject;
    subject = new StaticFilesAttributes(noteSink, reader);

    final BasicFileAttributes res;
    res = subject.readOrCreate(path, null);

    assertEquals(res, null);
  }

  @Test(description = "return attrs when existing")
  public void read04() {
    final ReaderY reader;
    reader = new ReaderY();

    final BasicFileAttributes attributes;
    attributes = BasicFileAttributesY.create(opts -> {
      opts.regularFile = true;
    });

    reader.attributes = attributes;

    final StaticFilesAttributes subject;
    subject = new StaticFilesAttributes(noteSink, reader);

    final BasicFileAttributes res;
    res = subject.readOrCreate(path, null);

    assertSame(res, attributes);

    assertSame(reader.path, path);
  }

  @Test(description = "create when non-existing and contents != null")
  public void read05() throws IOException {
    final ReaderY reader;
    reader = new ReaderY();

    final BasicFileAttributes attributes;
    attributes = BasicFileAttributesY.create(opts -> {
      opts.regularFile = true;
    });

    reader.attributes = attributes;

    reader.exception = new NoSuchFileException("");

    final StaticFilesAttributes subject;
    subject = new StaticFilesAttributes(noteSink, reader);

    final Content c;
    c = Content.of(MediaType.TEXT_PLAIN, "Created!");

    final BasicFileAttributes res;
    res = subject.readOrCreate(path, c);

    assertSame(res, attributes);

    assertSame(reader.path, path);

    assertEquals(Files.readString(path), "Created!");
  }

}
