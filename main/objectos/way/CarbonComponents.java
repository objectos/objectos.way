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

abstract class CarbonComponents {

  CarbonComponents() {}

  final Css.Option button() {
    return Css.components("""
    # button-reset
    cursor-pointer appearance-none
    """);
  }

  final Css.Option grid() {
    return Css.components("""
    # grid-wide
    mx-auto grid w-full max-w-screen-max grid-cols-4
    px-0px
    md:grid-cols-8 md:px-16px
    lg:grid-cols-16
    max:px-24px
    *:mx-16px
    """);
  }

  final Css.Option header() {
    return Css.components("""
    # header
    fixed top-0px right-0px left-0px z-header
    flex items-center h-header
    border-b border-b-subtle
    bg

    # __header-button
    button-reset
    size-header items-center justify-center
    border border-transparent
    transition-colors duration-100
    active:bg-active
    focus:border-focus focus:outline-none
    hover:bg-hover
    lg:hidden
    svg:fill-primary

    # header-menu-button
    __header-button
    flex

    # header-menu-button-toggle
    __header-button
    hidden

    # header-close-button
    __header-button
    hidden border-x-subtle bg-layer

    # header-close-button-toggle
    __header-button
    flex border-x-subtle bg-layer

    # header-name
    flex h-full select-none items-center
    border-2 border-transparent
    px-16px
    text-body-compact-01 text-primary font-600 leading-20px tracking-0.1px
    outline-none
    transition-colors duration-100
    focus:border-focus
    lg:pl-16px lg:pr-32px
    span:font-400

    # header-nav
    relative hidden h-full pl-16px
    lg:flex lg:items-center
    lg:before:relative lg:before:-left-16px lg:before:z-header lg:before:block
    lg:before:h-1/2 lg:before:w-1px
    lg:before:border-l lg:before:border-l-subtle
    lg:before:content-empty

    # header-nav-link
    relative flex h-full select-none items-center
    border-2 border-transparent
    bg
    px-16px
    text-14px leading-18px tracking-0 font-400
    transition-colors duration-100
    active:bg-active active:text-primary
    focus:border-focus focus:outline-none
    hover:bg-hover hover:text-primary

    # header-nav-link-active
    header-nav-link
    text-primary
    after:absolute after:-bottom-2px after:-left-2px after:-right-2px
    after:block after:border-b-3 after:border-b-interactive after:content-empty

    # header-nav-link-inactive
    header-nav-link
    text-secondary

    # header-nav-list
    h-full flex text-secondary

    # header-offset
    mt-header
    """);
  }

  final Css.Option overlay() {
    return Css.components("""
    # __overlay
    fixed inset-0px z-overlay
    bg-overlay
    transition-opacity duration-300
    lg:hidden

    # overlay
    __overlay hidden opacity-0

    # overlay-toggle
    __overlay block opacity-100
    """);
  }

  final Css.Option sideNav() {
    return Css.components("""
    # __side-nav
    fixed top-0px bottom-0px left-0px z-header
    bg
    text-secondary
    transition-all duration-100
    lg:hidden

    # side-nav
    __side-nav invisible w-0px

    # side-nav-toggle
    __side-nav visible w-side-nav

    # side-nav-header-link
    relative flex min-h-32px
    items-center justify-between whitespace-nowrap
    border-2 border-transparent
    px-16px
    text-heading-compact-01 text-secondary
    outline outline-2 -outline-offset-2 outline-transparent
    transition-colors duration-100
    active:bg-active active:text-primary
    focus:outline-focus
    hover:bg-hover hover:text-primary

    # side-nav-header-link-active
    side-nav-header-link
    after:absolute after:-top-2px after:-bottom-2px after:-left-2px
    after:block after:border-l-3 after:border-l-interactive after:content-empty

    # side-nav-header-link-inactive
    side-nav-header-link
    text-secondary

    # side-nav-header-list
    margin-bottom-32px
    lg:hidden

    # side-nav-header-item
    w-auto h-auto overflow-hidden

    # side-nav-list
    flex-1 pt-16px

    # side-nav-link
    relative flex min-h-32px
    items-center justify-between whitespace-nowrap
    px-16px
    text-heading-compact-01
    outline outline-2 -outline-offset-2 outline-transparent
    transition-colors duration-100

    focus:outline-focus
    hover:bg-hover hover:text-primary

    span:select-none span:text-14px span:leading-20px span:tracking-0.1px span:truncate

    # side-nav-link-active
    side-nav-link
    bg-selected text-link-primary font-600
    after:absolute after:top-0px after:bottom-0px after:left-0px
    after:block after:border-l-3 after:border-l-interactive after:content-empty
    span:text-primary

    # side-nav-link-inactive
    side-nav-link
    span:text-secondary

    # side-nav-offset
    lg:ml-side-nav

    # side-nav-persistent
    lg:visible lg:more:block lg:w-side-nav
    """);
  }

  final Css.Option tile() {
    return Css.components("""
    # tile
    relative block min-w-128px min-h-64px
    bg-layer
    p-16px
    outline outline-2 -outline-offset-2 outline-transparent
    """);
  }

}