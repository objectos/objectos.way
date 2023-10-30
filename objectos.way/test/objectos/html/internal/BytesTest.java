/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectos.html.internal;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class BytesTest {

	@Test
	public void commonEnd() {
		byte[] buf = new byte[5];

		// values [0,127] = 1 byte (7 bits)
		commonEnd1(buf, 0);
		commonEnd1(buf, 1);
		commonEnd1(buf, 2);
		commonEnd1(buf, 126);
		commonEnd1(buf, 127);

		// values [128,16383] = 2 bytes (7 + 7 bits)
		commonEnd2(buf, 128);
		commonEnd2(buf, 129);
		commonEnd2(buf, 5678);
		commonEnd2(buf, 16382);
		commonEnd2(buf, 16383);

		// values [16384,2097151] = 3 bytes (7 + 7 + 7 bits)
		commonEnd3(buf, 16384); // 0b1_0000000_0000000
		commonEnd3(buf, 16385);
		commonEnd3(buf, 1097151);
		commonEnd3(buf, 2097150);
		commonEnd3(buf, 2097151);
	}

	private void commonEnd1(byte[] buf, int value) {
		assertEquals(Bytes.encodeCommonEnd(buf, 0, value), 1);
		assertEquals(Bytes.decodeCommonEnd(buf, -1, 0), value);
	}

	private void commonEnd2(byte[] buf, int value) {
		assertEquals(Bytes.encodeCommonEnd(buf, 0, value), 2);
		assertEquals(Bytes.decodeCommonEnd(buf, -1, 1), value);
	}

	private void commonEnd3(byte[] buf, int value) {
		assertEquals(Bytes.encodeCommonEnd(buf, 0, value), 3);
		assertEquals(Bytes.decodeCommonEnd(buf, -1, 2), value);
	}

	@Test
	public void offset() {
		byte[] buf = new byte[5];

		// values [0,127] = 1 byte (7 bits)
		offset1(buf, 0);
		offset1(buf, 1);
		offset1(buf, 2);
		offset1(buf, 126);
		offset1(buf, 127);

		// values [128,16383] = 2 bytes (7 + 7 bits)
		offset2(buf, 128);
		offset2(buf, 129);
		offset2(buf, 5678);
		offset2(buf, 16382);
		offset2(buf, 16383);

		// values [16384,2097151] = 3 bytes (7 + 7 + 7 bits)
		offset3(buf, 16384); // 0b1_0000000_0000000
		offset3(buf, 16385);
		offset3(buf, 1097151);
		offset3(buf, 2097150);
		offset3(buf, 2097151);
	}

	private void offset1(byte[] buf, int value) {
		assertEquals(Bytes.encodeOffset(buf, 0, value), 1);
		assertEquals(Bytes.decodeOffset(buf, 0, 1), value);
	}

	private void offset2(byte[] buf, int value) {
		assertEquals(Bytes.encodeOffset(buf, 0, value), 2);
		assertEquals(Bytes.decodeOffset(buf, 0, 2), value);
	}

	private void offset3(byte[] buf, int value) {
		assertEquals(Bytes.encodeOffset(buf, 0, value), 3);
		assertEquals(Bytes.decodeOffset(buf, 0, 3), value);
	}

	@Test
	public void varIntMax() {
		assertEquals(Bytes.VARINT_MAX2, 0b1111111_1111111);
	}

	@Test
	public void rVarIntMax() {
		assertEquals(Bytes.VARINT_MAX2, 0b111_1111_111_1111);
	}

}