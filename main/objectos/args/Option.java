/*
 * Copyright (C) 2024 Objectos Software LTDA.
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
package objectos.args;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

public abstract class Option<T> {

  public interface Activator {
    Activator ALWAYS_ACTIVE = () -> true;

    boolean test();
  }

  private Activator activator = Activator.ALWAYS_ACTIVE;

  private RuntimeException error;

  private final List<String> names;

  private boolean required;

  private List<Validator<? super T>> validators;

  protected T value;

  protected Option(String name) {
    Objects.requireNonNull(name, "name == null");

    names = List.of(name);
  }

  public final void activator(Activator activator) {
    this.activator = Objects.requireNonNull(activator, "activator == null");
  }

  public final void description(String description) {
    // no-op for now...
  }

  public final T get() {
    if (value == null) {
      throw new NoSuchElementException();
    }

    return value;
  }

  public final boolean is(T value) {
    return Objects.equals(this.value, value);
  }

  public final void required() {
    required = true;
  }

  public final void set(T newValue) {
    value = newValue;
  }

  public final void validator(Predicate<T> predicate, String reasonPhrase) {
    Objects.requireNonNull(predicate, "predicate == null");
    Objects.requireNonNull(reasonPhrase, "reasonPhrase == null");

    Validator<T> validator;
    validator = new Validator<>(predicate, reasonPhrase);

    addValidator(validator);
  }

  private void addValidator(Validator<T> validator) {
    if (validators == null) {
      validators = new ArrayList<>();
    }

    validators.add(validator);
  }

  final int accept(String[] args, int index) {
    if (index < args.length) {
      String arg;
      arg = args[index++];

      try {
        parseValue(arg);
      } catch (RuntimeException e) {
        error = e;
      }
    } else {
      parseValue();
    }

    return index;
  }

  final void acceptByName(Map<String, Option<?>> map) {
    for (var name : names) {
      Option<?> previous;
      previous = map.put(name, this);

      if (previous != null) {
        throw new UnsupportedOperationException("Implement me");
      }
    }
  }

  void parseValue() {
    value = null;
  }

  abstract void parseValue(String s);

  final void validate(ErrorCollector collector) {
    if (!activator.test()) {
      return;
    }

    if (error != null) {
      collector.addMessage(error.getMessage());
    }

    if (required && value == null) {
      collector.missingRequiredOption(names.get(0));
    }

    if (validators != null && value != null) {

      for (Validator<? super T> validator : validators) {
        validator.accept(collector, names.get(0), value);
      }

    }
  }

}