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
package br.com.objectos.code.fakes;

import br.com.objectos.code.Testing;

@Testing
public interface SimpleBuilder {

  SimpleBuilderStep0 step0(String step0);
  
  interface SimpleBuilderStep0 {
    SimpleBuilderStep1 step1(boolean step1);
  }

  interface SimpleBuilderStep1 {
    String build();
  }

}