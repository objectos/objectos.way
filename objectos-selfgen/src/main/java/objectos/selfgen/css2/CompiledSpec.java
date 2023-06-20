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
package objectos.selfgen.css2;

import java.io.IOException;
import java.util.Collection;
import objectos.code.JavaSink;

abstract class CompiledSpec {

  abstract Collection<KeywordName> keywords();

  abstract LengthType lengthType();

  abstract PercentageType percentageType();

  abstract Collection<Property> properties();

  abstract Collection<SelectorName> selectors();

  abstract StringType stringType();

  abstract UrlType urlType();

  abstract Collection<ValueType> valueTypes();

  final void write(JavaSink sink, ThisTemplate template) throws IOException {
    template.write(sink, this);
  }

  abstract ZeroType zeroType();

}