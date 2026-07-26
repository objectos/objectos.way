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

public class CtxTest {

  @Test(description = "true when code has bytes")
  public void hasByte01() {
    final Ctx subject;
    subject = CtxY.create(opts -> {
      opts.addByte((byte) 42);
    });

    assertEquals(subject.hasByte(), true);
  }

  @Test(description = "false when code is empty")
  public void hasByte02() {
    final Ctx subject;
    subject = CtxY.create(_ -> {});

    assertEquals(subject.hasByte(), false);
  }

  @Test(description = "iterate over code bytes")
  public void nextByte01() {
    final Ctx subject;
    subject = CtxY.create(opts -> {
      opts.addByte((byte) 1);
      opts.addByte((byte) 2);
      opts.addByte((byte) 3);
    });

    assertEquals(subject.hasByte(), true);
    assertEquals(subject.nextByte(), 1);
    assertEquals(subject.hasByte(), true);
    assertEquals(subject.nextByte(), 2);
    assertEquals(subject.hasByte(), true);
    assertEquals(subject.nextByte(), 3);
    assertEquals(subject.hasByte(), false);
  }

  @Test(description = "push and pop")
  public void push01() {
    final Ctx subject;
    subject = CtxY.create(opts -> {
      opts.addByte((byte) 1);
      opts.addByte((byte) 2);
      opts.addByte((byte) 3);
    });

    assertEquals(subject.hasByte(), true);
    assertEquals(subject.nextByte(), 1);
    assertEquals(subject.hasByte(), true);
    assertEquals(subject.nextByte(), 2);

    subject.push(0);

    assertEquals(subject.hasByte(), true);
    assertEquals(subject.nextByte(), 1);

    subject.pop();

    assertEquals(subject.hasByte(), true);
    assertEquals(subject.nextByte(), 3);
    assertEquals(subject.hasByte(), false);
  }

}
