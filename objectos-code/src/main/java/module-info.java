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
module br.com.objectos.code {
  exports br.com.objectos.code.java;
  exports br.com.objectos.code.java.declaration;
  exports br.com.objectos.code.java.element;
  exports br.com.objectos.code.java.expression;
  exports br.com.objectos.code.java.expression.production;
  exports br.com.objectos.code.java.io;
  exports br.com.objectos.code.java.statement;
  exports br.com.objectos.code.java.type;
  exports br.com.objectos.code.model;
  exports br.com.objectos.code.model.element;
  exports br.com.objectos.code.processing;
  exports br.com.objectos.code.processing.type;
  exports br.com.objectos.code.util;
  exports objectos.code;

  requires transitive objectos.util;
  requires transitive java.compiler;

  requires br.com.objectos.code.annotations;
  requires objectos.lang;
}