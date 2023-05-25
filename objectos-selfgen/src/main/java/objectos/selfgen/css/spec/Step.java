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

public interface Step {

  void addAngleUnit(String unit);

  void addColorName(ColorName colorName);

  void addElementName(String elementName);

  void addFunction(FunctionName function);

  void addKeyword(KeywordName keyword);

  void addLengthUnit(String unit);

  void addMethodSignature(FunctionOrProperty property, MethodSignature signature);

  void addPrimitiveType(PrimitiveType type);

  void addProperty(Property property);

  void addPseudoClass(String name);

  void addPseudoElement(String name);

  void addValueType(ValueType type);

  void execute();

}