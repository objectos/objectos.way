/*
 * Copyright (C) 2016-2023 Objectos Software LTDA.
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
package br.com.objectos.http;

abstract class AbstractStringHeader<T extends Header> extends AbstractHeader<T> {

  private StringBuilder builder = new StringBuilder();

  private State state = State.CONSUME;

  private String value;

  @Override
  public final void consume(char c) {
    state = execute(c);
  }

  @Override
  public final String getHeaderValue() {
    return value;
  }

  @Override
  public final boolean isMalformed() {
    return state == State.MALFORMED;
  }

  @Override
  public final boolean shouldConsume() {
    switch (state) {
      case CONSUME:
      case CR:
        return true;
      default:
        return false;
    }
  }

  @Override
  final void clear() {
    state = null;

    value = builder.toString();

    builder = null;
  }

  @Override
  final void toStringValue(StringBuilder result) {
    result.append(value);
  }

  private AbstractStringHeader.State execute(char c) {
    switch (state) {
      case CONSUME:
        return executeConsume(c);
      case CR:
        return executeCr(c);
      default:
        throw new UnsupportedOperationException("Implement me @ " + state);
    }
  }

  private AbstractStringHeader.State executeConsume(char c) {
    if (c == Http.CR) {
      return State.CR;
    }

    builder.append(c);

    return State.CONSUME;
  }

  private AbstractStringHeader.State executeCr(char c) {
    if (c == Http.LF) {
      return State.RESULT;
    }

    return State.MALFORMED;
  }

  private enum State {

    CONSUME,

    CR,

    MALFORMED,

    RESULT;

  }

}