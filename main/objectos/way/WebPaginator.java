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

final record WebPaginator(Http.RequestTarget request, Sql.Page page, int firstRow, int lastRow, int rowCount, int previousPage, int nextPage) implements Web.Paginator {

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
    String value;
    value = Integer.toString(page);

    if (request instanceof HttpExchange line) {
      return line.rawValue("page", value);
    } else {
      throw new UnsupportedOperationException("Implement me");
    }
  }

}
