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
package br.com.objectos.http;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class HeaderContentLengthImplTest {

  @Test
  public void testCase01() {
    HeaderContentLengthImpl result;
    result = parse("5\r\n");

    assertEquals(result.getLength(), 5);
  }

  @Test
  public void testCase02() {
    HeaderContentLengthImpl result;
    result = parse("27\r\n");

    assertEquals(result.getLength(), 27);
  }

  private HeaderContentLengthImpl parse(String source) {
    HeaderContentLengthImpl header;
    header = new HeaderContentLengthImpl();

    return HeaderTesting.parse(header, source);
  }

}
