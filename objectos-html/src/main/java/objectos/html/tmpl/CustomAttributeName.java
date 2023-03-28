/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html.tmpl;

import objectos.html.spi.Marker;
import objectos.html.spi.Renderer;

public sealed abstract class CustomAttributeName implements AttributeName, Value {

  public static final PathTo PATH_TO = new PathTo();

  private static final CustomAttributeName[] ARRAY = {
      PATH_TO
  };

  private final int code;

  private final AttributeKind kind;

  private final String name;

  CustomAttributeName(int code, AttributeKind kind, String name) {
    this.code = code;
    this.kind = kind;
    this.name = name;
  }

  public static CustomAttributeName getByCode(int code) {
    int index = code - StandardAttributeName.size();

    return ARRAY[index];
  }

  @Override
  public final int getCode() {
    return code;
  }

  @Override
  public final AttributeKind getKind() {
    return kind;
  }

  @Override
  public final String getName() {
    return name;
  }

  @Override
  public final void mark(Marker marker) {
    marker.markAttribute();
  }

  @Override
  public final void render(Renderer renderer) {}

  public static final class PathTo extends CustomAttributeName
      implements AValue, AnchorValue, LinkValue {
    private PathTo() {
      super(StandardAttributeName.size() + 0, AttributeKind.STRING, "href");
    }
  }

}
