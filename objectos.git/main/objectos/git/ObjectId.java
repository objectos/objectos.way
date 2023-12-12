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

import java.util.Arrays;
import objectos.lang.object.Check;
import objectos.util.array.ByteArrays;

/**
 * A hash value that uniquely identifies a Git object.
 *
 * @since 1
 */
public final class ObjectId extends MaybeObjectId {

  static final int BYTE_LENGTH = 20;

  static final int CHAR_LENGTH = 40;

  private final byte[] bytes;

  private int fanOutIndex = Integer.MAX_VALUE;

  private String hexString;

  ObjectId(byte[] bytes) {
    this.bytes = bytes;
  }

  ObjectId(byte[] bytes, String hexString) {
    this.bytes = bytes;

    this.hexString = hexString;
  }

  /**
   * Parses the specified text to produce a {@code ObjectId} instance.
   *
   * @param id
   *        the text to be parsed
   *
   * @return a new {@code ObjectId} instance
   *
   * @throws InvalidObjectIdFormatException
   *         if the text could not be parsed
   */
  public static ObjectId parse(String id) throws InvalidObjectIdFormatException {
    byte[] bytes;
    bytes = toByteArray(id);

    return new ObjectId(bytes, id);
  }

  static ObjectId copyOf(byte[] bytes) {
    byte[] copy;
    copy = Arrays.copyOf(bytes, BYTE_LENGTH);

    return fromByteArray(copy);
  }

  static ObjectId fromByteArray(byte[] bytes) {
    return new ObjectId(bytes);
  }

  static ObjectId parse(CharSequence cs, int offset) throws InvalidObjectIdFormatException {
    byte[] bytes;
    bytes = toByteArray(cs, offset);

    return new ObjectId(bytes);
  }

  private static byte getByte(CharSequence chars, int index) throws InvalidObjectIdFormatException {
    char c;
    c = chars.charAt(index);

    if (c > 'f') {
      throw newInvalidChar(c, index);
    }

    if (c >= 'a') {
      return (byte) (c - 87);
    }

    if (c > '9') {
      throw newInvalidChar(c, index);
    }

    if (c >= '0') {
      return (byte) (c - 48);
    }

    throw newInvalidChar(c, index);
  }

  private static InvalidObjectIdFormatException newInvalidChar(char c, int index) {
    return new InvalidObjectIdFormatException("Invalid char " + c + " at index " + index);
  }

  private static byte[] toByteArray(CharSequence chars, int offset) throws InvalidObjectIdFormatException {
    int length;
    length = chars.length() - offset;

    if (length < CHAR_LENGTH) {
      throw new InvalidObjectIdFormatException("id must have 40 characters");
    }

    byte[] bytes;
    bytes = new byte[BYTE_LENGTH];

    for (int i = 0; i < bytes.length; i++) {
      int j;
      j = i * 2;

      byte highValue;
      highValue = getByte(chars, offset + j);

      byte high;
      high = (byte) (highValue << 4);

      byte low;
      low = getByte(chars, offset + j + 1);

      byte result;
      result = (byte) (high | low);

      bytes[i] = result;
    }

    return bytes;
  }

  private static byte[] toByteArray(String id) throws InvalidObjectIdFormatException {
    Check.notNull(id, "id == null");

    return toByteArray(id, 0);
  }

  /**
   * Compares the specified object with this Git object id for equality.
   *
   * <p>
   * Returns {@code true} if and only if the specified object is also a
   * {@code ObjectId} and if both instances have the same hash value.
   *
   * @param obj
   *        the object to be compared for equality with this object id
   *
   * @return {@code true} if the specified object is equal to this object id
   */
  @Override
  public final boolean equals(Object obj) {
    return obj == this
        || obj instanceof ObjectId && equals0((ObjectId) obj);
  }

  /**
   * Returns this id as a hex string.
   *
   * @return this id as a hex string
   */
  public final String getHexString() {
    if (hexString == null) {
      hexString = getHexString0();
    }

    return hexString;
  }

  /**
   * Returns this reference.
   *
   * @return this reference
   */
  @Override
  public final ObjectId getObjectId() {
    return this;
  }

  /**
   * Returns the hash code value of this id.
   *
   * @return the hash code value of this id
   */
  @Override
  public final int hashCode() {
    return Arrays.hashCode(bytes);
  }

  /**
   * Returns {@code true}.
   *
   * @return {@code true} always
   */
  @Override
  public final boolean isObjectId() {
    return true;
  }

  /**
   * Returns this id as a hex string.
   *
   * @return this id as a hex string
   */
  public final String print() {
    return getHexString();
  }

  /**
   * Returns this id as a hex string.
   *
   * @return this id as a hex string
   */
  @Override
  public final String toString() {
    return getHexString();
  }

  final byte[] getBytes() {
    return bytes;
  }

  final String getDirectoryName() {
    String hexString;
    hexString = getHexString();

    return hexString.substring(0, 2);
  }

  final int getFanOutIndex() {
    if (fanOutIndex == Integer.MAX_VALUE) {
      fanOutIndex = getFanOutIndex0();
    }

    return fanOutIndex;
  }

  final String getHexString0() {
    return ByteArrays.toHexString(bytes);
  }

  final String getRegularFileName() {
    String hexString;
    hexString = getHexString();

    return hexString.substring(2);
  }

  final String toString(String prefix, String suffix) {
    return prefix + getHexString() + suffix;
  }

  final int writeTo(byte[] output, int offset) {
    System.arraycopy(bytes, 0, output, offset, bytes.length);

    return offset + bytes.length;
  }

  private boolean equals0(ObjectId that) {
    return Arrays.equals(bytes, that.bytes);
  }

  private int getFanOutIndex0() {
    return bytes[0] & 0xFF;
  }

}