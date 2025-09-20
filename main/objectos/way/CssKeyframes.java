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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

final class CssKeyframes implements Css.StyleSheet.Keyframes {

  record Frame(String selector, List<String> declarations) {}

  List<Frame> frames = List.of();

  String name = "unnamed";

  @Override
  public final void name(String value) {
    name = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void frame(String selector, String value) {
    final String s;
    s = checkSelector(selector);

    final List<String> declaractions;
    declaractions = parseDeclarations(value);

    final Frame frame;
    frame = new Frame(s, declaractions);

    if (frames.isEmpty()) {
      frames = new ArrayList<>();
    }

    frames.add(frame);
  }

  private String checkSelector(String selector) {
    return Objects.requireNonNull(selector, "selector == null");
  }

  private List<String> parseDeclarations(String value) {
    return value.stripIndent().lines().toList();
  }

}