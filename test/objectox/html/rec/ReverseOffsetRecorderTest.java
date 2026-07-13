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
package objectox.html.rec;

import static org.testng.Assert.assertEquals;

import objectox.html.ByteArray;
import objectox.html.HtmlByteProto;
import objectox.html.HtmlBytes;
import org.testng.annotations.Test;

public class ReverseOffsetRecorderTest {

  @Test(description = "offset <= 127")
  public void record01() {
    final ByteArray main;
    main = ByteArray.of(new int[127]);

    assertEquals(main.size(), 127);

    final ReverseOffsetRecorder subject;
    subject = new ReverseOffsetRecorder(main);

    subject.record(0);

    assertEquals(
        new byte[] {
            main.get(126),
            main.get(127),
            main.get(128),
            main.get(129)
        },

        new byte[] {
            0,
            HtmlByteProto.END,
            HtmlBytes.encodeInt0(127),
            HtmlByteProto.INTERNAL
        }
    );
  }

  @Test(description = "offset <= 16383")
  public void record02() {
    final ByteArray main;
    main = ByteArray.of(new int[128]);

    assertEquals(main.size(), 128);

    final ReverseOffsetRecorder subject;
    subject = new ReverseOffsetRecorder(main);

    subject.record(0);

    assertEquals(
        new byte[] {
            main.get(127),
            main.get(128),
            main.get(129),
            main.get(130),
            main.get(131)
        },

        new byte[] {
            0,
            HtmlByteProto.END,
            HtmlBytes.encodeVarintHigh(128, 7),
            HtmlBytes.encodeVarint(128, 0),
            HtmlByteProto.INTERNAL
        }
    );
  }

  @Test(description = "offset <= 16383")
  public void record03() {
    final ByteArray main;
    main = ByteArray.of(new int[16383]);

    assertEquals(main.size(), 16383);

    final ReverseOffsetRecorder subject;
    subject = new ReverseOffsetRecorder(main);

    subject.record(0);

    assertEquals(
        new byte[] {
            main.get(16382),
            main.get(16383),
            main.get(16384),
            main.get(16385),
            main.get(16386)
        },

        new byte[] {
            0,
            HtmlByteProto.END,
            HtmlBytes.encodeVarintHigh(16383, 7),
            HtmlBytes.encodeVarint(16383, 0),
            HtmlByteProto.INTERNAL
        }
    );
  }

  @Test(description = "offset <= 2097151")
  public void record04() {
    final ByteArray main;
    main = ByteArray.of(new int[16384]);

    assertEquals(main.size(), 16384);

    final ReverseOffsetRecorder subject;
    subject = new ReverseOffsetRecorder(main);

    subject.record(0);

    assertEquals(
        new byte[] {
            main.get(16383),
            main.get(16384),
            main.get(16385),
            main.get(16386),
            main.get(16387),
            main.get(16388)
        },

        new byte[] {
            0,
            HtmlByteProto.END,
            HtmlBytes.encodeVarintHigh(16384, 14),
            HtmlBytes.encodeVarint(16384, 7),
            HtmlBytes.encodeVarint(16384, 0),
            HtmlByteProto.INTERNAL
        }
    );
  }

  @Test(description = "offset <= 2097151")
  public void record05() {
    final ByteArray main;
    main = ByteArray.of(new int[2097151]);

    assertEquals(main.size(), 2097151);

    final ReverseOffsetRecorder subject;
    subject = new ReverseOffsetRecorder(main);

    subject.record(0);

    assertEquals(
        new byte[] {
            main.get(2097150),
            main.get(2097151),
            main.get(2097152),
            main.get(2097153),
            main.get(2097154),
            main.get(2097155)
        },

        new byte[] {
            0,
            HtmlByteProto.END,
            HtmlBytes.encodeVarintHigh(2097151, 14),
            HtmlBytes.encodeVarint(2097151, 7),
            HtmlBytes.encodeVarint(2097151, 0),
            HtmlByteProto.INTERNAL
        }
    );
  }

  @Test(
      description = "too large",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "HTML is too large to record: offset=2097152")
  public void record06() {
    final ByteArray main;
    main = ByteArray.of(new int[2097152]);

    assertEquals(main.size(), 2097152);

    final ReverseOffsetRecorder subject;
    subject = new ReverseOffsetRecorder(main);

    subject.record(0);
  }

}
