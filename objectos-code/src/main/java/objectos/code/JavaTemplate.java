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

  private JavaGeneratorImpl generator;

  /**
   * Sole constructor.
   */
  protected JavaTemplate() {}

  public final void acceptJavaGenerator(JavaGeneratorImpl generator) {
    Check.state(this.generator == null, """
    Another code generation is already is progress.
    """);

    this.generator = Check.notNull(generator, "generator == null");

    try {
      definition();

      this.generator.templateEnd();
    } finally {
      this.generator = null;
    }
  }

  protected final ClassRef _class(ClassElement... elements) {
    generator._class(elements.length);

    return Ref.INSTANCE;
  }

  protected abstract void definition();

  protected final IdentifierRef id(String name) {
    generator.id(name);

    return Ref.INSTANCE;
  }

}