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
package objectos.http;

import static org.testng.Assert.assertEquals;

import objectox.http.HttpStatus0;
import objectox.http.RedirectionPojo;
import org.testng.annotations.Test;

public class RedirectionTest {

  @Test
  public void found() {
    final Redirection res;
    res = Redirection.found("/foo");

    final RedirectionPojo pojo;
    pojo = (RedirectionPojo) res;

    assertEquals(pojo.status(), HttpStatus0.FOUND);

    assertEquals(pojo.location(), "/foo");
  }

  @Test
  public void movedPermanently() {
    final Redirection res;
    res = Redirection.movedPermanently("/foo");

    final RedirectionPojo pojo;
    pojo = (RedirectionPojo) res;

    assertEquals(pojo.status(), HttpStatus0.MOVED_PERMANENTLY);

    assertEquals(pojo.location(), "/foo");
  }

  @Test
  public void seeOther() {
    final Redirection res;
    res = Redirection.seeOther("/foo");

    final RedirectionPojo pojo;
    pojo = (RedirectionPojo) res;

    assertEquals(pojo.status(), HttpStatus0.SEE_OTHER);

    assertEquals(pojo.location(), "/foo");
  }

}
