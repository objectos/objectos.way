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

import objectos.lang.Check;

public abstract class JavaTemplate extends AbstractJavaTemplate {

  public interface Renderer {

    void blockEnd();

    void blockStart();

    void classEnd();

    void classStart();

    void compilationUnitEnd();

    void compilationUnitStart();

    void identifier(String name);

    void keyword(Keyword keyword);

    void name(String name);

    void packageEnd();

    void packageStart();

    void separator(Separator separator);

  }

  private Pass0 pass0;

  /**
   * Sole constructor.
   */
  protected JavaTemplate() {}

  public final void acceptJavaGenerator(JavaGenerator generator) {
    Check.state(this.pass0 == null, """
    Another code generation is already is progress.
    """);
    Check.notNull(generator, "generator == null");

    pass0 = generator.pass0;

    try {
      definition();
    } finally {
      this.pass0 = null;
    }
  }

  protected final ClassRef _class(ClassElement... elements) {
    pass0._class(elements.length);

    return Ref.INSTANCE;
  }

  protected final void _package(String packageName) {
    pass0._package(packageName);
  }

  protected abstract void definition();

  protected final IdentifierRef id(String name) {
    pass0.id(name);

    return Ref.INSTANCE;
  }

  final void pass0(Pass0 pass0) {
    this.pass0 = pass0;

    definition();
  }

}