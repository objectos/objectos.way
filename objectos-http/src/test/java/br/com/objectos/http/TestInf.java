/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package br.com.objectos.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

public abstract class TestInf {

  private static final TestInf INSTANCE = create();

  TestInf() {}

  public static Path get() throws URISyntaxException {
    return INSTANCE.getImpl();
  }

  private static TestInf create() {
    try {
      Thread currentThread;
      currentThread = Thread.currentThread();

      ClassLoader loader;
      loader = currentThread.getContextClassLoader();

      URL marker;
      marker = loader.getResource("TEST-INF/.marker");

      URI markerUri;
      markerUri = marker.toURI();

      Path markerPath;
      markerPath = Path.of(markerUri);

      Path directory;
      directory = markerPath.getParent();

      return new Success(directory);
    } catch (URISyntaxException e) {
      return new Failed(e);
    }
  }

  abstract Path getImpl() throws URISyntaxException;

  private static class Failed extends TestInf {
    private final URISyntaxException exception;

    Failed(URISyntaxException exception) {
      this.exception = exception;
    }

    @Override
    final Path getImpl() throws URISyntaxException {
      throw exception;
    }
  }

  private static class Success extends TestInf {
    private final Path directory;

    Success(Path directory) {
      this.directory = directory;
    }

    @Override
    final Path getImpl() throws URISyntaxException {
      return directory;
    }
  }

}