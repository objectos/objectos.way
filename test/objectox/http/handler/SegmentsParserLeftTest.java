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
package objectox.http.handler;

import static org.testng.Assert.assertEquals;

import java.util.List;
import org.testng.annotations.Test;

public class SegmentsParserLeftTest {

  @Test(description = "/")
  public void execute01() {
    final SegmentsParser ctx;
    ctx = new SegmentsParser("/", 0);

    final SegmentsParserLeft subject;
    subject = new SegmentsParserLeft(ctx);

    subject.execute();

    assertEquals(ctx.index(), 1);
    assertEquals(ctx.segments(), List.of(new SegmentExact("/")));
    assertEquals(ctx.stop(), true);
  }

  @Test(description = "/foo")
  public void execute02() {
    final SegmentsParser ctx;
    ctx = new SegmentsParser("/foo", 0);

    final SegmentsParserLeft subject;
    subject = new SegmentsParserLeft(ctx);

    subject.execute();

    assertEquals(ctx.index(), 4);
    assertEquals(ctx.segments(), List.of(new SegmentExact("/foo")));
    assertEquals(ctx.stop(), true);
  }

  @Test(description = "/{foo}.html")
  public void execute03() {
    final SegmentsParser ctx;
    ctx = new SegmentsParser("/{foo}.html", 7);

    final SegmentsParserLeft subject;
    subject = new SegmentsParserLeft(ctx);

    subject.execute();

    assertEquals(ctx.index(), 11);
    assertEquals(ctx.segments(), List.of(new SegmentExact("html")));
    assertEquals(ctx.stop(), true);
  }

  @Test(description = "/{a}")
  public void execute04() {
    final SegmentsParser ctx;
    ctx = new SegmentsParser("/{a}", 0);

    final SegmentsParserLeft subject;
    subject = new SegmentsParserLeft(ctx);

    subject.execute();

    assertEquals(ctx.index(), 2);
    assertEquals(ctx.segments(), List.of(new SegmentRegion("/")));
    assertEquals(ctx.stop(), false);
  }

}
