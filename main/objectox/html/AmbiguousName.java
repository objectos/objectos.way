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

import objectos.html.ElementName;
import objectox.html.attr.AttributeName;
import objectox.html.elem.ElementNamePojo;

public enum AmbiguousName {

  CLIPPATH(AttributeName.CLIP_PATH, ElementNamePojo.CLIPPATH),

  FORM(AttributeName.FORM, ElementNamePojo.FORM),

  LABEL(AttributeName.LABEL, ElementNamePojo.LABEL),

  STYLE(AttributeName.STYLE, ElementNamePojo.STYLE),

  TITLE(AttributeName.TITLE, ElementNamePojo.TITLE);

  public final AttributeName attribute;

  public final ElementNamePojo element;

  private AmbiguousName(AttributeName attribute, ElementNamePojo element) {
    this.attribute = attribute;

    this.element = element;
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