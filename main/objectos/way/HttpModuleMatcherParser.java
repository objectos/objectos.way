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

class HttpModuleMatcherParser {

  private static final char ASTERISK = '*';

  private static final char COLON = ':';

  private static final char SOLIDUS = '/';

  private static final String PATH_DOES_START_WITH_SOLIDUS = "Path does not start with a '/' character";

  private static final String WILDCARD_CHAR = "The '*' wildcard character can only be used once at the end of the path expression";

  final HttpModuleMatcher matcher(String pathExpression) {
    int length;
    length = pathExpression.length(); // implicit null check

    if (length == 0) {
      throw illegal(PATH_DOES_START_WITH_SOLIDUS, pathExpression);
    }

    int index;
    index = 0;

    char c;
    c = pathExpression.charAt(index++);

    if (c != '/') {
      throw illegal(PATH_DOES_START_WITH_SOLIDUS, pathExpression);
    }

    HttpModuleMatcher matcher;
    matcher = null;

    int colon;
    colon = pathExpression.indexOf(COLON, index);

    while (colon > 0) {
      if (matcher == null) {
        String value;
        value = pathExpression.substring(0, colon);

        matcher = new HttpModuleMatcher.StartsWith(value);
      } else {
        String value;
        value = pathExpression.substring(index, colon);

        matcher = matcher.append(new HttpModuleMatcher.Region(value));
      }

      index = colon + 1;

      if (index == length) {
        throw new UnsupportedOperationException("Implement me");
      }

      int solidus;
      solidus = pathExpression.indexOf(SOLIDUS, index);

      String name;

      if (solidus < 0) {
        name = pathExpression.substring(index);
      } else {
        name = pathExpression.substring(index, solidus);
      }

      matcher = matcher.append(new HttpModuleMatcher.NamedVariable(name));

      if (solidus < 0) {
        return matcher;
      }

      index = solidus;

      colon = pathExpression.indexOf(COLON, index);
    }

    if (matcher != null) {
      String value;
      value = pathExpression.substring(index);

      return matcher.append(new HttpModuleMatcher.Region(value));
    }

    int asterisk;
    asterisk = pathExpression.indexOf(ASTERISK, index);

    if (asterisk > 0) {
      int lastIndex;
      lastIndex = length - 1;

      if (asterisk != lastIndex) {
        throw illegal(WILDCARD_CHAR, pathExpression);
      }

      String value;
      value = pathExpression.substring(0, lastIndex);

      return new HttpModuleMatcher.StartsWith(value);
    } else {
      return new HttpModuleMatcher.Exact(pathExpression);
    }
  }

  private IllegalArgumentException illegal(String prefix, String path) {
    return new IllegalArgumentException(prefix + ": " + path);
  }

}