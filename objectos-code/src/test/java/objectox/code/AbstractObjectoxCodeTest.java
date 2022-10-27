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

import objectos.code.JavaGenerator;
import objectos.code.JavaTemplate;
import objectos.code.JavaWriter;
import objectos.code.PackageName;
import org.testng.annotations.BeforeClass;

public abstract class AbstractObjectoxCodeTest {

  static final PackageName TEST = PackageName.of("test");

  private JavaGenerator generator;

  private JavaWriter writer;

  @BeforeClass
  public void _beforeClass() {
    if (generator == null) {
      generator = JavaGenerator.of();

      writer = JavaWriter.of();
    }
  }

  protected final void testDefault(JavaTemplate template, String expected) {
    generator.render(template, writer);

    assertEquals(writer.toString(), expected);
  }

}