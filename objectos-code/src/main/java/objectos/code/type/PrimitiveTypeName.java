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
package objectos.code.type;

import objectos.code.internal.Keyword;
import objectos.code.internal.PrimitiveTypeNameImpl;
import objectos.code.tmpl.ArrayTypeComponent;
import objectos.code.tmpl.StatementPart;

/**
 * TODO
 *
 * @since 0.4.2
 */
public sealed interface PrimitiveTypeName
    extends TypeName, ArrayTypeComponent, StatementPart permits PrimitiveTypeNameImpl {

  /**
   * The {@code boolean} primitive type.
   */
  PrimitiveTypeName BOOLEAN = new PrimitiveTypeNameImpl(Keyword.BOOLEAN);

  /**
   * The {@code double} primitive type.
   */
  PrimitiveTypeName DOUBLE = new PrimitiveTypeNameImpl(Keyword.DOUBLE);

  /**
   * The {@code int} primitive type.
   */
  PrimitiveTypeName INT = new PrimitiveTypeNameImpl(Keyword.INT);

}