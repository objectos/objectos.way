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

import org.testng.annotations.Test;

public class BytePlayerTest {

  @Test
  public void next() {
    final BytePlayer subject;
    subject = new BytePlayer(
        new byte[] {0, 1, 2, 3, 4, 5},

        1,

        4
    );

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), 1);
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), 2);
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), 3);
    assertEquals(subject.hasNext(), false);
  }

  @Test
  public void peek() {
    final BytePlayer subject;
    subject = new BytePlayer(
        new byte[] {0, 1, 2, 3, 4, 5},

        1,

        4
    );

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.peek(), 1);
    assertEquals(subject.peek(), 1);
    assertEquals(subject.next(), 1);
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.peek(), 2);
  }

  @Test
  public void skip() {
    final BytePlayer subject;
    subject = new BytePlayer(
        new byte[] {0, 1, 2, 3, 4, 5},

        1,

        4
    );

    subject.skip(1);

    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), 2);
    assertEquals(subject.hasNext(), true);
    assertEquals(subject.next(), 3);
    assertEquals(subject.hasNext(), false);
  }

}
