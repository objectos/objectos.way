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

import java.util.List;
import objectos.lang.object.Check;
import objectos.util.list.GrowableList;
import objectos.way.Carbon.Attribute;
import objectos.way.Carbon.ChildOf;

final class CarbonUi implements Carbon.Ui {

  private final Html.Template tmpl;

  CarbonUi(Html.Template tmpl) {
    this.tmpl = tmpl;
  }

  // attributes

  record HrefAttribute(String value) implements Attribute.Href {}

  @Override
  public final Attribute.Href href(String value) {
    Check.notNull(value, "value == null");
    return new HrefAttribute(value);
  }

  record IsActiveAttribute(boolean value) implements Attribute.IsActive {}

  @Override
  public final Attribute.IsActive isActive(boolean value) {
    return new IsActiveAttribute(value);
  }

  record NameAttribute(String value) implements Attribute.Name {}

  @Override
  public final Attribute.Name name(String value) {
    Check.notNull(value, "value == null");
    return new NameAttribute(value);
  }

  //
  // Header
  //

  static final class HeaderPojo {
    HeaderNamePojo headerName;

    HeaderNavigationPojo headerNavigation;

    CarbonTheme theme;

    HeaderPojo(ChildOf.Header[] components) {
      for (ChildOf.Header c : components) { // implicit null check
        switch (c) {
          case HeaderNamePojo o -> headerName = o;

          case HeaderNavigationPojo o -> headerNavigation = o;

          case CarbonTheme o -> theme = o;
        }
      }
    }
  }

  @Override
  public final Html.ElementInstruction header(ChildOf.Header... components) {
    HeaderPojo pojo;
    pojo = new HeaderPojo(components);

    return tmpl.header(
        tmpl.className("fixed inset-0px flex h-48px"),
        tmpl.className("border-b border-subtle"),
        tmpl.className("bg"),

        pojo.theme != null ? tmpl.className(pojo.theme.className) : tmpl.noop(),

        pojo.headerName != null ? headerName(pojo.headerName) : tmpl.noop(),

        pojo.headerNavigation != null ? headerNavigation(pojo.headerNavigation) : tmpl.noop()
    );
  }

  //
  // Header: HeaderMenuItem
  //

  static final class HeaderMenuItemPojo implements ChildOf.HeaderNavigation {
    boolean active;
    String href;
    String name;

    HeaderMenuItemPojo(ChildOf.HeaderMenuItem[] components) {
      for (ChildOf.HeaderMenuItem c : components) {
        switch (c) {
          case IsActiveAttribute o -> active = o.value();
          case HrefAttribute o -> href = o.value();
          case NameAttribute o -> name = o.value();
        }
      }
    }
  }

  @Override
  public final ChildOf.HeaderNavigation headerMenuItem(ChildOf.HeaderMenuItem... components) {
    return new HeaderMenuItemPojo(components);
  }

  //
  // Header: HeaderName
  //

  static final class HeaderNamePojo implements ChildOf.Header {
    String href;

    HeaderNameTextPojo text;

    HeaderNamePojo(ChildOf.HeaderName[] components) {
      for (ChildOf.HeaderName c : components) { // implicit null check
        switch (c) {
          case HrefAttribute o -> href = o.value();

          case HeaderNameTextPojo o -> text = o;
        }
      }
    }
  }

  @Override
  public final ChildOf.Header headerName(ChildOf.HeaderName... components) {
    return new HeaderNamePojo(components);
  }

  private Html.ElementInstruction headerName(HeaderNamePojo pojo) {
    return tmpl.a(
        tmpl.className("flex h-full select-none items-center"),
        tmpl.className("border-2 border-transparent"),
        tmpl.className("pr-32px pl-16px"),
        tmpl.className("text-body-compact-01 font-semibold"),
        tmpl.className("outline-none"),
        tmpl.className("transition-colors duration-100"),
        tmpl.className("focus:border-focus"),

        pojo.href != null ? tmpl.href(pojo.href) : tmpl.noop(),
        pojo.text != null
            ? tmpl.flatten(
                tmpl.span(
                    tmpl.className("font-normal"),

                    tmpl.t(pojo.text.prefix)
                ),
                tmpl.raw("&nbsp;"),
                tmpl.t(pojo.text.text)
            )
            : tmpl.noop()
    );
  }

  //
  // Header: HeaderNameText
  //

  static final class HeaderNameTextPojo implements ChildOf.HeaderName {
    final String prefix;
    final String text;

    HeaderNameTextPojo(String prefix, String text) {
      this.prefix = prefix;
      this.text = text;
    }
  }

  @Override
  public final ChildOf.HeaderName headerNameText(String prefix, String text) {
    Check.notNull(prefix, "prefix == null");
    Check.notNull(text, "text == null");

    return new HeaderNameTextPojo(prefix, text);
  }

  //
  // Header: HeaderNavigation
  //

  static final class HeaderNavigationPojo implements ChildOf.Header {
    List<HeaderMenuItemPojo> items;

    HeaderNavigationPojo(ChildOf.HeaderNavigation[] components) {
      for (ChildOf.HeaderNavigation c : components) {
        switch (c) {
          case HeaderMenuItemPojo o -> addItem(o);
        }
      }
    }

    private void addItem(HeaderMenuItemPojo item) {
      if (items == null) {
        items = new GrowableList<>();
      }

      items.add(item);
    }
  }

  @Override
  public final ChildOf.Header headerNavigation(ChildOf.HeaderNavigation... components) {
    return new HeaderNavigationPojo(components);
  }

  private Html.ElementInstruction headerNavigation(HeaderNavigationPojo pojo) {
    return tmpl.nav(
        tmpl.className("fixed hidden"),
        tmpl.className("w-256px top-48px bottom-0px"),
        tmpl.className("flex-col"),
        tmpl.className("bg"),
        tmpl.className("z-40"),
        tmpl.className("lg:relative lg:block"),
        tmpl.className("lg:top-0px"),
        tmpl.className("lg:bg-transparent"),
        tmpl.className("lg:pl-16px"),
        tmpl.className("lg:before:absolute lg:before:block lg:before:top-12px lg:before:left-0px"),
        tmpl.className("lg:before:h-24px lg:before:w-1px"),
        tmpl.className("lg:before:bg-border-subtle"),
        tmpl.className("lg:before:content-empty"),

        pojo.items != null
            ? tmpl.ul(
                tmpl.className("flex flex-col h-full"),
                tmpl.className("pt-16px"),
                tmpl.className("lg:flex-row"),
                tmpl.className("lg:pt-0px"),

                tmpl.f(this::headerNavigationItems, pojo.items)
            )
            : tmpl.noop()
    );
  }

  private void headerNavigationItems(List<HeaderMenuItemPojo> items) {
    for (HeaderMenuItemPojo pojo : items) {
      tmpl.li(
          tmpl.a(
              tmpl.className("relative flex h-32px select-none items-center"),
              tmpl.className("border-2 border-transparent"),
              tmpl.className("bg"),
              tmpl.className("px-16px"),
              tmpl.className("text-heading-compact-01"),
              tmpl.className("transition-colors duration-100"),
              tmpl.className("active:bg-active active:text-primary"),
              tmpl.className("focus:border-focus focus:outline-none"),
              tmpl.className("hover:bg-hover hover:text-primary"),
              tmpl.className("lg:h-full"),
              tmpl.className("lg:text-body-compact-01 lg:tracking-normal"),

              pojo.active
                  ? tmpl.className("text-primary after:absolute after:-bottom-2px after:left-0px after:block after:w-full after:border-b-3 after:border-b-interactive after:content-empty")
                  : tmpl.className("text-secondary"),

              pojo.href != null ? tmpl.href(pojo.href) : tmpl.noop(),

              tmpl.tabindex("0"),

              tmpl.span(
                  pojo.name != null ? tmpl.t(pojo.name) : tmpl.noop()
              )
          )
      );
    }
  }

}