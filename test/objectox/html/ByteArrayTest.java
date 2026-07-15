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
package objectox.html;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class ByteArrayTest {

  @Test
  public void add01() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.add((byte) 1);

    assertEquals(subject, ByteArray.of(1));
    assertEquals(subject.toString(), "01");
  }

  @Test
  public void addBytes() {
    final ByteArray subject;
    subject = ByteArray.of(-1, -1);

    assertEquals(subject.addBytes(ByteArray.of(1, 2, 3), 0, 3), 3);

    assertEquals(subject, ByteArray.of(-1, -1, 1, 2, 3));
    assertEquals(subject.toString(), "ffff010203");
  }

  @Test(
      description = "reject int >= 256",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Invalid 1-byte int value: 256")
  public void addInt8_01() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addInt8(256);
  }

  @Test(
      description = "reject int < 0",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Invalid 1-byte int value: -1")
  public void addInt8_02() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addInt8(-1);
  }

  @Test
  public void addInt8_03() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addInt8(0);
    subject.addInt8(1);
    subject.addInt8(126);
    subject.addInt8(127);
    subject.addInt8(128);
    subject.addInt8(255);

    assertEquals(subject.toString(), "00017e7f80ff");
  }

  @Test(
      description = "reject int >= 65536",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Invalid 2-byte int value: 65536")
  public void addInt16_01() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addInt16(65536);
  }

  @Test(
      description = "reject int < 0",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Invalid 2-byte int value: -1")
  public void addInt16_02() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addInt16(-1);
  }

  @Test
  public void addInt16_03() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addInt16(0);
    subject.addInt16(ByteArray.MAX_INT16);

    assertEquals(subject.toString(), "0000ffff");
  }

  @Test(
      description = "reject int >= 16,777,216",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Invalid 3-byte int value: 16777216")
  public void addInt24_01() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addInt24(16777216);
  }

  @Test(
      description = "reject int < 0",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Invalid 3-byte int value: -1")
  public void addInt24_02() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addInt24(-1);
  }

  @Test
  public void addInt24_03() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addInt24(0);
    subject.addInt24(ByteArray.MAX_INT24);

    assertEquals(subject.toString(), "000000ffffff");
  }

  @Test(
      description = "reject int > 2097151",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Invalid varint value: 2097152")
  public void addVarInt01() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addVarInt(2097152);
  }

  @Test(
      description = "reject int < 0",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Invalid varint value: -1")
  public void addVarInt02() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addVarInt(-1);
  }

  @Test(description = "varint 1-byte")
  public void addVarInt03() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addVarInt(0);
    subject.addVarInt(127);

    assertEquals(subject.toString(), "007f");
  }

  @Test(description = "varint 2-byte")
  public void addVarInt04() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addVarInt(128);
    subject.addVarInt(16383);

    assertEquals(subject.toString(), "01807fff");
  }

  @Test(description = "varint 3-byte")
  public void addVarInt05() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addVarInt(16384);
    subject.addVarInt(2097151);

    assertEquals(subject.toString(), "0180807fffff");
  }

  @Test(
      description = "reject int > 2097151",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Invalid varint value: 2097152")
  public void addVarIntLE01() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addVarIntLE(2097152);
  }

  @Test(
      description = "reject int < 0",
      expectedExceptions = IllegalArgumentException.class,
      expectedExceptionsMessageRegExp = "Invalid varint value: -1")
  public void addVarIntLE02() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addVarIntLE(-1);
  }

  @Test(description = "varint 1-byte")
  public void addVarIntLE03() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addVarIntLE(0);
    subject.addVarIntLE(127);

    assertEquals(subject.toString(), "007f");
  }

  @Test(description = "varint 2-byte")
  public void addVarIntLE04() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addVarIntLE(128);
    subject.addVarIntLE(16383);

    assertEquals(subject.toString(), "8001ff7f");
  }

  @Test(description = "varint 3-byte")
  public void addVarIntLE05() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addVarIntLE(16384);
    subject.addVarIntLE(2097151);

    assertEquals(subject.toString(), "808001ffff7f");
  }

  @Test
  public void getInt16() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addInt16(0);
    subject.addInt16(ByteArray.MAX_INT16);

    assertEquals(subject.getInt16(0), 0);
    assertEquals(subject.getInt16(2), ByteArray.MAX_INT16);
  }

  @Test
  public void getInt24() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addInt24(0);
    subject.addInt24(ByteArray.MAX_INT24);

    assertEquals(subject.getInt24(0), 0);
    assertEquals(subject.getInt24(3), ByteArray.MAX_INT24);
  }

  @Test
  public void getVarIntLE01() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addVarIntLE(0);
    subject.addVarIntLE(127);

    assertEquals(subject.getVarIntLE(0, subject.varIntLEEndIndex(0)), 0);
    assertEquals(subject.getVarIntLE(1, subject.varIntLEEndIndex(1)), 127);
  }

  @Test
  public void getVarIntLE02() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addVarIntLE(128);
    subject.addVarIntLE(16383);

    assertEquals(subject.getVarIntLE(0, subject.varIntLEEndIndex(0)), 128);
    assertEquals(subject.getVarIntLE(2, subject.varIntLEEndIndex(2)), 16383);
  }

  @Test
  public void getVarIntLE03() {
    final ByteArray subject;
    subject = new ByteArray(0);

    subject.addVarIntLE(16384);
    subject.addVarIntLE(2097151);

    assertEquals(subject.getVarIntLE(0, subject.varIntLEEndIndex(0)), 16384);
    assertEquals(subject.getVarIntLE(3, subject.varIntLEEndIndex(3)), 2097151);
  }

  @Test
  public void maxVarInt2() {
    assertEquals(ByteArray.VARINT_MAX2, 0b1111111_1111111);
  }

  @Test
  public void set01() {
    final ByteArray subject;
    subject = ByteArray.of((byte) 1, (byte) 10, (byte) 3);

    subject.set(1, (byte) 2);

    assertEquals(subject, ByteArray.of(1, 2, 3));
  }

  @Test
  public void setInt16() {
    final ByteArray subject;
    subject = ByteArray.of((byte) 1, (byte) 2, (byte) 3);

    subject.setInt16(1, ByteArray.MAX_INT16);

    assertEquals(subject.toString(), "01ffff");
  }

  @Test
  public void setInt24() {
    final ByteArray subject;
    subject = ByteArray.of((byte) 1, (byte) 2, (byte) 3, (byte) 4);

    subject.setInt24(1, ByteArray.MAX_INT24);

    assertEquals(subject.toString(), "01ffffff");
  }

}
