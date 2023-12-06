/*
 * Copyright (C) 2020-2023 Objectos Software LTDA.
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
package objectos.git;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.zip.Deflater;
import objectos.fs.WritablePathName;
import objectos.util.array.ByteArrays;

final class ByteArrayWriter {

  private byte[] data;

  private int index;

  ByteArrayWriter() {
    data = new byte[32];
  }

  ByteArrayWriter(byte[] data) {
    this.data = data;
  }

  public final ByteBuffer asByteBuffer() {
    return ByteBuffer.wrap(data, 0, index);
  }

  public final ByteArrayWriter clear() {
    index = 0;

    return this;
  }

  public final ObjectId computeObjectId(MessageDigest messageDigest) {
    messageDigest.reset();

    messageDigest.update(data, 0, index);

    byte[] objectIdBytes;
    objectIdBytes = messageDigest.digest();

    return ObjectId.fromByteArray(objectIdBytes);
  }

  public final int deflate(Deflater deflater, ByteArrayWriter dataArray) {
    deflater.reset();

    deflater.setInput(data, 0, index);

    deflater.finish();

    return dataArray.deflate0(deflater, index);
  }

  public final long getLong(int offset) {
    long result;
    result = 0;

    result |= data[offset + 7] & 0xFF;

    result <<= 8;

    result |= data[offset + 6] & 0xFF;

    result <<= 8;

    result |= data[offset + 5] & 0xFF;

    result <<= 8;

    result |= data[offset + 4] & 0xFF;

    result <<= 8;

    result |= data[offset + 3] & 0xFF;

    result <<= 8;

    result |= data[offset + 2] & 0xFF;

    result <<= 8;

    result |= data[offset + 1] & 0xFF;

    result <<= 8;

    result |= data[offset + 0] & 0xFF;

    return result;
  }

  public final ObjectId getObjectId(int offset) {
    return null;
  }

  public final boolean isEmtpy() {
    return size() == 0;
  }

  public final boolean matches(byte[] bytes) {
    if (index > bytes.length) {
      return false;
    }

    if (data.length < bytes.length) {
      return false;
    }

    for (int i = 0; i < index; i++) {
      if (data[i] != bytes[i]) {
        return false;
      }
    }

    return true;
  }

  public final ObjectId parseObjectId() throws InvalidObjectIdFormatException {
    return ObjectId.parse(new CharSequence() {
      @Override
      public final char charAt(int index) {
        return (char) data[index];
      }

      @Override
      public final int length() {
        return index;
      }

      @Override
      public final CharSequence subSequence(int start, int end) {
        throw new UnsupportedOperationException();
      }
    }, 0);
  }

  public final void putInt(int offset, int value) {
    int i;
    i = offset;

    data[i++] = (byte) (value >>> 24);

    data[i++] = (byte) (value >>> 16);

    data[i++] = (byte) (value >>> 8);

    data[i++] = (byte) (value >>> 0);
  }

  public final void putLong(long value) {
    int requiredIndex;
    requiredIndex = index + 8;

    data = ByteArrays.growIfNecessary(data, requiredIndex);

    data[index++] = (byte) (value >>> 0);

    data[index++] = (byte) (value >>> 8);

    data[index++] = (byte) (value >>> 16);

    data[index++] = (byte) (value >>> 24);

    data[index++] = (byte) (value >>> 32);

    data[index++] = (byte) (value >>> 40);

    data[index++] = (byte) (value >>> 48);

    data[index++] = (byte) (value >>> 56);
  }

  public final int size() {
    return index;
  }

  public final byte[] toByteArray() {
    return Arrays.copyOf(data, index);
  }

  public final String toString(Charset charset) {
    return new String(data, 0, index, charset);
  }

  public final String toString(CharsetDecoder decoder) throws CharacterCodingException {
    ByteBuffer byteBuffer;
    byteBuffer = ByteBuffer.wrap(data, 0, index);

    CharBuffer decoded;
    decoded = decoder.decode(byteBuffer);

    return decoded.toString();
  }

  public final void update(MessageDigest messageDigest) {
    messageDigest.update(data, 0, index);
  }

  public final void write(byte value) {
    int newLength;
    newLength = index + 1;

    data = ByteArrays.growIfNecessary(data, newLength);

    data[index] = value;

    index = newLength;
  }

  public final void write(byte[] bytes) {
    int newLength;
    newLength = index + bytes.length;

    data = ByteArrays.growIfNecessary(data, newLength);

    System.arraycopy(bytes, 0, data, index, bytes.length);

    index = newLength;
  }

  public final void write(byte[] array, int offset, int length) {
    int newLength;
    newLength = index + length;

    data = ByteArrays.growIfNecessary(data, newLength);

    System.arraycopy(array, offset, data, index, length);

    index = newLength;
  }

  public final void write(ByteArrayWriter other) {
    int newLength;
    newLength = index + other.size();

    data = ByteArrays.growIfNecessary(data, newLength);

    System.arraycopy(other.data, 0, data, index, other.size());

    index = newLength;
  }

  public final void write(ByteArrayWriter other, int offset, int size) {
    int newLength;
    newLength = index + size;

    data = ByteArrays.growIfNecessary(data, newLength);

    System.arraycopy(other.data, offset, data, index, size);

    index = newLength;
  }

  public final void write(ByteBuffer buffer) {
    int bufferRemaining;
    bufferRemaining = buffer.remaining();

    int requiredIndex;
    requiredIndex = index + bufferRemaining;

    data = ByteArrays.growIfNecessary(data, requiredIndex);

    buffer.get(data, index, bufferRemaining);

    index += bufferRemaining;
  }

  public final void writeTo(WritablePathName file) throws IOException {
    try (var out = file.openOutputStream()) {
      out.write(data, 0, index);
    }
  }

  final byte[] array() {
    return data;
  }

  private int deflate0(Deflater deflater, int sizeHint) {
    data = ByteArrays.growIfNecessary(data, sizeHint);

    int remainingLength;
    remainingLength = remaining();

    int deflate;
    deflate = deflater.deflate(data, index, remainingLength);

    if (deflate <= 0) {
      throw new AssertionError(
          "Should not have happened. setInput + finish were called. return=" + deflate);
    }

    index += deflate;

    while (!deflater.finished()) {
      data = ByteArrays.growIfNecessary(data, index + 1);

      remainingLength = remaining();

      deflate = deflater.deflate(data, index, remainingLength);

      index += deflate;
    }

    return index;
  }

  private int remaining() {
    return data.length - index;
  }

}