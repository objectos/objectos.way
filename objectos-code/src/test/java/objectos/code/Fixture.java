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

final class Fixture extends JavaTemplate2 {

  enum Kind {
    STATIC_BLOCK,

    VOID_METHOD;
  }

  private final String className;

  private final Kind kind;

  JavaTemplate2 subject;

  public Fixture(String className, Kind kind) {
    this.className = className;
    this.kind = kind;
  }

  public final String ture(JavaTemplate2 subject) {
    this.subject = subject;

    var result = toString();

    this.subject = null;

    return result;
  }

  @Override
  protected final void definition() {
    _class(className);

    body(include(this::bodyImpl));
  }

  private void bodyImpl() {
    // @formatter:off
    switch (kind) {
      case STATIC_BLOCK -> {
        _static(); block(
          include(subject)
        );
      }

      case VOID_METHOD -> {
        _void(); id("method"); block(
          include(subject)
        );
      }
    }
    // @formatter:on
  }
}