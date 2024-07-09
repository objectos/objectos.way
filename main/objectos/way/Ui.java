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

import java.util.EnumMap;
import java.util.Map;
import objectos.lang.object.Check;

/**
 * The <strong>Objectos UI</strong> main class.
 */
public abstract class Ui {

  /**
   * An UI binder provides component factories that are bound to an specific
   * HTML template.
   */
  public interface Binder {

    /**
     * Creates a new UI component factory bound to the specified template.
     *
     * @param template
     *        the template responsible for the actual rendering
     *
     * @return a newly created UI component factory
     */
    Ui ui(Html.Template template);

  }

  /**
   * An attribute of an UI component.
   */
  public sealed interface Attribute {

    /**
     * The key of this attribute.
     *
     * @return the key of this attribute
     */
    AttributeKey key();

    /**
     * The value of this attribute.
     *
     * @return the value of this attribute
     */
    Object value();

  }

  /**
   * The keys representing all of the attribute kinds.
   */
  public enum AttributeKey {

    /**
     * The title attribute.
     */
    TITLE;

  }

  /**
   * An element of an UI component.
   */
  public sealed interface Element {

    /**
     * The key of this element.
     *
     * @return the key of this element
     */
    ElementKey key();

    /**
     * The value of this element.
     */
    Object value();

  }

  /**
   * The keys representing all of the element kinds.
   */
  public enum ElementKey {

    /**
     * The body element.
     */
    BODY;

  }

  /**
   * The body of an UI component.
   */
  public sealed interface Body extends PageComponent, Element {}

  /**
   * An UI component.
   */
  public sealed interface Component {

    void index(Map<AttributeKey, Object> attributes, Map<ElementKey, Object> elements);

  }

  /**
   * Component of a page.
   */
  public sealed interface PageComponent extends Component {}

  /**
   * The title of an UI component.
   */
  public sealed interface Title extends PageComponent, Attribute {}

  private record UiAttribute(AttributeKey key, Object value) implements Title {

    @Override
    public final void index(Map<AttributeKey, Object> attributes, Map<ElementKey, Object> elements) {
      attributes.put(key, value);
    }

  }

  private record UiElement(ElementKey key, Object value) implements Body {

    @Override
    public final void index(Map<AttributeKey, Object> attributes, Map<ElementKey, Object> elements) {
      elements.put(key, value);
    }

  }

  private final Map<AttributeKey, Object> attributes = new EnumMap<>(AttributeKey.class);

  private final Map<ElementKey, Object> elements = new EnumMap<>(ElementKey.class);

  /**
   * Sole constructor.
   */
  protected Ui() {}

  public final Body body(Html.FragmentLambda fragment) {
    Check.notNull(fragment, "fragment == null");

    return new UiElement(ElementKey.BODY, fragment);
  }

  public abstract void page(PageComponent... contents);

  public final PageComponent title(String title) {
    Check.notNull(title, "title == null");

    return new UiAttribute(AttributeKey.TITLE, title);
  }

  protected final Object $attr(AttributeKey key) {
    return attributes.get(key);
  }

  protected final Object $elem(ElementKey key) {
    return elements.get(key);
  }

  protected final void $index(Component... components) {
    for (Component c : components) {
      c.index(attributes, elements);
    }
  }

}