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
import objectos.way.Html.AttributeName;

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

    PX16("16px", "0 0 16 16"),

    PX20("20px", "0 0 20 20"),

    PX24("24px", "0 0 24 24"),

    PX32("32px", "0 0 32 32");

    final String size;

    final String viewBox;

    private IconSize(String size, String viewBox) {
      this.size = size;
      this.viewBox = viewBox;
    }

  }

  // attributes

  /**
   * The nested types of this interface represent the Carbon UI attributes.
   */
  public sealed interface Attribute extends Component {}

  enum AttributeKey {
    ICON,

    IS_ACTIVE,

    NAME,

    PREFIX;

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

  sealed static abstract class HasRenderMethod {
    abstract void render();
  }

  /**
   * The UI builder.
   */
  public sealed static abstract class Ui permits CarbonUi {

    final class CarbonStringAttribute extends HasRenderMethod implements Attribute {
      final Html.AttributeName name;
      final String value;

      public CarbonStringAttribute(AttributeName name, String value) {
        this.name = name;
        this.value = Check.notNull(value, "value == null");
      }

      @Override
      final void render() {
        tmpl.attribute(name, value);
      }
    }

    non-sealed abstract class Pojo extends HasRenderMethod implements Carbon.Element, Html.FragmentLambda {

      private Map<AttributeKey, Object> attributes;

      private List<HasRenderMethod> children;

      private CarbonTheme theme;

      public Pojo(Carbon.Component... components) {
        for (Carbon.Component c : components) {
          switch (c) {
            case CarbonAttribute o -> addAttribute(o);

            case CarbonTheme o -> theme = o;

            case HasRenderMethod o -> addChild(o);
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
        for (HasRenderMethod child : children) {
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

      private void addChild(HasRenderMethod o) {
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
     * Creates a new {@code aria-hidden} attribute with the {@code true} value.
     *
     * @return a new attribute
     */
    public final Attribute ariaHidden() {
      return new CarbonStringAttribute(HtmlAttributeName.ARIA_HIDDEN, "true");
    }

    /**
     * Creates a new {@code aria-label} attribute with the specified value.
     *
     * @param value the string value of the attribute
     *
     * @return a new attribute
     */
    public final Attribute ariaLabel(String value) {
      return new CarbonStringAttribute(HtmlAttributeName.ARIA_LABEL, value);
    }

    /**
     * Creates a new {@code data-frame} attribute with the specified name and
     * value.
     *
     * @param value the string value of the attribute
     *
     * @return a new attribute
     */
    public final Attribute dataFrame(String name, String value) {
      Check.notNull(name, "name == null");
      Check.notNull(value, "value == null");

      return new CarbonStringAttribute(HtmlAttributeName.DATA_FRAME, name + ":" + value);
    }

    /**
     * Creates a new {@code href} attribute with the specified value.
     *
     * @param value the string value of the attribute
     *
     * @return a new attribute
     */
    public final Attribute href(String value) {
      return new CarbonStringAttribute(HtmlAttributeName.HREF, value);
    }

    /**
     * Creates a new {@code id} attribute with the specified value.
     *
     * @param value
     *        the id value
     *
     * @return a new attribute
     */
    public final Attribute id(Html.Id id) {
      return new CarbonStringAttribute(HtmlAttributeName.ID, id.value());
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
      return new CarbonStringAttribute(HtmlAttributeName.TITLE, value);
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

    public final Element icon(Icon icon, IconSize size, Attribute... attributes) {
      final Icon i = Check.notNull(icon, "icon == null");
      final IconSize s = Check.notNull(size, "size == null");

      return new Pojo(attributes) {
        @Override
        final void render() {
          renderIcon(this, i, s);
        }
      };
    }

    abstract void renderIcon(Pojo pojo, Icon icon, IconSize size);

    /**
     * Creates a navigation overlay component.
     *
     * @param attributes
     *        the attributes of this element
     *
     * @return a newly constructed element
     */
    public final Element overlay(Attribute... attributes) {
      return new Pojo(attributes) {
        @Override
        final void render() { renderOverlay(this); }
      };
    }

    abstract void renderOverlay(Pojo pojo);

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

    protected final Html.FragmentInstruction ui(Element el1, Element el2) {
      return include(() -> {
        renderNow(el1);
        renderNow(el2);
      });
    }

    protected final Html.FragmentInstruction ui(Element el1, Element el2, Element el3) {
      return include(() -> {
        renderNow(el1);
        renderNow(el2);
        renderNow(el3);
      });
    }

    protected final Html.FragmentInstruction ui(Element el1, Element el2, Element el3, Element el4) {
      return include(() -> {
        renderNow(el1);
        renderNow(el2);
        renderNow(el3);
        renderNow(el4);
      });
    }

    protected final Html.FragmentInstruction ui(Element... elements) {
      return include(() -> {
        for (var el : elements) {
          renderNow(el);
        }
      });
    }

    private void renderNow(Element element) {
      Ui.Pojo pojo;
      pojo = (Ui.Pojo) element;

      pojo.render();
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