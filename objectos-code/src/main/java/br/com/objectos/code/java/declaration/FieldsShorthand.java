/*
 * Copyright (C) 2014-2022 Objectos Software LTDA.
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
package br.com.objectos.code.java.declaration;

import objectos.lang.Check;

public class FieldsShorthand implements ClassCodeElement, InterfaceCodeElement {

  private final Iterable<FieldCode> fields;

  private FieldsShorthand(Iterable<FieldCode> fields) {
    this.fields = fields;
  }

  public static FieldsShorthand fields(Iterable<FieldCode> fields) {
    Check.notNull(fields, "fields == null");
    return new FieldsShorthand(fields);
  }

  @Override
  public final void acceptClassCodeBuilder(ClassCode.Builder builder) {
    builder.addFields(fields);
  }

  @Override
  public final void acceptInterfaceCodeBuilder(InterfaceCode.Builder builder) {
    builder.addFields(fields);
  }

}