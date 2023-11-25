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

import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * The {@code java.io.tmpdir} system property as a {@link Directory} instance.
 *
 * @since 2
 */
public abstract class JavaIoTmpdir {

  private static final JavaIoTmpdir INSTANCE;

  static {
    String value;
    value = System.getProperty("java.io.tmpdir");

    if (value == null) {
      throw new NoSuchElementException("java.io.tmpdir");
    }

    JavaIoTmpdir tmpdir;

    try {
      Directory directory;
      directory = LocalFs.getDirectory(value);

      tmpdir = new Success(directory);
    } catch (IOException e) {
      tmpdir = new Failed(e);
    }

    INSTANCE = tmpdir;
  }

  private JavaIoTmpdir() {}

  /**
   * Returns the directory resolved from the {@code java.io.tmpdir} system
   * property.
   *
   * @return the directory resolved from the {@code java.io.tmpdir} system
   *         property
   *
   * @throws IOException
   *         if a directory could not be resolved from the
   *         {@code java.io.tmpdir} system property
   */
  public static Directory get() throws IOException {
    return INSTANCE.getImpl();
  }

  abstract Directory getImpl() throws IOException;

  private static class Failed extends JavaIoTmpdir {

    private final IOException exception;

    Failed(IOException exception) {
      this.exception = exception;
    }

    @Override
    final Directory getImpl() throws IOException {
      throw exception;
    }

  }

  private static class Success extends JavaIoTmpdir {

    private final Directory directory;

    Success(Directory directory) {
      this.directory = directory;
    }

    @Override
    final Directory getImpl() {
      return directory;
    }

  }

}