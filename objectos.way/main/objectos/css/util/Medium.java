/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.css.util;

import objectos.html.tmpl.Api.ExternalAttribute.StyleClass;
import objectox.css.ClassSelectorSeqId;

// Generated by selfgen.css.CssUtilSpec. Do not edit!
public final class Medium {

  private Medium() {}

  /**
   * Utility classes for the {@code display} CSS property.
   */
  public enum Display implements StyleClass {

    NONE("none"),

    BLOCK("block"),

    FLOW_ROOT("flow-root"),

    INLINE_BLOCK("inline-block"),

    INLINE("inline"),

    FLEX("flex"),

    INLINE_FLEX("inline-flex"),

    GRID("grid"),

    INLINE_GRID("inline-grid"),

    TABLE("table"),

    TABLE_CAPTION("table-caption"),

    TABLE_CELL("table-cell"),

    TABLE_COLUMN("table-column"),

    TABLE_COLUMN_GROUP("table-column-group"),

    TABLE_FOOTER_GROUP("table-footer-group"),

    TABLE_HEADER_GROUP("table-header-group"),

    TABLE_ROW_GROUP("table-row-group"),

    TABLE_ROW("table-row");

    private final String className = ClassSelectorSeqId.next();

    private final String value;

    private Display(String value) {
      this.value = value;
    }

    /**
     * Returns the CSS class name.
     *
     * @return the CSS class name
     */
    @Override
    public final String className() {
      return className;
    }

    /**
     * Returns the CSS style rule represented by this utility class.
     *
     * @return the CSS style rule
     */
    @Override
    public final String toString() {
      return "." + className + " { display: " + value + " }";
    }

  }

}
