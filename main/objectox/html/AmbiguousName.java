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

import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementName;

public enum AmbiguousName {

  CLIPPATH(AttributeName.CLIP_PATH, ElementName.CLIPPATH),

  FORM(AttributeName.FORM, ElementName.FORM),

  LABEL(AttributeName.LABEL, ElementName.LABEL),

  STYLE(AttributeName.STYLE, ElementName.STYLE),

  TITLE(AttributeName.TITLE, ElementName.TITLE);

  public final AttributeName attribute;

  public final ElementName element;

  private AmbiguousName(AttributeName attribute, ElementName element) {
    this.attribute = attribute;

    this.element = element;
  }

  private static final AmbiguousName[] VALUES = values();

  public static AmbiguousName get(int index) {
    return VALUES[index];
  }

  public final int index() {
    return ordinal();
  }

  public final boolean isAttributeOf(ElementName element) {
    return switch (this) {
      case CLIPPATH -> element != ElementName.SVG;

      case FORM -> element == ElementName.BUTTON
          || element == ElementName.INPUT
          || element == ElementName.SELECT
          || element == ElementName.TEXTAREA;

      case LABEL -> element == ElementName.OPTION;

      case STYLE -> element != ElementName.HEAD;

      case TITLE -> element != ElementName.HEAD
          && element != ElementName.SVG;
    };
  }

}