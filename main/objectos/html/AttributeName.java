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
  public static final AttributeName DATA_CLICK = WayAttributeNameBuilder.action("data-click");

  /**
   * The {@code data-frame} attribute.
   */
  public static final AttributeName DATA_FRAME = WayAttributeNameBuilder.create("data-frame", false);

  /**
   * The {@code data-on-click} attribute.
   */
  public static final AttributeName DATA_ON_CLICK = WayAttributeNameBuilder.action("data-on-click");

  /**
   * The {@code data-on-input} attribute.
   */
  public static final AttributeName DATA_ON_INPUT = WayAttributeNameBuilder.action("data-on-input");

  /**
   * The {@code data-way-click} attribute.
   */
  public static final AttributeName DATA_WAY_CLICK = WayAttributeNameBuilder.action("data-way-click");

  AttributeName() {}

  /**
   * Index of this attribute.
   *
   * @return index of this attribute.
   */
  public abstract int index();

  /**
   * Name of the attribute.
   *
   * @return name of the attribute
   */
  public abstract String name();

  /**
   * Indicates if this is the name of a boolean atttribute.
   *
   * @return {@code true} if this is the name of a boolean atttribute and
   *         {@code false} otherwise
   */
  public abstract boolean booleanAttribute();

  /**
   * Indicates if the value of this attribute must be formatted inside single
   * quotes.
   *
   * @return {@code true} if the value of this attribute must be formatted
   *         inside single quotes.
   */
  public abstract boolean singleQuoted();
  
  /**
   * Indicates the type of the value associated to an attribute with this name.
   * 
   * @return the type of the value associated to an attribute with this name.
   *         {@code null} if this is the name of a boolean attribute.
   */
  abstract Class<?> type();

}
