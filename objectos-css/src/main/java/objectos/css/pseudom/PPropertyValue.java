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
package objectos.css.pseudom;

import objectos.css.internal.Keyword;
import objectos.css.internal.PDoubleValueImpl;
import objectos.css.internal.PIntValueImpl;
import objectos.css.internal.PLengthIntValueImpl;
import objectos.css.internal.PStringValueImpl;
import objectos.css.tmpl.LengthUnit;

public sealed interface PPropertyValue {

  sealed interface PDoubleValue extends PPropertyValue permits PDoubleValueImpl {
    double doubleValue();
  }

  sealed interface PIntValue extends PPropertyValue permits PIntValueImpl {
    int intValue();
  }

  sealed interface PKeyword extends PPropertyValue permits Keyword {
    String keywordName();
  }

  sealed interface PLengthIntValue extends PPropertyValue permits PLengthIntValueImpl {
    LengthUnit unit();
    int value();
  }

  sealed interface PStringValue extends PPropertyValue permits PStringValueImpl {
    String value();
  }

}