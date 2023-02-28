/*
 * Copyright (C) 2014-2023 Objectos Software LTDA.
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
package objectos.code.internal;

import java.util.Arrays;
import java.util.Objects;
import objectos.code.ClassTypeName;
import objectos.code.ParameterizedTypeName;
import objectos.code.tmpl.ReferenceTypeName;

/**
 * TODO
 *
 * @since 0.4.2
 */
public final class ParameterizedTypeNameImpl extends External implements ParameterizedTypeName {
  private final ClassTypeName raw;

  private final ReferenceTypeName first;

  private final ReferenceTypeName[] rest;

  ParameterizedTypeNameImpl(ClassTypeName raw,
                            ReferenceTypeName first,
                            ReferenceTypeName[] rest) {
    this.raw = raw;
    this.first = first;
    this.rest = rest;
  }

  public static ParameterizedTypeNameImpl of(
      ClassTypeName raw, ReferenceTypeName first, ReferenceTypeName... rest) {
    Objects.requireNonNull(raw, "raw == null");
    Objects.requireNonNull(first, "first == null");

    for (int i = 0; i < rest.length; i++) {
      var e = rest[i];

      if (e == null) {
        throw new NullPointerException("rest[" + i + "] == null");
      }
    }

    return new ParameterizedTypeNameImpl(raw, first, Arrays.copyOf(rest, rest.length));
  }

  @Override
  public final void execute(InternalApi api) {
    Object[] many = rest;
    api.elemMany(ByteProto.PARAMETERIZED_TYPE, raw, first, many);
    api.localToExternal();
  }
}