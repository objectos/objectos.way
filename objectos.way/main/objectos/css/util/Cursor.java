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

/**
 * Utility classes for the {@code cursor} CSS property.
 */
// Generated by selfgen.css.CssUtilSpec. Do not edit!
public enum Cursor implements StyleClass {

  AUTO("auto"),

  DEFAULT("default"),

  POINTER("pointer"),

  WAIT("wait"),

  TEXT("text"),

  MOVE("move"),

  HELP("help"),

  NOT_ALLOWED("not-allowed"),

  NONE("none"),

  CONTEXT_MENU("context-menu"),

  PROGRESS("progress"),

  CELL("cell"),

  CROSSHAIR("crosshair"),

  VERTICAL_TEXT("vertical-text"),

  ALIAS("alias"),

  COPY("copy"),

  NO_DROP("no-drop"),

  GRAB("grab"),

  GRABBING("grabbing"),

  ALL_SCROLL("all-scroll"),

  COL_RESIZE("col-resize"),

  ROW_RESIZE("row-resize"),

  N_RESIZE("n-resize"),

  E_RESIZE("e-resize"),

  S_RESIZE("s-resize"),

  W_RESIZE("w-resize"),

  NE_RESIZE("ne-resize"),

  NW_RESIZE("nw-resize"),

  SE_RESIZE("se-resize"),

  SW_RESIZE("sw-resize"),

  EW_RESIZE("ew-resize"),

  NS_RESIZE("ns-resize"),

  NESW_RESIZE("nesw-resize"),

  NWSE_RESIZE("nwse-resize"),

  ZOOM_IN("zoom-in"),

  ZOOM_OUT("zoom-out");

  private final String className = ClassSelectorSeqId.next();

  private final String value;

  private Cursor(String value) {
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
    return "." + className + " { cursor: " + value + " }";
  }

}
