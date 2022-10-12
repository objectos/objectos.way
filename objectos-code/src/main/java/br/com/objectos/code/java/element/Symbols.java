/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.java.element;

public class Symbols {

  private static final CodeElement AT = WordCodeElement.ofChar('@');
  private static final CodeElement CLOSE_BRACE = WordCodeElement.ofChar('}');
  private static final CodeElement ELLIPSIS = WordCodeElement.ofString("...");
  private static final CodeElement EQUALS = WordCodeElement.ofChar('=');
  private static final CodeElement VERTICAL_BAR = WordCodeElement.ofChar('|');

  private Symbols() {}

  public static CodeElement at() {
    return AT;
  }

  public static CodeElement closeBrace() {
    return CLOSE_BRACE;
  }

  public static CodeElement ellipsis() {
    return ELLIPSIS;
  }

  public static CodeElement equals() {
    return EQUALS;
  }

  public static CodeElement space() {
    return SpaceCodeElement.INSTANCE;
  }

  public static CodeElement verticalBar() {
    return VERTICAL_BAR;
  }

}
