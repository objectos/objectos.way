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
package objectos.html.internal;

import java.util.EnumSet;
import java.util.Set;
import objectos.html.tmpl.AttributeName;
import objectos.html.tmpl.StandardElementName;

public final class PrettyPrintWriter extends Writer {

  private static final int START = 0,
      NL = 1,
      CONTENTS = 2;

  private static final Set<StandardElementName> PHRASING = EnumSet.of(
    StandardElementName.A,
    StandardElementName.ABBR,
    StandardElementName.B,
    StandardElementName.BR,
    StandardElementName.BUTTON,
    StandardElementName.CODE,
    StandardElementName.EM,
    StandardElementName.IMG,
    StandardElementName.INPUT,
    StandardElementName.KBD,
    StandardElementName.LABEL,
    StandardElementName.LINK,
    StandardElementName.META,
    StandardElementName.PROGRESS,
    StandardElementName.SAMP,
    StandardElementName.SCRIPT,
    StandardElementName.SELECT,
    StandardElementName.SMALL,
    StandardElementName.SPAN,
    StandardElementName.STRONG,
    StandardElementName.SUB,
    StandardElementName.SUP,
    StandardElementName.SVG,
    StandardElementName.TEMPLATE,
    StandardElementName.TEXTAREA
  );

  private int last;

  private boolean metadata;

  @Override
  public final void attribute(AttributeName name) {
    write(' ');

    write(name.getName());
  }

  @Override
  public final void attributeFirstValue(String value) {
    write("=\"");

    escaped(value);
  }

  @Override
  public final void attributeNextValue(String value) {
    write(' ');

    escaped(value);
  }

  @Override
  public final void attributeValueEnd() {
    write('"');
  }

  @Override
  public final void doctype() {
    write("<!DOCTYPE html>");

    nl();
  }

  @Override
  public final void documentEnd() {
    if (last != NL) {
      nl();
    }
  }

  @Override
  public final void documentStart() {
    last = START;

    metadata = false;
  }

  @Override
  public final void endTag(StandardElementName name) {
    write('<');
    write('/');
    write(name.getName());
    write('>');

    if (metadata || !phrasing(name)) {
      nl();
    }

    if (head(name)) {
      metadata = false;
    }
  }

  @Override
  public final void raw(String value) {
    int length = value.length();

    if (metadata && length > 0) {
      var first = value.charAt(0);

      if (!nl(first)) {
        nl();
      }

      write(value);

      var last = value.charAt(length - 1);

      if (!nl(last)) {
        nl();
      }
    } else {
      write(value);
    }
  }

  @Override
  public final void startTag(StandardElementName name) {
    if (last >= CONTENTS && (metadata || !phrasing(name))) {
      nl();
    }

    write('<');
    write(name.getName());

    if (head(name)) {
      metadata = true;
    }
  }

  @Override
  public final void startTagEnd(StandardElementName name) {
    write('>');

    last = CONTENTS;

    var kind = name.getKind();

    if (kind.isVoid() && (metadata || !phrasing(name))) {
      nl();
    }
  }

  @Override
  public final void text(String value) {
    escaped(value);
  }

  private boolean head(StandardElementName name) {
    return name == StandardElementName.HEAD;
  }

  private void nl() {
    write(System.lineSeparator());

    last = NL;
  }

  private boolean nl(char c) {
    return c == '\n' || c == '\r';
  }

  private boolean phrasing(StandardElementName name) {
    return PHRASING.contains(name);
  }

}