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

import objectos.internal.Ascii;

public final class UrlEncoder {

  private final String input;

  public UrlEncoder(String input) {
    this.input = input;
  }

  public final String encode() {
    final String visible;
    visible = Ascii.visible();

    final PercentDictionary dictionary;
    dictionary = new PercentDictionary(visible);

    final PercentEncoder encoder;
    encoder = new PercentEncoder(dictionary, input);

    return encoder.encode();
  }

}
