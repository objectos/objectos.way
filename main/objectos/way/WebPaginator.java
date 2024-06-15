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

import objectos.sql.Page;
import objectos.way.Http.Request.Target.Query;

final record WebPaginator(Http.Request.Target target, Page current, int firstItem, int lastItem, int previousPage, int nextPage) implements Web.Paginator {

  private record ThisPage(int number, int size) implements Page {}

  public static WebPaginator of(Http.Request.Target target, String pageAttrName, int pageSize, int totalCount) {
    Query query;
    query = target.query();

    int pageNumber;
    pageNumber = query.getAsInt(pageAttrName, 1);

    int previousPage;
    previousPage = pageNumber - 1;

    int nextPage;
    nextPage = pageNumber > 0 ? pageNumber + 1 : 2;

    int firstItem;
    firstItem = 1;

    int lastItem;
    lastItem = pageSize;

    if (pageNumber > 0) {
      int zeroBasedNumber;
      zeroBasedNumber = pageNumber - 1;

      firstItem = zeroBasedNumber * pageSize + 1;

      lastItem = firstItem + pageSize - 1;
    }

    if (lastItem > totalCount) {
      lastItem = totalCount;

      nextPage = 0;
    }

    ThisPage current;
    current = new ThisPage(pageNumber, pageSize);

    return new WebPaginator(target, current, firstItem, lastItem, previousPage, nextPage);
  }

  @Override
  public final boolean hasPrevious() {
    return previousPage > 0;
  }

  @Override
  public final boolean hasNext() {
    return nextPage > 0;
  }

  @Override
  public final String previousHref() {
    return hasPrevious() ? pageHref(previousPage) : "#";
  }

  @Override
  public final String nextHref() {
    return hasNext() ? pageHref(nextPage) : "#";
  }

  private String pageHref(int page) {
    Http.Request.Target.Path path;
    path = target.path();

    String originalPath;
    originalPath = path.toString();

    StringBuilder href;
    href = new StringBuilder(originalPath);

    href.append('?');

    Http.Request.Target.Query query;
    query = target.query();

    query = query.set("page", Integer.toString(page));

    href.append(query.encodedValue());

    return href.toString();
  }

}
