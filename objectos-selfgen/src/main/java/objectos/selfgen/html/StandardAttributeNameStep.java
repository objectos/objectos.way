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

public final class StandardAttributeNameStep extends ThisTemplate {

  private static final ParameterizedTypeName MAP = ParameterizedTypeName.of(
    UNMODIFIABLE_MAP, STRING, STD_ATTR_NAME
  );

  private int counter;

  private AttributeSpec currentAttribute;

  @Override
  protected final void definition() {
    packageDeclaration(attr);

    autoImports();

    classDeclaration(
      PUBLIC, ABSTRACT, name(STD_ATTR_NAME),
      implementsClause(ATTRIBUTE_NAME, VALUE),
      include(this::standardAttributeName)
    );
  }

  private void standardAttributeName() {
    for (var attribute : spec.attributes()) {
      field(
        PUBLIC, STATIC, FINAL, attribute.className, name(attribute.constantName),
        NEW, attribute.className
      );
    }

    var arrayType = ArrayTypeName.of(STD_ATTR_NAME);

    field(
      PRIVATE, STATIC, FINAL, arrayType, name("ARRAY"),
      arrayInitializer(), include(this::standardAttributeNameArray)
    );

    field(
      PRIVATE, STATIC, FINAL, MAP, name("MAP"), v("mapInit")
    );

    field(
      PRIVATE, FINAL, INT, name("code")
    );

    field(
      PRIVATE, FINAL, ATTRIBUTE_KIND, name("kind")
    );

    field(
      PRIVATE, FINAL, STRING, name("name")
    );

    constructor(
      parameter(INT, name("code")),
      parameter(ATTRIBUTE_KIND, name("kind")),
      parameter(STRING, name("name")),

      p(THIS, n("code"), IS, n("code")),
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
      p(RETURN, n("code"))
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

    method(
      annotation(OVERRIDE),
      PUBLIC, FINAL, VOID, name("mark"),
      parameter(MARKER, name("marker")),
      p(n("marker"), v("markAttribute"))
    );

    method(
      annotation(OVERRIDE),
      PUBLIC, FINAL, VOID, name("render"),
      parameter(RENDERER, name("renderer"))
    );

    for (var attribute : spec.attributes()) {
      currentAttribute = attribute;

      var kind = currentAttribute.kind();

      classDeclaration(
        PUBLIC, STATIC, name(currentAttribute.className), extendsClause(STD_ATTR_NAME),

        currentAttribute.global()
            ? implementsClause(GLB_ATTR_NAME)
            : include(this::standardAttributeNameType),

        constructor(
          PRIVATE,
          p(
            SUPER,
            argument(i(counter++)),
            argument(ATTRIBUTE_KIND, n(kind.name())),
            argument(s(currentAttribute.name()))
          )
        )
      );
    }
  }

  private void standardAttributeNameArray() {
    code(NL);

    for (var attribute : spec.attributes()) {
      value(n(attribute.constantName()));

      code(NL);
    }
  }

  private void standardAttributeNameMapInit() {
    p(VAR, name("builder"), NEW, NAMES_BUILDER);

    for (var attribute : spec.attributes()) {
      p(n("builder"), v("put"), argument(s(attribute.name())), argument(n(attribute.constantName)));
    }

    p(RETURN, n("builder"), v("build"));
  }

  private void standardAttributeNameType() {
    for (var ifaceName : currentAttribute.interfaces()) {
      implementsClause(ifaceName);
    }
  }

}
