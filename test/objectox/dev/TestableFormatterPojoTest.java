/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
package objectox.dev;

import static org.testng.Assert.assertEquals;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiConsumer;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestableFormatterPojoTest {

  @DataProvider
  public Iterator<BiConsumer<TestableFormatter2Pojo, String>> heading01Provider() {
    return List.<BiConsumer<TestableFormatter2Pojo, String>> of(
        (subject, value) -> subject.h1(value),
        (subject, value) -> subject.h2(value)
    ).iterator();
  }

  @Test(dataProvider = "heading01Provider", description = "reject null value")
  public void heading01(BiConsumer<TestableFormatter2Pojo, String> method) {
    final TestableFormatter2Pojo subject;
    subject = new TestableFormatter2Pojo();

    try {
      method.accept(subject, null);

      Assert.fail("It should have thrown");
    } catch (NullPointerException expected) {
      final String msg = expected.getMessage();

      assertEquals(msg, "value == null");
    }
  }

}
