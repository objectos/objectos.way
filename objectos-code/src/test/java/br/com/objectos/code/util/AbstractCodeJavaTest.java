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
package br.com.objectos.code.util;

import br.com.objectos.code.java.declaration.PackageName;
import br.com.objectos.code.java.declaration.PackageNameFake;
import java.io.Closeable;
import java.io.InputStream;

public abstract class AbstractCodeJavaTest extends AbstractCodeCoreTest {

  protected static final PackageName TESTING_CODE = PackageNameFake.TESTING_CODE;
  protected static final PackageName TESTING_OTHER = PackageNameFake.TESTING_OTHER;

  protected abstract class Empty {}

  protected abstract class Generic<//
      U /* U meaning Unbounded */, //
      B extends InputStream /* B meaning Bounded */, //
      I extends InputStream & Closeable /* I as Intersection */> {
    Generic() {}
  }

  protected final void test(Object el, String... lines) {
    testToString(el, lines);
  }

}