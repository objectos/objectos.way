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

import java.util.Objects;

final class WebPaginatorConfig implements Web.Paginator.Config {

  int pageSize = 15;

  String parameterName = "page";

  Http.RequestTarget requestTarget;

  int rowCount;

  @Override
  public final void pageSize(int value) {
    if (value < 1) {
      throw new IllegalArgumentException("pageSize must be greater than zero");
    }

    pageSize = value;
  }

  @Override
  public final void parameterName(String value) {
    if (value.isBlank()) {
      throw new IllegalArgumentException("parameterName must not be blank");
    }

    parameterName = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void requestTarget(Http.RequestTarget value) {
    requestTarget = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void rowCount(int value) {
    if (value < 0) {
      throw new IllegalArgumentException("rowCount cannot be negative");
    }

    rowCount = value;
  }

  final WebPaginator build() {
    if (requestTarget == null) {
      throw new IllegalArgumentException("requestTarget was not set");
    }

    int pageNumber;
    pageNumber = requestTarget.queryParamAsInt(parameterName, 1);

    int previousPage;
    previousPage = pageNumber - 1;

    int nextPage;
    nextPage = pageNumber > 0 ? pageNumber + 1 : 2;

    int firstRow;
    firstRow = 1;

    int lastRow;
    lastRow = pageSize;

    if (pageNumber > 0) {
      int zeroBasedNumber;
      zeroBasedNumber = pageNumber - 1;

      firstRow = zeroBasedNumber * pageSize + 1;

      lastRow = firstRow + pageSize - 1;
    }

    if (lastRow >= rowCount) {
      lastRow = rowCount;

      nextPage = 0;
    }

    Sql.Page page;
    page = Sql.Page.of(pageNumber, pageSize);

    return new WebPaginator(requestTarget, page, firstRow, lastRow, rowCount, previousPage, nextPage);
  }

}