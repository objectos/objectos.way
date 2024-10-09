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

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

abstract class AppBootstrap {

  private List<AppOption<?>> options;

  private List<String> messages;

  protected final <C extends Collection<? super E>, E> App.Option.Converter<C> ofCollection(Supplier<C> supplier, App.Option.Converter<? extends E> converter) {
    Check.notNull(converter, "conveter == null");

    C collection;
    collection = supplier.get();

    return new OfCollection<>(collection, converter);
  }

  private record OfCollection<C extends Collection<? super E>, E>(C collection, App.Option.Converter<? extends E> converter) implements App.Option.Converter<C> {

    @Override
    public final C convert(String value) {
      E convert;
      convert = converter.convert(value);

      collection.add(convert);

      return collection;
    }

  }

  protected final App.Option.Converter<Integer> ofInteger() {
    return Integer::parseInt;
  }

  protected final App.Option.Converter<Path> ofPath() {
    return Path::of;
  }

  protected final App.Option.Converter<String> ofString() {
    return s -> s;
  }

  @SafeVarargs
  protected final <T> App.Option<T> option(String name, App.Option.Converter<T> converter, App.Option.Configuration<T>... configurations) {
    Check.notNull(name, "name == null");
    Check.notNull(converter, "converter == null");

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

  protected final <T> App.Option.Configuration<T> withValidator(Predicate<T> predicate, String reasonPhrase) {
    Check.notNull(predicate, "predicate == null");
    Check.notNull(reasonPhrase, "reasonPhrase == null");

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
   * @param value the option initial value
   *
   * @return an option configuration
   */
  protected final <T> App.Option.Configuration<T> withValue(T value) {
    Check.notNull(value, "value == null");

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