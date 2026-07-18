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
package objectox.html.play;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertSame;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

public class EndDocumentStateTest {

  private final EndDocumentState subject = EndDocumentState.INSTANCE;

  @Test
  public void compute00() {
    assertEquals(subject.compute(), EndState.INSTANCE);
  }

  @Test
  public void hasNext() {
    assertTrue(subject.hasNext());
  }

  @Test
  public void next() {
    assertSame(subject.next(), subject);
  }

}
