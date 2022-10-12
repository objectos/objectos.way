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

import br.com.objectos.code.java.element.AbstractImmutableCodeElement;
import br.com.objectos.code.java.element.CodeElement;
import br.com.objectos.code.java.expression.Identifier;
import br.com.objectos.code.java.io.CodeWriter;
import br.com.objectos.code.java.io.JavaFile;
import br.com.objectos.code.java.io.NewLineFormatting.NewLineFormattingAction;
import br.com.objectos.code.java.type.NamedClass;
import objectos.lang.Check;
import objectos.util.UnmodifiableList;

abstract class AbstractTypeCode extends AbstractImmutableCodeElement implements TypeCode {

  AbstractTypeCode(CodeElement... elements) {
    super(elements);
  }

  AbstractTypeCode(UnmodifiableList<CodeElement> elements) {
    super(elements);
  }

  static CodeElement popSimpleName() {
    return PopSimpleName.INSTANCE;
  }

  static CodeElement pushSimpleName(Identifier simpleName) {
    return new PushSimpleName(simpleName);
  }

  @Override
  public final void acceptClassCodeBuilder(ClassCode.Builder builder) {
    builder.addType(this);
  }

  @Override
  public final void acceptJavaFileBuilder(JavaFile.Builder builder) {
    builder.addType(this);
  }

  @Override
  public final void acceptNewLineFormattingAction(NewLineFormattingAction action) {
    action.propagateElement(this);
  }

  @Override
  public final NamedClass className(PackageName packageName) {
    return packageName.nestedClass(simpleName());
  }

  @Override
  public final Kind kind() {
    return Kind.TYPE;
  }

  @Override
  public abstract String simpleName();

  public final JavaFile toJavaFile(PackageName packageName) {
    Check.notNull(packageName, "packageName == null");

    return new JavaFile(packageName, this);
  }

  private enum PopSimpleName implements CodeElement {

    INSTANCE;

    @Override
    public final CodeWriter acceptCodeWriter(CodeWriter w) {
      w.popSimpleName();
      return w;
    }

  }

  private static class PushSimpleName implements CodeElement {

    private final Identifier simpleName;

    PushSimpleName(Identifier simpleName) {
      this.simpleName = simpleName;
    }

    @Override
    public final CodeWriter acceptCodeWriter(CodeWriter w) {
      w.pushSimpleName(simpleName.name());
      return w;
    }

  }

}