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
package objectos.css.sheet;

import objectos.css.function.StandardFunctionName;
import objectos.css.property.StandardPropertyName;
import objectos.css.select.AttributeValueOperator;
import objectos.css.type.Creator;
import objectos.css.type.Marker;
import objectos.css.type.Value;
import objectos.util.UnmodifiableList;

public interface StyleEngine extends Creator, Marker {

  void addAtMedia(AtMediaElement... elements);

  void addDeclaration(StandardPropertyName name, double value);

  void addDeclaration(StandardPropertyName name, int value);

  void addDeclaration(StandardPropertyName name, MultiDeclarationElement... elements);

  void addDeclaration(StandardPropertyName name, String value);

  void addDeclaration(StandardPropertyName name, Value v1);

  void addDeclaration(StandardPropertyName name, Value v1, Value v2);

  void addDeclaration(StandardPropertyName name, Value v1, Value v2, Value v3);

  void addDeclaration(StandardPropertyName name, Value v1, Value v2, Value v3, Value v4);

  void addDeclaration(StandardPropertyName name, Value v1, Value v2, Value v3, Value v4, Value v5);

  void addDeclaration(
      StandardPropertyName name, Value v1, Value v2, Value v3, Value v4, Value v5, Value v6);

  void addFunction(StandardFunctionName name, Value v1);

  void addRule(RuleElement... elements);

  void addRule(UnmodifiableList<RuleElement> elements);

  void clearRulePrefix();

  void createAttributeSelector(String name);

  void createAttributeValueElement(AttributeValueOperator operator, String value);

  void createAttributeValueSelector(String name);

  void createClassSelector(String className);

  void createIdSelector(String id);

  void markAttributeValueElement();

  void setRulePrefix(RuleElement... elements);

}
