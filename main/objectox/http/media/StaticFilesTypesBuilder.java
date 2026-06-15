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

import java.util.HashMap;
import java.util.Map;

final class StaticFilesTypesBuilder {

  private String defaultType;

  private final Map<String, String> types = new HashMap<>();

  StaticFilesTypesBuilder(String defaultType) {
    this.defaultType = defaultType;
  }

  public final StaticFilesTypes build() {
    return new StaticFilesTypes(
        defaultType,

        Map.copyOf(types)
    );
  }

  public final void contentTypes(String propertiesString) {
    final String[] lines;
    lines = propertiesString.split("\n");

    for (String line : lines) {
      if (line.isBlank()) {
        continue;
      }

      final int colon;
      colon = parseColon(line);

      final String ext;
      ext = parseExt(line, colon);

      if ("*".equals(ext)) {
        defaultType = parseType(line, colon);
      }

      else {
        final char dot;
        dot = ext.charAt(0);

        if (dot != '.') {
          final String msg;
          msg = "File extension must begin with the '.' character: %s".formatted(ext);

          throw new IllegalArgumentException(msg);
        }

        final String type;
        type = parseType(line, colon);

        types.put(ext, type);
      }

    }
  }

  private int parseColon(String line) {
    final int colon;
    colon = line.indexOf(':');

    if (colon < 0) {
      final String msg;
      msg = "Colon character ':' not found in the following line:\n\n%s\n".formatted(line);

      throw new IllegalArgumentException(msg);
    }

    return colon;
  }

  private String parseExt(String line, int colon) {
    final String key;
    key = line.substring(0, colon);

    final String ext;
    ext = key.trim();

    if (ext.isEmpty()) {
      final String msg;
      msg = "File extension must not be empty";

      throw new IllegalArgumentException(msg);
    }

    return ext;
  }

  private String parseType(String line, int colon) {
    final String value;
    value = line.substring(colon + 1);

    final String fullType;
    fullType = value.trim();

    final MediaTypeValidator validator;
    validator = new MediaTypeValidator(fullType);

    validator.validate();

    return fullType;
  }

}
