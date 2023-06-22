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
package br.com.objectos.http.media;

import java.util.Locale;

public class MediaTypes {

  private MediaTypes() {}

  static String qualifiedNameImpl(Enum<? extends MediaType> subtype) {
    return qualifiedNameImpl(subtype, subtypeNameImpl(subtype));
  }

  static String qualifiedNameImpl(Enum<? extends MediaType> subtype, String subtypeName) {
    MediaType mediaType;
    mediaType = (MediaType) subtype;

    TopLevel topLevel;
    topLevel = mediaType.getTopLevel();

    return topLevel.getSimpleName() + '/' + subtypeName;
  }

  static String subtypeNameImpl(Enum<? extends MediaType> subType) {
    return subType.name().toLowerCase(Locale.US).replace('_', '-');
  }

}
