/*
 * Copyright (C) 2023 Objectos Software LTDA.
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
package objectox.http;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class HttpRequestPathTest {

  @Test
  public void root() {
    byte[] bytes;
    bytes = Bytes.utf8("/");

    HttpRequestPath path;
    path = new HttpRequestPath(bytes, 0, 1);

    assertEquals(path.toString(), "/");
  }

  @Test
  public void single() {
    byte[] bytes;
    bytes = Bytes.utf8("/foo");

    HttpRequestPath path;
    path = new HttpRequestPath(bytes, 0, 4);

    assertEquals(path.toString(), "/foo");
  }

}