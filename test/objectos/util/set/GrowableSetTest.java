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
package objectos.util.set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class GrowableSetTest {

	private GrowableSet<Thing> it;

	@BeforeMethod
	public void _beforeMethod() {
		it = new GrowableSet<>();
	}

	@Test
	public void add() {
		// empty
		assertEquals(it.size(), 0);
		assertTrue(it.isEmpty());

		// one
		var t1 = Thing.next();

		assertTrue(it.add(t1));
		assertFalse(it.add(t1));

		assertContents(t1);

		// two
		var t2 = Thing.next();

		assertTrue(it.add(t2));
		assertFalse(it.add(t2));

		assertContents(t1, t2);

		// many
		var many = Thing.nextArray();

		for (var t : many) {
			assertTrue(it.add(t));
			assertFalse(it.add(t));
		}

		assertContents(t1, t2, many);

		// must reject null
		try {
			Thing t = null;

			it.add(t);

			Assert.fail("Must throw NullPointerException");
		} catch (NullPointerException expected) {
			assertEquals(expected.getMessage(), "e == null");
		}
	}

	@Test
	public void addAll() {
		Consumer<Collection<? extends Thing>> tester = c -> {
			assertEquals(it.addAll(c), !c.isEmpty());
			assertFalse(it.addAll(c));

			assertContents(c);

			_beforeMethod();
		};

		// empty
		tester.accept(Thing.EMPTY_LIST);
		tester.accept(Thing.EMPTY_SET);

		// one
		var t1 = Thing.next();

		tester.accept(List.of(t1));
		tester.accept(Set.of(t1));

		// two
		var t2 = Thing.next();

		tester.accept(List.of(t1, t2));
		tester.accept(Set.of(t1, t2));

		// many
		var many = Thing.nextArray();

		tester.accept(List.of(many));
		tester.accept(Set.of(many));

		// must reject null
		Consumer<Collection<? extends Thing>> nullTester = c -> {
			try {
				it.addAll(c);

				Assert.fail("Must throw NullPointerException");
			} catch (NullPointerException expected) {
				String msg = expected.getMessage();

				assertTrue(msg.matches("c\\[[0-9]{1,2}\\] == null"));
			}

			_beforeMethod();
		};

		many[Thing.HALF] = null;

		var listWithNull = new ArrayList<Thing>(Thing.MANY);
		var setWithNull = new HashSet<Thing>(Thing.MANY);

		for (var t : many) {
			listWithNull.add(t);
			setWithNull.add(t);
		}

		nullTester.accept(listWithNull);
		nullTester.accept(setWithNull);
	}

	@Test
	public void addAllIterable() {
		var test = new GrowableCollectionAddAllIterableTest(it, this::assertContents);

		test.execute();
	}

	@Test
	public void addWithNullMessage() {
		var test = new GrowableCollectionAddWithNullMessageTest(it);

		test.execute();
	}

	@Test
	public void clear() {
		var test = new GrowableCollectionClearTest(it, this::assertContents);

		test.execute();
	}

	@Test
	public void contains() {
		var test = new GrowableCollectionContainsTest(it);

		test.execute();
	}

	@Test
	public void containsAll() {
		var test = new GrowableCollectionContainsAllTest(it);

		test.execute();
	}

	@Test
	public void equals() {
		var a = new GrowableSet<Thing>();

		var b = new GrowableSet<Thing>();

		assertTrue(a.equals(b));
		assertTrue(b.equals(a));

		assertFalse(a.equals(null));

		assertTrue(a.equals(Collections.emptySet()));

		var arrayList = Thing.nextArrayList();

		a.addAll(arrayList);

		var t2 = Thing.next();

		b.addAll(arrayList);
		b.add(t2);

		assertFalse(a.equals(b));

		var c = new GrowableSet<Thing>();

		c.addAll(arrayList);

		assertTrue(a.equals(c));
		assertTrue(c.equals(a));
	}

	@Test
	public void hashCodeTest() {
		// empty
		assertEquals(it.hashCode(), 0);

		// one
		var t1 = Thing.next();

		it.add(t1);

		var hashCode = t1.hashCode();

		assertEquals(it.hashCode(), hashCode);

		// two
		var t2 = Thing.next();

		it.add(t2);

		hashCode = hashCode + t2.hashCode();

		assertEquals(it.hashCode(), hashCode);

		// many
		var arrayList = Thing.nextArrayList();

		it.addAll(arrayList);

		for (var e : arrayList) {
			hashCode = hashCode + e.hashCode();
		}

		assertEquals(it.hashCode(), hashCode);
	}

	@Test
	public void isEmpty() {
		var test = new GrowableCollectionIsEmptyTest(it);

		test.execute();
	}

	@Test
	public void iterator() {
		// empty
		assertIterator();

		// one
		var t1 = Thing.next();

		it.add(t1);

		assertIterator(t1);

		// two
		var t2 = Thing.next();

		it.add(t2);

		assertIterator(t1, t2);

		// many
		var arrayList = Thing.nextArrayList();

		it.addAll(arrayList);

		assertIterator(t1, t2, arrayList);
	}

	@Test
	public void join() {
		// empty
		assertEquals(
				it.join(),
				""
		);
		assertEquals(
				it.join("|"),
				""
		);
		assertEquals(
				it.join("|", "{", "}"),
				"{}"
		);

		// one
		var t1 = Thing.next();

		it.add(t1);

		assertEquals(
				it.join(),
				t1.toString()
		);
		assertEquals(
				it.join("|"),
				t1.toString()
		);
		assertEquals(
				it.join("|", "{", "}"),
				"{" + t1.toString() + "}"
		);

		// two
		var t2 = Thing.next();

		it.add(t2);

		var iterator = it.iterator();

		var o1 = iterator.next();

		var o2 = iterator.next();

		assertEquals(
				it.join(),
				o1.toString() + o2.toString()
		);
		assertEquals(
				it.join("|"),
				o1.toString() + "|" + o2.toString()
		);
		assertEquals(
				it.join("|", "{", "}"),
				"{" + o1.toString() + "|" + o2.toString() + "}"
		);
	}

	@Test
	public void remove() {
		var t1 = Thing.next();

		it.add(t1);

		try {
			it.remove(t1);

			Assert.fail("Expected an UnsupportedOperationException");
		} catch (UnsupportedOperationException expected) {
			assertTrue(it.contains(t1));
		}
	}

	@Test
	public void removeAll() {
		var test = new GrowableCollectionRemoveAllTest(it);

		test.execute();
	}

	@Test
	public void removeIf() {
		var test = new GrowableCollectionRemoveIfTest(it);

		test.execute();
	}

	@Test
	public void retainAll() {
		var test = new GrowableCollectionRetainAllTest(it);

		test.execute();
	}

	@Test
	public void toStringTest() {
		assertEquals(it.toString(), "GrowableSet []");

		var t1 = Thing.next();

		it.add(t1);

		assertEquals(
				it.toString(),

				"""
      GrowableSet [
        0 = Thing [
          value = %s
        ]
      ]""".formatted(t1.toHexString())
		);

		var t2 = Thing.next();

		it.add(t2);

		var iterator = it.iterator();

		var o1 = iterator.next();

		var o2 = iterator.next();

		assertEquals(
				it.toString(),

				"""
      GrowableSet [
        0 = Thing [
          value = %s
        ]
        1 = Thing [
          value = %s
        ]
      ]""".formatted(o1.toHexString(), o2.toHexString())
		);
	}

	@Test
	public void toUnmodifiableSet() {
		var us0 = it.toUnmodifiableSet();

		var t1 = Thing.next();
		assertTrue(it.add(t1));
		var us1 = it.toUnmodifiableSet();

		var t2 = Thing.next();
		assertTrue(it.add(t2));
		var us2 = it.toUnmodifiableSet();

		var t3 = Thing.next();
		assertTrue(it.add(t3));
		var us3 = it.toUnmodifiableSet();

		var t4 = Thing.next();
		assertTrue(it.add(t4));
		var us4 = it.toUnmodifiableSet();

		assertEquals(us0.size(), 0);
		assertContents(us0);

		assertEquals(us1.size(), 1);
		assertContents(us1, t1);

		assertEquals(us2.size(), 2);
		assertContents(us2, t1, t2);

		assertEquals(us3.size(), 3);
		assertContents(us3, t1, t2, t3);

		assertEquals(us4.size(), 4);
		assertContents(us4, t1, t2, t3, t4);

		it.clear();

		var array = Thing.nextArray();

		for (var thing : array) {
			assertTrue(it.add(thing));
		}

		var usX = it.toUnmodifiableSet();

		assertEquals(usX.size(), array.length);
		assertContents(usX, (Object) array);
	}

	private void assertContents(Object... expected) {
		assertContents(it, expected);
	}

	private void assertContents(Set<Thing> set, Object... expected) {
		var jdk = new HashSet<>();

		for (var o : expected) {
			if (o instanceof Thing t) {
				jdk.add(t);
			} else if (o instanceof Thing[] a) {
				for (var t : a) {
					jdk.add(t);
				}
			} else if (o instanceof Iterable<?> iter) {
				for (var t : iter) {
					jdk.add(t);
				}
			} else {
				throw new UnsupportedOperationException("Implement me: " + o.getClass());
			}
		}

		var elements = set.toArray();

		for (var e : elements) {
			assertTrue(jdk.remove(e));
		}

		assertTrue(jdk.isEmpty());
	}

	private void assertIterator(Object... expected) {
		SetAssert.iterator(it, expected);
	}

}