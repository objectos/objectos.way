/*
 * Copyright (C) 2023-2025 Objectos Software LTDA.
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
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import objectos.way.AppBootstrapOption.Validator;

final class AppBootstrapOptionBuilder<T> implements App.Bootstrap.Option.Options<T> {

  final Function<String, T> converter;

  String name;

  boolean required;

  UtilList<AppBootstrapOption.Validator<? super T>> validators;

  T value;

  AppBootstrapOptionBuilder(Function<String, T> converter) {
    this.converter = converter;
  }

  @Override
  public final void description(String value) {
    throw new UnsupportedOperationException("Implement me");
  }

  @Override
  public final void name(String value) {
    if (name != null) {
      throw new IllegalStateException("name was already defined");
    }

    name = Objects.requireNonNull(value, "value == null");
  }

  @Override
  public final void required() {
    required = true;
  }

  @Override
  public final void validator(Predicate<? super T> predicate, String reasonPhrase) {
    Objects.requireNonNull(predicate, "predicate == null");
    Objects.requireNonNull(reasonPhrase, "reasonPhrase == null");

    if (validators == null) {
      validators = new UtilList<>();
    }

    final AppBootstrapOption.Validator<? super T> validator;
    validator = new AppBootstrapOption.Validator<>(predicate, reasonPhrase);

    validators.add(validator);
  }

  @Override
  public final void value(T value) {
    this.value = Objects.requireNonNull(value, "value == null");
  }

  final AppBootstrapOption<T> build() {
    if (name == null) {
      throw new IllegalStateException("A name was not defined");
    }

    return new AppBootstrapOption<>(this);
  }

  final List<Validator<? super T>> validators() {
    return validators != null ? validators.toUnmodifiableList() : List.of();
  }

}