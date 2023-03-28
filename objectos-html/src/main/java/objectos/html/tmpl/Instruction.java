/*
 * Copyright (C) 2015-2023 Objectos Software LTDA.
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
package objectos.html.tmpl;

import objectos.html.internal.InternalInstruction;

public sealed interface Instruction {
  sealed interface AInstruction extends Instruction permits InternalInstruction {}

  sealed interface AbbrInstruction extends Instruction permits InternalInstruction {}

  sealed interface ArticleInstruction extends Instruction permits InternalInstruction {}

  sealed interface BInstruction extends Instruction permits InternalInstruction {}

  sealed interface BlockquoteInstruction extends Instruction permits InternalInstruction {}

  sealed interface BodyInstruction extends Instruction permits InternalInstruction {}

  sealed interface BrInstruction extends Instruction permits InternalInstruction {}

  sealed interface ButtonInstruction extends Instruction permits InternalInstruction {}

  sealed interface ClipPathInstruction extends Instruction permits InternalInstruction {}

  sealed interface CodeInstruction extends Instruction permits InternalInstruction {}

  sealed interface DdInstruction extends Instruction permits InternalInstruction {}

  sealed interface DefsInstruction extends Instruction permits InternalInstruction {}

  sealed interface DetailsInstruction extends Instruction permits InternalInstruction {}

  sealed interface DivInstruction extends Instruction permits InternalInstruction {}

  sealed interface DlInstruction extends Instruction permits InternalInstruction {}

  sealed interface DtInstruction extends Instruction permits InternalInstruction {}

  sealed interface EmInstruction extends Instruction permits InternalInstruction {}

  sealed interface FieldsetInstruction extends Instruction permits InternalInstruction {}

  sealed interface FigureInstruction extends Instruction permits InternalInstruction {}

  sealed interface FooterInstruction extends Instruction permits InternalInstruction {}

  sealed interface FormInstruction extends Instruction permits InternalInstruction {}

  sealed interface GInstruction extends Instruction permits InternalInstruction {}

  sealed interface H1Instruction extends Instruction permits InternalInstruction {}

  sealed interface H2Instruction extends Instruction permits InternalInstruction {}

  sealed interface H3Instruction extends Instruction permits InternalInstruction {}

  sealed interface H4Instruction extends Instruction permits InternalInstruction {}

  sealed interface H5Instruction extends Instruction permits InternalInstruction {}

  sealed interface H6Instruction extends Instruction permits InternalInstruction {}

  sealed interface HeadInstruction extends Instruction permits InternalInstruction {}

  sealed interface HeaderInstruction extends Instruction permits InternalInstruction {}

  sealed interface HgroupInstruction extends Instruction permits InternalInstruction {}

  sealed interface HrInstruction extends Instruction permits InternalInstruction {}

  sealed interface HtmlInstruction extends Instruction permits InternalInstruction {}

  sealed interface ImgInstruction extends Instruction permits InternalInstruction {}

  sealed interface InputInstruction extends Instruction permits InternalInstruction {}

  sealed interface KbdInstruction extends Instruction permits InternalInstruction {}

  sealed interface LabelInstruction extends Instruction permits InternalInstruction {}

  sealed interface LegendInstruction extends Instruction permits InternalInstruction {}

  sealed interface LiInstruction extends Instruction permits InternalInstruction {}

  sealed interface LinkInstruction extends Instruction permits InternalInstruction {}

  sealed interface MainInstruction extends Instruction permits InternalInstruction {}

  sealed interface MenuInstruction extends Instruction permits InternalInstruction {}

  sealed interface MetaInstruction extends Instruction permits InternalInstruction {}

  sealed interface NavInstruction extends Instruction permits InternalInstruction {}

  sealed interface OlInstruction extends Instruction permits InternalInstruction {}

  sealed interface OptgroupInstruction extends Instruction permits InternalInstruction {}

  sealed interface OptionInstruction extends Instruction permits InternalInstruction {}

  sealed interface PInstruction extends Instruction permits InternalInstruction {}

  sealed interface PathInstruction extends Instruction permits InternalInstruction {}

  sealed interface PreInstruction extends Instruction permits InternalInstruction {}

  sealed interface ProgressInstruction extends Instruction permits InternalInstruction {}

  sealed interface SampInstruction extends Instruction permits InternalInstruction {}

  sealed interface ScriptInstruction extends Instruction permits InternalInstruction {}

  sealed interface SectionInstruction extends Instruction permits InternalInstruction {}

  sealed interface SelectInstruction extends Instruction permits InternalInstruction {}

  sealed interface SmallInstruction extends Instruction permits InternalInstruction {}

  sealed interface SpanInstruction extends Instruction permits InternalInstruction {}

  sealed interface StrongInstruction extends Instruction permits InternalInstruction {}

  sealed interface StyleInstruction extends Instruction permits InternalInstruction {}

  sealed interface SubInstruction extends Instruction permits InternalInstruction {}

  sealed interface SummaryInstruction extends Instruction permits InternalInstruction {}

  sealed interface SupInstruction extends Instruction permits InternalInstruction {}

  sealed interface SvgInstruction extends Instruction permits InternalInstruction {}

  sealed interface TableInstruction extends Instruction permits InternalInstruction {}

  sealed interface TbodyInstruction extends Instruction permits InternalInstruction {}

  sealed interface TdInstruction extends Instruction permits InternalInstruction {}

  sealed interface TemplateInstruction extends Instruction permits InternalInstruction {}

  sealed interface TextareaInstruction extends Instruction permits InternalInstruction {}

  sealed interface ThInstruction extends Instruction permits InternalInstruction {}

  sealed interface TheadInstruction extends Instruction permits InternalInstruction {}

  sealed interface TitleInstruction extends Instruction permits InternalInstruction {}

  sealed interface TrInstruction extends Instruction permits InternalInstruction {}

  sealed interface UlInstruction extends Instruction permits InternalInstruction {}
}
