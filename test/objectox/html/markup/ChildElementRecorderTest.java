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
package objectox.html.markup;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class ChildElementRecorderTest {

  @Test(description = "ELEM16")
  public void element16() {
    final ElementPointer child;
    child = new ElementPointer(HtmlSink.MAX_INT16);

    assertEquals(
        record(child),

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.ELEM16);
          opts.addInt16(HtmlSink.MAX_INT16);
        })
    );
  }

  @Test(description = "ELEM24")
  public void element24() {
    final ElementPointer child;
    child = new ElementPointer(HtmlSink.MAX_INT24);

    assertEquals(
        record(child),

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.ELEM24);
          opts.addInt24(HtmlSink.MAX_INT24);
        })
    );
  }

  @Test(description = "ELEM8")
  public void element8() {
    final ElementPointer child;
    child = new ElementPointer(32);

    assertEquals(
        record(child),

        HtmlSinkY.create(opts -> {
          opts.addByte(HtmlBytes.ELEM8);
          opts.addInt8(32);
        })
    );
  }

  @Test(
      description = "reject negative value",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Illegal int value: negative values \\(-1\\) are now allowed")
  public void reject01() {
    final ElementPointer child;
    child = new ElementPointer(-1);

    record(child);
  }

  @Test(
      description = "reject value that is too large",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Illegal int value: \\(16777216\\) is too large")
  public void reject02() {
    final ElementPointer child;
    child = new ElementPointer(HtmlSink.MAX_INT24 + 1);

    record(child);
  }

  private HtmlSink record(ElementPointer child) {
    final HtmlSink sink;
    sink = new HtmlSink();

    final ChildElementRecorder subject;
    subject = new ChildElementRecorder(sink);

    subject.record(child);

    return sink;
  }

}
