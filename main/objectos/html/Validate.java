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
package objectos.html;

final class Validate {

  private Validate() {}

  public static void pathName(String value) {
    int length = value.length();

    if (length == 0) {
      throw new IllegalArgumentException("Path name must not be empty");
    }

    var first = value.charAt(0);

    if (first != '/') {
      throw new IllegalArgumentException(
          "Path name must be absolute and start with a '/' character");
    }

    // ???
    // probably prevent
    // - 'index.html?foo'
    // - 'index.html?foo&abc=345'
    // - ' /abc\n ' (i.e. whitespace)
  }

}