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
package objectos.util;

import static org.testng.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

final class SetAssert {

	private SetAssert() {}

	public static Set<Object> all(Object... expected) {
		var jdk = new HashSet<>();

		for (var o : expected) {
			if (o instanceof Thing t) {
				jdk.add(t);
			} else if (o instanceof Iterable<?> iter) {
				for (var t : iter) {
					jdk.add(t);
				}
			} else if (o instanceof Thing[] a) {
				for (var t : a) {
					jdk.add(t);
				}
			} else {
				throw new UnsupportedOperationException("Implement me: " + o.getClass());
			}
		}
		return jdk;
	}

	public static void iterator(Set<?> it, Object... expected) {
		var jdk = all(expected);

		for (Object e : it) {
			assertTrue(jdk.remove(e));
		}

		assertTrue(jdk.isEmpty());
	}

}