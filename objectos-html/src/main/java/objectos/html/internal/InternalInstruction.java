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
package objectos.html.internal;

import objectos.html.tmpl.Instruction.AbbreviationInstruction;
import objectos.html.tmpl.Instruction.AnchorInstruction;
import objectos.html.tmpl.Instruction.ArticleInstruction;
import objectos.html.tmpl.Instruction.BlockquoteInstruction;
import objectos.html.tmpl.Instruction.BodyInstruction;
import objectos.html.tmpl.Instruction.BringAttentionToInstruction;
import objectos.html.tmpl.Instruction.ButtonInstruction;
import objectos.html.tmpl.Instruction.ClipPathInstruction;
import objectos.html.tmpl.Instruction.CodeInstruction;
import objectos.html.tmpl.Instruction.DefinitionDescriptionInstruction;
import objectos.html.tmpl.Instruction.DefinitionListInstruction;
import objectos.html.tmpl.Instruction.DefinitionTermInstruction;
import objectos.html.tmpl.Instruction.DefsInstruction;
import objectos.html.tmpl.Instruction.DetailsInstruction;
import objectos.html.tmpl.Instruction.DivInstruction;
import objectos.html.tmpl.Instruction.EmphasisInstruction;
import objectos.html.tmpl.Instruction.FieldsetInstruction;
import objectos.html.tmpl.Instruction.FigureInstruction;
import objectos.html.tmpl.Instruction.FooterInstruction;
import objectos.html.tmpl.Instruction.FormInstruction;
import objectos.html.tmpl.Instruction.GInstruction;
import objectos.html.tmpl.Instruction.HeadInstruction;
import objectos.html.tmpl.Instruction.HeaderInstruction;
import objectos.html.tmpl.Instruction.Heading1Instruction;
import objectos.html.tmpl.Instruction.Heading2Instruction;
import objectos.html.tmpl.Instruction.Heading3Instruction;
import objectos.html.tmpl.Instruction.Heading4Instruction;
import objectos.html.tmpl.Instruction.Heading5Instruction;
import objectos.html.tmpl.Instruction.Heading6Instruction;
import objectos.html.tmpl.Instruction.HeadingGroupInstruction;
import objectos.html.tmpl.Instruction.HorizontalRuleInstruction;
import objectos.html.tmpl.Instruction.HtmlInstruction;
import objectos.html.tmpl.Instruction.ImageInstruction;
import objectos.html.tmpl.Instruction.InputInstruction;
import objectos.html.tmpl.Instruction.KeyboardInputInstruction;
import objectos.html.tmpl.Instruction.LabelInstruction;
import objectos.html.tmpl.Instruction.LegendInstruction;
import objectos.html.tmpl.Instruction.LineBreakInstruction;
import objectos.html.tmpl.Instruction.LinkInstruction;
import objectos.html.tmpl.Instruction.ListItemInstruction;
import objectos.html.tmpl.Instruction.MainInstruction;
import objectos.html.tmpl.Instruction.MenuInstruction;
import objectos.html.tmpl.Instruction.MetaInstruction;
import objectos.html.tmpl.Instruction.NavInstruction;
import objectos.html.tmpl.Instruction.OptionGroupInstruction;
import objectos.html.tmpl.Instruction.OptionInstruction;
import objectos.html.tmpl.Instruction.OrderedListInstruction;
import objectos.html.tmpl.Instruction.ParagraphInstruction;
import objectos.html.tmpl.Instruction.PathInstruction;
import objectos.html.tmpl.Instruction.PreInstruction;
import objectos.html.tmpl.Instruction.ProgressInstruction;
import objectos.html.tmpl.Instruction.SampleOutputInstruction;
import objectos.html.tmpl.Instruction.ScriptInstruction;
import objectos.html.tmpl.Instruction.SectionInstruction;
import objectos.html.tmpl.Instruction.SelectInstruction;
import objectos.html.tmpl.Instruction.SmallInstruction;
import objectos.html.tmpl.Instruction.SpanInstruction;
import objectos.html.tmpl.Instruction.StrongInstruction;
import objectos.html.tmpl.Instruction.StyleInstruction;
import objectos.html.tmpl.Instruction.SubscriptInstruction;
import objectos.html.tmpl.Instruction.SummaryInstruction;
import objectos.html.tmpl.Instruction.SuperscriptInstruction;
import objectos.html.tmpl.Instruction.SvgInstruction;
import objectos.html.tmpl.Instruction.TableBodyInstruction;
import objectos.html.tmpl.Instruction.TableDataInstruction;
import objectos.html.tmpl.Instruction.TableHeadInstruction;
import objectos.html.tmpl.Instruction.TableHeaderInstruction;
import objectos.html.tmpl.Instruction.TableInstruction;
import objectos.html.tmpl.Instruction.TableRowInstruction;
import objectos.html.tmpl.Instruction.TemplateInstruction;
import objectos.html.tmpl.Instruction.TextAreaInstruction;
import objectos.html.tmpl.Instruction.TitleInstruction;
import objectos.html.tmpl.Instruction.UnorderedListInstruction;

public enum InternalInstruction implements AnchorInstruction, AbbreviationInstruction, ArticleInstruction, BringAttentionToInstruction, BlockquoteInstruction, BodyInstruction, LineBreakInstruction, ButtonInstruction, ClipPathInstruction, CodeInstruction, DefinitionDescriptionInstruction, DefsInstruction, DetailsInstruction, DivInstruction, DefinitionListInstruction, DefinitionTermInstruction, EmphasisInstruction, FieldsetInstruction, FigureInstruction, FooterInstruction, FormInstruction, GInstruction, Heading1Instruction, Heading2Instruction, Heading3Instruction, Heading4Instruction, Heading5Instruction, Heading6Instruction, HeadInstruction, HeaderInstruction, HeadingGroupInstruction, HorizontalRuleInstruction, HtmlInstruction, ImageInstruction, InputInstruction, KeyboardInputInstruction, LabelInstruction, LegendInstruction, ListItemInstruction, LinkInstruction, MainInstruction, MenuInstruction, MetaInstruction, NavInstruction, OrderedListInstruction, OptionGroupInstruction, OptionInstruction, ParagraphInstruction, PathInstruction, PreInstruction, ProgressInstruction, SampleOutputInstruction, ScriptInstruction, SectionInstruction, SelectInstruction, SmallInstruction, SpanInstruction, StrongInstruction, StyleInstruction, SubscriptInstruction, SummaryInstruction, SuperscriptInstruction, SvgInstruction, TableInstruction, TableBodyInstruction, TableDataInstruction, TemplateInstruction, TextAreaInstruction, TableHeaderInstruction, TableHeadInstruction, TitleInstruction, TableRowInstruction, UnorderedListInstruction {
  INSTANCE;
}
