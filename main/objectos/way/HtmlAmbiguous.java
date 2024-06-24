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
package objectos.way;

enum HtmlAmbiguous {

  FORM(HtmlAttributeName.FORM, HtmlElementName.FORM) {
    @Override
    public final boolean isAttributeOf(Html.ElementName element) {
      return element == HtmlElementName.SELECT
          || element == HtmlElementName.TEXTAREA;
    }
  },

  LABEL(HtmlAttributeName.LABEL, HtmlElementName.LABEL) {
    @Override
    public final boolean isAttributeOf(Html.ElementName element) {
      return element == HtmlElementName.OPTION;
    }
  },

  TITLE(HtmlAttributeName.TITLE, HtmlElementName.TITLE) {
    @Override
    public final boolean isAttributeOf(Html.ElementName element) {
      return element != HtmlElementName.HEAD;
    }
  };

  private static final HtmlAmbiguous[] ALL = HtmlAmbiguous.values();

  private final int attributeByteCode;

  public final HtmlElementName element;

  private final int elementByteCode;

  private HtmlAmbiguous(HtmlAttributeName attribute, HtmlElementName element) {
    this.attributeByteCode = attribute.index();

    this.element = element;

    this.elementByteCode = element.index();
  }

  public static HtmlAmbiguous decode(byte b0) {
    int ordinal;
    ordinal = Bytes.decodeInt(b0);

    return ALL[ordinal];
  }

  public static HtmlAmbiguous get(int code) {
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

  public abstract boolean isAttributeOf(Html.ElementName element);

}
