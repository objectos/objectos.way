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

  private final Object value;

  ScriptElement(ScriptWriter writer) {
    this(writer, null);
  }

  ScriptElement(ScriptWriter writer, Object value) {
    this.writer = writer;

    this.value = value;
  }

  @Override
  public final StringQuery attr(Html.AttributeName name) {
    final String _name;
    _name = name.name();

    return ScriptStringQuery.elementMethodInvocation(this, "getAttribute", _name, writer);
  }

  @Override
  public final void attr(Html.AttributeName name, StringQuery value) {
    String _name = name.name();
    Objects.requireNonNull(value, "value == null");

    writer.actionStart();

    elementAction();

    writer.comma();
    writer.stringLiteral("attr-0");

    writer.comma();
    writer.stringLiteral(_name);

    writer.comma();
    writer.stringQuery(value);

    writer.actionEnd();
  }

  @Override
  public final void attr(Html.AttributeName name, String value) {
    String _name = name.name();
    Objects.requireNonNull(value, "value == null");

    writer.actionStart();

    methodInvocation();

    writer.comma();
    writer.stringLiteral("setAttribute");

    writer.comma();
    writer.stringLiteral(_name);

    writer.comma();
    writer.stringLiteral(value);

    writer.actionEnd();
  }

  @Override
  public final void close() {
    writer.actionStart();

    elementAction();

    writer.comma();
    writer.stringLiteral("close-0");

    writer.actionEnd();
  }

  @Override
  public final void focus() {
    writer.actionStart();

    elementAction();

    writer.comma();
    writer.stringLiteral("focus-0");

    writer.actionEnd();
  }

  @Override
  public final void scroll(int x, int y) {
    writer.actionStart();

    methodInvocation();

    writer.comma();
    writer.stringLiteral("scroll");

    writer.comma();
    writer.intLiteral(x);

    writer.comma();
    writer.intLiteral(y);

    writer.actionEnd();
  }

  @Override
  public final void showModal() {
    writer.actionStart();

    elementAction();

    writer.comma();
    writer.stringLiteral("show-modal-0");

    writer.actionEnd();
  }

  @Override
  public final void submit() {
    writer.actionStart();

    elementAction();

    writer.comma();
    writer.stringLiteral("submit-0");

    writer.actionEnd();
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

  final void elementAction() {
    switch (kind) {
      case ELEMENT -> writer.stringLiteral("element-2");

      case BY_ID -> {
        writer.stringLiteral("id-2");
        writer.comma();
        writer.stringLiteralOrQuery(value);
      }
    }
  }

  final void methodInvocation() {
    switch (kind) {
      case ELEMENT -> writer.stringLiteral("element-1");

      case BY_ID -> {
        writer.stringLiteral("id-1");
        writer.comma();
        writer.stringLiteralOrQuery(value);
      }
    }
  }

}