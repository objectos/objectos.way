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

abstract class AbstractIntHeader<T extends Header> extends AbstractHeader<T> {

  private int multiplier = 1;

  int value;

  private State state = State.CONSUME;

  @Override
  public final void consume(char c) {
    state = execute(c);
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
  }

  @Override
  final void toStringValue(StringBuilder result) {
    result.append(Integer.toString(value));
  }

  private AbstractIntHeader.State execute(char c) {
    switch (state) {
      case CONSUME:
        return executeConsume(c);
      case CR:
        return executeCr(c);
      default:
        throw new UnsupportedOperationException("Implement me @ " + state);
    }
  }

  private AbstractIntHeader.State executeConsume(char c) {
    if (c == Http.CR) {
      return State.CR;
    }

    int digit;
    digit = Http.toUsAsciiDigit(c);

    if (digit < 0 || digit > 9) {
      return State.MALFORMED;
    }

    value *= multiplier;

    value += digit;

    multiplier *= 10;

    return State.CONSUME;
  }

  private AbstractIntHeader.State executeCr(char c) {
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