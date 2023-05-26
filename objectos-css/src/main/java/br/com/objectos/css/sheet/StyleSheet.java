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
import br.com.objectos.css.parser.IsNonTerminal;
import br.com.objectos.css.property.StandardPropertyName;
import br.com.objectos.css.select.AttributeValueOperator;
import br.com.objectos.css.select.Combinator;
import br.com.objectos.css.select.SimpleSelector;
import br.com.objectos.css.select.UniversalSelector;
import br.com.objectos.css.type.AngleUnit;
import br.com.objectos.css.type.ColorName;
import br.com.objectos.css.type.LengthUnit;

public interface StyleSheet
    extends
    IsNonTerminal {

  interface Processor {

    void visitAfterLastDeclaration();

    void visitAngle(AngleUnit unit, double value);

    void visitAngle(AngleUnit unit, int value);

    void visitAttributeSelector(String attributeName);

    void visitAttributeValueSelector(
        String attributeName, AttributeValueOperator operator, String value);

    void visitBeforeNextDeclaration();

    void visitBeforeNextValue();

    void visitBlockStart();

    void visitClassSelector(String className);

    void visitColor(ColorName value);

    void visitColor(String value);

    void visitCombinator(Combinator combinator);

    void visitDeclarationStart(StandardPropertyName name);

    void visitDouble(double value);

    void visitEmptyBlock();

    void visitFunctionEnd();

    void visitFunctionStart(StandardFunctionName name);

    void visitIdSelector(String id);

    void visitInt(int value);

    void visitKeyword(StandardKeyword value);

    void visitKeyword(String keyword);

    void visitLength(LengthUnit unit, double value);

    void visitLength(LengthUnit unit, int value);

    void visitLogicalExpressionEnd();

    void visitLogicalExpressionStart(LogicalOperator operator);

    void visitMediaEnd();

    void visitMediaStart();

    void visitMediaType(MediaType type);

    void visitMultiDeclarationSeparator();

    void visitPercentage(double value);

    void visitPercentage(int value);

    void visitRgb(double r, double g, double b);

    void visitRgb(double r, double g, double b, double alpha);

    void visitRgb(int r, int g, int b);

    void visitRgb(int r, int g, int b, double alpha);

    void visitRgba(double r, double g, double b, double alpha);

    void visitRgba(int r, int g, int b, double alpha);

    void visitRuleEnd();

    void visitRuleStart();

    void visitSimpleSelector(SimpleSelector selector);

    void visitString(String value);

    void visitUniversalSelector(UniversalSelector selector);

    void visitUri(String value);

  }

  void eval(StyleEngine engine);

  String printMinified();

}