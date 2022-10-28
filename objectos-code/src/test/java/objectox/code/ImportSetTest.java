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

import static org.testng.Assert.assertEquals;

import objectos.code.JavaWriter;
import objectos.code.TypeName;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ImportSetTest {

  private final ImportSet importSet = new ImportSet();

  private final JavaWriter writer = new JavaWriter();

  @BeforeMethod
  public void _beforeMethod() {
    importSet.clear();

    writer.compilationUnitStart();
  }

  @Test
  public void voidReturnType() {
    var type = TypeName.VOID;

    type.acceptClassNameSet(importSet);

    importSet.execute(writer, type);

    assertEquals(writer.toString(), "void");
  }

}