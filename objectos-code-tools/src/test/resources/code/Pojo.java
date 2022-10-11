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

import java.time.LocalDate;
import java.util.List;
import java.util.Map.Entry;

import br.com.objectos.code.Testing;
import br.com.objectos.way.testable.Testable;

@Testing
public abstract class Pojo implements Testable {

  abstract String name();
  abstract LocalDate date();
  abstract double value();
  abstract boolean active();

  int id;

  @Deprecated
  String deprecated;

  protected Pojo() {
  }

  public double getTotal(LocalDate date) {
    return 123d;
  }

  public boolean isValid() {
    return true;
  }

  public void array(String[] stringArray) {
  }

  public void doNothing() {
  }

  public void varargsMixed(Object first, Object... varargs) {
  }

  public void varargsOnly(Object... varargs) {
  }

  protected List<Pojo> listOfSelf() {
    return null;
  }

  protected Pojo self() {
    return this;
  }

  public static class PojoInner {}

}