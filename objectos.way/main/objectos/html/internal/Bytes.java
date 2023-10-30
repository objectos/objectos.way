/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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

public final class Bytes {

	public static final int VARINT_MAX1 = 0x7F;

	public static final int VARINT_MAX2 = 0x7F00 | 0x00FF;

	private static final int BYTE_MASK = 0xFF;

	private Bytes() {}

	public static int decodeInt(byte b0) {
		return toInt(b0, 0);
	}

	public static int decodeInt(byte b0, byte b1) {
		int int0;
		int0 = toInt(b0, 0);

		int int1;
		int1 = toInt(b1, 8);

		return int1 | int0;
	}

	public static int decodeVarInt(byte int0, byte int1) {
		int b0;
		b0 = int0 & VARINT_MAX1;

		int b1;
		b1 = toInt(int1, 7);

		return b0 | b1;
	}

	public static byte encodeInt0(int value) {
		return (byte) value;
	}

	public static byte encodeInt1(int value) {
		return (byte) (value >>> 8);
	}

	public static byte encodeName(StandardElementName name) {
		int ordinal;
		ordinal = name.ordinal();

		return encodeInt0(ordinal);
	}

	public static int encodeVarInt(byte[] buf, int off, int value) {
		if (value < 0) {
			throw new IllegalArgumentException("value has to be >= 0");
		}

		if (value <= VARINT_MAX1) {
			buf[off++] = (byte) value;

			return off;
		}

		if (value <= VARINT_MAX2) {
			buf[off++] = encodeVarInt0(value);

			buf[off++] = encodeVarInt1(value);

			return off;
		}

		throw new IllegalArgumentException(
				"HtmlTemplate is too large :: value=" + value
		);
	}

	public static byte encodeVarInt0(int value) {
		byte b0;
		b0 = (byte) (value & 0x7F);

		b0 |= (byte) 0x80;

		return b0;
	}

	public static byte encodeVarInt1(int value) {
		value = value >>> 7;

		return (byte) value;
	}

	static final int COMMON_END_MAX1 = 0x7F;

	static final int COMMON_END_MAX2 = (1 << 14) - 1;

	static final int COMMON_END_MAX3 = (1 << 21) - 1;

	public static int encodeCommonEnd(byte[] buf, int off, int length) {
		if (length < 0) {
			throw new IllegalArgumentException("Length has to be >= 0");
		}

		if (length <= COMMON_END_MAX1) {
			buf[off++] = (byte) length;

			return off;
		}

		if (length <= COMMON_END_MAX2) {
			buf[off++] = encodeCommonEndHigh(length, 7);

			buf[off++] = encodeCommonEnd(length, 0);

			return off;
		}

		if (length <= COMMON_END_MAX3) {
			buf[off++] = encodeCommonEndHigh(length, 14);

			buf[off++] = encodeCommonEnd(length, 7);

			buf[off++] = encodeCommonEnd(length, 0);

			return off;
		}

		throw new IllegalArgumentException(
				"HtmlTemplate is too large :: length=" + length
		);
	}

	private static byte encodeCommonEndHigh(int value, int shift) {
		value = value >>> shift;

		return (byte) value;
	}

	private static byte encodeCommonEnd(int value, int shift) {
		value = value >>> shift;

		value = value & COMMON_END_MAX1;

		value = value | 0x80;

		return (byte) value;
	}

	public static int decodeCommonEnd(byte[] buf, int startIndex, int endIndex) {
		int length;
		length = endIndex - startIndex;

		return switch (length) {
			case 1 -> buf[endIndex];

			case 2 -> decodeCommonEnd(buf[endIndex], buf[endIndex - 1]);

			case 3 -> decodeCommonEnd(buf[endIndex], buf[endIndex - 1], buf[endIndex - 2]);

			default -> throw new IllegalArgumentException(
					"HtmlTemplate is too large :: length=" + length
			);
		};
	}

	private static int decodeCommonEnd(byte b0, byte b1) {
		int int0;
		int0 = decodeCommonEnd(b0, 0);

		int int1;
		int1 = decodeCommonEnd(b1, 7);

		return int0 | int1;
	}

	private static int decodeCommonEnd(byte b0, byte b1, byte b2) {
		int int0;
		int0 = decodeCommonEnd(b0, 0);

		int int1;
		int1 = decodeCommonEnd(b1, 7);

		int int2;
		int2 = decodeCommonEnd(b2, 14);

		return int0 | int1 | int2;
	}

	private static int decodeCommonEnd(byte value, int shift) {
		int intValue;
		intValue = value & COMMON_END_MAX1;

		return intValue << shift;
	}

	public static int encodeOffset(byte[] buf, int off, int value) {
		if (value < 0) {
			throw new IllegalArgumentException("value has to be >= 0");
		}

		if (value <= COMMON_END_MAX1) {
			buf[off++] = (byte) value;

			return off;
		}

		if (value <= COMMON_END_MAX2) {
			buf[off++] = encodeCommonEnd(value, 0);

			buf[off++] = encodeCommonEndHigh(value, 7);

			return off;
		}

		if (value <= COMMON_END_MAX3) {
			buf[off++] = encodeCommonEnd(value, 0);

			buf[off++] = encodeCommonEnd(value, 7);

			buf[off++] = encodeCommonEndHigh(value, 14);

			return off;
		}

		throw new IllegalArgumentException(
				"HtmlTemplate is too large :: value=" + value
		);
	}

	public static int decodeOffset(byte[] buf, int startIndex, int endIndex) {
		int length;
		length = endIndex - startIndex;

		return switch (length) {
			case 1 -> buf[startIndex];

			case 2 -> decodeCommonEnd(buf[startIndex], buf[startIndex + 1]);

			case 3 -> decodeCommonEnd(buf[startIndex], buf[startIndex + 1], buf[startIndex + 2]);

			default -> throw new IllegalArgumentException(
					"HtmlTemplate is too large :: length=" + length
			);
		};
	}

	public static int toInt(byte b, int shift) {
		return (b & BYTE_MASK) << shift;
	}

}