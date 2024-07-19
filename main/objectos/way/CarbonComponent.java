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

import objectos.way.Carbon.Icon;

abstract class CarbonComponent {

  final Html.Template tmpl;

  CarbonComponent(Html.Template tmpl) {
    this.tmpl = tmpl;
  }

  public abstract void render();

  final Html.ElementInstruction icon16(Icon icon, Html.AttributeInstruction... attributes) {
    return renderIcon(icon, "16px", attributes);
  }

  final Html.ElementInstruction icon20(Icon icon, Html.AttributeInstruction... attributes) {
    return renderIcon(icon, "20px", attributes);
  }

  final Html.ElementInstruction icon24(Icon icon, Html.AttributeInstruction... attributes) {
    return renderIcon(icon, "24px", attributes);
  }

  final Html.ElementInstruction icon32(Icon icon, Html.AttributeInstruction... attributes) {
    return renderIcon(icon, "32px", attributes);
  }

  private Html.ElementInstruction renderIcon(Carbon.Icon icon, String size, Html.AttributeInstruction... attributes) {
    return tmpl.svg(
        tmpl.xmlns("http://www.w3.org/2000/svg"),
        tmpl.fill("currentColor"),
        tmpl.width(size), tmpl.height(size), tmpl.viewBox("0 0 32 32"),

        tmpl.flatten(attributes),

        tmpl.raw(icon.raw)
    );
  }

}