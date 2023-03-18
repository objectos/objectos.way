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
import objectos.html.tmpl.StandardElementName;

public final class PrettyPrintWriter extends MinifiedWriter {

  private static final int START = 0,
      CONTENTS = 1,
      ELANG = 2,
      TEXT = 3,
      TEXT_NL = 4,
      INLINE_END = 5;

  private static final Set<StandardElementName> BLOCKS = EnumSet.of(
    StandardElementName.HTML,
    StandardElementName.HEAD,
    StandardElementName.TITLE,
    StandardElementName.META,
    StandardElementName.LINK,
    StandardElementName.STYLE,
    StandardElementName.SCRIPT,
    StandardElementName.BODY,
    StandardElementName.DIV,
    StandardElementName.HEADER,
    StandardElementName.NAV,
    StandardElementName.MAIN,
    StandardElementName.ARTICLE,
    StandardElementName.SECTION,
    StandardElementName.FOOTER,
    StandardElementName.H1,
    StandardElementName.H2,
    StandardElementName.H3,
    StandardElementName.H4,
    StandardElementName.H5,
    StandardElementName.H6,
    StandardElementName.P,
    StandardElementName.PRE,
    StandardElementName.UL,
    StandardElementName.OL,
    StandardElementName.LI,
    StandardElementName.BLOCKQUOTE
  );

  private static final Set<StandardElementName> ELANG_SET = EnumSet.of(
    StandardElementName.STYLE,
    StandardElementName.SCRIPT
  );

  private int state;

  @Override
  public final void doctype() {
    super.doctype();

    nl();
  }

  @Override
  public final void documentEnd() {
    super.documentEnd();

    if (state == INLINE_END) {
      nl();
    }
  }

  @Override
  public final void documentStart() {
    super.documentStart();

    state = START;
  }

  @Override
  public final void endTag(StandardElementName name) {
    if (ELANG_SET.contains(name) && state == TEXT) {
      nl();
    }

    super.endTag(name);

    if (isBlock(name)) {
      nl();
    } else {
      state = INLINE_END;
    }
  }

  @Override
  public final void raw(String value) {
    preText(value);

    super.raw(value);

    postText(value);
  }

  @Override
  public final void selfClosingEnd() {
    super.selfClosingEnd();

    nl();
  }

  @Override
  public final void startTag(StandardElementName name) {
    if (state != START) {
      if (isBlock(name)) {
        nl();
      }
    }

    super.startTag(name);
  }

  @Override
  public final void startTagEnd(StandardElementName name) {
    super.startTagEnd(name);

    if (ELANG_SET.contains(name)) {
      state = ELANG;
    } else {
      state = CONTENTS;
    }
  }

  @Override
  public final void text(String value) {
    preText(value);

    super.text(value);

    postText(value);
  }

  private boolean isBlock(StandardElementName name) {
    return BLOCKS.contains(name);
  }

  private void nl() {
    write(System.lineSeparator());

    state = START;
  }

  private void postText(String value) {
    state = TEXT;

    int length = value.length();

    if (length == 0) {
      return;
    }

    var last = value.charAt(length - 1);

    if (last != '\n' && last != '\r') {
      return;
    }

    state = TEXT_NL;
  }

  private void preText(String value) {
    if (state != ELANG) {
      return;
    }

    if (value.isEmpty()) {
      return;
    }

    var first = value.charAt(0);

    if (first == '\n' || first == '\r') {
      return;
    }

    nl();
  }

}