/*
 * Copyright (C) 2015-2024 Objectos Software LTDA.
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
package objectos.html;

/**
 * The name of a HTML attribute.
 */
public sealed abstract class AttributeName extends GeneratedAttributeName permits WayAttributeName {

  /**
   * The {@code data-click} attribute.
   */
  public static final AttributeName DATA_CLICK = WayAttributeNameBuilder.create("data-click", false);

  /**
   * The {@code data-frame} attribute.
   */
  public static final AttributeName DATA_FRAME = WayAttributeNameBuilder.create("data-frame", false);

  /**
   * The {@code data-on-input} attribute.
   */
  public static final AttributeName DATA_ON_INPUT = WayAttributeNameBuilder.create("data-on-input", false);

  /**
   * The {@code data-way-click} attribute.
   */
  public static final AttributeName DATA_WAY_CLICK = WayAttributeNameBuilder.create("data-way-click", false);

  AttributeName() {}

  /**
   * Returns the index of this attribute.
   *
   * @return the index of this attribute.
   */
  public abstract int index();

  /**
   * Returns the name of the attribute.
   *
   * @return the name of the attribute
   */
  public abstract String name();

  /**
   * Returns {@code true} if this is the name of a boolean atttribute and
   * {@code false} otherwise.
   *
   * @return {@code true} if this is the name of a boolean atttribute and
   *         {@code false} otherwise
   */
  public abstract boolean booleanAttribute();

}
