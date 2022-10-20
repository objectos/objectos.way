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
package objectox.code;

import objectos.code.ClassName;
import objectos.code.JavaTemplate;
import objectos.code.PackageName;
import org.testng.annotations.BeforeClass;

public abstract class AbstractObjectoxCodeTest {

  static final PackageName TEST = PackageName.of("test");

  private final ObjectosCodeTest outer;

  AbstractObjectoxCodeTest(ObjectosCodeTest outer) {
    this.outer = outer;
  }

  @BeforeClass
  public void _beforeClass() {
    outer._beforeClass();
  }

  final ImportSet imports(PackageName packageName, ClassName... names) {
    var set = new ImportSet();

    set.packageName(packageName);

    for (var name : names) {
      set.addClassName(name);
    }

    set.sort();

    return set;
  }

  final Object[] objs(Object... values) { return values; }

  final int[] pass0(int... values) { return values; }

  final int[] pass1(int... values) { return values; }

  final void test(
      JavaTemplate template,
      int[] pass0,
      Object[] objs,
      int[] pass1,
      ImportSet imports,
      String expectedSource) {
    outer.test(template, pass0, objs, pass1, imports, expectedSource);
  }

}