/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.html.rec;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;

import objectos.html.AttributeName;
import objectos.html.ElementName;
import objectos.html.rec.AttributeMarkup;
import objectos.html.rec.ElementMarkup;
import objectos.html.rec.Instruction;
import org.testng.annotations.Test;

public class RootTest {

  @Test
  public void testCase01() {
    final Root subject;
    subject = Root.create(1);

    final AttributeMarkup attr;
    attr = AttributeInstruction.of(AttributeName.LANG, "pt-BR");

    assertSame(subject.add(attr), attr);

    final Instruction[] res;
    res = subject.compile();

    assertEquals(res.length, 1);
    assertEquals(res[0], attr);
  }

  @Test
  public void testCase02() {
    final Root subject;
    subject = Root.create(1);

    final AttributeMarkup attr;
    attr = AttributeInstruction.of(AttributeName.LANG, "pt-BR");

    assertSame(subject.add(attr), attr);

    final ElementMarkup elem;
    elem = ElementInstruction.of(ElementName.HTML, attr);

    assertSame(subject.add(elem), elem);

    final Instruction[] res;
    res = subject.compile();

    assertEquals(res.length, 1);
    assertEquals(res[0], elem);
  }

}
