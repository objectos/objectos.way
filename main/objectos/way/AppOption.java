/*
 * Copyright (C) 2023-2024 Objectos Software LTDA.
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
package objectos.way;

import java.util.Map;

final class AppOption<T> implements App.Option<T> {

  private final String name;

  private final App.Option.Converter<T> converter;

  private RuntimeException error;

  private boolean required;

  private T value;

  AppOption(String name, Converter<T> converter) {
    this.name = name;
    this.converter = converter;
  }

  @Override
  public final T get() {
    return value;
  }

  final int accept(String[] args, int index) {
    if (index < args.length) {
      String arg;
      arg = args[index++];

      try {
        value = converter.convert(arg);
      } catch (RuntimeException e) {
        error = e;
      }
    } else {
      value = null;
    }

    return index;
  }

  final void acceptByName(Map<String, AppOption<?>> map) {
    AppOption<?> previous;
    previous = map.put(name, this);

    if (previous != null) {
      throw new IllegalArgumentException("Duplicate option name: " + name);
    }
  }

  final void required() {
    required = true;
  }

  final void set(T newValue) {
    value = newValue;
  }

  final void validate(AppBootstrap collector) {
    if (error != null) {
      collector.addMessage(error.getMessage());
    }

    if (required && value == null) {
      String message;
      message = "Missing required option: " + name;

      collector.addMessage(message);
    }
  }

}