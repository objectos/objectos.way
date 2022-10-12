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
package br.com.objectos.code.java.expression;

import br.com.objectos.code.java.element.AbstractDefaultImmutableCodeElement;
import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.type.NamedClass;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;

final class LiteralImpl extends AbstractDefaultImmutableCodeElement implements Literal {

  static final Literal FALSE = l0(false);
  static final Literal NULL = ofPrimitive("null");
  static final Literal TRUE = l0(true);

  private LiteralImpl(CodeElement... elements) {
    super(elements);
  }

  private LiteralImpl(UnmodifiableList<CodeElement> elements) {
    super(elements);
  }

  static String escapeJava(String string) {
    Check.notNull(string, "string == null");

    StringBuilder result = new StringBuilder();

    char[] chars = string.toCharArray();

    for (char c : chars) {
      switch (c) {
        case '\b':
          result.append("\\b");
          break;
        case '\t':
          result.append("\\t");
          break;
        case '\n':
          result.append("\\n");
          break;
        case '\f':
          result.append("\\f");
          break;
        case '\r':
          result.append("\\r");
          break;
        case '\"':
          result.append("\\\"");
          break;
        case '\'':
          result.append("\\\'");
          break;
        case '\\':
          result.append("\\\\");
          break;
        default:
          if (Character.isISOControl(c)) {
            int asInt = c;
            result.append(String.format("\\u%04x", asInt));
          } else {
            result.append(c);
          }
          break;
      }
    }

    return result.toString();
  }

  static Literal l0(boolean value) {
    return ofPrimitive(Boolean.toString(value));
  }

  static Literal l0(char value) {
    return ofPrimitive("'" + escapeJava(Character.toString(value)) + "'");
  }

  static Literal l0(Class<?> type) {
    return l0(NamedClass.of(type));
  }

  static Literal l0(double value) {
    return ofPrimitive(Double.toString(value));
  }

  static Literal l0(float value) {
    return ofPrimitive(Float.toString(value) + "F");
  }

  static Literal l0(int value) {
    return ofPrimitive(Integer.toString(value));
  }

  static Literal l0(long value) {
    return ofPrimitive(Long.toString(value) + "L");
  }

  static Literal l0(NamedClass className) {
    return new LiteralImpl(
        className, raw(".class")
    );
  }

  static Literal l0(String s) {
    return new LiteralImpl(
        indentIfNecessary(), quote(), raw(escapeJava(s)), quote()
    );
  }

  private static Literal ofPrimitive(String value) {
    return new LiteralImpl(
        word(value)
    );
  }

  @Override
  public final void acceptArgumentsBuilder(Arguments.Builder builder) {
    builder.addArgumentUnchecked(this);
  }

  @Override
  public final FieldAccess id(Identifier id) {
    return Expressions.fieldAccess(this, id);
  }

  @Override
  public final FieldAccess id(String id) {
    return Expressions.fieldAccess(this, id);
  }

  @Override
  public final Literal nl() {
    return new LiteralImpl(appendNextLine());
  }

  @Override
  protected final ArrayReferenceExpression selfArrayReferenceExpression() {
    throw newUoe(Literal.class);
  }

  @Override
  protected final Callee selfCallee() {
    return this;
  }

  @Override
  protected final ConditionalAndExpression selfConditionalAndExpression() {
    return this;
  }

  @Override
  protected final LeftHandSide selfLeftHandSide() {
    throw newUoe(Literal.class);
  }

  @Override
  protected final MethodReferenceReferenceExpression selfMethodReferenceReferenceExpression() {
    return this;
  }

  @Override
  protected final MultiplicativeExpression selfMultiplicativeExpression() {
    return this;
  }

  @Override
  protected final PostfixExpression selfPostfixExpression() {
    return this;
  }

  @Override
  protected final RelationalExpression selfRelationalExpression() {
    return this;
  }

}
