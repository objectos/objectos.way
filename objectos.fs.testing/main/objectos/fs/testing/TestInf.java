/*
 * Copyright (C) 2011-2023 Objectos Software LTDA.
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
package objectos.fs.testing;

import java.io.IOException;
import objectos.core.io.Resource;
import objectos.fs.Directory;
import objectos.fs.LocalFs;
import objectos.fs.RegularFile;

/**
 * The {@code src/test/resources/TEST-INF} directory as a {@link Directory}
 * instance.
 *
 * <p>
 * This class worker by first locating a resource named {@code TEST-INF/.marker}
 * and then finding its parent directory.
 */
public abstract class TestInf {

  private static final TestInf INSTANCE = create();

  TestInf() {}

  /**
   * Returns the parent directory of the resource named
   * {@code TEST-INF/.marker}.
   *
   * <p>
   * This is intended to ease working with test resources (as defined by Maven
   * build tool) in modular projects.
   *
   * @return the parent directory of the resource named {@code TEST-INF/.marker}
   *
   * @throws IOException
   *         if the marker cannot be found, the marker is not a regular file or
   *         another I/O error occurs
   */
  public static Directory get() throws IOException {
    return INSTANCE.getImpl();
  }

  private static TestInf create() {
    try {
      Resource marker;
      marker = Resource.getResource("TEST-INF/.marker");

      RegularFile markerFile;
      markerFile = LocalFs.getRegularFile(marker);

      Directory parent;
      parent = markerFile.getParent();

      return new Success(parent);
    } catch (IOException e) {
      return new Failed(e);
    }
  }

  abstract Directory getImpl() throws IOException;

  private static class Failed extends TestInf {
    private final IOException exception;

    Failed(IOException exception) {
      this.exception = exception;
    }

    @Override
    final Directory getImpl() throws IOException {
      throw exception;
    }
  }

  private static class Success extends TestInf {
    private final Directory directory;

    Success(Directory directory) {
      this.directory = directory;
    }

    @Override
    final Directory getImpl() throws IOException {
      return directory;
    }
  }

}