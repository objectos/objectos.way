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
    path = new HttpRequestPath(bytes, 0);

    path.segment(1, 1);

    path.end(1);

    assertEquals(path.toString(), "/");
    assertEquals(path.nextSegment(), "");
    assertEquals(path.nextSegment(), null);
    assertEquals(path.nextSegment(), null);
  }

  @Test
  public void single() {
    byte[] bytes;
    bytes = Bytes.utf8("/foo");

    HttpRequestPath path;
    path = new HttpRequestPath(bytes, 0);

    path.segment(1, 4);

    path.end(4);

    assertEquals(path.toString(), "/foo");
    assertEquals(path.nextSegment(), "foo");
    assertEquals(path.nextSegment(), null);
    assertEquals(path.nextSegment(), null);
  }

  @Test
  public void singleWithSlash() {
    byte[] bytes;
    bytes = Bytes.utf8("/foo/");

    HttpRequestPath path;
    path = new HttpRequestPath(bytes, 0);

    path.segment(1, 4);

    path.segment(5, 5);

    path.end(5);

    assertEquals(path.toString(), "/foo/");
    assertEquals(path.nextSegment(), "foo");
    assertEquals(path.nextSegment(), "");
    assertEquals(path.nextSegment(), null);
    assertEquals(path.nextSegment(), null);
  }

  @Test
  public void two() {
    byte[] bytes;
    bytes = Bytes.utf8("/a/b");

    HttpRequestPath path;
    path = new HttpRequestPath(bytes, 0);

    path.segment(1, 2);

    path.segment(3, 4);

    path.end(4);

    assertEquals(path.toString(), "/a/b");
    assertEquals(path.nextSegment(), "a");
    assertEquals(path.nextSegment(), "b");
    assertEquals(path.nextSegment(), null);
    assertEquals(path.nextSegment(), null);
  }

  @Test
  public void forceArrayResize() {
    byte[] bytes;
    bytes = Bytes.utf8("/a/b/c/d/e/f/g/h/i");

    HttpRequestPath path;
    path = new HttpRequestPath(bytes, 0);

    path.segment(1, 2);

    path.segment(3, 4);

    path.segment(5, 6);

    path.segment(7, 8);

    path.segment(9, 10);

    path.segment(11, 12);

    path.segment(13, 14);

    path.segment(15, 16);

    path.segment(17, 18);

    path.end(18);

    assertEquals(path.toString(), "/a/b/c/d/e/f/g/h/i");
    assertEquals(path.nextSegment(), "a");
    assertEquals(path.nextSegment(), "b");
    assertEquals(path.nextSegment(), "c");
    assertEquals(path.nextSegment(), "d");
    assertEquals(path.nextSegment(), "e");
    assertEquals(path.nextSegment(), "f");
    assertEquals(path.nextSegment(), "g");
    assertEquals(path.nextSegment(), "h");
    assertEquals(path.nextSegment(), "i");
    assertEquals(path.nextSegment(), null);
    assertEquals(path.nextSegment(), null);
  }

}