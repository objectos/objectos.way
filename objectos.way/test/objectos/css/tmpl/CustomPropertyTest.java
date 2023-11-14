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
package objectos.css.tmpl;

import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class CustomPropertyTest {

  @Test
  public void names_valid() {
    assertEquals(CustomProperty.named("foo").toString(), "--foo");
    assertEquals(CustomProperty.named("Foo").toString(), "--Foo");
    assertEquals(CustomProperty.named("foo-bar").toString(), "--foo-bar");
    assertEquals(CustomProperty.named("foo_bar").toString(), "--foo_bar");
    assertEquals(CustomProperty.named("foo123").toString(), "--foo123");
    assertEquals(CustomProperty.named("123_abc").toString(), "--123_abc");
  }

  @DataProvider
  public String[][] namesInvalidProvider() {
    return new String[][] {
        {""}, {"foo?"}, {"fo.o"}
    };
  }

  @Test(dataProvider = "namesInvalidProvider")
  public void namesInvalid(String name) {
    try {
      CustomProperty.named(name);

      Assert.fail();
    } catch (IllegalArgumentException expected) {

    }
  }

}