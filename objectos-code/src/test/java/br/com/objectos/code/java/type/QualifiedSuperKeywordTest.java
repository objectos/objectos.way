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
package br.com.objectos.code.java.type;

import static br.com.objectos.code.java.type.NamedTypes.t;

import br.com.objectos.code.java.element.SuperKeywordTest;
import br.com.objectos.code.util.AbstractCodeJavaTest;
import org.testng.annotations.Test;

public class QualifiedSuperKeywordTest extends AbstractCodeJavaTest {

  @Test
  public void fieldAccess() {
    test(
        t(SuperKeywordTest.class)._super().id("FIELD"),
        "br.com.objectos.code.java.element.SuperKeywordTest.super.FIELD"
    );
  }
  
  @Test
  public void methodInvocation() {
    test(
        t(SuperKeywordTest.class)._super().invoke("method"),
        "br.com.objectos.code.java.element.SuperKeywordTest.super.method()"
    );
  }

}
