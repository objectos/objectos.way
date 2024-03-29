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
package objectos.css;

enum Side {

  ALL,

  X,

  Y,

  TOP,

  RIGHT,

  BOTTOM,

  LEFT,

  INVALID;

  static Side parse(String suffix) {
    int dash;
    dash = suffix.indexOf('-');

    if (dash < 0) {
      return ALL;
    }

    if (dash != 1) {
      return ALL;
    }

    char first;
    first = suffix.charAt(0);

    return switch (first) {
      case 'x' -> X;

      case 'y' -> Y;

      case 't' -> TOP;

      case 'r' -> RIGHT;

      case 'b' -> BOTTOM;

      case 'l' -> LEFT;

      default -> INVALID;
    };
  }

}