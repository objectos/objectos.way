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

import objectos.sql.Page;
import org.testng.annotations.Test;

public class WebPaginatorTest {

  @Test(description = "First page, single page")
  public void testCase01() {
    Http.Request.Target target = Http.parseRequestTarget("/foo");
    int pageSize = 15, totalCount = 8;

    WebPaginator paginator;
    paginator = WebPaginator.of(target, "page", pageSize, totalCount);

    Page page;
    page = paginator.current();

    assertEquals(page.number(), 1);
    assertEquals(page.size(), 15);

    assertEquals(paginator.firstItem(), 1);
    assertEquals(paginator.lastItem(), 8);
    assertEquals(paginator.hasPrevious(), false);
    assertEquals(paginator.hasNext(), false);
    assertEquals(paginator.previousHref(), "#");
    assertEquals(paginator.nextHref(), "#");
  }
  
}
