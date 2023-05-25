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
package objectos.specgen.css.mdn;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Resource {

  private Resource() {}

  public static String readString(String resourceName) throws IOException {
    var currentThread = Thread.currentThread();

    var classLoader = currentThread.getContextClassLoader();

    var url = classLoader.getResource(resourceName);

    if (url == null) {
      throw new IllegalArgumentException(
        "Resource %s was not found".formatted(resourceName)
      );
    }

    var out = new ByteArrayOutputStream();

    try (var in = url.openStream()) {
      in.transferTo(out);
    }

    return new String(out.toByteArray(), StandardCharsets.UTF_8);
  }

}