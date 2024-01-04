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
package objectox.http.server;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class ObjectoxUriPathTest {

  @Test
  public void is() {
    ObjectoxUriPath path;
    path = new ObjectoxUriPath();

    path.set("/");

    assertEquals(path.is("/"), true);
    assertEquals(path.is("/index.html"), false);
  }

  @Test
  public void startsWith() {
    ObjectoxUriPath path;
    path = new ObjectoxUriPath();

    path.set("/foo/bar.html");

    assertEquals(path.startsWith("/foo"), true);
    assertEquals(path.startsWith("/foo/"), true);
    assertEquals(path.startsWith("/foo/bar.html"), true);
    assertEquals(path.startsWith("/foo/bar.html/"), false);
    assertEquals(path.startsWith("/goo"), false);
  }

}