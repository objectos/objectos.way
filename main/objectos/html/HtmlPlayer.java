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

final class HtmlPlayer {

  private StringBuilder stringBuilder;

  public HtmlPlayer() {
  }

  /*
   * Visible for testing.
   */
  final String processHref(String pathName, String attributeValue) {
    var thisName = pathName;
    var thatName = attributeValue;

    var thisLen = thisName.length();
    var thatLen = thatName.length();

    int baseDir = -1;
    int dirCount = 0;
    int mismatch = -1;

    for (int i = 0; i < thisLen; i++) {
      char thisChar = thisName.charAt(i);

      if (i < thatLen) {
        char thatChar = thatName.charAt(i);

        if (thisChar == thatChar) {
          if (thisChar == '/') {
            if (mismatch == -1) {
              baseDir = i;
            } else {
              dirCount++;
            }
          }
        } else {
          if (mismatch == -1) {
            mismatch = i;
          }

          if (thisChar == '/') {
            dirCount++;
          }
        }
      } else {
        if (thisChar == '/') {
          dirCount++;
        }
      }
    }

    return switch (mismatch) {
      case -1 -> {
        if (baseDir == -1) {
          yield attributeValue;
        } else {
          int valueIndex = baseDir + 1;

          yield attributeValue.substring(valueIndex);
        }
      }

      case 0 -> {
        if (dirCount == 0) {
          yield attributeValue;
        } else {
          var sb = stringBuilder();

          for (int i = 0; i < dirCount; i++) {
            sb.append("../");
          }

          sb.append(attributeValue);

          yield sb.toString();
        }
      }

      default -> {
        if (dirCount == 0) {
          int valueIndex = baseDir + 1;

          yield attributeValue.substring(valueIndex);
        }

        if (baseDir == -1) {
          var sb = stringBuilder();

          for (int i = 0; i < dirCount; i++) {
            sb.append("../");
          }

          sb.append(attributeValue);

          yield sb.toString();
        }

        var sb = stringBuilder();

        for (int i = 0; i < dirCount; i++) {
          sb.append("../");
        }

        int valueIndex = baseDir + 1;

        var subValue = attributeValue.substring(valueIndex);

        sb.append(subValue);

        yield sb.toString();
      }
    };
  }

  private StringBuilder stringBuilder() {
    if (stringBuilder == null) {
      stringBuilder = new StringBuilder();
    } else {
      stringBuilder.setLength(0);
    }

    return stringBuilder;
  }

}