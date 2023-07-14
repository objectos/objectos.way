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
package objectos.css.internal;

import objectos.css.StyleSheet;
import objectos.css.om.PropertyName;
import objectos.css.om.PropertyValue;
import objectos.css.tmpl.StyleRuleElement;

public abstract class CssTemplateApi {

  public abstract void compilationStart();

  public abstract void declarationEnd();

  public abstract void declarationStart(PropertyName name);

  public abstract void declarationValue(PropertyValue value);

  public abstract void length(double value, LengthUnit unit);

  public abstract void length(int value, LengthUnit unit);

  public abstract void percentage(double value);

  public abstract void percentage(int value);

  public abstract void styleRuleElement(StyleRuleElement element);

  public abstract void styleRuleEnd();

  public abstract void styleRuleStart();

  public abstract void compilationEnd();

  public void optimize() {
    throw new UnsupportedOperationException();
  }

  public StyleSheet compile() {
    throw new UnsupportedOperationException();
  }

}
