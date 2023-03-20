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

import java.util.EnumSet;
import java.util.Set;
import objectos.html.spi.Marker;
import objectos.html.spi.Renderer;

/**
 * TODO
 */
public enum AttributeOrElement implements AnyElementValue {

  CLIPPATH(StandardAttributeName.CLIPPATH, StandardElementName.CLIPPATH) {
    private final Set<StandardElementName> attribute = EnumSet.of(
      StandardElementName.CLIPPATH,
      StandardElementName.DEFS,
      StandardElementName.G,
      StandardElementName.PATH,
      StandardElementName.SVG
    );

    @Override
    public final boolean isAttributeOf(StandardElementName element) {
      return attribute.contains(element);
    }
  },

  LABEL(StandardAttributeName.LABEL, StandardElementName.LABEL) {
    private final Set<StandardElementName> attribute = EnumSet.of(
      StandardElementName.OPTION
    );

    @Override
    public final boolean isAttributeOf(StandardElementName element) {
      return attribute.contains(element);
    }
  },

  TITLE(StandardAttributeName.TITLE, StandardElementName.TITLE) {
    @Override
    public final boolean isAttributeOf(StandardElementName element) {
      return !element.equals(StandardElementName.HEAD);
    }
  };

  private static final AttributeOrElement[] ALL = AttributeOrElement.values();

  private final int attributeByteCode;

  private final int elementByteCode;

  private AttributeOrElement(StandardAttributeName attribute, StandardElementName element) {
    this.attributeByteCode = attribute.getCode();
    this.elementByteCode = element.getCode();
  }

  public static AttributeOrElement get(int code) {
    return ALL[code];
  }

  public final int attributeByteCode() {
    return attributeByteCode;
  }

  public final int code() {
    return ordinal();
  }

  public final int elementByteCode() {
    return elementByteCode;
  }

  public abstract boolean isAttributeOf(StandardElementName element);

  @Override
  public final void mark(Marker marker) {
    // noop
  }

  @Override
  public final void render(Renderer renderer) {
    // noop
  }

}
