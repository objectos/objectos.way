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
import objectos.way.Html.ElementInstruction;

@SuppressWarnings("unused")
final class CarbonProgressIndicator implements Carbon.Component.ProgressIndicator, Carbon.Component.ProgressIndicator.Step {

  private static final class CarbonProgressStep {

    final String title;
    final String description;
    final String optionalLabel;

    boolean enabled = true;
    boolean valid = true;

    CarbonProgressStep(String title, String description) {
      this(title, description, null);
    }

    CarbonProgressStep(String title, String description, String optionalLabel) {
      this.title = title;
      this.description = description;
      this.optionalLabel = optionalLabel;
    }

  }

  private int currentIndex;

  private final List<CarbonProgressIndicator.CarbonProgressStep> steps = new GrowableList<>();

  private final Html.TemplateBase tmpl;

  private boolean vertical;

  CarbonProgressIndicator(Html.TemplateBase tmpl) {
    this.tmpl = tmpl;
  }

  @Override
  public final ProgressIndicator currentIndex(int value) {
    currentIndex = value;

    return this;
  }

  @Override
  public final ProgressIndicator vertical() {
    vertical = true;

    return this;
  }

  @Override
  public final ProgressIndicator.Step step(String title, String description) {
    Check.notNull(title, "title == null");
    Check.notNull(description, "description == null");

    steps.add(new CarbonProgressIndicator.CarbonProgressStep(title, description));

    return this;
  }

  @Override
  public final ProgressIndicator.Step step(String title, String description, String optionalLabel) {
    Check.notNull(title, "title == null");
    Check.notNull(description, "description == null");
    Check.notNull(optionalLabel, "optionalLabel == null");

    steps.add(new CarbonProgressIndicator.CarbonProgressStep(title, description, optionalLabel));

    return this;
  }

  @Override
  public final ProgressIndicator.Step enabled(boolean value) {
    CarbonProgressIndicator.CarbonProgressStep step;
    step = steps.getLast();

    step.enabled = value;

    return this;
  }

  @Override
  public final ProgressIndicator.Step valid(boolean value) {
    CarbonProgressIndicator.CarbonProgressStep step;
    step = steps.getLast();

    step.valid = value;

    return this;
  }

  @Override
  public final ElementInstruction render() {
    return tmpl.ul(
        vertical ? tmpl.className("flex flex-col") : tmpl.noop()
    );
  }

}