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
package objectos.http.media;

import java.util.Locale;

public enum TextType implements MediaType {

  CSS,

  HTML,

  PLAIN;

  private final String qualifiedName = MediaTypes.qualifiedNameImpl(this);

  private final String simpleName = name().toLowerCase(Locale.US);

  @Override
  public final void acceptMediaTypeVisitor(MediaTypeVisitor visitor) {
    visitor.visitTextType(this);
  }

  @Override
  public final String getSimpleName() {
    return simpleName;
  }

  @Override
  public final TopLevel getTopLevel() {
    return TopLevel.TEXT;
  }

  @Override
  public final String toString() {
    return qualifiedName;
  }

}
