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
package objectos.code;

import java.lang.annotation.Annotation;
import objectos.code.tmpl.Api;
import objectos.code.tmpl.Api.AtRef;
import objectos.code.tmpl.Api.ClassElement;
import objectos.code.tmpl.Api.ClassRef;
import objectos.code.tmpl.Api.ExpressionElement;
import objectos.code.tmpl.Api.ExtendsRef;
import objectos.code.tmpl.Api.FinalRef;
import objectos.code.tmpl.Api.IdentifierRef;
import objectos.code.tmpl.Api.LiteralRef;
import objectos.code.tmpl.Api.LocalVariableDeclarationRef;
import objectos.code.tmpl.Api.MethodElement;
import objectos.code.tmpl.Api.MethodRef;
import objectos.lang.Check;

public abstract class JavaTemplate {

  public interface Renderer {

    void annotationEnd();

    void annotationStart();

    void blockAfterLastItem();

    void blockBeforeFirstItem();

    void blockBeforeNextItem();

    void blockEnd();

    void blockStart();

    void classEnd();

    void classStart();

    void compilationUnitEnd();

    void compilationUnitStart();

    void identifier(String name);

    void keyword(String keyword);

    void methodEnd();

    void methodStart();

    void modifier(String name);

    void name(String name);

    void packageEnd();

    void packageStart();

    void parameterListEnd();

    void parameterListStart();

    void semicolon();

    void separator(char c);

    void statementEnd();

    void statementStart();

    void stringLiteral(String s);

  }

  private Api api;

  /**
   * Sole constructor.
   */
  protected JavaTemplate() {}

  public final void acceptJavaGenerator(JavaGenerator generator) {
    Check.state(this.api == null, """
    Another code generation is already is progress.
    """);
    Check.notNull(generator, "generator == null");

    api = generator.pass0;

    try {
      definition();
    } finally {
      this.api = null;
    }
  }

  public final void eval(Api api) {
    Check.state(this.api == null, """
    Another evaluation is already is progress.
    """);

    this.api = Check.notNull(api, "api == null");

    definition();
  }

  protected final ClassRef _class(ClassElement... elements) {
    api._class(elements.length); // implicit elements null check

    return Api.REF;
  }

  protected final ExtendsRef _extends(ClassName superclass) {
    api._extends(superclass);

    return Api.REF;
  }

  protected final FinalRef _final() {
    api._final();

    return Api.REF;
  }

  protected final void _package(String packageName) {
    api._package(packageName);
  }

  protected final AtRef annotation(Class<? extends Annotation> annotationType) {
    var name = ClassName.of(annotationType); // implicit null-check

    api.className(name);

    api.annotation(1);

    return Api.REF;
  }

  protected final void autoImports() {
    api.autoImports();
  }

  protected abstract void definition();

  protected final IdentifierRef id(String name) {
    api.id(name);

    return Api.REF;
  }

  protected final MethodRef method(MethodElement... elements) {
    api.method(elements.length); // implicit elements null check

    return Api.REF;
  }

  protected final LiteralRef s(String value) {
    api.stringLiteral(value);

    return Api.REF;
  }

  protected final LocalVariableDeclarationRef var(IdentifierRef id, ExpressionElement expression) {
    api.localVariable(2);

    return Api.REF;
  }

}