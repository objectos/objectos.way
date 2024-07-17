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

abstract class CarbonClasses {

  static final String GENERATE_ME = "opacity-100";

  private static final String _BUTTON_RESET = "cursor-pointer appearance-none";

  public static final Html.ClassName HEADER = Html.className(
      "fixed top-0px right-0px left-0px z-header",
      "flex items-center h-header",
      "border-b border-b-subtle",
      "bg"
  );

  public static final Html.ClassName HEADER_MENU_BUTTON = Html.className(
      _BUTTON_RESET,

      // header__action
      "size-header",
      "border border-transparent",
      "transition-colors duration-100",
      "active:bg-active",
      "focus:border-focus focus:outline-none",
      "hover:bg-hover",
      // header__menu-toggle
      "flex items-center justify-center",
      // header__menu-toggle__hidden
      "lg:hidden",
      // header__menu-trigger
      "svg:fill-primary"
  );

  public static final Html.ClassName HEADER_CLOSE_BUTTON = Html.className(
      HEADER_MENU_BUTTON,

      "hidden",

      // header__action--active
      "bg-layer",
      "border-x-subtle"
  );

  public static final Html.ClassName HEADER_MENU_ITEM = Html.className(
      "relative flex h-full select-none items-center",
      "border-2 border-transparent",
      "bg",
      "px-16px",
      "text-14px leading-18px tracking-0 font-400",
      "transition-colors duration-100",
      "active:bg-active active:text-primary",
      "focus:border-focus focus:outline-none",
      "hover:bg-hover hover:text-primary"
  );

  public static final Html.ClassName HEADER_MENU_ITEM_ACTIVE = Html.className(
      "text-primary",
      "after:absolute after:-bottom-2px after:-left-2px after:-right-2px",
      "after:block after:border-b-3 after:border-b-interactive after:content-empty"
  );

  public static final Html.ClassName HEADER_MENU_ITEM_INACTIVE = Html.className(
      "text-secondary"
  );

  public static final Html.ClassName HEADER_NAME = Html.className(
      "flex h-full select-none items-center",
      "border-2 border-transparent",
      "px-16px",
      "text-body-compact-01 text-primary font-600 leading-20px tracking-0.1px",
      "outline-none",
      "transition-colors duration-100",
      "focus:border-focus",
      "lg:pl-16px lg:pr-32px",

      "span:font-400"
  );

  public static final Html.ClassName HEADER_NAV = Html.className(
      "relative hidden h-full pl-16px",
      "lg:flex lg:items-center",
      "lg:before:relative lg:before:-left-16px lg:before:z-header lg:before:block",
      "lg:before:h-1/2 lg:before:w-1px",
      "lg:before:border-l lg:before:border-l-subtle",
      "lg:before:content-empty"
  );

  public static final Html.ClassName HEADER_NAV_LIST = Html.className(
      "h-full flex text-secondary"
  );

  public static final Html.ClassName HEADER_OFFSET = Html.className(
      "mt-header"
  );

  public static final Html.ClassName OVERLAY = Html.className(
      "fixed inset-0px block hidden z-overlay",
      "bg-overlay opacity-0",
      "transition-opacity duration-300"
  );

  public static final Html.ClassName SIDE_NAV = Html.className(
      "fixed top-0px bottom-0px left-0px z-header hidden",
      "w-0px",
      "bg",
      "text-secondary",
      "transition-all duration-200"
  );

  public static final Html.ClassName SIDE_NAV_HEADER_LIST = Html.className(
      "margin-bottom-32px"
  );

  public static final Html.ClassName SIDE_NAV_HEADER_ITEM = Html.className(
      "relative flex min-h-32px",
      "items-center justify-between whitespace-nowrap",
      "border-2 border-transparent",
      "px-16px",
      "text-heading-compact-01 text-secondary",
      "outline outline-2 -outline-offset-2 outline-transparent",
      "transition-colors duration-100",
      "active:bg-active active:text-primary",
      "focus:outline-focus",
      "hover:bg-hover hover:text-primary"
  );

  public static final Html.ClassName SIDE_NAV_HEADER_ITEM_ACTIVE = Html.className(
      "after:absolute after:-top-2px after:-bottom-2px after:-left-2px",
      "after:block after:border-l-3 after:border-l-interactive after:content-empty"
  );

  public static final Html.ClassName SIDE_NAV_HEADER_ITEM_INACTIVE = Html.className(
  );

  public static final Html.ClassName SIDE_NAV_ITEMS = Html.className(
      "flex-1 pt-16px"
  );

  public static final Html.ClassName SIDE_NAV_WIDTH = Html.className(
      "w-256px"
  );

  // utilities

  public static final Html.ClassName HIDDEN = Html.className("hidden");

  public static final Html.ClassName OPACITY_0 = Html.className("opacity-0");

  public static final Html.ClassName OPACITY_100 = Html.className("opacity-100");

}