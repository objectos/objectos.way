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
package objectos.css.random;

import static org.testng.Assert.assertEquals;

import java.util.Random;
import objectos.css.select.ClassSelector;
import objectos.css.tmpl.CustomProperty;
import org.testng.annotations.Test;

public class NextTest {

	@Test
	public void test() {
		Next.Builder b;
		b = Next.builder();

		b.random(new Random(123456789L));

		b.nameLength(6);

		Next next;
		next = b.build();

		assertEquals(next.classSelector(), ClassSelector.of("ufczvl"));
		assertEquals(next.classSelector(), ClassSelector.of("bwrnib"));

		assertEquals(next.customProperty(), CustomProperty.named("--apvswh"));
		assertEquals(next.customProperty(), CustomProperty.named("--ateivq"));
	}

}