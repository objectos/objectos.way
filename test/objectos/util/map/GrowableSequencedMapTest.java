/*
 * Copyright (C) 2022-2023 Objectos Software LTDA.
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
package objectos.util.map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GrowableSequencedMapTest {

	private GrowableSequencedMap<Thing, String> it;

	@BeforeClass
	public void _beforeClass() {
		it = new GrowableSequencedMap<>();
	}

	@BeforeMethod
	public void _beforeMethod() {
		it.clear();
	}

	@Test
	public void clear() {
		var test = new GrowableMapClearTest(it, this::assertContents);

		test.execute();
	}

	@Test
	public void compute() {
		var test = new GrowableMapComputeTest(it);

		test.execute();
	}

	@Test
	public void computeIfAbsent() {
		var test = new GrowableMapComputeIfAbsentTest(it);

		test.execute();
	}

	@Test
	public void computeIfPresent() {
		var test = new GrowableMapComputeIfPresentTest(it);

		test.execute();
	}

	@Test
	public void containsKey() {
		var test = new GrowableMapContainsKeyTest(it);

		test.execute();
	}

	@Test
	public void containsValue() {
		var test = new GrowableMapContainsValueTest(it);

		test.execute();
	}

	@Test
	public void entrySet() {
		var test = new GrowableMapEntrySetTest(it, this::assertContents);

		test.execute();
	}

	@Test
	public void forEach() {
		var test = new GrowableMapForEachTest(it, this::assertContents);

		test.execute();
	}

	@Test
	public void get() {
		var test = new GrowableMapGetTest(it);

		test.execute();
	}

	@Test
	public void getOrDefault() {
		var test = new GrowableMapGetOrDefaultTest(it);

		test.execute();
	}

	@Test
	public void hashCodeTest() {
		var test = new GrowableMapHashCodeTest(it);

		test.execute();
	}

	@Test
	public void isEmpty() {
		var test = new GrowableMapIsEmptyTest(it);

		test.execute();
	}

	@Test
	public void keySet() {
		var test = new GrowableMapKeySetTest(it, this::assertContents);

		test.execute();
	}

	@Test
	public void merge() {
		var test = new GrowableMapMergeTest(it);

		test.execute();
	}

	@Test
	public void put() {
		var test = new GrowableMapPutTest(it, this::assertContents);

		test.execute();
	}

	@Test
	public void putAll() {
		var test = new GrowableMapPutAllTest(it, this::assertContents);

		test.execute();
	}

	@Test
	public void putIfAbsent() {
		var test = new GrowableMapPutIfAbsentTest(it);

		test.execute();
	}

	@Test
	public void remove() {
		var test = new GrowableMapRemoveTest(it, this::assertContents);

		test.execute();
	}

	@Test
	public void replace() {
		var test = new GrowableMapReplaceTest(it, this::assertContents);

		test.execute();
	}

	@Test
	public void replaceAll() {
		var test = new GrowableMapReplaceAllTest(it, this::assertContents);

		test.execute();
	}

	@Test
	public void size() {
		var test = new GrowableMapSizeTest(it);

		test.execute();
	}

	@Test
	public void toStringTest() {
		assertEquals(it.toString(), "GrowableSequencedMap []");

		var t1 = Thing.next();

		t1.putHex(it);

		assertEquals(
				it.toString(),

				"""
      GrowableSequencedMap [
        %s = %s
      ]""".formatted(t1, t1.toHexString())
		);

		var t2 = Thing.next();

		t2.putHex(it);

		assertEquals(
				it.toString(),

				"""
      GrowableSequencedMap [
        %s = %s
        %s = %s
      ]""".formatted(t1, t1.toHexString(), t2, t2.toHexString())
		);

		var t3 = Thing.next();

		t3.putHex(it);

		assertEquals(
				it.toString(),

				"""
      GrowableSequencedMap [
        %s = %s
        %s = %s
        %s = %s
      ]""".formatted(
						t1, t1.toHexString(), t2, t2.toHexString(), t3, t3.toHexString())
		);
	}

	@Test
	public void toUnmodifiableMap() {
		var test = new GrowableMapToUnmodifiableMapTest(it, this::assertContents);

		test.execute();
	}

	@Test
	public void values() {
		var test = new GrowableMapValuesTest(it, this::assertValues);

		test.execute();
	}

	private void assertContents(Map<?, ?> map, Object... expected) {
		var jdk = new LinkedHashMap<Thing, String>();

		for (var o : expected) {
			if (o instanceof Thing t) {
				t.putDec(jdk);
			} else if (o instanceof Thing[] a) {
				for (var t : a) {
					t.putDec(jdk);
				}
			} else if (o instanceof Hex hex) {
				var t = hex.value();

				t.putHex(jdk);
			} else {
				throw new UnsupportedOperationException("Implement me: " + o.getClass());
			}
		}

		var jdkEntries = jdk.entrySet().iterator();

		for (var entry : map.entrySet()) {
			var jdkEntry = jdkEntries.next();

			assertEquals(entry.getKey(), jdkEntry.getKey());

			assertEquals(entry.getValue(), jdkEntry.getValue());

			jdkEntries.remove();
		}

		assertTrue(jdk.isEmpty());
	}

	private void assertValues(Collection<String> set, Thing... expected) {
		assertEquals(set.size(), expected.length);

		var index = 0;

		for (var value : expected) {
			assertEquals(value, expected[index++]);
		}
	}

}