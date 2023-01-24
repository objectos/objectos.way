/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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
package objectos.code;

import static org.testng.Assert.assertEquals;

import java.util.List;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class AutoImportsTest {

  private final AutoImports autoImports = new AutoImports();

  @BeforeMethod
  public void _beforeMethod() {
    autoImports.clear();
  }

  @Test(description = "single type")
  public void testCase01() {
    autoImports.enable();
    autoImports.packageName("com.example");

    assertEquals(t("java.lang", "String"), 1);
    assertEquals(t("java.io", "InputStream"), 1);

    assertEquals(
      List.copyOf(autoImports.types()),

      List.of("java.io.InputStream")
    );
  }

  private int t(String packageName, String... names) {
    autoImports.classTypePackageName(packageName);

    for (var name : names) {
      autoImports.classTypeSimpleName(name);
    }

    return autoImports.classTypeInstruction();
  }

}
