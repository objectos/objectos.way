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
package br.com.objectos.http;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import objectos.util.UnmodifiableList;

public final class ByteArrayReadableByteChannel implements ReadableByteChannel {

  private final byte[] byteArray;

  private int index;

  ByteArrayReadableByteChannel(byte[] byteArray) {
    this.byteArray = byteArray;
  }

  public static ByteArrayReadableByteChannel ofLines(UnmodifiableList<String> lines) {
    String text;
    text = lines.join(Http.CRLF);

    byte[] bytes;
    bytes = text.getBytes();

    return new ByteArrayReadableByteChannel(bytes);
  }

  public static ByteArrayReadableByteChannel ofLines(String... lines) {
    UnmodifiableList<String> list;
    list = UnmodifiableList.copyOf(lines);

    return ofLines(list);
  }

  @Override
  public void close() throws IOException {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public boolean isOpen() {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final int read(ByteBuffer dst) throws IOException {
    int remaining;
    remaining = dst.remaining();

    int bytesToPut;
    bytesToPut = Math.min(byteArray.length - index, remaining);

    if (bytesToPut == 0) {
      return -1;
    }

    dst.put(byteArray, index, bytesToPut);

    index += bytesToPut;

    return bytesToPut;
  }

}
