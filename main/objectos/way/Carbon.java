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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import objectos.lang.object.Check;
import objectos.util.list.GrowableList;

/**
 * The <strong>Objectos Carbon UI</strong> main class.
 *
 * <p>
 * Objectos Carbon UI is an implementation of the Carbon Design System in pure
 * Java.
 */
public final class Carbon {

  /**
   * The White theme.
   */
  public static final Theme WHITE = CarbonTheme.WHITE;

  /**
   * The Gray 10 theme.
   */
  public static final Theme G10 = CarbonTheme.G10;

  /**
   * The Gray 90 theme.
   */
  public static final Theme G90 = CarbonTheme.G90;

  /**
   * The Gray 100 theme.
   */
  public static final Theme G100 = CarbonTheme.G100;

  public enum Icon {

    MENU;

  }

  public enum IconSize {

    PX16,

    PX20,

    PX24,

    PX32;

  }

  // attributes

  /**
   * The nested types of this interface represent the Carbon UI attributes.
   */
  public sealed interface Attribute extends Component {}

  enum AttributeKey {
    HREF,

    ICON,

    IS_ACTIVE,

    NAME,

    PREFIX,

    TITLE;

    public final CarbonAttribute of(boolean value) {
      return new CarbonAttribute(this, Boolean.valueOf(value));
    }

    public final CarbonAttribute of(String value) {
      Check.notNull(value, "value == null");
      return new CarbonAttribute(this, value);
    }
  }

  record CarbonAttribute(AttributeKey key, Object value) implements Attribute {}

  enum CarbonTheme implements Theme {

    WHITE("cds--white"),

    G10("cds--g10"),

    G90("cds--g90"),

    G100("cds--g100");

    final String className;

    private CarbonTheme(String className) {
      this.className = className;
    }

  }

  /**
   * A nested type of this interface can be used as a child of the corresponding
   * component.
   */
  public sealed interface Element extends Component {

    @FunctionalInterface
    public interface Provider {

      Element get(Component... components);

    }

  }

  public sealed interface Theme extends Component {}

  public sealed interface Component {}

  /**
   * The UI builder.
   */
  public sealed static abstract class Ui permits CarbonUi {

    non-sealed abstract class Pojo implements Carbon.Element, Html.FragmentLambda {

      private Map<Carbon.AttributeKey, Object> attributes;

      private List<Pojo> children;

      private CarbonTheme theme;

      public Pojo(Carbon.Component... components) {
        for (Carbon.Component c : components) {
          switch (c) {
            case Carbon.CarbonAttribute o -> addAttribute(o);

            case Carbon.CarbonTheme o -> theme = o;

            case Pojo o -> addChild(o);
          }
        }
      }

      @Override
      public final void invoke() {
        render();
      }

      final boolean booleanValue(AttributeKey key) {
        Object o;
        o = value(key);

        return Boolean.TRUE.equals(o);
      }

      abstract void render();

      final Html.Instruction renderChildren() {
        return children != null ? tmpl.include(this::children) : tmpl.noop();
      }

      private void children() {
        for (Pojo child : children) {
          child.render();
        }
      }

      final Html.Instruction renderTheme() {
        return theme != null ? tmpl.className(theme.className) : tmpl.noop();
      }

      final String stringValue(AttributeKey key) {
        return (String) value(key);
      }

      private void addAttribute(CarbonAttribute attr) {
        if (attributes == null) {
          attributes = new EnumMap<>(AttributeKey.class);
        }

        attributes.put(attr.key(), attr.value());
      }

      private void addChild(Pojo o) {
        if (children == null) {
          children = new GrowableList<>();
        }

        children.add(o);
      }

      private Object value(AttributeKey key) {
        if (attributes != null) {
          return attributes.remove(key);
        }

        return null;
      }

    }

    final Html.Template tmpl;

    Ui(Html.Template tmpl) {
      this.tmpl = tmpl;
    }

    // attributes

    /**
     * Creates a new {@code href} attribute with the specified value.
     *
     * @param value the string value of the attribute
     *
     * @return a new attribute
     */
    public final Attribute href(String value) {
      return AttributeKey.HREF.of(value);
    }

    public final Attribute isActive(boolean value) {
      return AttributeKey.IS_ACTIVE.of(value);
    }

    public final Attribute name(String value) {
      return AttributeKey.NAME.of(value);
    }

    public final Attribute prefix(String value) {
      return AttributeKey.PREFIX.of(value);
    }

    public final Attribute title(String value) {
      return AttributeKey.TITLE.of(value);
    }

    // elements

    /**
     * Renders the UI shell content container.
     *
     * @param fragment
     *        the fragment to render
     *
     * @return an HTML instruction
     */
    public final Element content(Component... components) {
      return new Pojo(components) {
        @Override
        final void render() { renderContent(this); }
      };
    }

    public final Element content(Html.FragmentLambda fragment) {
      return content(
          htmlFragment(fragment)
      );
    }

    abstract void renderContent(Pojo pojo);

    /**
     * Renders the UI shell Header component.
     *
     * @param components
     *        the nested components
     *
     * @return an HTML instruction
     */
    public final Element header(Component... components) {
      return new Pojo(components) {
        @Override
        final void render() { renderHeader(this); }
      };
    }

    abstract void renderHeader(Pojo pojo);

    /**
     * Declares an UI shell header menu button.
     *
     * @param components
     *        the nested components
     *
     * @return instructions to render a header menu button
     */
    public final Element headerMenuButton(Component... components) {
      return new Pojo(components) {
        @Override
        final void render() { renderHeaderMenuButton(this); }
      };
    }

    abstract void renderHeaderMenuButton(Pojo pojo);

    /**
     * Declares an UI shell header menu item.
     *
     * @param components
     *        the nested components
     *
     * @return instructions to render a header menu item
     */
    public final Element headerMenuItem(Component... components) {
      return new Pojo(components) {
        @Override
        final void render() { renderHeaderMenuItem(this); }
      };
    }

    abstract void renderHeaderMenuItem(Pojo pojo);

    public final Element headerName(Component... components) {
      return new Pojo(components) {
        @Override
        final void render() { renderHeaderName(this); }
      };
    }

    abstract void renderHeaderName(Pojo pojo);

    /**
     * Declares an UI shell header navigation section.
     *
     * @param components
     *        the nested components
     *
     * @return instructions to render a header navigation section
     */
    public final Element headerNavigation(Component... components) {
      return new Pojo(components) {
        @Override
        final void render() { renderHeaderNavigation(this); }
      };
    }

    public final Element htmlFragment(Html.FragmentLambda fragment) {
      final Html.FragmentLambda f = Check.notNull(fragment, "fragment == null");

      return new Pojo() {
        @Override
        final void render() {
          tmpl.include(f);
        }
      };
    }

    abstract void renderHeaderNavigation(Pojo pojo);

    public final Element icon(Icon icon, IconSize size) {
      final Icon i = Check.notNull(icon, "icon == null");
      final IconSize s = Check.notNull(size, "size == null");

      return new Pojo() {
        @Override
        final void render() {
          renderIcon(i, s);
        }
      };
    }

    abstract void renderIcon(Icon icon, IconSize size);

    /**
     * Creates a new side navigation component enclosing the specified nested
     * components.
     *
     * @param components
     *        the nested components
     *
     * @return a newly constructed element
     */
    public final Element sideNav(Component... components) {
      return new Pojo(components) {
        @Override
        final void render() { renderSideNav(this); }
      };
    }

    abstract void renderSideNav(Pojo pojo);

    public final Element sideNavItems(Component... components) {
      return new Pojo(components) {
        @Override
        final void render() { renderSideNavItems(this); }
      };
    }

    abstract void renderSideNavItems(Pojo pojo);

    public final Element sideNavMenuItem(Component... components) {
      return new Pojo(components) {
        @Override
        final void render() { renderSideNavMenuItem(this); }
      };
    }

    abstract void renderSideNavMenuItem(Pojo pojo);

  }

  /**
   * The UI shell is the top level UI component of an web application.
   */
  public static abstract class Shell extends CarbonShell {

    /**
     * Sole constructor.
     *
     * @param http
     *        the HTTP exchange
     */
    protected Shell(Http.Exchange http) {
      super(http);
    }

    protected final Html.FragmentInstruction ui(Element element) {
      Ui.Pojo pojo;
      pojo = (Ui.Pojo) element;

      return include(pojo);
    }

  }

  private Carbon() {}

  public static Carbon create() {
    return new Carbon();
  }

  public final Http.Module createHttpModule() {
    final byte[] script;

    try {
      script = Script.getBytes();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    return new Web.Module() {
      @Override
      protected final void configure() {
        route("/ui/script.js", GET(this::script));
        route("/ui/carbon.css", GET(new CarbonStyles()));

        filter(this::injectCarbon);
      }

      private void script(Http.Exchange http) {
        http.status(Http.OK);

        http.dateNow();

        http.header(Http.CONTENT_TYPE, "text/javascript; charset=utf-8");

        http.header(Http.CONTENT_LENGTH, script.length);

        http.send(script);
      }

      private void injectCarbon(Http.Exchange http) {
        http.set(Carbon.class, Carbon.this);
      }
    };
  }

  public final Ui ui(Shell shell) {
    Check.notNull(shell, "shell == null");

    return new CarbonUi(shell);
  }

  final Ui ui(Html.Template tmpl) {
    return new CarbonUi(tmpl);
  }

}