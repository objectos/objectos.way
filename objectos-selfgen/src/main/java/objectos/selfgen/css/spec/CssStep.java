/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.selfgen.css.spec;

public final class CssStep implements Step {

  private final AngleUnitStep angleUnitStep;

  private final FunctionInterfaceStep functionInterfaceStep;

  private final GeneratedColorStep generatedColorStep;

  private final GeneratedStyleSheetStep generatedStyleSheetStep;

  private final KeywordClassStep keywordClassStep;

  private final KeywordsClassStep keywordsClassStep;

  private final LengthUnitStep lengthUnitStep;

  private final PrimitiveTypeStep primitiveValueStep;

  private final PseudoClassSelectorsGen pseudoClassSelectorsGen;

  private final PseudoElementSelectorsGen pseudoElementSelectorsGen;

  private final StandardFunctionNameStep standardFunctionNameStep;

  private final StandardPropertyNameStep standardPropertyNameStep;

  private final TypeSelectorsGen typeSelectorsGen;

  private final ValueTypeIfaceStep valueTypeIfaceStep;

  public CssStep(StepAdapter adapter) {
    angleUnitStep = new AngleUnitStep(adapter);

    functionInterfaceStep = new FunctionInterfaceStep(adapter);

    generatedColorStep = new GeneratedColorStep(adapter);

    generatedStyleSheetStep = new GeneratedStyleSheetStep(adapter);

    keywordClassStep = new KeywordClassStep(adapter);

    keywordsClassStep = new KeywordsClassStep(adapter);

    lengthUnitStep = new LengthUnitStep(adapter);

    primitiveValueStep = new PrimitiveTypeStep(adapter);

    pseudoClassSelectorsGen = new PseudoClassSelectorsGen(adapter);

    pseudoElementSelectorsGen = new PseudoElementSelectorsGen(adapter);

    standardFunctionNameStep = new StandardFunctionNameStep(adapter);

    standardPropertyNameStep = new StandardPropertyNameStep(adapter);

    typeSelectorsGen = new TypeSelectorsGen(adapter);

    valueTypeIfaceStep = new ValueTypeIfaceStep(adapter);
  }

  @Override
  public final void addAngleUnit(String unit) {
    angleUnitStep.addAngleUnit(unit);

    generatedStyleSheetStep.addAngleUnit(unit);
  }

  @Override
  public final void addColorName(ColorName colorName) {
    generatedColorStep.addColorName(colorName);

    generatedStyleSheetStep.addColorName(colorName);
  }

  @Override
  public final void addElementName(String elementName) {
    generatedStyleSheetStep.addElementName(elementName);

    typeSelectorsGen.addElementName(elementName);
  }

  @Override
  public final void addFunction(FunctionName function) {
    functionInterfaceStep.addFunction(function);

    generatedStyleSheetStep.addFunction(function);

    standardFunctionNameStep.addFunction(function);
  }

  @Override
  public final void addKeyword(KeywordName keyword) {
    generatedStyleSheetStep.addKeyword(keyword);
    keywordClassStep.addKeyword(keyword);
    keywordsClassStep.addKeyword(keyword);
  }

  @Override
  public final void addLengthUnit(String unit) {
    generatedStyleSheetStep.addLengthUnit(unit);
    lengthUnitStep.addLengthUnit(unit);
  }

  @Override
  public final void addMethodSignature(
      FunctionOrProperty property, MethodSignature signature) {
    generatedStyleSheetStep.addMethodSignature(property, signature);
  }

  @Override
  public final void addPrimitiveType(PrimitiveType type) {
    primitiveValueStep.addPrimitiveType(type);
  }

  @Override
  public final void addProperty(Property property) {
    generatedStyleSheetStep.addProperty(property);

    standardPropertyNameStep.addProperty(property);
  }

  @Override
  public final void addPseudoClass(String name) {
    generatedStyleSheetStep.addPseudoClass(name);

    pseudoClassSelectorsGen.addPseudoClass(name);
  }

  @Override
  public final void addPseudoElement(String name) {
    generatedStyleSheetStep.addPseudoElement(name);

    pseudoElementSelectorsGen.addPseudoElement(name);
  }

  @Override
  public final void addValueType(ValueType type) {
    valueTypeIfaceStep.addValueType(type);
  }

  @Override
  public final void execute() {
    angleUnitStep.execute();

    generatedColorStep.execute();

    generatedStyleSheetStep.execute();

    keywordsClassStep.execute();

    lengthUnitStep.execute();

    pseudoClassSelectorsGen.execute();

    pseudoElementSelectorsGen.execute();

    standardFunctionNameStep.execute();

    standardPropertyNameStep.execute();

    typeSelectorsGen.execute();
  }

}
