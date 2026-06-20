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
package objectox.http;

import java.nio.charset.StandardCharsets;
import objectos.internal.Util;

final class PercentString {

  private byte[] bytes;

  private int bytesIndex;

  PercentString(byte[] bytes) {
    this.bytes = bytes;
  }

  public static PercentString of(int length, int offset) {
    final int worstCaseChars;
    worstCaseChars = length - offset;

    final int initialBytesLength;
    initialBytesLength = (offset + 1) + (worstCaseChars * 3);

    final byte[] bytes;
    bytes = new byte[initialBytesLength];

    return new PercentString(bytes);
  }

  public final void append(char value) {
    ensureBytes(1);

    bytes[bytesIndex++] = (byte) value;
  }

  public final void encode(int b0) {
    ensureBytes(3);

    percent(b0);
  }

  public final void encode(int b0, int b1) {
    ensureBytes(6);

    percent(b0);
    percent(b1);
  }

  public final void encode(int b0, int b1, int b2) {
    ensureBytes(9);

    percent(b0);
    percent(b1);
    percent(b2);
  }

  public final void encode(int b0, int b1, int b2, int b3) {
    ensureBytes(12);

    percent(b0);
    percent(b1);
    percent(b2);
    percent(b3);
  }

  @Override
  public final String toString() {
    return new String(bytes, 0, bytesIndex, StandardCharsets.US_ASCII);
  }

  private void ensureBytes(int requiredLength) {
    final int requiredIndex;
    requiredIndex = bytesIndex + requiredLength - 1;

    bytes = Util.growIfNecessary(bytes, requiredIndex);
  }

  private byte hexDigit(int nibble) {
    return (byte) (nibble < 10 ? '0' + nibble : 'A' + (nibble - 10));
  }

  private void percent(int value) {
    // value is < 256
    bytes[bytesIndex++] = '%';
    bytes[bytesIndex++] = hexDigit(value >> 4);
    bytes[bytesIndex++] = hexDigit(value & 0b1111);
  }

}
