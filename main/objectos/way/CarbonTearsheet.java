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

final class CarbonTearsheet {

  private static final Html.ClassName INVISIBLE = Html.ClassName.className("invisible");

  private static final Html.ClassName SHELL = Html.ClassName.classText("""
      fixed inset-0px z-tearsheet
      bg-overlay
      opacity-0
      transition-opacity duration-300
      """);

  private static final Html.ClassName MODAL = Html.ClassName.classText("""
      absolute flex flex-col inset-0px
      bg-layer
      outline outline-3 -outline-offset-3 outline-transparent
      transition-transform duration-300
      translate-y-3/4

      md:top-48px md:left-64px md:right-64px
      """);

  private static final Html.ClassName HEADER = Html.ClassName.classText("""
      flex-none
      border-b border-b-border-subtle
      py-spacing-06 px-spacing-07
      """);

  private static final Html.ClassName __HEADER_TITLE = Html.ClassName.classText("""
      text-text-primary
      """);

  private static final Html.ClassName HEADER_TITLE = Html.ClassName.className(
      __HEADER_TITLE, Carbon.HEADING_04
  );

  private static final Html.ClassName __HEADER_DESCRIPTION = Html.ClassName.classText("""
      mt-spacing-05
      overflow-hidden

      md:max-w-[60%]
      """);

  private static final Html.ClassName HEADER_DESCRIPTION = Html.ClassName.className(
      __HEADER_DESCRIPTION, Carbon.BODY_COMPACT_01
  );

  private static final Html.ClassName BODY = Html.ClassName.classText("""
      flex grow
      """);

  private static final Html.ClassName INFLUENCER = Html.ClassName.classText("""
      flex-[0_0_257px]
      border-r border-r-border-subtle
      overflow-y-auto
      """);

  private static final Html.ClassName RIGHT = Html.ClassName.classText("""
      flex flex-col grow
      bg-background
      """);

  private static final Html.ClassName MAIN = Html.ClassName.classText("""
      grow
      """);

  private static final Html.ClassName ACTIONS = Html.ClassName.classText("""
      inline-flex min-w-full grow-0
      border-t border-t-border-subtle
      """);

  private static final Html.ClassName CANCEL_ACTION = Html.ClassName.classText("""
      w-full h-80px grow shrink basis-1/2 items-center
      pt-16px pr-[15px] pb-32px pl-32px
      """);

  private static final Html.ClassName ACTION_25 = Html.ClassName.classText("""
      max-w-[14.5rem] h-80px grow-0 shrink basis-1/4 items-center
      pt-16px pr-[63px] pb-32px pl-[15px]
      """);

  private CarbonTearsheet() {}

  public static Script.Action showTearsheet(Html.Id id) {
    return Script.actions(
        Script.replaceClass(id, "invisible", "visible"),
        Script.replaceClass(id, "opacity-0", "opacity-100"),
        Script.setProperty(id, "aria-hidden", "false")
    //Script.delay(50, Script.addClass(id, "tearsheet-transition"))
    );
  }

  public static Script.Action showTearsheetModal(Html.Id id) {
    return Script.replaceClass(id, "translate-y-3/4", "translate-y-0");
  }

  public static Html.ElementInstruction tearsheet(Html.TemplateBase tmpl, Html.Instruction... contents) {
    return tmpl.div(
        Carbon.ROLE_PRESENTATION,
        Carbon.ARIA_HIDDEN_TRUE,

        SHELL, INVISIBLE,

        tmpl.flatten(contents)
    );
  }

  public static Html.ElementInstruction tearsheetModal(Html.TemplateBase tmpl, Html.Instruction... contents) {
    return tmpl.div(
        Carbon.ROLE_DIALOG,

        MODAL,

        tmpl.flatten(contents)
    );
  }

  public static Html.ElementInstruction tearsheetHeader(Html.TemplateBase tmpl, Html.Instruction... contents) {
    return tmpl.div(
        HEADER,

        tmpl.flatten(contents)
    );
  }

  public static Html.ElementInstruction tearsheetHeaderTitle(Html.TemplateBase tmpl, String text) {
    return tmpl.h3(
        HEADER_TITLE,

        tmpl.t(text)
    );
  }

  public static Html.ElementInstruction tearsheetHeaderDescription(Html.TemplateBase tmpl, String text) {
    return tmpl.p(
        HEADER_DESCRIPTION,

        tmpl.t(text)
    );
  }

  public static Html.ElementInstruction tearsheetBody(Html.TemplateBase tmpl, Html.Instruction... contents) {
    return tmpl.div(
        BODY,

        tmpl.flatten(contents)
    );
  }

  public static Html.ElementInstruction tearsheetInfluencer(Html.TemplateBase tmpl, Html.Instruction... contents) {
    return tmpl.div(
        INFLUENCER,

        tmpl.flatten(contents)
    );
  }

  public static Html.ElementInstruction tearsheetRight(Html.TemplateBase tmpl, Html.Instruction... contents) {
    return tmpl.div(
        RIGHT,

        tmpl.flatten(contents)
    );
  }

  public static Html.ElementInstruction tearsheetMain(Html.TemplateBase tmpl, Html.Instruction... contents) {
    return tmpl.div(
        MAIN,

        tmpl.flatten(contents)
    );
  }

  public static Html.ElementInstruction tearsheetActions(Html.TemplateBase tmpl, Html.Instruction... contents) {
    return tmpl.div(
        ACTIONS,

        tmpl.flatten(contents)
    );
  }

  public static Html.ElementInstruction tearsheetCancelAction(Html.TemplateBase tmpl, String label) {
    return tmpl.button(
        CarbonButton.__BUTTON_BASE, CarbonButton.__BUTTON_GHOST,

        CANCEL_ACTION,

        tmpl.type("button"),

        tmpl.t(label)
    );
  }

  public static Html.ElementInstruction tearsheetBackAction(Html.TemplateBase tmpl, String label) {
    return tmpl.button(
        CarbonButton.__BUTTON_BASE, CarbonButton.__BUTTON_SECONDARY,

        ACTION_25,

        tmpl.type("button"),

        tmpl.t(label)
    );
  }

  public static Html.ElementInstruction tearsheetNextAction(Html.TemplateBase tmpl, String label) {
    return tmpl.button(
        CarbonButton.__BUTTON_BASE, CarbonButton.__BUTTON_PRIMARY,

        ACTION_25,

        tmpl.type("button"),

        tmpl.t(label)
    );
  }

}