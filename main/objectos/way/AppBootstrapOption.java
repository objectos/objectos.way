/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

final class AppBootstrapOption<T> implements App.Bootstrap.Option<T> {

  record Validator<T>(Predicate<T> predicate, String reasonPhrase) {
    public final void accept(AppBootstrap collector, String name, T value) {
      if (!predicate.test(value)) {
        collector.addMessage("Invalid " + name + " value: " + reasonPhrase);
      }
    }
  }

  private final Function<String, T> converter;

  private RuntimeException error;

  final String name;

  private final boolean required;

  private final List<Validator<? super T>> validators;

  private T value;

  AppBootstrapOption(AppBootstrapOptionBuilder<T> builder) {
    converter = builder.converter;

    name = builder.name;

    required = builder.required;

    validators = builder.validators();

    value = builder.value;
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
        value = converter.apply(arg);
      } catch (RuntimeException e) {
        error = e;
      }
    }

    return index;
  }

  final void set(T newValue) {
    value = newValue;
  }

  final void validate(AppBootstrap collector) {
    if (error != null) {
      String message;
      message = error.getMessage();

      message = String.valueOf(message);

      collector.addMessage(message);
    }

    if (required && value == null) {
      String message;
      message = "Missing required option: " + name;

      collector.addMessage(message);
    }

    if (validators != null && value != null) {
      int sizeBefore;
      sizeBefore = collector.messagesSize();

      for (Validator<? super T> validator : validators) {
        validator.accept(collector, name, value);
      }

      int sizeAfter;
      sizeAfter = collector.messagesSize();

      if (sizeAfter > sizeBefore) {
        value = null;
      }
    }
  }

}