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

import objectos.html.tmpl.Instruction.AInstruction;
import objectos.html.tmpl.Instruction.AbbrInstruction;
import objectos.html.tmpl.Instruction.ArticleInstruction;
import objectos.html.tmpl.Instruction.BInstruction;
import objectos.html.tmpl.Instruction.BlockquoteInstruction;
import objectos.html.tmpl.Instruction.BodyInstruction;
import objectos.html.tmpl.Instruction.BrInstruction;
import objectos.html.tmpl.Instruction.ButtonInstruction;
import objectos.html.tmpl.Instruction.ClipPathInstruction;
import objectos.html.tmpl.Instruction.CodeInstruction;
import objectos.html.tmpl.Instruction.DdInstruction;
import objectos.html.tmpl.Instruction.DefsInstruction;
import objectos.html.tmpl.Instruction.DetailsInstruction;
import objectos.html.tmpl.Instruction.DivInstruction;
import objectos.html.tmpl.Instruction.DlInstruction;
import objectos.html.tmpl.Instruction.DtInstruction;
import objectos.html.tmpl.Instruction.EmInstruction;
import objectos.html.tmpl.Instruction.FieldsetInstruction;
import objectos.html.tmpl.Instruction.FigureInstruction;
import objectos.html.tmpl.Instruction.FooterInstruction;
import objectos.html.tmpl.Instruction.FormInstruction;
import objectos.html.tmpl.Instruction.GInstruction;
import objectos.html.tmpl.Instruction.H1Instruction;
import objectos.html.tmpl.Instruction.H2Instruction;
import objectos.html.tmpl.Instruction.H3Instruction;
import objectos.html.tmpl.Instruction.H4Instruction;
import objectos.html.tmpl.Instruction.H5Instruction;
import objectos.html.tmpl.Instruction.H6Instruction;
import objectos.html.tmpl.Instruction.HeadInstruction;
import objectos.html.tmpl.Instruction.HeaderInstruction;
import objectos.html.tmpl.Instruction.HgroupInstruction;
import objectos.html.tmpl.Instruction.HrInstruction;
import objectos.html.tmpl.Instruction.HtmlInstruction;
import objectos.html.tmpl.Instruction.ImgInstruction;
import objectos.html.tmpl.Instruction.InputInstruction;
import objectos.html.tmpl.Instruction.KbdInstruction;
import objectos.html.tmpl.Instruction.LabelInstruction;
import objectos.html.tmpl.Instruction.LegendInstruction;
import objectos.html.tmpl.Instruction.LiInstruction;
import objectos.html.tmpl.Instruction.LinkInstruction;
import objectos.html.tmpl.Instruction.MainInstruction;
import objectos.html.tmpl.Instruction.MenuInstruction;
import objectos.html.tmpl.Instruction.MetaInstruction;
import objectos.html.tmpl.Instruction.NavInstruction;
import objectos.html.tmpl.Instruction.OlInstruction;
import objectos.html.tmpl.Instruction.OptgroupInstruction;
import objectos.html.tmpl.Instruction.OptionInstruction;
import objectos.html.tmpl.Instruction.PInstruction;
import objectos.html.tmpl.Instruction.PathInstruction;
import objectos.html.tmpl.Instruction.PreInstruction;
import objectos.html.tmpl.Instruction.ProgressInstruction;
import objectos.html.tmpl.Instruction.SampInstruction;
import objectos.html.tmpl.Instruction.ScriptInstruction;
import objectos.html.tmpl.Instruction.SectionInstruction;
import objectos.html.tmpl.Instruction.SelectInstruction;
import objectos.html.tmpl.Instruction.SmallInstruction;
import objectos.html.tmpl.Instruction.SpanInstruction;
import objectos.html.tmpl.Instruction.StrongInstruction;
import objectos.html.tmpl.Instruction.StyleInstruction;
import objectos.html.tmpl.Instruction.SubInstruction;
import objectos.html.tmpl.Instruction.SummaryInstruction;
import objectos.html.tmpl.Instruction.SupInstruction;
import objectos.html.tmpl.Instruction.SvgInstruction;
import objectos.html.tmpl.Instruction.TableInstruction;
import objectos.html.tmpl.Instruction.TbodyInstruction;
import objectos.html.tmpl.Instruction.TdInstruction;
import objectos.html.tmpl.Instruction.TemplateInstruction;
import objectos.html.tmpl.Instruction.TextareaInstruction;
import objectos.html.tmpl.Instruction.ThInstruction;
import objectos.html.tmpl.Instruction.TheadInstruction;
import objectos.html.tmpl.Instruction.TitleInstruction;
import objectos.html.tmpl.Instruction.TrInstruction;
import objectos.html.tmpl.Instruction.UlInstruction;

public enum InternalInstruction implements AInstruction, AbbrInstruction, ArticleInstruction, BInstruction, BlockquoteInstruction, BodyInstruction, BrInstruction, ButtonInstruction, ClipPathInstruction, CodeInstruction, DdInstruction, DefsInstruction, DetailsInstruction, DivInstruction, DlInstruction, DtInstruction, EmInstruction, FieldsetInstruction, FigureInstruction, FooterInstruction, FormInstruction, GInstruction, H1Instruction, H2Instruction, H3Instruction, H4Instruction, H5Instruction, H6Instruction, HeadInstruction, HeaderInstruction, HgroupInstruction, HrInstruction, HtmlInstruction, ImgInstruction, InputInstruction, KbdInstruction, LabelInstruction, LegendInstruction, LiInstruction, LinkInstruction, MainInstruction, MenuInstruction, MetaInstruction, NavInstruction, OlInstruction, OptgroupInstruction, OptionInstruction, PInstruction, PathInstruction, PreInstruction, ProgressInstruction, SampInstruction, ScriptInstruction, SectionInstruction, SelectInstruction, SmallInstruction, SpanInstruction, StrongInstruction, StyleInstruction, SubInstruction, SummaryInstruction, SupInstruction, SvgInstruction, TableInstruction, TbodyInstruction, TdInstruction, TemplateInstruction, TextareaInstruction, ThInstruction, TheadInstruction, TitleInstruction, TrInstruction, UlInstruction {
  INSTANCE;
}
