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
package objectos.css.sheet;

import java.util.Locale;
import objectos.css.config.framework.ConfigurationDsl.FrameworkAtMediaElement;

public enum MediaType implements AtMediaElement, FrameworkAtMediaElement, MediaQuery {

  ALL,

  SCREEN,

  PRINT;

  public interface Visitor {

    void visitMediaType(MediaType type);

  }

  private static final MediaType[] ARRAY = values();

  private final String name = name().toLowerCase(Locale.US);

  public static MediaType getByCode(int code) {
    return ARRAY[code];
  }

  @Override
  public final void acceptFrameworkObjectVisitor(Object visitor) {
    if (visitor instanceof Visitor) {
      Visitor thisVisitor = (Visitor) visitor;
      thisVisitor.visitMediaType(this);
    }
  }

  public final int getCode() {
    return ordinal();
  }

  public final String getName() {
    return name;
  }

  public final boolean isAll() {
    return this == ALL;
  }

}
