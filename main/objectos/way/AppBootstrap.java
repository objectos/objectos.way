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

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import objectos.way.App.Option.Converter;

abstract class AppBootstrap {

  private List<AppOption<?>> options;

  private List<String> messages;

  private static final class OfCollection<C extends Collection<? super E>, E>
      implements App.Option.Converter<C> {

    private final C collection;
    private final App.Option.Converter<? extends E> converter;

    OfCollection(C collection, Converter<? extends E> converter) {
      this.collection = collection;
      this.converter = converter;
    }

    @Override
    public final C convert(String value) {
      E convert;
      convert = converter.convert(value);

      collection.add(convert);

      return collection;
    }
  }

  /**
   * Option converter: command line values are converted with the
   * specified converter and the resulting options added to specified
   * {@link Collection}.
   *
   * @param <C> the collection type
   * @param <E> the option type
   *
   * @param collection
   *        converted options will be added to this collection
   * @param converter
   *        converts each command line value of this option
   *
   * @return an option converter
   */
  protected final <C extends Collection<? super E>, E> App.Option.Converter<C> ofCollection(C collection, App.Option.Converter<? extends E> converter) {
    Objects.requireNonNull(collection, "collection == null");
    Objects.requireNonNull(converter, "converter == null");

    return new OfCollection<>(collection, converter);
  }

  /**
   * Option converter: from a string to an {@link Integer} object.
   *
   * @return an option converter
   */
  protected final App.Option.Converter<Integer> ofInteger() {
    return Integer::parseInt;
  }

  /**
   * Option converter: from a string to a {@link Path} object.
   *
   * @return an option converter
   */
  protected final App.Option.Converter<Path> ofPath() {
    return Path::of;
  }

  /**
   * Option converter: from a string to the string itself. In other words, it is
   * a no-op; it simply defines the option value type.
   *
   * @return an option converter
   */
  protected final App.Option.Converter<String> ofString() {
    return s -> s;
  }

  /**
   * Creates a new command line option.
   *
   * @param <T> the option type
   *
   * @param name
   *        the option name
   * @param converter
   *        converts from a string to an instance of the option type
   * @param configurations
   *        configures the created option
   *
   * @return a newly created command line option
   */
  @SafeVarargs
  protected final <T> App.Option<T> option(String name, App.Option.Converter<T> converter, App.Option.Configuration<T>... configurations) {
    Objects.requireNonNull(name, "name == null");
    Objects.requireNonNull(converter, "converter == null");

    AppOption<T> option;
    option = new AppOption<>(name, converter);

    for (int i = 0; i < configurations.length; i++) { // configurations implicit null-check
      App.Option.Configuration<T> c;
      c = Check.notNull(configurations[i], "configurations[", i, "] == null");

      App.OptionConfiguration<T> configuration;
      configuration = (App.OptionConfiguration<T>) c;

      configuration.accept(option);
    }

    return register(option);
  }

  /**
   * Option config: sets the option to be required. In other words, the
   * bootstrap will fail if a valid value for the option is not supplied.
   *
   * @param <T> the option type
   *
   * @return an option configuration
   */
  protected final <T> App.Option.Configuration<T> required() {
    return new App.OptionConfiguration<T>() {
      @Override
      final void accept(AppOption<T> option) {
        option.required();
      }
    };
  }

  /**
   * Option config: validates an option with the specified {@code predicate}.
   * The specified reason phrase will be used to inform of a failed validation.
   *
   * @param <T> the option type
   * @param predicate
   *        it should evaluate to {@code true} when the option is valid; and
   *        {@code false} otherwise
   * @param reasonPhrase
   *        the message to inform of a failed validation
   *
   * @return an option configuration
   */
  protected final <T> App.Option.Configuration<T> withValidator(Predicate<T> predicate, String reasonPhrase) {
    Objects.requireNonNull(predicate, "predicate == null");
    Objects.requireNonNull(reasonPhrase, "reasonPhrase == null");

    return new App.OptionConfiguration<T>() {
      @Override
      final void accept(AppOption<T> option) {
        option.addValidator(predicate, reasonPhrase);
      }
    };
  }

  /**
   * Option config: sets the initial value of the option to the specified value.
   *
   * @param <T> the option type
   * @param value the option initial value
   *
   * @return an option configuration
   */
  protected final <T> App.Option.Configuration<T> withValue(T value) {
    Objects.requireNonNull(value, "value == null");

    return new App.OptionConfiguration<T>() {
      @Override
      final void accept(AppOption<T> option) {
        option.set(value);
      }
    };
  }

  final void addMessage(String message) {
    if (messages == null) {
      messages = Util.createList();
    }

    messages.add(message);
  }

  final String message(int index) {
    return messages == null ? null : messages.get(index);
  }

  final int messagesSize() {
    return messages == null ? 0 : messages.size();
  }

  final void parseArgs(String[] args) {
    if (options == null) {
      return;
    }

    if (messages != null) {
      messages.clear();
    }

    Map<String, AppOption<?>> optionsByName;
    optionsByName = Util.createMap();

    for (AppOption<?> o : options) {
      o.acceptByName(optionsByName);
    }

    int index;
    index = 0;

    int length;
    length = args.length;

    while (index < length) {
      String arg;
      arg = args[index++];

      AppOption<?> option;
      option = optionsByName.get(arg);

      if (option != null) {
        index = option.accept(args, index);
      }
    }

    for (AppOption<?> option : options) {
      option.validate(this);
    }
  }

  private <T> App.Option<T> register(AppOption<T> option) {
    if (options == null) {
      options = Util.createList();
    }

    options.add(option);

    return option;
  }

}