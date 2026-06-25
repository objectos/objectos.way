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
package objectox.http.req;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import objectos.http.RequestBodyFiles;

public final class RequestBodySupport implements Closeable {

  private final RequestBodyConfig config;

  private Path file;

  public RequestBodySupport(RequestBodyConfig config) {
    this.config = config;
  }

  @Override
  public final void close() throws IOException {
    if (file != null) {
      final Path deleteMe;
      deleteMe = file;

      file = null;

      Files.delete(deleteMe);
    }
  }

  public final Path file() throws IOException {
    if (file == null) {
      final RequestBodyFiles files;
      files = config.files();

      final Path f;
      f = files.get();

      if (f == null) {
        final String msg;
        msg = "RequestBodyFiles provided a null file";

        throw new NullPointerException(msg);
      }

      file = files.get();
    }

    return file;
  }

  public final int memoryMax() {
    return config.memoryMax();
  }

  public final long sizeMax() {
    return config.sizeMax();
  }

}
