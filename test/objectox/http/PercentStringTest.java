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
package objectox.http;

import static org.testng.Assert.assertEquals;

import objectos.internal.Util;
import org.testng.annotations.Test;

public class PercentStringTest {

  private final byte[] start = Util.EMPTY_BYTE_ARRAY;

  @Test
  public void append01() {
    final PercentString subject;
    subject = new PercentString(start);

    subject.append('a');
    subject.append('b');
    subject.append('c');

    assertEquals(subject.toString(), "abc");
  }

  @Test
  public void encode01() {
    final PercentString subject;
    subject = new PercentString(start);

    subject.encode((byte) 0x20);

    assertEquals(subject.toString(), "%20");
  }

  @Test
  public void encode02() {
    final PercentString subject;
    subject = new PercentString(start);

    subject.encode((byte) 0x20, (byte) 0x21);

    assertEquals(subject.toString(), "%20%21");
  }

  @Test
  public void encode03() {
    final PercentString subject;
    subject = new PercentString(start);

    subject.encode((byte) 0x20, (byte) 0x21, (byte) 0x22);

    assertEquals(subject.toString(), "%20%21%22");
  }

  @Test
  public void encode04() {
    final PercentString subject;
    subject = new PercentString(start);

    subject.encode((byte) 0x20, (byte) 0x21, (byte) 0x22, (byte) 0x23);

    assertEquals(subject.toString(), "%20%21%22%23");
  }

  @Test
  public void toString01() {
    final PercentString subject;
    subject = new PercentString(start);

    assertEquals(subject.toString(), "");
  }

}
