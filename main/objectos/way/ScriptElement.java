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

import module java.base;
import objectos.way.Script.StringQuery;

final class ScriptElement implements Script.Element {

  private final ScriptWriter writer;

  private final Object locator;

  ScriptElement(ScriptWriter writer) {
    this(writer, null);
  }

  ScriptElement(ScriptWriter writer, Object value) {
    this.writer = writer;

    this.locator = value;
  }

  @Override
  public final StringQuery attr(Html.AttributeName name) {
    final String _name;
    _name = name.name();

    return ScriptStringQuery.elementMethodInvocation(this, "getAttribute", _name, writer);
  }

  @Override
  public final void attr(Html.AttributeName name, StringQuery value) {
    final String attrName;
    attrName = name.name();

    final StringQuery q;
    q = Objects.requireNonNull(value, "value == null");

    writer.elementAttr(locator, attrName, q);
  }

  @Override
  public final void attr(Html.AttributeName name, String value) {
    final String attrName;
    attrName = name.name();

    final String q;
    q = Objects.requireNonNull(value, "value == null");

    writer.elementAttr(locator, attrName, q);
  }

  @Override
  public final void close() {
    writer.elementAction(locator, "close-0");
  }

  @Override
  public final void focus() {
    writer.elementAction(locator, "focus-0");
  }

  @Override
  public final void scroll(int x, int y) {
    writer.elementScroll(x, y);
  }

  @Override
  public final void showModal() {
    writer.elementAction(locator, "show-modal-0");
  }

  @Override
  public final void submit() {
    writer.elementAction(locator, "submit-0");
  }

  @Override
  public final void toggleClass(String classes) {
    Check.argument(!classes.isBlank(), "Classes to toggle must not be blank");

    final String[] parts;
    parts = classes.split(" ");

    writer.actionStart();

    elementAction();

    writer.comma();
    writer.stringLiteral("toggle-class-0");

    for (var part : parts) {
      writer.comma();
      writer.stringLiteral(part);
    }

    writer.actionEnd();
  }

}