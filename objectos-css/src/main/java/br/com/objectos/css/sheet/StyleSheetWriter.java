/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package br.com.objectos.css.sheet;

import br.com.objectos.css.function.StandardFunctionName;
import br.com.objectos.css.keyword.StandardKeyword;
import br.com.objectos.css.select.AttributeValueOperator;
import br.com.objectos.css.select.SimpleSelector;
import br.com.objectos.css.select.UniversalSelector;
import br.com.objectos.css.type.AngleUnit;
import br.com.objectos.css.type.ColorName;
import br.com.objectos.css.type.LengthUnit;
import java.util.function.Predicate;
import objectos.lang.Check;

public abstract class StyleSheetWriter implements StyleSheet.Processor {

  private static final Predicate<String> CLASS_SELECTORS_ALWAYS_TRUE = (s) -> true;

  private final StyleSheetEngine engine = new StyleSheetEngine();

  private final StringBuilder out = new StringBuilder();

  private Predicate<String> classSelectorsByName;

  private boolean ignoreRule;

  private int mediaStart;

  private int ruleCount;

  private int ruleStart;

  StyleSheetWriter() {}

  public static StyleSheetWriter ofMinified() {
    return new MinifiedStyleSheetWriter();
  }

  public static StyleSheetWriter ofPretty() {
    return new PrettyStyleSheetWriter();
  }

  public static String toMinifiedString(StyleSheet sheet) {
    var writer = StyleSheetWriter.ofMinified();

    return writer.toString(sheet);
  }

  public final void clear() {
    if (classSelectorsByName != null) {
      classSelectorsByName = null;
    }
  }

  public final void filterClassSelectorsByName(Predicate<String> predicate) {
    classSelectorsByName = Check.notNull(predicate, "predicate == null");
  }

  public final String toString(StyleSheet sheet) {
    if (classSelectorsByName == null) {
      classSelectorsByName = CLASS_SELECTORS_ALWAYS_TRUE;
    }

    out.setLength(0);

    ruleCount = 0;

    engine.process(sheet, this);

    return out.toString();
  }

  @Override
  public void visitAngle(AngleUnit unit, double value) {
    writeDouble(value);
    write(unit.getName());
  }

  @Override
  public void visitAngle(AngleUnit unit, int value) {
    writeInt(value);
    write(unit.getName());
  }

  @Override
  public void visitAttributeSelector(String attributeName) {
    write('[');
    write(attributeName);
    write(']');
  }

  @Override
  public void visitAttributeValueSelector(
      String attributeName, AttributeValueOperator operator, String value) {
    write('[');
    write(attributeName);
    write(operator.getSymbol());
    quoteIfNecessary(value);
    write(']');
  }

  @Override
  public void visitBeforeNextValue() {
    write(' ');
  }

  @Override
  public void visitClassSelector(String className) {
    ignoreRule = !classSelectorsByName.test(className);

    write('.');
    write(className);
  }

  @Override
  public void visitColor(ColorName value) {
    write(value.getName());
  }

  @Override
  public void visitColor(String value) {
    writeValueColorHex(value);
  }

  @Override
  public void visitDouble(double value) {
    writeDoubleImpl(value);
  }

  @Override
  public void visitFunctionEnd() {
    write(')');
  }

  @Override
  public void visitFunctionStart(StandardFunctionName name) {
    write(name.getName());
    write('(');
  }

  @Override
  public void visitIdSelector(String id) {
    write('#');
    write(id);
  }

  @Override
  public void visitInt(int value) {
    write(Integer.toString(value));
  }

  @Override
  public void visitKeyword(StandardKeyword value) {
    write(value.getName());
  }

  @Override
  public void visitKeyword(String keyword) {
    write(keyword);
  }

  @Override
  public void visitLength(LengthUnit unit, double value) {
    writeDouble(value);
    write(unit.getName());
  }

  @Override
  public void visitLength(LengthUnit unit, int value) {
    writeInt(value);
    write(unit.getName());
  }

  @Override
  public void visitLogicalExpressionEnd() {
    write(')');
  }

  @Override
  public void visitLogicalExpressionStart(LogicalOperator operator) {
    write(' ');
    write(operator.getName());
    write(' ');
    write('(');
  }

  @Override
  public void visitMediaEnd() {
    if (ruleCount == 0) {
      out.setLength(mediaStart);
    }
  }

  @Override
  public final void visitMediaStart() {
    mediaStart = out.length();

    blockSeparatorIfNecessary();

    ruleCount = 0;

    write("@media");
  }

  @Override
  public void visitMediaType(MediaType type) {
    write(' ');
    write(type.getName());
  }

  @Override
  public void visitPercentage(double value) {
    writeDouble(value);
    write('%');
  }

  @Override
  public void visitPercentage(int value) {
    writeInt(value);
    write('%');
  }

  @Override
  public void visitRgb(double r, double g, double b) {
    writeRgbStart();
    writeDouble(r);
    writeComma();
    writeDouble(g);
    writeComma();
    writeDouble(b);
    write(')');
  }

  @Override
  public void visitRgb(double r, double g, double b, double alpha) {
    writeRgbStart();
    writeDouble(r);
    writeComma();
    writeDouble(g);
    writeComma();
    writeDouble(b);
    writeComma();
    writeDouble(alpha);
    write(')');
  }

  @Override
  public void visitRgb(int r, int g, int b) {
    writeRgbStart();
    writeInt(r);
    writeComma();
    writeInt(g);
    writeComma();
    writeInt(b);
    write(')');
  }

  @Override
  public void visitRgb(int r, int g, int b, double alpha) {
    writeRgbStart();
    writeInt(r);
    writeComma();
    writeInt(g);
    writeComma();
    writeInt(b);
    writeComma();
    writeDouble(alpha);
    write(')');
  }

  @Override
  public void visitRgba(double r, double g, double b, double alpha) {
    writeRgbaStart();
    writeDouble(r);
    writeComma();
    writeDouble(g);
    writeComma();
    writeDouble(b);
    writeComma();
    writeDouble(alpha);
    write(')');
  }

  @Override
  public void visitRgba(int r, int g, int b, double alpha) {
    writeRgbaStart();
    writeInt(r);
    writeComma();
    writeInt(g);
    writeComma();
    writeInt(b);
    writeComma();
    writeDouble(alpha);
    write(')');
  }

  @Override
  public void visitRuleEnd() {
    if (ignoreRule) {
      out.setLength(ruleStart);

      ignoreRule = false;
    } else {
      ruleCount++;
    }
  }

  @Override
  public void visitRuleStart() {
    ruleStart = out.length();

    blockSeparatorIfNecessary();
  }

  @Override
  public void visitSimpleSelector(SimpleSelector selector) {
    write(selector.toString());
  }

  @Override
  public void visitString(String value) {
    quoteIfNecessary(value);
  }

  @Override
  public void visitUniversalSelector(UniversalSelector selector) {
    write('*');
  }

  @Override
  public void visitUri(String value) {
    write("url(");
    quoteIfNecessary(value);
    write(')');
  }

  final void quote(String value) {
    write('"');
    write(value);
    write('"');
  }

  abstract void quoteIfNecessary(String value);

  final void write(char c) {
    out.append(c);
  }

  final void write(String s) {
    out.append(s);
  }

  abstract void writeBlockSeparator();

  abstract void writeComma();

  void writeDoubleImpl(double value) {
    write(Double.toString(value));
  }

  abstract void writeFirstValuePrefix();

  final void writeInt(int value) {
    write(Integer.toString(value));
  }

  final void writeUrl(String src) {
    write("url(");
    write('"');
    write(src);
    write('"');
    write(')');
  }

  abstract void writeValueColorHex(String value);

  private void blockSeparatorIfNecessary() {
    if (ruleCount > 0) {
      writeBlockSeparator();
    }
  }

  private boolean isIntWithinTolerance(double value) {
    int whole;
    whole = (int) value;

    double decimal;
    decimal = value - whole;

    return isZeroWithinTolerance(decimal);
  }

  private boolean isZeroWithinTolerance(double value) {
    if (value == 0) {
      return true;
    }

    double abs;
    abs = Math.abs(value);

    return abs < 1e-5;
  }

  private void writeDouble(double value) {
    if (isZeroWithinTolerance(value)) {
      write('0');
    }

    else if (isIntWithinTolerance(value)) {
      writeInt((int) value);
    }

    else {
      writeDoubleImpl(value);
    }
  }

  private void writeRgbaStart() {
    write("rgba(");
  }

  private void writeRgbStart() {
    write("rgb(");
  }

}