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
package objectox.html;

import objectos.html.AttributeName;
import objectos.html.ElementName;
import objectox.html.attr.AttributeNamePojo;
import objectox.html.elem.ElementNamePojo;
import objectox.html.rec.HtmlBytes;

public enum Ambiguous {

  CLIPPATH(AttributeNamePojo.CLIP_PATH, ElementNamePojo.CLIPPATH) {
    @Override
    public final boolean isAttributeOf(ElementName element) {
      return element != ElementName.SVG;
    }
  },

  FORM(AttributeNamePojo.FORM, ElementNamePojo.FORM) {
    @Override
    public final boolean isAttributeOf(ElementName element) {
      return element == ElementName.BUTTON
          || element == ElementName.INPUT
          || element == ElementName.SELECT
          || element == ElementName.TEXTAREA;
    }
  },

  LABEL(AttributeNamePojo.LABEL, ElementNamePojo.LABEL) {
    @Override
    public final boolean isAttributeOf(ElementName element) {
      return element == ElementName.OPTION;
    }
  },

  STYLE(AttributeNamePojo.STYLE, ElementNamePojo.STYLE) {
    @Override
    public final boolean isAttributeOf(ElementName element) {
      return element != ElementName.HEAD;
    }
  },

  TITLE(AttributeNamePojo.TITLE, ElementNamePojo.TITLE) {
    @Override
    public final boolean isAttributeOf(ElementName element) {
      return element != ElementName.HEAD
          && element != ElementName.SVG;
    }
  };

  private static final Ambiguous[] ALL = Ambiguous.values();

  private final int attributeByteCode;

  public final ElementNamePojo element;

  private final int elementByteCode;

  private Ambiguous(AttributeName attribute, ElementNamePojo element) {
    this.attributeByteCode = attribute.index();

    this.element = element;

    this.elementByteCode = element.index();
  }

  public static Ambiguous decode(byte b0) {
    int ordinal;
    ordinal = HtmlBytes.decodeInt(b0);

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
    return HtmlBytes.encodeInt0(attributeByteCode);
  }

  public abstract boolean isAttributeOf(ElementName element);

}