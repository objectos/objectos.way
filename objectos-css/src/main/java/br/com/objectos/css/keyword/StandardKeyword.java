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
package br.com.objectos.css.keyword;

import br.com.objectos.css.type.Creator;
import br.com.objectos.css.type.Marker;

public abstract class StandardKeyword extends Keyword {

  private final int code;
  private final String javaName;
  private final String name;

  protected StandardKeyword(int code, String javaName, String name) {
    this.code = code;
    this.javaName = javaName;
    this.name = name;
  }

  @Override
  public final void acceptValueCreator(Creator creator) {
    // noop
  }

  @Override
  public final void acceptValueMarker(Marker marker) {
    marker.addKeyword(this);
  }

  public final int getCode() {
    return code;
  }

  @Override
  public final String getJavaName() {
    return javaName;
  }

  @Override
  public final String getName() {
    return name;
  }

  @Override
  public final int hashCode() {
    return code;
  }

  @Override
  protected final boolean equalsImpl(Keyword obj) {
    StandardKeyword that = (StandardKeyword) obj;
    return code == that.code;
  }

}
