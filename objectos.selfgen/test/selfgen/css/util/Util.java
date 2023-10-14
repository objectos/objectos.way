/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package selfgen.css.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import objectos.selfgen.css.Rmdir;

final class Util {

  private Util() {}

  public static Map<String, String> generate(CssUtilSelfGen gen) throws IOException {
    var root = Files.createTempDirectory("selfgen-css-util-");

    try {
      gen.execute(new String[] {
          root.toString()
      });

      var result = new HashMap<String, String>();

      try (var walk = Files.walk(root)) {
        walk.filter(Files::isRegularFile)
            .forEach(file -> {
              try {
                var relative = root.relativize(file);

                var key = relative.toString();

                var value = Files.readString(file);

                result.put(key, value);
              } catch (IOException e) {
                throw new UncheckedIOException(e);
              }
            });
      }

      return result;
    } finally {
      Rmdir.rmdir(root);
    }
  }

}