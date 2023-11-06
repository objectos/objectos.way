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
package objectox.http;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import objectos.http.Segment;
import objectos.util.IntArrays;
import objectox.lang.Check;

final class HttpRequestPath {

	final byte[] buffer;

	private final int startIndex;

	int[] slash = new int[10];

	private int slashIndex;

	private String value;

	public HttpRequestPath(byte[] buffer, int startIndex) {
		this.buffer = buffer;

		this.startIndex = startIndex;
	}

	public final void slash(int index) {
		slash = IntArrays.growIfNecessary(slash, slashIndex);

		slash[slashIndex++] = toOffset(index);
	}

	public final void end(int index) {
		Check.state(slashIndex > 0, "no slashs were defined");

		int length;
		length = toOffset(index);

		value = new String(buffer, startIndex, length, StandardCharsets.UTF_8);
	}

	public final boolean matches(Segment s0) {
		if (segmentCount() == 1) {
			return match(s0, segment(0));
		}

		return false;
	}

	public final boolean matches(Segment s0, Segment s1) {
		if (segmentCount() == 2) {
			return match(s0, segment(0))
					&& match(s1, segment(1));
		}

		return false;
	}

	public final boolean pathEquals(String s) {
		String value;
		value = toString();

		return value.equals(s);
	}

	public final boolean pathStartsWith(String prefix) {
		String value;
		value = toString();

		return value.startsWith(prefix);
	}

	public final String segment(int index) {
		Check.state(slashIndex > 0, "no slashs were defined");

		if (index < 0 || index >= slashIndex) {
			throw new IndexOutOfBoundsException(
					"Index out of range: " + index + "; valid values: 0 <= index < " + slashIndex
			);
		}

		int segmentStart;
		segmentStart = slash[index] + 1;

		int segmentEnd;

		int nextIndex;
		nextIndex = index + 1;

		if (nextIndex == slashIndex) {
			segmentEnd = value.length();
		} else {
			segmentEnd = slash[nextIndex];
		}

		return value.substring(segmentStart, segmentEnd);
	}

	public final int segmentCount() {
		return slashIndex;
	}

	public final Path toPath() {
		Check.state(slashIndex > 0, "no slashs were defined");

		Path path;
		path = switch (slashIndex) {
			case 1 -> Path.of(segment(0));

			case 2 -> Path.of(segment(0), segment(1));

			default -> {
				String[] rest;
				rest = new String[slashIndex - 1];

				for (int i = 1; i < slashIndex; i++) {
					rest[i - 1] = segment(i);
				}

				yield Path.of(segment(0), rest);
			}
		};

		return path.normalize();
	}

	@Override
	public final String toString() {
		Check.state(value != null, "no slashs were defined");

		return value;
	}

	private boolean match(Segment segment, String value) {
		return switch (segment) {
			case SegmentExact exact -> exact.matches(value);

			case SegmentKind.ALWAYS_TRUE -> true;

			default -> false;
		};
	}

	private int toOffset(int index) {
		return index - startIndex;
	}

}
