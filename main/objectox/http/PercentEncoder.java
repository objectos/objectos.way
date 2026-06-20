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
package objectox.http;

public final class PercentEncoder {

  private final PercentDictionary dictionary;

  private int index;

  private final String input;

  public PercentEncoder(PercentDictionary dictionary, String input) {
    this.dictionary = dictionary;

    this.input = input;
  }

  public final String encode() {
    while (hasNext()) {
      final char c;
      c = next();

      if (dictionary.test(c)) {
        continue;
      }

      return encode0();
    }

    return input;
  }

  private String encode0() {
    final int length;
    length = input.length();

    // we'll need to revisit the first to encode
    index--;

    final int offset;
    offset = index;

    final PercentString string;
    string = PercentString.of(length, offset);

    for (int idx = 0; idx < offset; idx++) {
      final char c;
      c = input.charAt(idx);

      string.append(c);
    }

    final PercentUtf8 actual;
    actual = new PercentUtf8(this, string);

    return actual.encode();
  }

  final boolean hasNext() {
    return index < input.length();
  }

  final char next() {
    return input.charAt(index++);
  }

  final boolean test(char c) {
    return dictionary.test(c);
  }

}
