/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

class HttpModuleMatcherParser {

  private static final char ASTERISK = '*';

  private static final char COLON = ':';

  private static final char SOLIDUS = '/';

  private static final String PATH_DOES_START_WITH_SOLIDUS = "Path does not start with a '/' character";

  private static final String WILDCARD_CHAR = "The '*' wildcard character can only be used once at the end of the path expression";

  final HttpModuleMatcher matcher(String path) {
    int length;
    length = path.length();

    if (length == 0) {
      throw illegal(PATH_DOES_START_WITH_SOLIDUS, path);
    }

    int index;
    index = 0;

    char c;
    c = path.charAt(index++);

    if (c != '/') {
      throw illegal(PATH_DOES_START_WITH_SOLIDUS, path);
    }

    HttpModuleMatcher matcher;
    matcher = null;

    int colon;
    colon = path.indexOf(COLON, index);

    while (colon > 0) {
      if (matcher == null) {
        String value;
        value = path.substring(0, colon);

        matcher = new HttpModuleMatcher.StartsWith(value);
      } else {
        throw new UnsupportedOperationException("Implement me");
      }

      index = colon + 1;

      if (index == length) {
        throw new UnsupportedOperationException("Implement me");
      }

      int solidus;
      solidus = path.indexOf(SOLIDUS, index);

      if (solidus < 0) {
        String name;
        name = path.substring(index);

        return matcher.append(new HttpModuleMatcher.NamedVariable(name));
      }

      throw new UnsupportedOperationException("Implement me");
    }

    int asterisk;
    asterisk = path.indexOf(ASTERISK, index);

    if (asterisk > 0) {
      int lastIndex;
      lastIndex = length - 1;

      if (asterisk != lastIndex) {
        throw illegal(WILDCARD_CHAR, path);
      }

      String value;
      value = path.substring(0, lastIndex);

      return new HttpModuleMatcher.StartsWith(value);
    } else {
      return new HttpModuleMatcher.Exact(path);
    }
  }

  private IllegalArgumentException illegal(String prefix, String path) {
    return new IllegalArgumentException(prefix + ": " + path);
  }

}