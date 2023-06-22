/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package br.com.objectos.http;

final class HeaderTesting {

  private HeaderTesting() {}

  @SuppressWarnings("unchecked")
  static <T extends AbstractHeader<?>> T parse(T header, String source) {
    char[] charArray;
    charArray = source.toCharArray();

    for (char c : charArray) {
      if (!header.shouldConsume()) {
        throw new AssertionError("under consumed");
      }

      header.consume(c);
    }

    if (header.isMalformed()) {
      throw new AssertionError("is malformed");
    }

    if (header.shouldConsume()) {
      throw new AssertionError("over consumed");
    }

    Header result;
    result = header.build();

    return (T) result;
  }

}
