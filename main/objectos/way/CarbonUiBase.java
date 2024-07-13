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
import objectos.way.Carbon.Icon;
import objectos.way.Carbon.IconSize;
import objectos.way.Html.FragmentLambda;

non-sealed abstract class CarbonUiBase implements Carbon.Ui {

  final Html.Template tmpl;

  CarbonUiBase(Html.Template tmpl) {
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

  //
  // Content
  //

  final class ContentPojo implements Html.FragmentLambda {

    final Html.FragmentLambda fragment;

    public ContentPojo(FragmentLambda fragment) {
      this.fragment = Check.notNull(fragment, "fragment == null");
    }

    @Override
    public final void invoke() {
      renderContent(this);
    }

  }

  abstract void renderContent(ContentPojo pojo);

  @Override
  public final Html.FragmentLambda content(Html.FragmentLambda fragment) {
    return new ContentPojo(fragment);
  }

  //
  // Header
  //

  final class HeaderPojo implements Html.FragmentLambda {
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

    @Override
    public final void invoke() {
      renderHeader(this);
    }
  }

  abstract void renderHeader(HeaderPojo pojo);

  @Override
  public final Html.FragmentLambda header(Carbon.ChildOf.Header... components) {
    return new HeaderPojo(components);
  }

  //
  // HeaderMenuButton
  //

  final class HeaderMenuButtonPojo implements Carbon.ChildOf.Header, Html.FragmentLambda {
    String ariaLabel;

    HeaderMenuButtonPojo(Carbon.ChildOf.HeaderMenuButton[] components) {
      for (Carbon.ChildOf.HeaderMenuButton c : components) {
        switch (c) {
          case AriaLabelAttribute o -> ariaLabel = o.value();
        }
      }
    }

    @Override
    public final void invoke() {
      renderHeaderMenuButton(this);
    }
  }

  abstract void renderHeaderMenuButton(HeaderMenuButtonPojo pojo);

  @Override
  public final Carbon.ChildOf.Header headerMenuButton(Carbon.ChildOf.HeaderMenuButton... components) {
    return new HeaderMenuButtonPojo(components);
  }

  //
  // HeaderMenuItem
  //

  final class HeaderMenuItemPojo implements Carbon.ChildOf.HeaderNavigation, Html.FragmentLambda {
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

    @Override
    public final void invoke() {
      renderHeaderMenuItem(this);
    }
  }

  abstract void renderHeaderMenuItem(HeaderMenuItemPojo pojo);

  @Override
  public final Carbon.ChildOf.HeaderNavigation headerMenuItem(Carbon.ChildOf.HeaderMenuItem... components) {
    return new HeaderMenuItemPojo(components);
  }

  //
  // HeaderName
  //

  final class HeaderNamePojo implements Carbon.ChildOf.Header, Html.FragmentLambda {
    String href;

    HeaderNameTextPojo text;

    HeaderNamePojo(Carbon.ChildOf.HeaderName[] components) {
      for (Carbon.ChildOf.HeaderName c : components) {
        switch (c) {
          case HrefAttribute o -> href = o.value();

          case HeaderNameTextPojo o -> text = o;
        }
      }
    }

    @Override
    public final void invoke() {
      renderHeaderName(this);
    }
  }

  abstract void renderHeaderName(HeaderNamePojo pojo);

  @Override
  public final Carbon.ChildOf.Header headerName(Carbon.ChildOf.HeaderName... components) {
    return new HeaderNamePojo(components);
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

  final class HeaderNavigationPojo implements Carbon.ChildOf.Header, Html.FragmentLambda {
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

    @Override
    public final void invoke() {
      renderHeaderNavigation(this);
    }
  }

  abstract void renderHeaderNavigation(HeaderNavigationPojo pojo);

  @Override
  public final Carbon.ChildOf.Header headerNavigation(Carbon.ChildOf.HeaderNavigation... components) {
    return new HeaderNavigationPojo(components);
  }

  //
  // Icon
  //

  final class IconPojo implements Html.FragmentLambda {

    final Carbon.Icon icon;
    final Carbon.IconSize size;

    IconPojo(Icon icon, IconSize size) {
      this.icon = icon;
      this.size = size;
    }

    @Override
    public final void invoke() {
      renderIcon(this);
    }

  }

  abstract void renderIcon(IconPojo pojo);

}