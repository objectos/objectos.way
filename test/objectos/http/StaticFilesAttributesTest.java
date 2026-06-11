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
package objectos.http;

import static org.testng.Assert.assertSame;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import objectos.internal.IOFunction;
import objectos.lang.Throwables;
import objectos.y.BasicFileAttributesY;
import org.testng.Assert;
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
        throw exception;
      }

      return attributes;
    }

  }

  private final Path path = Path.of("");

  @Test(description = "ignore non-regular file")
  public void read01() throws IOException {
    final ReaderY reader;
    reader = new ReaderY();

    final BasicFileAttributes attributes;
    attributes = BasicFileAttributesY.create();

    reader.attributes = attributes;

    final StaticFilesAttributes subject;
    subject = new StaticFilesAttributes(reader);

    try {
      subject.read(path);

      Assert.fail("It should have thrown");
    } catch (StaticFilesErrNonRegular expected) {
      assertSame(expected.path, path);
    }
  }

  @Test(description = "ignore non-existing file")
  public void read02() throws IOException {
    final ReaderY reader;
    reader = new ReaderY();

    final BasicFileAttributes attributes;
    attributes = BasicFileAttributesY.create(opts -> {
      opts.regularFile = true;
    });

    reader.attributes = attributes;

    reader.exception = new NoSuchFileException("");

    final StaticFilesAttributes subject;
    subject = new StaticFilesAttributes(reader);

    try {
      subject.read(path);

      Assert.fail("It should have thrown");
    } catch (StaticFilesErrNonRegular expected) {
      assertSame(expected.path, path);
    }
  }

  @Test(description = "pass through IOException")
  public void read03() throws StaticFilesErrNonRegular {
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
    subject = new StaticFilesAttributes(reader);

    try {
      subject.read(path);

      Assert.fail("It should have thrown");
    } catch (IOException e) {
      assertSame(e, exception);
    }
  }

  @Test(description = "return attrs")
  public void read04() throws IOException, StaticFilesErrNonRegular {
    final ReaderY reader;
    reader = new ReaderY();

    final BasicFileAttributes attributes;
    attributes = BasicFileAttributesY.create(opts -> {
      opts.regularFile = true;
    });

    reader.attributes = attributes;

    final StaticFilesAttributes subject;
    subject = new StaticFilesAttributes(reader);

    final BasicFileAttributes res;
    res = subject.read(path);

    assertSame(res, attributes);

    assertSame(reader.path, path);
  }

}
