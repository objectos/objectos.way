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

import objectox.html.IntSize;
import org.testng.annotations.Test;

public class HtmlSourceTest {

  @Test(description = "decode 1 byte")
  public void getInt01() {
    final HtmlSource source;
    source = CtxY.create(sink -> {
      sink.addInt8(42);
    });

    final int res;
    res = source.getInt(0, IntSize.BIT8);

    assertEquals(res, 42);
  }

  @Test(description = "decode 1 byte max value")
  public void getInt02() {
    final HtmlSource source;
    source = CtxY.create(sink -> {
      sink.addInt8(255);
    });

    final int res;
    res = source.getInt(0, IntSize.BIT8);

    assertEquals(res, 255);
  }

  @Test(description = "decode 2 bytes")
  public void getInt03() {
    final HtmlSource source;
    source = CtxY.create(sink -> {
      sink.addInt16(12345);
    });

    final int res;
    res = source.getInt(0, IntSize.BIT16);

    assertEquals(res, 12345);
  }

  @Test(description = "decode 2 bytes max value")
  public void getInt04() {
    final HtmlSource source;
    source = CtxY.create(sink -> {
      sink.addInt16(65535);
    });

    final int res;
    res = source.getInt(0, IntSize.BIT16);

    assertEquals(res, 65535);
  }

  @Test(description = "decode 3 bytes")
  public void getInt05() {
    final HtmlSource source;
    source = CtxY.create(sink -> {
      sink.addInt24(987654);
    });

    final int res;
    res = source.getInt(0, IntSize.BIT24);

    assertEquals(res, 987654);
  }

  @Test(description = "decode 3 bytes max value")
  public void getInt06() {
    final HtmlSource source;
    source = CtxY.create(sink -> {
      sink.addInt24(16777215);
    });

    final int res;
    res = source.getInt(0, IntSize.BIT24);

    assertEquals(res, 16777215);
  }

}
