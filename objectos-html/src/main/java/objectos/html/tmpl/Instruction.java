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
  sealed interface AnchorInstruction extends Instruction {}

  sealed interface AbbreviationInstruction extends Instruction {}

  sealed interface ArticleInstruction extends Instruction {}

  sealed interface BringAttentionToInstruction extends Instruction {}

  sealed interface BlockquoteInstruction extends Instruction {}

  sealed interface BodyInstruction extends Instruction {}

  sealed interface LineBreakInstruction extends Instruction {}

  sealed interface ButtonInstruction extends Instruction {}

  sealed interface ClipPathInstruction extends Instruction {}

  sealed interface CodeInstruction extends Instruction {}

  sealed interface DefinitionDescriptionInstruction extends Instruction {}

  sealed interface DefsInstruction extends Instruction {}

  sealed interface DetailsInstruction extends Instruction {}

  sealed interface DivInstruction extends Instruction {}

  sealed interface DefinitionListInstruction extends Instruction {}

  sealed interface DefinitionTermInstruction extends Instruction {}

  sealed interface EmphasisInstruction extends Instruction {}

  sealed interface FieldsetInstruction extends Instruction {}

  sealed interface FigureInstruction extends Instruction {}

  sealed interface FooterInstruction extends Instruction {}

  sealed interface FormInstruction extends Instruction {}

  sealed interface GInstruction extends Instruction {}

  sealed interface Heading1Instruction extends Instruction {}

  sealed interface Heading2Instruction extends Instruction {}

  sealed interface Heading3Instruction extends Instruction {}

  sealed interface Heading4Instruction extends Instruction {}

  sealed interface Heading5Instruction extends Instruction {}

  sealed interface Heading6Instruction extends Instruction {}

  sealed interface HeadInstruction extends Instruction {}

  sealed interface HeaderInstruction extends Instruction {}

  sealed interface HeadingGroupInstruction extends Instruction {}

  sealed interface HorizontalRuleInstruction extends Instruction {}

  sealed interface HtmlInstruction extends Instruction {}

  sealed interface ImageInstruction extends Instruction {}

  sealed interface InputInstruction extends Instruction {}

  sealed interface KeyboardInputInstruction extends Instruction {}

  sealed interface LabelInstruction extends Instruction {}

  sealed interface LegendInstruction extends Instruction {}

  sealed interface ListItemInstruction extends Instruction {}

  sealed interface LinkInstruction extends Instruction {}

  sealed interface MainInstruction extends Instruction {}

  sealed interface MenuInstruction extends Instruction {}

  sealed interface MetaInstruction extends Instruction {}

  sealed interface NavInstruction extends Instruction {}

  sealed interface OrderedListInstruction extends Instruction {}

  sealed interface OptionGroupInstruction extends Instruction {}

  sealed interface OptionInstruction extends Instruction {}

  sealed interface ParagraphInstruction extends Instruction {}

  sealed interface PathInstruction extends Instruction {}

  sealed interface PreInstruction extends Instruction {}

  sealed interface ProgressInstruction extends Instruction {}

  sealed interface SampleOutputInstruction extends Instruction {}

  sealed interface ScriptInstruction extends Instruction {}

  sealed interface SectionInstruction extends Instruction {}

  sealed interface SelectInstruction extends Instruction {}

  sealed interface SmallInstruction extends Instruction {}

  sealed interface SpanInstruction extends Instruction {}

  sealed interface StrongInstruction extends Instruction {}

  sealed interface StyleInstruction extends Instruction {}

  sealed interface SubscriptInstruction extends Instruction {}

  sealed interface SummaryInstruction extends Instruction {}

  sealed interface SuperscriptInstruction extends Instruction {}

  sealed interface SvgInstruction extends Instruction {}

  sealed interface TableInstruction extends Instruction {}

  sealed interface TableBodyInstruction extends Instruction {}

  sealed interface TableDataInstruction extends Instruction {}

  sealed interface TemplateInstruction extends Instruction {}

  sealed interface TextAreaInstruction extends Instruction {}

  sealed interface TableHeaderInstruction extends Instruction {}

  sealed interface TableHeadInstruction extends Instruction {}

  sealed interface TitleInstruction extends Instruction {}

  sealed interface TableRowInstruction extends Instruction {}

  sealed interface UnorderedListInstruction extends Instruction {}

  sealed interface GlobalAttribute extends AnchorInstruction, AbbreviationInstruction, ArticleInstruction, BringAttentionToInstruction, BlockquoteInstruction, BodyInstruction, LineBreakInstruction, ButtonInstruction, ClipPathInstruction, CodeInstruction, DefinitionDescriptionInstruction, DefsInstruction, DetailsInstruction, DivInstruction, DefinitionListInstruction, DefinitionTermInstruction, EmphasisInstruction, FieldsetInstruction, FigureInstruction, FooterInstruction, FormInstruction, GInstruction, Heading1Instruction, Heading2Instruction, Heading3Instruction, Heading4Instruction, Heading5Instruction, Heading6Instruction, HeadInstruction, HeaderInstruction, HeadingGroupInstruction, HorizontalRuleInstruction, HtmlInstruction, ImageInstruction, InputInstruction, KeyboardInputInstruction, LabelInstruction, LegendInstruction, ListItemInstruction, LinkInstruction, MainInstruction, MenuInstruction, MetaInstruction, NavInstruction, OrderedListInstruction, OptionGroupInstruction, OptionInstruction, ParagraphInstruction, PathInstruction, PreInstruction, ProgressInstruction, SampleOutputInstruction, ScriptInstruction, SectionInstruction, SelectInstruction, SmallInstruction, SpanInstruction, StrongInstruction, StyleInstruction, SubscriptInstruction, SummaryInstruction, SuperscriptInstruction, SvgInstruction, TableInstruction, TableBodyInstruction, TableDataInstruction, TemplateInstruction, TextAreaInstruction, TableHeaderInstruction, TableHeadInstruction, TitleInstruction, TableRowInstruction, UnorderedListInstruction permits InternalInstruction {}
}
