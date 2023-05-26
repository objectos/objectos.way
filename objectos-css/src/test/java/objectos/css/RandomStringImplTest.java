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
package objectos.css;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class RandomStringImplTest {

  @Test(description = """
  Verifies setSeed generates the same sequence of pseudo random every time.
  """)
  public void setSeed() {
    long seed = 1233456789L;

    RandomStringImpl.randomSeed(seed);

    assertEquals(RandomStringImpl.next(3), "Ds2");
    assertEquals(RandomStringImpl.next(4), "yIny");
    assertEquals(RandomStringImpl.next(5), "0kdzu");
  }

}