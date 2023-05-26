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
package br.com.objectos.css.type;

public final class ColorName extends Color {

  private final int code;
  private final String name;

  ColorName(int code, String name) {
    this.code = code;
    this.name = name;
  }

  @Override
  public final <R> R accept(ColorVisitor<R> visitor) {
    return visitor.visitNamedColor(this);
  }

  @Override
  public final void acceptValueCreator(Creator creator) {
    // noop
  }

  @Override
  public final void acceptValueMarker(Marker marker) {
    marker.addColor(this);
  }

  public final int getCode() {
    return code;
  }

  public final String getJavaName() {
    return name;
  }

  public final String getName() {
    return name;
  }

  @Override
  public final int hashCode() {
    return code;
  }

  @Override
  public final String toString() {
    return name;
  }

  @Override
  final boolean equalsImpl(Color obj) {
    ColorName that = (ColorName) obj;
    return code == that.code;
  }

}
