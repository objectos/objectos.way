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
package objectox.html;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class ByteArrayTest {

  @Test
  public void add03() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.add((byte) 1, (byte) 2, (byte) 3);

    assertEquals(subject, ByteArray.of(1, 2, 3));
    assertEquals(subject.toString(), "010203");
  }

  @Test
  public void add04() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.add((byte) 1, (byte) 2, (byte) 3, (byte) 4);

    assertEquals(subject, ByteArray.of(1, 2, 3, 4));
    assertEquals(subject.toString(), "01020304");
  }

  @Test
  public void add05() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.add((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5);

    assertEquals(subject, ByteArray.of(1, 2, 3, 4, 5));
    assertEquals(subject.toString(), "0102030405");
  }

  @Test
  public void add06() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.add((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5, (byte) 6);

    assertEquals(subject, ByteArray.of(1, 2, 3, 4, 5, 6));
    assertEquals(subject.toString(), "010203040506");
  }

}
