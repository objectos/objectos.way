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
package objectox.html.play;

final class NextElementAttribute {

  private final HtmlSource source;

  final Stack stack;

  final Values values;

  final int valueIndex;

  NextElementAttribute(HtmlSource source, Stack stack, Values values, int valueIndex) {
    this.source = source;

    this.stack = stack;

    this.values = values;

    this.valueIndex = valueIndex;
  }

  public final State compute() {
    if (values.hasNext(valueIndex)) {
      throw new UnsupportedOperationException("Implement me");
    } else {
      throw new UnsupportedOperationException("Implement me");
    }
  }

}
