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
package objectos.code2;

class JavaSinkOfStringBuilder extends JavaSink {

  private final StringBuilder out;

  public JavaSinkOfStringBuilder(StringBuilder out) { this.out = out; }

  @Override
  public final String toString() { return out.toString(); }

  @Override
  protected void writeCompilationUnitEnd(String packageName, String fileName) {
    writenl();
  }

  @Override
  protected final void writeIdentifier(String name) { out.append(name); }

  @Override
  protected final void writeKeyword(Keyword value) { out.append(value); }

  @Override
  protected final void writeSeparator(Separator value) { out.append(value); }

  @Override
  protected void writeWhitespace(Whitespace value) {
    switch (value) {
      case MANDATORY, OPTIONAL -> out.append(' ');

      case NEW_LINE -> writenl();

      case AFTER_ANNOTATION -> writenl();

      case BEFORE_FIRST_MEMBER -> writenl();

      case BEFORE_NEXT_MEMBER -> { writenl(); writenl(); }

      case BEFORE_NEXT_TOP_LEVEL_ITEM -> { writenl(); writenl(); }

      case BEFORE_NEXT_STATEMENT -> writenl();

      case BEFORE_NEXT_COMMA_SEPARATED_ITEM -> out.append(' ');

      case BEFORE_NON_EMPTY_BLOCK_END -> writenl();

      case BEFORE_EMPTY_BODY_END -> {}
    }
  }

  private void writenl() {
    out.append(System.lineSeparator());

    elemIndex = out.length();
  }

}