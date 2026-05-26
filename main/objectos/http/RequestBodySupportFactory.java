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

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

record RequestBodySupportFactory(Path directory, int memoryMax, long sizeMax) {

  public final RequestBodySupport create(long id) {
    return new RequestBodySupport(directory, id, memoryMax, sizeMax);
  }

  public static RequestBodySupportFactory of(Path root, RequestBodyOptionsPojo bodyOptions) throws IOException {
    Path directory = bodyOptions.directory();

    if (directory == null) {
      directory = Files.createTempDirectory(root, "request-body-");
    }

    return new RequestBodySupportFactory(
        directory,

        bodyOptions.memoryMax(),

        bodyOptions.sizeMax()
    );
  }

}
