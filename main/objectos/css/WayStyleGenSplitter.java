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

abstract class WayStyleGenSplitter extends WayStyleGenScanner {

  @Override
  final void onScan(String s) {
    int beginIndex;
    beginIndex = 0;

    int endIndex;
    endIndex = s.indexOf(' ', beginIndex);

    while (endIndex >= 0) {
      if (beginIndex < endIndex) {
        String candidate;
        candidate = s.substring(beginIndex, endIndex);

        onSplit(candidate);
      }

      beginIndex = endIndex + 1;

      endIndex = s.indexOf(' ', beginIndex);
    }

    if (beginIndex == 0) {
      onSplit(s);
    }

    else if (beginIndex < s.length() - 1) {
      onSplit(s.substring(beginIndex));
    }
  }

  abstract void onSplit(String s);

}