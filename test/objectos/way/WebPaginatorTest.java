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
    Web.Paginator paginator;
    paginator = Web.Paginator.create(config -> {
      config.requestTarget(HttpExchange.parseRequestTarget("/foo"));

      config.pageSize(15);

      config.rowCount(8);
    });

    assertEquals(paginator.firstRow(), 1);
    assertEquals(paginator.lastRow(), 8);
    assertEquals(paginator.hasPrevious(), false);
    assertEquals(paginator.hasNext(), false);
    assertEquals(paginator.previousHref(), "#");
    assertEquals(paginator.nextHref(), "#");
  }

  @Test(description = "First page, multiple pages")
  public void testCase02() {
    Web.Paginator paginator;
    paginator = Web.Paginator.create(config -> {
      config.requestTarget(HttpExchange.parseRequestTarget("/foo"));

      config.pageSize(15);

      config.rowCount(16);
    });

    assertEquals(paginator.firstRow(), 1);
    assertEquals(paginator.lastRow(), 15);
    assertEquals(paginator.hasPrevious(), false);
    assertEquals(paginator.hasNext(), true);
    assertEquals(paginator.previousHref(), "#");
    assertEquals(paginator.nextHref(), "/foo?page=2");
  }

  @Test(description = "First page, multiple pages, explicit query param")
  public void testCase03() {
    Web.Paginator paginator;
    paginator = Web.Paginator.create(config -> {
      config.requestTarget(HttpExchange.parseRequestTarget("/foo?page=1"));

      config.pageSize(15);

      config.rowCount(16);
    });

    assertEquals(paginator.firstRow(), 1);
    assertEquals(paginator.lastRow(), 15);
    assertEquals(paginator.hasPrevious(), false);
    assertEquals(paginator.hasNext(), true);
    assertEquals(paginator.previousHref(), "#");
    assertEquals(paginator.nextHref(), "/foo?page=2");
  }

  @Test(description = "Last page")
  public void testCase04() {
    Web.Paginator paginator;
    paginator = Web.Paginator.create(config -> {
      config.requestTarget(HttpExchange.parseRequestTarget("/foo?page=2"));

      config.pageSize(15);

      config.rowCount(16);
    });

    assertEquals(paginator.firstRow(), 16);
    assertEquals(paginator.lastRow(), 16);
    assertEquals(paginator.hasPrevious(), true);
    assertEquals(paginator.hasNext(), false);
    assertEquals(paginator.previousHref(), "/foo?page=1");
    assertEquals(paginator.nextHref(), "#");
  }

  @Test(description = "Middle page")
  public void testCase05() {
    Web.Paginator paginator;
    paginator = Web.Paginator.create(config -> {
      config.requestTarget(HttpExchange.parseRequestTarget("/foo?page=3"));

      config.pageSize(10);

      config.rowCount(50);
    });

    assertEquals(paginator.firstRow(), 21);
    assertEquals(paginator.lastRow(), 30);
    assertEquals(paginator.hasPrevious(), true);
    assertEquals(paginator.hasNext(), true);
    assertEquals(paginator.previousHref(), "/foo?page=2");
    assertEquals(paginator.nextHref(), "/foo?page=4");
  }

  @Test(description = "keep existing query parameters")
  public void testCase06() {
    Web.Paginator paginator;
    paginator = Web.Paginator.create(config -> {
      config.requestTarget(HttpExchange.parseRequestTarget("/foo?q=abc&page=3"));

      config.pageSize(10);

      config.rowCount(50);
    });

    assertEquals(paginator.firstRow(), 21);
    assertEquals(paginator.lastRow(), 30);
    assertEquals(paginator.hasPrevious(), true);
    assertEquals(paginator.hasNext(), true);
    assertEquals(paginator.previousHref(), "/foo?q=abc&page=2");
    assertEquals(paginator.nextHref(), "/foo?q=abc&page=4");
  }

  @Test(description = "Last page, full size")
  public void testCase07() {
    Web.Paginator paginator;
    paginator = Web.Paginator.create(config -> {
      config.requestTarget(HttpExchange.parseRequestTarget("/foo?page=2"));

      config.pageSize(15);

      config.rowCount(30);
    });

    assertEquals(paginator.firstRow(), 16);
    assertEquals(paginator.lastRow(), 30);
    assertEquals(paginator.hasPrevious(), true);
    assertEquals(paginator.hasNext(), false);
    assertEquals(paginator.previousHref(), "/foo?page=1");
    assertEquals(paginator.nextHref(), "#");
  }

  @Test(description = "First page, full size")
  public void testCase08() {
    Web.Paginator paginator;
    paginator = Web.Paginator.create(config -> {
      config.requestTarget(HttpExchange.parseRequestTarget("/foo?page=1"));

      config.pageSize(15);

      config.rowCount(15);
    });

    assertEquals(paginator.firstRow(), 1);
    assertEquals(paginator.lastRow(), 15);
    assertEquals(paginator.hasPrevious(), false);
    assertEquals(paginator.hasNext(), false);
    assertEquals(paginator.previousHref(), "#");
    assertEquals(paginator.nextHref(), "#");
  }

}
