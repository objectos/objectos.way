/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.lang;

import java.io.IOException;

public class TestingCharWritable implements CharWritable {

  private static final int LINE_LENGTH = 50;

  private static final String FULL_LINE = "..........".repeat(4).concat(".........\n");

  final int length;

  public TestingCharWritable(int length) {
    this.length = length;
  }

  public static TestingCharWritable ofLength(int length) {
    return new TestingCharWritable(length);
  }

  @Override
  public final String toString() {
    try {
      StringBuilder sb;
      sb = new StringBuilder();

      writeTo(sb);

      return sb.toString();
    } catch (IOException e) {
      throw new AssertionError("StringBuilder should not have thrown", e);
    }
  }

  @Override
  public final void writeTo(Appendable dest) throws IOException {
    if (length == 0) {
      return;
    }

    int count = 0;

    int fullLines = length / LINE_LENGTH;

    for (int fullLine = 0; fullLine < fullLines; fullLine++) {
      dest.append(FULL_LINE);

      count += LINE_LENGTH;
    }

    int digit = 1;

    for (; count < length; count++) {
      int print;
      print = digit % 10;

      dest.append(Integer.toString(print));

      digit++;
    }
  }

}