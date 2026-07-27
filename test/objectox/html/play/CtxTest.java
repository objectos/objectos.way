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

import objectos.html.play.HtmlEvent;
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

  @Test(description = "false when result is null")
  public void hasEvent01() {
    final Ctx subject;
    subject = CtxY.create(_ -> {});

    subject.result(null);

    assertEquals(subject.hasEvent(), false);
  }

  @Test(description = "true when result is set")
  public void hasEvent02() {
    final Ctx subject;
    subject = CtxY.create(_ -> {});

    subject.result(HtmlEventPojo.BEGIN_DOCUMENT);

    assertEquals(subject.hasEvent(), true);
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

  @Test(description = "returns the result and clears state")
  public void nextEvent01() {
    final Ctx subject;
    subject = CtxY.create(_ -> {});

    subject.result(HtmlEventPojo.BEGIN_DOCUMENT);

    assertEquals(subject.hasEvent(), true);

    final HtmlEvent res;
    res = subject.nextEvent();

    assertEquals(res, HtmlEventPojo.BEGIN_DOCUMENT);

    assertEquals(subject.hasEvent(), false);
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

  @Test(description = "sets the result")
  public void result01() {
    final Ctx subject;
    subject = CtxY.create(_ -> {});

    subject.result(HtmlEventPojo.BEGIN_DOCUMENT);

    assertEquals(subject.hasEvent(), true);
  }

  @Test(
      description = "throws when already set",
      expectedExceptions = IllegalStateException.class,
      expectedExceptionsMessageRegExp = "Result has not been consumed yet")
  public void result02() {
    final Ctx subject;
    subject = CtxY.create(_ -> {});

    subject.result(HtmlEventPojo.BEGIN_DOCUMENT);
    subject.result(HtmlEventPojo.END_DOCUMENT);
  }

  @Test(description = "true initially")
  public void shouldCompute01() {
    final Ctx subject;
    subject = CtxY.create(_ -> {});

    assertEquals(subject.shouldCompute(), true);
  }

  @Test(description = "false after result, true after nextEvent")
  public void shouldCompute02() {
    final Ctx subject;
    subject = CtxY.create(_ -> {});

    subject.result(HtmlEventPojo.BEGIN_DOCUMENT);

    assertEquals(subject.shouldCompute(), false);

    subject.nextEvent();

    assertEquals(subject.shouldCompute(), true);
  }

  @Test(description = "advances position")
  public void skip01() {
    final Ctx subject;
    subject = CtxY.create(opts -> {
      opts.addByte((byte) 1);
      opts.addByte((byte) 2);
      opts.addByte((byte) 3);
    });

    subject.skip(2);

    assertEquals(subject.nextByte(), 3);
  }

  @Test(description = "skip past end makes hasByte false")
  public void skip02() {
    final Ctx subject;
    subject = CtxY.create(opts -> {
      opts.addByte((byte) 1);
      opts.addByte((byte) 2);
    });

    subject.skip(2);

    assertEquals(subject.hasByte(), false);
  }

}
