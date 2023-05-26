/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package objectos.css.type;

import objectos.lang.Check;

public class Url implements ImageType {

  private final String value;

  public Url(String value) {
    this.value = Check.notNull(value, "value == null");
  }

  @Override
  public final void acceptValueCreator(Creator creator) {
    // noop
  }

  @Override
  public final void acceptValueMarker(Marker marker) {
    // noop
  }

  @Override
  public final boolean equals(Object obj) {
    if (!(obj instanceof Url)) {
      return false;
    }
    Url that = (Url) obj;
    return value.equals(that.value);
  }

  @Override
  public final int hashCode() {
    return value.hashCode();
  }

  @Override
  public final String toString() {
    return value;
  }

}