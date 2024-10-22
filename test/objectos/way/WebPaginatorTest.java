/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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

public class WebPaginatorTest {

  @Test(description = "First page, single page")
  public void testCase01() {
    Http.RequestTarget target = Http.parseRequestTarget("/foo");
    int pageSize = 15, totalCount = 8;

    WebPaginator paginator;
    paginator = WebPaginator.of(target, "page", pageSize, totalCount);

    assertEquals(paginator.page().number(), 1);
    assertEquals(paginator.page().size(), 15);
    assertEquals(paginator.firstItem(), 1);
    assertEquals(paginator.lastItem(), 8);
    assertEquals(paginator.hasPrevious(), false);
    assertEquals(paginator.hasNext(), false);
    assertEquals(paginator.previousHref(), "#");
    assertEquals(paginator.nextHref(), "#");
  }

  @Test(description = "First page, multiple pages")
  public void testCase02() {
    Http.RequestTarget target = Http.parseRequestTarget("/foo");
    int pageSize = 15, totalCount = 16;

    WebPaginator paginator;
    paginator = WebPaginator.of(target, "page", pageSize, totalCount);

    assertEquals(paginator.page().number(), 1);
    assertEquals(paginator.page().size(), 15);
    assertEquals(paginator.firstItem(), 1);
    assertEquals(paginator.lastItem(), 15);
    assertEquals(paginator.hasPrevious(), false);
    assertEquals(paginator.hasNext(), true);
    assertEquals(paginator.previousHref(), "#");
    assertEquals(paginator.nextHref(), "/foo?page=2");
  }

  @Test(description = "First page, multiple pages, explicit query param")
  public void testCase03() {
    Http.RequestTarget target = Http.parseRequestTarget("/foo?page=1");
    int pageSize = 15, totalCount = 16;

    WebPaginator paginator;
    paginator = WebPaginator.of(target, "page", pageSize, totalCount);

    assertEquals(paginator.page().number(), 1);
    assertEquals(paginator.page().size(), 15);
    assertEquals(paginator.firstItem(), 1);
    assertEquals(paginator.lastItem(), 15);
    assertEquals(paginator.hasPrevious(), false);
    assertEquals(paginator.hasNext(), true);
    assertEquals(paginator.previousHref(), "#");
    assertEquals(paginator.nextHref(), "/foo?page=2");
  }

  @Test(description = "Last page")
  public void testCase04() {
    Http.RequestTarget target = Http.parseRequestTarget("/foo?page=2");
    int pageSize = 15, totalCount = 16;

    WebPaginator paginator;
    paginator = WebPaginator.of(target, "page", pageSize, totalCount);

    assertEquals(paginator.page().number(), 2);
    assertEquals(paginator.page().size(), 15);
    assertEquals(paginator.firstItem(), 16);
    assertEquals(paginator.lastItem(), 16);
    assertEquals(paginator.hasPrevious(), true);
    assertEquals(paginator.hasNext(), false);
    assertEquals(paginator.previousHref(), "/foo?page=1");
    assertEquals(paginator.nextHref(), "#");
  }

  @Test(description = "Middle page")
  public void testCase05() {
    Http.RequestTarget target = Http.parseRequestTarget("/foo?page=3");
    int pageSize = 10, totalCount = 50;

    WebPaginator paginator;
    paginator = WebPaginator.of(target, "page", pageSize, totalCount);

    assertEquals(paginator.page().number(), 3);
    assertEquals(paginator.page().size(), 10);
    assertEquals(paginator.firstItem(), 21);
    assertEquals(paginator.lastItem(), 30);
    assertEquals(paginator.hasPrevious(), true);
    assertEquals(paginator.hasNext(), true);
    assertEquals(paginator.previousHref(), "/foo?page=2");
    assertEquals(paginator.nextHref(), "/foo?page=4");
  }

  @Test(description = "keep existing query parameters")
  public void testCase06() {
    Http.RequestTarget target = Http.parseRequestTarget("/foo?q=abc&page=3");
    int pageSize = 10, totalCount = 50;

    WebPaginator paginator;
    paginator = WebPaginator.of(target, "page", pageSize, totalCount);

    assertEquals(paginator.page().number(), 3);
    assertEquals(paginator.page().size(), 10);
    assertEquals(paginator.firstItem(), 21);
    assertEquals(paginator.lastItem(), 30);
    assertEquals(paginator.hasPrevious(), true);
    assertEquals(paginator.hasNext(), true);
    assertEquals(paginator.previousHref(), "/foo?q=abc&page=2");
    assertEquals(paginator.nextHref(), "/foo?q=abc&page=4");
  }

  @Test(description = "Last page, full size")
  public void testCase07() {
    Http.RequestTarget target = Http.parseRequestTarget("/foo?page=2");
    int pageSize = 15, totalCount = 30;

    WebPaginator paginator;
    paginator = WebPaginator.of(target, "page", pageSize, totalCount);

    assertEquals(paginator.page().number(), 2);
    assertEquals(paginator.page().size(), 15);
    assertEquals(paginator.firstItem(), 16);
    assertEquals(paginator.lastItem(), 30);
    assertEquals(paginator.hasPrevious(), true);
    assertEquals(paginator.hasNext(), false);
    assertEquals(paginator.previousHref(), "/foo?page=1");
    assertEquals(paginator.nextHref(), "#");
  }

  @Test(description = "First page, full size")
  public void testCase08() {
    Http.RequestTarget target = Http.parseRequestTarget("/foo?page=1");
    int pageSize = 15, totalCount = 15;

    WebPaginator paginator;
    paginator = WebPaginator.of(target, "page", pageSize, totalCount);

    assertEquals(paginator.page().number(), 1);
    assertEquals(paginator.page().size(), 15);
    assertEquals(paginator.firstItem(), 1);
    assertEquals(paginator.lastItem(), 15);
    assertEquals(paginator.hasPrevious(), false);
    assertEquals(paginator.hasNext(), false);
    assertEquals(paginator.previousHref(), "#");
    assertEquals(paginator.nextHref(), "#");
  }

}
