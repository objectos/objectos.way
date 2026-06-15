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
package objectos.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import objectos.lang.BinaryObject;

public final class ContentY {

  private static final int LINE_LENGTH = 50;

  private static final String FULL_LINE = "..........".repeat(4).concat(".........\n");

  private ContentY() {}

  private record Chunked(int length) implements BinaryObject {
    @Override
    public final void binaryTo(OutputStream out) throws IOException {
      if (length == 0) {
        return;
      }

      final OutputStreamWriter writer;
      writer = new OutputStreamWriter(out, StandardCharsets.UTF_8);

      int count;
      count = 0;

      final int fullLines;
      fullLines = length / LINE_LENGTH;

      for (int fullLine = 0; fullLine < fullLines; fullLine++) {
        writer.append(FULL_LINE);

        count += LINE_LENGTH;
      }

      int digit = 1;

      for (; count < length; count++) {
        int print;
        print = digit % 10;

        final String s;
        s = Integer.toString(print);

        writer.append(s);

        digit++;
      }

      writer.flush();
    }

    @Override
    public final String toString() {
      try {
        final ByteArrayOutputStream out;
        out = new ByteArrayOutputStream();

        binaryTo(out);

        final byte[] bytes;
        bytes = out.toByteArray();

        return new String(bytes, StandardCharsets.UTF_8);
      } catch (IOException e) {
        throw new AssertionError("ByteArrayOutputStream does not throw", e);
      }
    }
  }

  public static Content chunked(MediaType contentType, int length) {
    final Chunked chunked;
    chunked = new Chunked(length);

    return Content.of(contentType, chunked);
  }

}
