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

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

abstract class UnmodifiableMapTestAdapter {

	@FunctionalInterface
	interface Tester {
		void execute(UnmodifiableMap<Thing, String> it, Thing... els);
	}

	abstract void assertContents(Map<Thing, String> map, Thing[] els);

	abstract <E> void assertSet(Collection<E> set, Thing[] els, Function<Thing, E> function);

	abstract Map<Thing, String> jdk(Thing... many);

	abstract UnmodifiableMap<Thing, String> map0();

	abstract UnmodifiableMap<Thing, String> map1(Thing t1);

	abstract UnmodifiableMap<Thing, String> map2(Thing t1, Thing t2);

	abstract UnmodifiableMap<Thing, String> map3(Thing t1, Thing t2, Thing t3);

	abstract UnmodifiableMap<Thing, String> mapX(Thing[] many);

	final void testAll(Tester tester) {
		var t1 = Thing.next();
		var t2 = Thing.next();
		var t3 = Thing.next();
		var many = Thing.nextArray();

		tester.execute(map0());

		tester.execute(map1(t1), t1);

		tester.execute(map2(t1, t2), t1, t2);

		tester.execute(map3(t1, t2, t3), t1, t2, t3);

		tester.execute(mapX(many), many);
	}

}