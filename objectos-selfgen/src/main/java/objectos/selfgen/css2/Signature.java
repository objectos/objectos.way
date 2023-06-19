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

import objectos.code.tmpl.TypeName;

abstract class Signature {

  static final class Custom2 extends Signature {

    private final ParameterType type1;
    private final ParameterType type2;

    public Custom2(ParameterType type1, ParameterType type2) {
      this.type1 = type1;
      this.type2 = type2;
    }

    @Override
    final Style style() { return Style.CUSTOM2; }

    @Override
    final TypeName typeName1() { return type1.typeName(); }

    final TypeName typeName2() { return type2.typeName(); }

  }

  static final class Custom3 extends Signature {

    private final ParameterType type1;
    private final ParameterType type2;
    private final ParameterType type3;

    public Custom3(ParameterType type1, ParameterType type2, ParameterType type3) {
      this.type1 = type1;
      this.type2 = type2;
      this.type3 = type3;
    }

    @Override
    final Style style() { return Style.CUSTOM3; }

    @Override
    final TypeName typeName1() { return type1.typeName(); }

    final TypeName typeName2() { return type2.typeName(); }

    final TypeName typeName3() { return type3.typeName(); }

  }

  static final class Standard extends Signature {

    private final Style style;

    private final ParameterType type;

    public Standard(Style style, ParameterType type) {
      this.style = style;
      this.type = type;
    }

    @Override
    final Style style() { return style; }

    @Override
    final TypeName typeName1() { return type.typeName(); }

  }

  abstract Style style();

  abstract TypeName typeName1();

}