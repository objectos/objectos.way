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

public class TapeTest {

  @Test
  public void nextByte() {
    final Tape subject;
    subject = TapeY.create(opts -> {
      opts.main = new byte[] {0, 1, 2, 3};

      opts.mainIndex = 1;
    });

    assertEquals(subject.hasByte(), true);
    assertEquals(subject.nextByte(), 1);
    assertEquals(subject.hasByte(), true);
    assertEquals(subject.nextByte(), 2);
    assertEquals(subject.hasByte(), true);
    assertEquals(subject.nextByte(), 3);
    assertEquals(subject.hasByte(), false);
  }

  @Test
  public void peekByte() {
    final Tape subject;
    subject = TapeY.create(opts -> {
      opts.main = new byte[] {0, 1, 2, 3};

      opts.mainIndex = 1;
    });

    assertEquals(subject.hasByte(), true);
    assertEquals(subject.peekByte(), 1);
    assertEquals(subject.peekByte(), 1);
    assertEquals(subject.nextByte(), 1);
    assertEquals(subject.hasByte(), true);
    assertEquals(subject.peekByte(), 2);
  }

  @Test
  public void skip() {
    final Tape subject;
    subject = TapeY.create(opts -> {
      opts.main = new byte[] {0, 1, 2, 3};

      opts.mainIndex = 1;
    });

    subject.skip(1);

    assertEquals(subject.hasByte(), true);
    assertEquals(subject.nextByte(), 2);
    assertEquals(subject.hasByte(), true);
    assertEquals(subject.nextByte(), 3);
    assertEquals(subject.hasByte(), false);
  }

}
