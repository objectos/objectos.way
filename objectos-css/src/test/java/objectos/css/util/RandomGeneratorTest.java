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
package objectos.css.util;

import static org.testng.Assert.assertEquals;

import java.util.Random;
import org.testng.annotations.Test;

public class RandomGeneratorTest {

  @Test
  public void test() {
    RandomGenerator.Builder b;
    b = RandomGenerator.builder();

    b.random(new Random(123456789L));

    b.nameLength(5);

    RandomGenerator g;
    g = b.build();

    assertEquals(g.classSelector(), ClassSelector.of("ieilf"));
    assertEquals(g.classSelector(), ClassSelector.of("mgmdu"));

    assertEquals(g.customProperty(), CustomProperty.named("--w1puk"));
    assertEquals(g.customProperty(), CustomProperty.named("--c8e9b"));
  }

}