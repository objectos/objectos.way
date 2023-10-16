/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.css.util;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import objectox.css.Utility01;
import org.testng.annotations.Test;

public class StylesGeneratorTest {

  @Test
  public void utility01() throws IOException {
    StylesGenerator generator;
    generator = StylesGenerator.of();

    generator.add(Utility01.class);

    assertEquals(
      generator.generate(),

      """
      %s {
        display: block;
      }
      """.formatted(Display.BLOCK.className())
    );
  }

}