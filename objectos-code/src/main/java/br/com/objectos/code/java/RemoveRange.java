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
package br.com.objectos.code.java;

final class RemoveRange extends StringConversion {

  static final StringConversion COMBINING_DIACRITICAL_MARKS
      = new RemoveRange('\u0300', '\u036F');

  private static final Object STATE = null;

  private final int max;

  private final int min;

  RemoveRange(int min, int max) {
    this.max = max;

    this.min = min;
  }

  @Override
  protected final Object executeOne(Object state, StringBuilder builder, int c) {
    if (c < min || c > max) {
      builder.appendCodePoint(c);
    }

    return STATE;
  }

  @Override
  protected final Object startingState() {
    return STATE;
  }

}