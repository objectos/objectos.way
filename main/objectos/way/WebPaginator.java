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
package objectos.way;

import objectos.http.Request;
import objectox.http.UrlEncoder;
import objectox.http.req.QueryString;

final record WebPaginator(Request request, Sql.Page page, int firstRow, int lastRow, int rowCount, int previousPage, int nextPage) implements Web.Paginator {

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
    final String value;
    value = Integer.toString(page);

    final QueryString queryString;
    queryString = QueryString.copyOf(request);

    queryString.put("page", value);

    final String query;
    query = queryString.toString();

    return encode(request.path()) + "?" + encode(query);
  }

  private String encode(String s) {
    final UrlEncoder encoder;
    encoder = new UrlEncoder(s);

    return encoder.encode();
  }

}
