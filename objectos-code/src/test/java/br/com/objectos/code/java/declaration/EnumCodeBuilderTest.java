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
package br.com.objectos.code.java.declaration;

import br.com.objectos.code.java.type.NamedClass;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import objectos.util.UnmodifiableList;
import org.testng.annotations.Test;

public class EnumCodeBuilderTest extends AbstractCodeJavaTest {
  
  @Test
  public void implementedInterface() {
    NamedClass iface1 = TESTING_CODE.nestedClass("Iface1");
    NamedClass iface2 = TESTING_CODE.nestedClass("Iface2");
    test(
        EnumCode.builder()
            .addImplementedInterface(iface1)
            .build(),
        "enum Unnamed implements testing.code.Iface1 {}"
    );
    test(
        EnumCode.builder()
            .addImplementedInterface(iface1)
            .addImplementedInterface(iface2)
            .build(),
        "enum Unnamed implements testing.code.Iface1, testing.code.Iface2 {}"
    );
    test(
        EnumCode.builder()
            .addImplementedInterfaces(UnmodifiableList.of(iface2, iface1))
            .build(),
        "enum Unnamed implements testing.code.Iface2, testing.code.Iface1 {}"
    );
  }

}
