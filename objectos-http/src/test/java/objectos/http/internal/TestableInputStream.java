/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.http.internal;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

final class TestableInputStream extends InputStream {

  public static final TestableInputStream EMPTY = new TestableInputStream(new Object[] {});

  static final Object THROW = new Object() {};

  private final Object[] data;

  private int index;

  TestableInputStream(Object[] data) {
    this.data = data;
  }

  public static TestableInputStream of(Object... data) {
    // we assume this is safe in a testing env...
    return new TestableInputStream(data);
  }

  @Override
  public final int read() throws IOException {
    // not used directly by HttpExchange
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final int read(byte[] b, int off, int len) throws IOException {
    if (index == data.length) {
      return -1;
    }

    Object next;
    next = data[index++];

    if (next instanceof String s) {
      next = s.getBytes(StandardCharsets.UTF_8);
    }

    if (next instanceof byte[] bytes) {
      if (bytes.length > len) {
        // we cannot write the whole test data into the buffer
        throw new AssertionError("""
        Data @ index=%d too large. Please split your test data.
        """.formatted(index - 1));
      }

      System.arraycopy(bytes, 0, b, off, bytes.length);

      return bytes.length;
    } else if (next instanceof IOException ioe) {
      throw ioe;
    } else {
      throw new UnsupportedOperationException(
        "Implement me :: type=" + next.getClass()
      );
    }
  }

}