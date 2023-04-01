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
package objectos.selfgen.html;

import objectos.code.ArrayTypeName;
import objectos.code.ParameterizedTypeName;

final class StandardAttributeNameStep extends ThisTemplate {

  private static final ParameterizedTypeName MAP = ParameterizedTypeName.of(
    UNMODIFIABLE_MAP, STRING, STD_ATTR_NAME
  );

  @Override
  protected final void definition() {
    packageDeclaration(HTML_TMPL);

    autoImports();

    enumDeclaration(
      PUBLIC, name(STD_ATTR_NAME),
      implementsClause(ATTRIBUTE_NAME),
      include(this::standardAttributeName)
    );
  }

  private void standardAttributeName() {
    for (var attribute : spec.attributes()) {
      var kind = attribute.kind();

      enumConstant(
        name(attribute.constantName),
        argument(ATTRIBUTE_KIND, n(kind.name())),
        argument(s(attribute.name()))
      );
    }

    var arrayType = ArrayTypeName.of(STD_ATTR_NAME);

    field(
      PRIVATE, STATIC, FINAL, arrayType, name("ARRAY"),
      STD_ATTR_NAME, v("values")
    );

    field(
      PRIVATE, STATIC, FINAL, MAP, name("MAP"), v("mapInit")
    );

    field(
      PRIVATE, FINAL, ATTRIBUTE_KIND, name("kind")
    );

    field(
      PRIVATE, FINAL, STRING, name("name")
    );

    constructor(
      parameter(ATTRIBUTE_KIND, name("kind")),
      parameter(STRING, name("name")),

      p(THIS, n("kind"), IS, n("kind")),
      p(THIS, n("name"), IS, n("name"))
    );

    method(
      PUBLIC, STATIC, STD_ATTR_NAME, name("getByCode"),
      parameter(INT, name("code")),
      p(RETURN, n("ARRAY"), dim(n("code")))
    );

    method(
      PUBLIC, STATIC, STD_ATTR_NAME, name("getByName"),
      parameter(STRING, name("name")),
      p(RETURN, n("MAP"), v("get"), argument(n("name")))
    );

    method(
      PUBLIC, STATIC, INT, name("size"),
      p(RETURN, n("ARRAY"), n("length"))
    );

    method(
      PRIVATE, STATIC, MAP, name("mapInit"),
      include(this::standardAttributeNameMapInit)
    );

    method(
      annotation(OVERRIDE),
      PUBLIC, FINAL, INT, name("getCode"),
      p(RETURN, v("ordinal"))
    );

    method(
      annotation(OVERRIDE),
      PUBLIC, FINAL, ATTRIBUTE_KIND, name("getKind"),
      p(RETURN, n("kind"))
    );

    method(
      annotation(OVERRIDE),
      PUBLIC, FINAL, STRING, name("getName"),
      p(RETURN, n("name"))
    );
  }

  private void standardAttributeNameMapInit() {
    p(VAR, name("builder"), NEW, NAMES_BUILDER);

    for (var attribute : spec.attributes()) {
      p(n("builder"), v("put"), argument(s(attribute.name())), argument(n(attribute.constantName)));
    }

    p(RETURN, n("builder"), v("build"));
  }

}
