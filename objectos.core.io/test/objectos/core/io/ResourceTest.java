/*
 * Copyright (C) 2021-2023 Objectos Software LTDA.
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
package objectos.core.io;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import objectos.util.list.UnmodifiableList;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ResourceTest {

  @Test
  public void equalsTest() {
    Resource a;
    a = Resource.getResource(getClass(), "resource0.txt");

    Resource b;
    b = Resource.getResource(getClass(), "resource0.txt");

    Resource c;
    c = Resource.getResource("resource0.txt");

    Resource d;
    d = Resource.getResource("resource0.txt");

    assertTrue(a.equals(a));

    assertTrue(a.equals(b));

    assertTrue(b.equals(a));

    assertTrue(c.equals(d));

    assertTrue(d.equals(c));

    assertFalse(a.equals(null));

    assertFalse(a.equals(c));
  }

  @Test
  public void getResource() throws IOException {
    Resource resource;
    resource = Resource.getResource(getClass(), "resource0.txt");

    UnmodifiableList<String> result;
    result = Read.lines(resource, Charsets.utf8());

    assertEquals(result.size(), 2);

    assertEquals(result.get(0), "xyz");
    assertEquals(result.get(1), "789");
  }

  @Test
  public void getResourceErrors() {
    try {
      Resource.getResource("i-do-not-exist");

      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "Resource i-do-not-exist not found.");
    }

    try {
      Resource.getResource("/no-absolute");

      Assert.fail();
    } catch (IllegalArgumentException expected) {
      assertEquals(expected.getMessage(), "resourceName must not start with a '/'");
    }
  }

  @Test
  public void toStringTest() {
    Resource r;
    r = Resource.getResource("resource0.txt");

    String result = r.toString();

    assertTrue(result.startsWith("Resource ["));

    assertTrue(result.contains("file:"));

    assertTrue(result.contains("resource0.txt"));

    assertTrue(result.endsWith("]"));
  }

  @Test
  public void toUri() throws URISyntaxException {
    Resource r;
    r = Resource.getResource("resource0.txt");

    assertNotNull(r.toUri());
  }

}