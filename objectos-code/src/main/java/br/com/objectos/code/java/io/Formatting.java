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
package br.com.objectos.code.java.io;

public abstract class Formatting {

  Formatting() {}

  public static Formatting constructors() {
    return BodyElementFormatting.CONSTRUCTORS;
  }

  public static Formatting fields() {
    return BodyElementFormatting.FIELDS;
  }

  public static Formatting methods() {
    return BodyElementFormatting.METHODS;
  }

  public static Formatting types() {
    return BodyElementFormatting.TYPES;
  }

  public static Formatting newLine() {
    return NewLineFormatting.getInstance();
  }

  abstract FormattingAction newAction(FormattingAction nextAction);

}