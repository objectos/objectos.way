/*
 * Copyright (C) 2022 Objectos Software LTDA.
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
package objectos.lang;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class StringConverterTest {

  @Test
  public void multiple() {
    StringConverter c;
    c = StringConverter.create(
        StringConversion.trim(),
        StringConversion.removeRange('1', '7')
    );

    test(c, "", "");
    test(c, " a ", "a");
    test(c, "  0123456789   ", "089");
  }

  @Test
  public void removeRange() {
    StringConverter c;
    c = StringConverter.create(
        StringConversion.removeRange('1', '7')
    );

    test(c, "", "");
    test(c, "a", "a");
    test(c, "0123456789", "089");
  }

  @Test
  public void toJavaIdentifier() {
    StringConverter c;
    c = StringConverter.create(
        StringConversion.toJavaIdentifier()
    );

    test(c, "", "");
    test(c, "SimPLES", "SimPLES");
    test(c, "$var", "$var");
    test(c, "@var", "var");
    test(c, "line-height", "lineheight");
    test(c, "Some Thing", "SomeThing");
    test(c, "Some thing", "Something");
  }

  @Test
  public void toJavaLowerCamelCase() {
    StringConverter c;
    c = StringConverter.create(
        StringConversion.toJavaLowerCamelCase()
    );

    test(c, "", "");
    test(c, "SimPLES", "simPLES");
    test(c, "$var", "$var");
    test(c, "@var", "var");
    test(c, "line-height", "lineHeight");
    test(c, "Some Thing", "someThing");
    test(c, "Some thing", "someThing");
  }

  @Test
  public void toJavaUpperCamelCase() {
    StringConverter c;
    c = StringConverter.create(
        StringConversion.toJavaUpperCamelCase()
    );

    test(c, "", "");
    test(c, "SimPLES", "SimPLES");
    test(c, "$var", "$var");
    test(c, "@var", "Var");
    test(c, "line-height", "LineHeight");
    test(c, "Some Thing", "SomeThing");
    test(c, "Some thing", "SomeThing");
  }

  @Test
  public void trim() {
    StringConverter c;
    c = StringConverter.create(
        StringConversion.trim()
    );

    test(c, "", "");
    test(c, "a", "a");
    test(c, " a", "a");
    test(c, " a ", "a");
    test(c, "a ", "a");
    test(c, "a x", "a x");
    test(c, "a     x", "a     x");
    test(c, "a     x ", "a     x");
    test(c, "     a     x            ", "a     x");
  }

  private void test(StringConverter c, String s, String e) {
    String r;
    r = c.convert(s);

    assertEquals(r, e);
  }

}