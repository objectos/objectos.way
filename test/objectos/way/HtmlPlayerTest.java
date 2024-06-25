/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.way;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class HtmlPlayerTest {

  private final HtmlPlayer player = new HtmlPlayer();

  @Test
  public void processHrefTC01() {
    processHrefImpl("/base.html", "/base.html", "base.html");
    processHrefImpl("/base.html", "/other.html", "other.html");
    processHrefImpl("/base.html", "/foo/other.html", "foo/other.html");
    processHrefImpl("/base.html", "/foo/base/other.html", "foo/base/other.html");
    processHrefImpl("/base.html", "/foo/bar/other.html", "foo/bar/other.html");
  }

  @Test
  public void processHrefTC02() {
    processHrefImpl("/foo/base.html", "/foo/base.html", "base.html");
    processHrefImpl("/foo/base.html", "/other.html", "../other.html");
    processHrefImpl("/foo/base.html", "/foo/other.html", "other.html");
    processHrefImpl("/foo/base.html", "/foo/base/other.html", "base/other.html");
    processHrefImpl("/foo/base.html", "/foo/base.html/other.html", "base.html/other.html");
    processHrefImpl("/foo/base.html", "/foo/bar/other.html", "bar/other.html");
    processHrefImpl("/foo/base.html", "/foobar/other.html", "../foobar/other.html");
    processHrefImpl("/foo/base.html", "/bar/other.html", "../bar/other.html");
    processHrefImpl("/foo/base.html", "/bar/baz/other.html", "../bar/baz/other.html");
  }

  @Test
  public void processHrefTC03() {
    processHrefImpl("/foo/bar/base.html", "/foo/bar/base.html", "base.html");
    processHrefImpl("/foo/bar/base.html", "/other.html", "../../other.html");
    processHrefImpl("/foo/bar/base.html", "/foo/other.html", "../other.html");
    processHrefImpl("/foo/bar/base.html", "/foo/baz/other.html", "../baz/other.html");
  }

  private void processHrefImpl(String pathName, String href, String expected) {
    var result = player.processHref(pathName, href);

    assertEquals(result, expected);
  }

}