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

import java.util.List;
import java.util.Set;

import br.com.objectos.code.Level.Level1;
import br.com.objectos.code.Level.Level2;
import br.com.objectos.code.Level.Level3;
import br.com.objectos.code.Testing;

@Testing
public abstract class SubType {

  abstract Level1<String> level1();
  abstract Level2<String, Integer> level2();
  abstract Level3<String, Integer, String> level3();
  
  abstract List<String> list();
  abstract Set<Integer> set();
  
}