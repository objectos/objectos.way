/*
 * Copyright (C) 2025 Objectos Software LTDA.
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

final class HttpHeaderNameCustom implements Http.HeaderName {

  private final String capitalized;

  public HttpHeaderNameCustom(String capitalized) {
    this.capitalized = capitalized;
  }

  @Override
  public final int index() {
    return -1;
  }

  @Override
  public final String capitalized() {
    return capitalized;
  }

  @Override
  public final boolean equals(Object obj) {
    return obj == this || obj instanceof HttpHeaderNameCustom that
        && capitalized.equals(that.capitalized);
  }

  @Override
  public final int hashCode() {
    return capitalized.hashCode();
  }

}
