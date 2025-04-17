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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

interface HttpExchangeTmp extends Closeable {

  String BODY_TEMP_FILE_PREFIX = "objectos-way-http-exchange-body-";

  String BODY_TEMP_FILE_SUFFIX = ".tmp";

  public static HttpExchangeTmp ofFile() throws IOException {
    return new HttpExchangeTmp() {

      private final Path file = Files.createTempFile(BODY_TEMP_FILE_PREFIX, BODY_TEMP_FILE_SUFFIX);

      @Override
      public final void close() throws IOException {
        Files.delete(file);
      }

      @Override
      public final InputStream input() throws IOException {
        return Files.newInputStream(file);
      }

      @Override
      public final OutputStream output() throws IOException {
        return Files.newOutputStream(file);
      }
    };
  }

  @Override
  void close() throws IOException;

  InputStream input() throws IOException;

  OutputStream output() throws IOException;

}