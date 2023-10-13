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

import objectos.http.Segment;
import org.testng.annotations.Test;

public class HttpRequestPathTest {

  private static final Segment ANY = Segment.ofAny();

  private static final Segment EMPTY = Segment.of();

  private static final Segment FOO = Segment.of("foo");

  @Test
  public void root() {
    byte[] bytes;
    bytes = Bytes.utf8("/[GARBAGE]");

    HttpRequestPath path;
    path = new HttpRequestPath(bytes);

    path.slash(0);

    path.end(1);

    assertEquals(path.toString(), "/");
    assertEquals(path.segmentCount(), 1);
    assertEquals(path.segment(0), "");

    // matcher

    assertEquals(path.matches(EMPTY), true);
    assertEquals(path.matches(ANY), true);
    assertEquals(path.matches(FOO), false);
    assertEquals(path.matches(EMPTY, EMPTY), false);
    assertEquals(path.matches(FOO, EMPTY), false);
  }

  @Test
  public void single() {
    byte[] bytes;
    bytes = Bytes.utf8("/foo[GARBAGE]");

    HttpRequestPath path;
    path = new HttpRequestPath(bytes);

    path.slash(0);

    path.end(4);

    assertEquals(path.toString(), "/foo");
    assertEquals(path.segmentCount(), 1);
    assertEquals(path.segment(0), "foo");

    // matcher

    assertEquals(path.matches(EMPTY), false);
    assertEquals(path.matches(ANY), true);
    assertEquals(path.matches(FOO), true);
    assertEquals(path.matches(EMPTY, EMPTY), false);
    assertEquals(path.matches(FOO, EMPTY), false);
  }

  @Test
  public void singleWithSlash() {
    byte[] bytes;
    bytes = Bytes.utf8("/foo/[GARBAGE]");

    HttpRequestPath path;
    path = new HttpRequestPath(bytes);

    path.slash(0);
    path.slash(4);
    path.end(5);

    assertEquals(path.toString(), "/foo/");
    assertEquals(path.segmentCount(), 2);
    assertEquals(path.segment(0), "foo");
    assertEquals(path.segment(1), "");

    // matcher

    assertEquals(path.matches(EMPTY), false);
    assertEquals(path.matches(ANY), false);
    assertEquals(path.matches(FOO), false);
    assertEquals(path.matches(EMPTY, EMPTY), false);
    assertEquals(path.matches(FOO, EMPTY), true);
    assertEquals(path.matches(FOO, ANY), true);
    assertEquals(path.matches(ANY, ANY), true);
  }

  @Test
  public void two() {
    byte[] bytes;
    bytes = Bytes.utf8("/a/b[GARBAGE]");

    HttpRequestPath path;
    path = new HttpRequestPath(bytes);

    path.slash(0);
    path.slash(2);
    path.end(4);

    assertEquals(path.toString(), "/a/b");
    assertEquals(path.segmentCount(), 2);
    assertEquals(path.segment(0), "a");
    assertEquals(path.segment(1), "b");

    // matcher

    Segment a = Segment.of("a");
    Segment b = Segment.of("b");

    assertEquals(path.matches(EMPTY), false);
    assertEquals(path.matches(ANY), false);
    assertEquals(path.matches(FOO), false);
    assertEquals(path.matches(ANY, ANY), true);
    assertEquals(path.matches(a, b), true);
    assertEquals(path.matches(a, Segment.of("B")), false);
    assertEquals(path.matches(ANY, b), true);
    assertEquals(path.matches(a, ANY), true);
  }

  @Test
  public void forceArrayResize() {
    byte[] bytes;
    bytes = Bytes.utf8("/a/b/c/d/e/f/g/h/i[GARBAGE]");

    HttpRequestPath path;
    path = new HttpRequestPath(bytes);

    path.slash = new int[2];

    path.slash(0);
    path.slash(2);
    path.slash(4);
    path.slash(6);
    path.slash(8);
    path.slash(10);
    path.slash(12);
    path.slash(14);
    path.slash(16);
    path.end(18);

    assertEquals(path.toString(), "/a/b/c/d/e/f/g/h/i");
    assertEquals(path.segmentCount(), 9);
    assertEquals(path.segment(0), "a");
    assertEquals(path.segment(1), "b");
    assertEquals(path.segment(2), "c");
    assertEquals(path.segment(3), "d");
    assertEquals(path.segment(4), "e");
    assertEquals(path.segment(5), "f");
    assertEquals(path.segment(6), "g");
    assertEquals(path.segment(7), "h");
    assertEquals(path.segment(8), "i");
  }

}