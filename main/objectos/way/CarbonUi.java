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

final class CarbonUi implements Carbon.Ui {

  private final Html.Template tmpl;

  private boolean headerRendered;

  CarbonUi(Html.Template tmpl) {
    this.tmpl = tmpl;
  }

  // attributes

  record AriaLabelAttribute(String value) implements Carbon.Attribute.AriaLabel {}

  @Override
  public final Carbon.Attribute.AriaLabel ariaLabel(String value) {
    Check.notNull(value, "value == null");
    return new AriaLabelAttribute(value);
  }

  record HrefAttribute(String value) implements Carbon.Attribute.Href {}

  @Override
  public final Carbon.Attribute.Href href(String value) {
    Check.notNull(value, "value == null");
    return new HrefAttribute(value);
  }

  record IsActiveAttribute(boolean value) implements Carbon.Attribute.IsActive {}

  @Override
  public final Carbon.Attribute.IsActive isActive(boolean value) {
    return new IsActiveAttribute(value);
  }

  record NameAttribute(String value) implements Carbon.Attribute.Name {}

  @Override
  public final Carbon.Attribute.Name name(String value) {
    Check.notNull(value, "value == null");
    return new NameAttribute(value);
  }

  private static final Html.ClassName BUTTON_RESET = Html.className(
      "cursor-pointer", "appearance-none"
  );

  //
  // Content
  //

  @Override
  public final Html.ElementInstruction content(Html.FragmentLambda fragment) {
    return tmpl.main(
        headerRendered ? tmpl.className("mt-header") : tmpl.noop(),

        tmpl.include(fragment)
    );
  }

  //
  // Header
  //

  static final class HeaderPojo {
    HeaderMenuButtonPojo headerMenuButton;

    HeaderNamePojo headerName;

    HeaderNavigationPojo headerNavigation;

    CarbonTheme theme;

    HeaderPojo(Carbon.ChildOf.Header[] components) {
      for (Carbon.ChildOf.Header c : components) { // implicit null check
        switch (c) {
          case HeaderMenuButtonPojo o -> headerMenuButton = o;

          case HeaderNamePojo o -> headerName = o;

          case HeaderNavigationPojo o -> headerNavigation = o;

          case CarbonTheme o -> theme = o;
        }
      }
    }
  }

  @Override
  public final Html.ElementInstruction header(Carbon.ChildOf.Header... components) {
    headerRendered = true;

    HeaderPojo pojo;
    pojo = new HeaderPojo(components);

    return tmpl.header(
        tmpl.className("fixed inset-0px flex h-header"),
        tmpl.className("border-b border-subtle"),
        tmpl.className("bg"),

        pojo.theme != null ? tmpl.className(pojo.theme.className) : tmpl.noop(),

        pojo.headerMenuButton != null ? headerMenuButton(pojo.headerMenuButton) : tmpl.noop(),

        pojo.headerName != null ? headerName(pojo.headerName) : tmpl.noop(),

        pojo.headerNavigation != null ? headerNavigation(pojo.headerNavigation) : tmpl.noop()
    );
  }

  //
  // HeaderMenuButton
  //

  static final class HeaderMenuButtonPojo implements Carbon.ChildOf.Header {
    String ariaLabel;

    HeaderMenuButtonPojo(Carbon.ChildOf.HeaderMenuButton[] components) {
      for (Carbon.ChildOf.HeaderMenuButton c : components) {
        switch (c) {
          case AriaLabelAttribute o -> ariaLabel = o.value();
        }
      }
    }
  }

  @Override
  public final Carbon.ChildOf.Header headerMenuButton(Carbon.ChildOf.HeaderMenuButton... components) {
    return new HeaderMenuButtonPojo(components);
  }

  private Html.ElementInstruction headerMenuButton(HeaderMenuButtonPojo pojo) {
    return tmpl.button(
        BUTTON_RESET,

        // header__action
        tmpl.className("size-header"),
        tmpl.className("border border-transparent"),
        tmpl.className("transition-colors duration-100"),
        tmpl.className("active:bg-active"),
        tmpl.className("focus:border-focus focus:outline-none"),
        tmpl.className("hover:bg-hover"),
        // header__menu-toggle
        tmpl.className("flex items-center justify-center"),
        // header__menu-toggle__hidden
        tmpl.className("lg:hidden"),
        // header__menu-trigger
        tmpl.className("svg:fill-primary"),

        pojo.ariaLabel != null ? tmpl.ariaLabel(pojo.ariaLabel) : tmpl.noop(),
        pojo.ariaLabel != null ? tmpl.title(pojo.ariaLabel) : tmpl.noop(),

        tmpl.type("button"),

        icon(Carbon.Icon.MENU, Carbon.IconSize.PX20)
    );
  }

  //
  // HeaderMenuItem
  //

  static final class HeaderMenuItemPojo implements Carbon.ChildOf.HeaderNavigation {
    boolean active;
    String href;
    String name;

    HeaderMenuItemPojo(Carbon.ChildOf.HeaderMenuItem[] components) {
      for (Carbon.ChildOf.HeaderMenuItem c : components) {
        switch (c) {
          case IsActiveAttribute o -> active = o.value();
          case HrefAttribute o -> href = o.value();
          case NameAttribute o -> name = o.value();
        }
      }
    }
  }

  @Override
  public final Carbon.ChildOf.HeaderNavigation headerMenuItem(Carbon.ChildOf.HeaderMenuItem... components) {
    return new HeaderMenuItemPojo(components);
  }

  //
  // HeaderName
  //

  static final class HeaderNamePojo implements Carbon.ChildOf.Header {
    String href;

    HeaderNameTextPojo text;

    HeaderNamePojo(Carbon.ChildOf.HeaderName[] components) {
      for (Carbon.ChildOf.HeaderName c : components) { // implicit null check
        switch (c) {
          case HrefAttribute o -> href = o.value();

          case HeaderNameTextPojo o -> text = o;
        }
      }
    }
  }

  @Override
  public final Carbon.ChildOf.Header headerName(Carbon.ChildOf.HeaderName... components) {
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
  // HeaderNameText
  //

  static final class HeaderNameTextPojo implements Carbon.ChildOf.HeaderName {
    final String prefix;
    final String text;

    HeaderNameTextPojo(String prefix, String text) {
      this.prefix = prefix;
      this.text = text;
    }
  }

  @Override
  public final Carbon.ChildOf.HeaderName headerNameText(String prefix, String text) {
    Check.notNull(prefix, "prefix == null");
    Check.notNull(text, "text == null");

    return new HeaderNameTextPojo(prefix, text);
  }

  //
  // HeaderNavigation
  //

  static final class HeaderNavigationPojo implements Carbon.ChildOf.Header {
    List<HeaderMenuItemPojo> items;

    HeaderNavigationPojo(Carbon.ChildOf.HeaderNavigation[] components) {
      for (Carbon.ChildOf.HeaderNavigation c : components) {
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
  public final Carbon.ChildOf.Header headerNavigation(Carbon.ChildOf.HeaderNavigation... components) {
    return new HeaderNavigationPojo(components);
  }

  private Html.ElementInstruction headerNavigation(HeaderNavigationPojo pojo) {
    return tmpl.nav(
        tmpl.className("fixed hidden"),
        tmpl.className("w-256px top-header bottom-0px"),
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

  //
  // Icon
  //

  public final Html.ElementInstruction icon(Carbon.Icon icon, Carbon.IconSize size) {
    return switch (icon) {
      case MENU -> switch (size) {
        case PX16 -> icon16("""
        <rect x="2" y="12" width="12" height="1"/><rect x="2" y="9" width="12" height="1"/><rect x="2" y="6" width="12" height="1"/><rect x="2" y="3" width="12" height="1"/>""");
        case PX20 -> icon20("""
        <rect x="2" y="14.8" width="16" height="1.2"/><rect x="2" y="11.2" width="16" height="1.2"/><rect x="2" y="7.6" width="16" height="1.2"/><rect x="2" y="4" width="16" height="1.2"/>""");
        case PX24 -> icon24("""
        <rect x="3" y="18" width="18" height="1.5"/><rect x="3" y="13.5" width="18" height="1.5"/><rect x="3" y="9" width="18" height="1.5"/><rect x="3" y="4.5" width="18" height="1.5"/>""");
        case PX32 -> icon32("""
        <rect x="4" y="6" width="24" height="2"/><rect x="4" y="24" width="24" height="2"/><rect x="4" y="12" width="24" height="2"/><rect x="4" y="18" width="24" height="2"/>""");
      };
    };
  }

  private Html.ElementInstruction icon16(String raw) {
    return tmpl.svg(
        tmpl.xmlns("http://www.w3.org/2000/svg"),
        tmpl.fill("currentColor"),
        tmpl.width("16px"), tmpl.height("16px"), tmpl.viewBox("0 0 16 16"),
        tmpl.raw(raw)
    );
  }

  private Html.ElementInstruction icon20(String raw) {
    return tmpl.svg(
        tmpl.xmlns("http://www.w3.org/2000/svg"),
        tmpl.fill("currentColor"),
        tmpl.width("20px"), tmpl.height("20px"), tmpl.viewBox("0 0 20 20"),
        tmpl.raw(raw)
    );
  }

  private Html.ElementInstruction icon24(String raw) {
    return tmpl.svg(
        tmpl.xmlns("http://www.w3.org/2000/svg"),
        tmpl.fill("currentColor"),
        tmpl.width("24px"), tmpl.height("24px"), tmpl.viewBox("0 0 24 24"),
        tmpl.raw(raw)
    );
  }

  private Html.ElementInstruction icon32(String raw) {
    return tmpl.svg(
        tmpl.xmlns("http://www.w3.org/2000/svg"),
        tmpl.fill("currentColor"),
        tmpl.width("32px"), tmpl.height("32px"), tmpl.viewBox("0 0 32 32"),
        tmpl.raw(raw)
    );
  }

}