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
package objectos.html.internal;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

public class HtmlPlayerTest {

  private final HtmlPlayer player = new HtmlPlayer();

  @Test
  public void processHrefTestCase01() {
    processHrefImpl("base.html", "other.html", "other.html");
    processHrefImpl("base.html", "foo/other.html", "foo/other.html");
    processHrefImpl("base.html", "foo/bar/other.html", "foo/bar/other.html");
  }

  private void processHrefImpl(String pathName, String href, String expected) {
    var result = player.processHref(pathName, href);

    assertEquals(result, expected);
  }

}