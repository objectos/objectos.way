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
import objectos.html.tmpl.ElementName;
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardElementName;

enum Ambiguous {

  FORM(StandardAttributeName.FORM, StandardElementName.FORM) {
    static final Set<StandardElementName> ELEMENTS = EnumSet.of(
      StandardElementName.SELECT, StandardElementName.TEXTAREA
    );

    @Override
    public final boolean isAttributeOf(ElementName element) {
      return ELEMENTS.contains(element);
    }
  },

  LABEL(StandardAttributeName.LABEL, StandardElementName.LABEL) {
    @Override
    public final boolean isAttributeOf(ElementName element) {
      return element == StandardElementName.OPTION;
    }
  },

  TITLE(StandardAttributeName.TITLE, StandardElementName.TITLE) {
    @Override
    public final boolean isAttributeOf(ElementName element) {
      return element != StandardElementName.HEAD;
    }
  };

  private static final Ambiguous[] ALL = Ambiguous.values();

  private final int attributeByteCode;

  public final StandardElementName element;

  private final int elementByteCode;

  private Ambiguous(StandardAttributeName attribute, StandardElementName element) {
    this.attributeByteCode = attribute.getCode();

    this.element = element;

    this.elementByteCode = element.getCode();
  }

  public static Ambiguous decode(byte b0) {
    int ordinal;
    ordinal = Bytes.decodeInt(b0);

    return ALL[ordinal];
  }

  public static Ambiguous get(int code) {
    return ALL[code];
  }

  public final int attributeByteCode() {
    return attributeByteCode;
  }

  public final int code() {
    return ordinal();
  }

  public final int elementByteCode() {
    return elementByteCode;
  }

  public final byte encodeAttribute() {
    return Bytes.encodeInt0(attributeByteCode);
  }

  public abstract boolean isAttributeOf(ElementName element);

}
