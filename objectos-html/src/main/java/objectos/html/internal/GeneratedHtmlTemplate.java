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

import objectos.html.tmpl.Instruction;
import objectos.html.tmpl.Instruction.AbbreviationInstruction;
import objectos.html.tmpl.Instruction.AlignmentBaselineAttribute;
import objectos.html.tmpl.Instruction.AnchorInstruction;
import objectos.html.tmpl.Instruction.ArticleInstruction;
import objectos.html.tmpl.Instruction.AutocompleteAttribute;
import objectos.html.tmpl.Instruction.BaselineShiftAttribute;
import objectos.html.tmpl.Instruction.BlockquoteInstruction;
import objectos.html.tmpl.Instruction.BodyInstruction;
import objectos.html.tmpl.Instruction.BringAttentionToInstruction;
import objectos.html.tmpl.Instruction.ButtonInstruction;
import objectos.html.tmpl.Instruction.ClipPathInstruction;
import objectos.html.tmpl.Instruction.ClipRuleAttribute;
import objectos.html.tmpl.Instruction.CodeInstruction;
import objectos.html.tmpl.Instruction.ColorAttribute;
import objectos.html.tmpl.Instruction.ColorInterpolationAttribute;
import objectos.html.tmpl.Instruction.ColorInterpolationFiltersAttribute;
import objectos.html.tmpl.Instruction.CrossoriginAttribute;
import objectos.html.tmpl.Instruction.CursorAttribute;
import objectos.html.tmpl.Instruction.DAttribute;
import objectos.html.tmpl.Instruction.DefinitionDescriptionInstruction;
import objectos.html.tmpl.Instruction.DefinitionListInstruction;
import objectos.html.tmpl.Instruction.DefinitionTermInstruction;
import objectos.html.tmpl.Instruction.DefsInstruction;
import objectos.html.tmpl.Instruction.DetailsInstruction;
import objectos.html.tmpl.Instruction.DirectionAttribute;
import objectos.html.tmpl.Instruction.DisabledAttribute;
import objectos.html.tmpl.Instruction.DisplayAttribute;
import objectos.html.tmpl.Instruction.DivInstruction;
import objectos.html.tmpl.Instruction.DominantBaselineAttribute;
import objectos.html.tmpl.Instruction.ElementContents;
import objectos.html.tmpl.Instruction.EmphasisInstruction;
import objectos.html.tmpl.Instruction.FieldsetInstruction;
import objectos.html.tmpl.Instruction.FigureInstruction;
import objectos.html.tmpl.Instruction.FillAttribute;
import objectos.html.tmpl.Instruction.FillOpacityAttribute;
import objectos.html.tmpl.Instruction.FillRuleAttribute;
import objectos.html.tmpl.Instruction.FilterAttribute;
import objectos.html.tmpl.Instruction.FloodColorAttribute;
import objectos.html.tmpl.Instruction.FloodOpacityAttribute;
import objectos.html.tmpl.Instruction.FontFamilyAttribute;
import objectos.html.tmpl.Instruction.FontSizeAdjustAttribute;
import objectos.html.tmpl.Instruction.FontSizeAttribute;
import objectos.html.tmpl.Instruction.FontStretchAttribute;
import objectos.html.tmpl.Instruction.FontStyleAttribute;
import objectos.html.tmpl.Instruction.FontVariantAttribute;
import objectos.html.tmpl.Instruction.FontWeightAttribute;
import objectos.html.tmpl.Instruction.FooterInstruction;
import objectos.html.tmpl.Instruction.FormAttribute;
import objectos.html.tmpl.Instruction.FormInstruction;
import objectos.html.tmpl.Instruction.GInstruction;
import objectos.html.tmpl.Instruction.GlobalAttribute;
import objectos.html.tmpl.Instruction.GlyphOrientationHorizontalAttribute;
import objectos.html.tmpl.Instruction.GlyphOrientationVerticalAttribute;
import objectos.html.tmpl.Instruction.HeadInstruction;
import objectos.html.tmpl.Instruction.HeaderInstruction;
import objectos.html.tmpl.Instruction.Heading1Instruction;
import objectos.html.tmpl.Instruction.Heading2Instruction;
import objectos.html.tmpl.Instruction.Heading3Instruction;
import objectos.html.tmpl.Instruction.Heading4Instruction;
import objectos.html.tmpl.Instruction.Heading5Instruction;
import objectos.html.tmpl.Instruction.Heading6Instruction;
import objectos.html.tmpl.Instruction.HeadingGroupInstruction;
import objectos.html.tmpl.Instruction.HeightAttribute;
import objectos.html.tmpl.Instruction.HorizontalRuleInstruction;
import objectos.html.tmpl.Instruction.HrefAttribute;
import objectos.html.tmpl.Instruction.HtmlInstruction;
import objectos.html.tmpl.Instruction.ImageInstruction;
import objectos.html.tmpl.Instruction.ImageRenderingAttribute;
import objectos.html.tmpl.Instruction.InputInstruction;
import objectos.html.tmpl.Instruction.KeyboardInputInstruction;
import objectos.html.tmpl.Instruction.LabelInstruction;
import objectos.html.tmpl.Instruction.LegendInstruction;
import objectos.html.tmpl.Instruction.LetterSpacingAttribute;
import objectos.html.tmpl.Instruction.LightingColorAttribute;
import objectos.html.tmpl.Instruction.LineBreakInstruction;
import objectos.html.tmpl.Instruction.LinkInstruction;
import objectos.html.tmpl.Instruction.ListItemInstruction;
import objectos.html.tmpl.Instruction.MainInstruction;
import objectos.html.tmpl.Instruction.MarkerEndAttribute;
import objectos.html.tmpl.Instruction.MarkerMidAttribute;
import objectos.html.tmpl.Instruction.MarkerStartAttribute;
import objectos.html.tmpl.Instruction.MaskAttribute;
import objectos.html.tmpl.Instruction.MaskTypeAttribute;
import objectos.html.tmpl.Instruction.MenuInstruction;
import objectos.html.tmpl.Instruction.MetaInstruction;
import objectos.html.tmpl.Instruction.NameAttribute;
import objectos.html.tmpl.Instruction.NavInstruction;
import objectos.html.tmpl.Instruction.OpacityAttribute;
import objectos.html.tmpl.Instruction.OptionGroupInstruction;
import objectos.html.tmpl.Instruction.OptionInstruction;
import objectos.html.tmpl.Instruction.OrderedListInstruction;
import objectos.html.tmpl.Instruction.OverflowAttribute;
import objectos.html.tmpl.Instruction.PaintOrderAttribute;
import objectos.html.tmpl.Instruction.ParagraphInstruction;
import objectos.html.tmpl.Instruction.PathInstruction;
import objectos.html.tmpl.Instruction.PlaceholderAttribute;
import objectos.html.tmpl.Instruction.PointerEventsAttribute;
import objectos.html.tmpl.Instruction.PreInstruction;
import objectos.html.tmpl.Instruction.ProgressInstruction;
import objectos.html.tmpl.Instruction.ReadonlyAttribute;
import objectos.html.tmpl.Instruction.ReferrerpolicyAttribute;
import objectos.html.tmpl.Instruction.RequiredAttribute;
import objectos.html.tmpl.Instruction.SampleOutputInstruction;
import objectos.html.tmpl.Instruction.ScriptInstruction;
import objectos.html.tmpl.Instruction.SectionInstruction;
import objectos.html.tmpl.Instruction.SelectInstruction;
import objectos.html.tmpl.Instruction.ShapeRenderingAttribute;
import objectos.html.tmpl.Instruction.SmallInstruction;
import objectos.html.tmpl.Instruction.SpanInstruction;
import objectos.html.tmpl.Instruction.SrcAttribute;
import objectos.html.tmpl.Instruction.StopColorAttribute;
import objectos.html.tmpl.Instruction.StopOpacityAttribute;
import objectos.html.tmpl.Instruction.StrokeAttribute;
import objectos.html.tmpl.Instruction.StrokeDasharrayAttribute;
import objectos.html.tmpl.Instruction.StrokeDashoffsetAttribute;
import objectos.html.tmpl.Instruction.StrokeLinecapAttribute;
import objectos.html.tmpl.Instruction.StrokeLinejoinAttribute;
import objectos.html.tmpl.Instruction.StrokeMiterlimitAttribute;
import objectos.html.tmpl.Instruction.StrokeOpacityAttribute;
import objectos.html.tmpl.Instruction.StrokeWidthAttribute;
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
import objectos.html.tmpl.Instruction.TargetAttribute;
import objectos.html.tmpl.Instruction.TemplateInstruction;
import objectos.html.tmpl.Instruction.TextAnchorAttribute;
import objectos.html.tmpl.Instruction.TextAreaInstruction;
import objectos.html.tmpl.Instruction.TextDecorationAttribute;
import objectos.html.tmpl.Instruction.TextOverflowAttribute;
import objectos.html.tmpl.Instruction.TextRenderingAttribute;
import objectos.html.tmpl.Instruction.TitleInstruction;
import objectos.html.tmpl.Instruction.TransformAttribute;
import objectos.html.tmpl.Instruction.TransformOriginAttribute;
import objectos.html.tmpl.Instruction.TypeAttribute;
import objectos.html.tmpl.Instruction.UnicodeBidiAttribute;
import objectos.html.tmpl.Instruction.UnorderedListInstruction;
import objectos.html.tmpl.Instruction.ValueAttribute;
import objectos.html.tmpl.Instruction.VectorEffectAttribute;
import objectos.html.tmpl.Instruction.VisibilityAttribute;
import objectos.html.tmpl.Instruction.WhiteSpaceAttribute;
import objectos.html.tmpl.Instruction.WidthAttribute;
import objectos.html.tmpl.Instruction.WordSpacingAttribute;
import objectos.html.tmpl.Instruction.WritingModeAttribute;
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardElementName;

abstract class GeneratedHtmlTemplate {
  public final ElementContents a(AnchorInstruction... contents) {
    element(StandardElementName.A, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents a(String text) {
    element(StandardElementName.A, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents abbr(AbbreviationInstruction... contents) {
    element(StandardElementName.ABBR, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents abbr(String text) {
    element(StandardElementName.ABBR, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents article(ArticleInstruction... contents) {
    element(StandardElementName.ARTICLE, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents article(String text) {
    element(StandardElementName.ARTICLE, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents b(BringAttentionToInstruction... contents) {
    element(StandardElementName.B, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents b(String text) {
    element(StandardElementName.B, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents blockquote(BlockquoteInstruction... contents) {
    element(StandardElementName.BLOCKQUOTE, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents blockquote(String text) {
    element(StandardElementName.BLOCKQUOTE, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents body(BodyInstruction... contents) {
    element(StandardElementName.BODY, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents body(String text) {
    element(StandardElementName.BODY, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents br(LineBreakInstruction... contents) {
    element(StandardElementName.BR, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents button(ButtonInstruction... contents) {
    element(StandardElementName.BUTTON, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents button(String text) {
    element(StandardElementName.BUTTON, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents clipPath(ClipPathInstruction... contents) {
    element(StandardElementName.CLIPPATH, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents code(CodeInstruction... contents) {
    element(StandardElementName.CODE, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents code(String text) {
    element(StandardElementName.CODE, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents dd(DefinitionDescriptionInstruction... contents) {
    element(StandardElementName.DD, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents dd(String text) {
    element(StandardElementName.DD, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents defs(DefsInstruction... contents) {
    element(StandardElementName.DEFS, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents defs(String text) {
    element(StandardElementName.DEFS, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents details(DetailsInstruction... contents) {
    element(StandardElementName.DETAILS, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents details(String text) {
    element(StandardElementName.DETAILS, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents div(DivInstruction... contents) {
    element(StandardElementName.DIV, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents div(String text) {
    element(StandardElementName.DIV, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents dl(DefinitionListInstruction... contents) {
    element(StandardElementName.DL, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents dl(String text) {
    element(StandardElementName.DL, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents dt(DefinitionTermInstruction... contents) {
    element(StandardElementName.DT, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents dt(String text) {
    element(StandardElementName.DT, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents em(EmphasisInstruction... contents) {
    element(StandardElementName.EM, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents em(String text) {
    element(StandardElementName.EM, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents fieldset(FieldsetInstruction... contents) {
    element(StandardElementName.FIELDSET, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents fieldset(String text) {
    element(StandardElementName.FIELDSET, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents figure(FigureInstruction... contents) {
    element(StandardElementName.FIGURE, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents figure(String text) {
    element(StandardElementName.FIGURE, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents footer(FooterInstruction... contents) {
    element(StandardElementName.FOOTER, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents footer(String text) {
    element(StandardElementName.FOOTER, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents form(FormInstruction... contents) {
    element(StandardElementName.FORM, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents g(GInstruction... contents) {
    element(StandardElementName.G, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents g(String text) {
    element(StandardElementName.G, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents h1(Heading1Instruction... contents) {
    element(StandardElementName.H1, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents h1(String text) {
    element(StandardElementName.H1, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents h2(Heading2Instruction... contents) {
    element(StandardElementName.H2, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents h2(String text) {
    element(StandardElementName.H2, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents h3(Heading3Instruction... contents) {
    element(StandardElementName.H3, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents h3(String text) {
    element(StandardElementName.H3, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents h4(Heading4Instruction... contents) {
    element(StandardElementName.H4, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents h4(String text) {
    element(StandardElementName.H4, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents h5(Heading5Instruction... contents) {
    element(StandardElementName.H5, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents h5(String text) {
    element(StandardElementName.H5, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents h6(Heading6Instruction... contents) {
    element(StandardElementName.H6, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents h6(String text) {
    element(StandardElementName.H6, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents head(HeadInstruction... contents) {
    element(StandardElementName.HEAD, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents head(String text) {
    element(StandardElementName.HEAD, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents header(HeaderInstruction... contents) {
    element(StandardElementName.HEADER, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents header(String text) {
    element(StandardElementName.HEADER, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents hgroup(HeadingGroupInstruction... contents) {
    element(StandardElementName.HGROUP, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents hgroup(String text) {
    element(StandardElementName.HGROUP, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents hr(HorizontalRuleInstruction... contents) {
    element(StandardElementName.HR, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents html(HtmlInstruction... contents) {
    element(StandardElementName.HTML, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents html(String text) {
    element(StandardElementName.HTML, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents img(ImageInstruction... contents) {
    element(StandardElementName.IMG, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents input(InputInstruction... contents) {
    element(StandardElementName.INPUT, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents kbd(KeyboardInputInstruction... contents) {
    element(StandardElementName.KBD, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents kbd(String text) {
    element(StandardElementName.KBD, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents label(LabelInstruction... contents) {
    element(StandardElementName.LABEL, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents legend(LegendInstruction... contents) {
    element(StandardElementName.LEGEND, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents legend(String text) {
    element(StandardElementName.LEGEND, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents li(ListItemInstruction... contents) {
    element(StandardElementName.LI, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents li(String text) {
    element(StandardElementName.LI, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents link(LinkInstruction... contents) {
    element(StandardElementName.LINK, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents main(MainInstruction... contents) {
    element(StandardElementName.MAIN, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents main(String text) {
    element(StandardElementName.MAIN, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents menu(MenuInstruction... contents) {
    element(StandardElementName.MENU, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents menu(String text) {
    element(StandardElementName.MENU, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents meta(MetaInstruction... contents) {
    element(StandardElementName.META, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents nav(NavInstruction... contents) {
    element(StandardElementName.NAV, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents nav(String text) {
    element(StandardElementName.NAV, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents ol(OrderedListInstruction... contents) {
    element(StandardElementName.OL, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents ol(String text) {
    element(StandardElementName.OL, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents optgroup(OptionGroupInstruction... contents) {
    element(StandardElementName.OPTGROUP, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents optgroup(String text) {
    element(StandardElementName.OPTGROUP, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents option(OptionInstruction... contents) {
    element(StandardElementName.OPTION, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents option(String text) {
    element(StandardElementName.OPTION, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents p(ParagraphInstruction... contents) {
    element(StandardElementName.P, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents p(String text) {
    element(StandardElementName.P, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents path(PathInstruction... contents) {
    element(StandardElementName.PATH, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents path(String text) {
    element(StandardElementName.PATH, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents pre(PreInstruction... contents) {
    element(StandardElementName.PRE, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents pre(String text) {
    element(StandardElementName.PRE, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents progress(ProgressInstruction... contents) {
    element(StandardElementName.PROGRESS, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents progress(String text) {
    element(StandardElementName.PROGRESS, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents samp(SampleOutputInstruction... contents) {
    element(StandardElementName.SAMP, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents samp(String text) {
    element(StandardElementName.SAMP, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents script(ScriptInstruction... contents) {
    element(StandardElementName.SCRIPT, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents script(String text) {
    element(StandardElementName.SCRIPT, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents section(SectionInstruction... contents) {
    element(StandardElementName.SECTION, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents section(String text) {
    element(StandardElementName.SECTION, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents select(SelectInstruction... contents) {
    element(StandardElementName.SELECT, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents select(String text) {
    element(StandardElementName.SELECT, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents small(SmallInstruction... contents) {
    element(StandardElementName.SMALL, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents small(String text) {
    element(StandardElementName.SMALL, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents span(SpanInstruction... contents) {
    element(StandardElementName.SPAN, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents span(String text) {
    element(StandardElementName.SPAN, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents strong(StrongInstruction... contents) {
    element(StandardElementName.STRONG, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents strong(String text) {
    element(StandardElementName.STRONG, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents style(StyleInstruction... contents) {
    element(StandardElementName.STYLE, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents style(String text) {
    element(StandardElementName.STYLE, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents sub(SubscriptInstruction... contents) {
    element(StandardElementName.SUB, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents sub(String text) {
    element(StandardElementName.SUB, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents summary(SummaryInstruction... contents) {
    element(StandardElementName.SUMMARY, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents summary(String text) {
    element(StandardElementName.SUMMARY, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents sup(SuperscriptInstruction... contents) {
    element(StandardElementName.SUP, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents sup(String text) {
    element(StandardElementName.SUP, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents svg(SvgInstruction... contents) {
    element(StandardElementName.SVG, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents svg(String text) {
    element(StandardElementName.SVG, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents table(TableInstruction... contents) {
    element(StandardElementName.TABLE, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents table(String text) {
    element(StandardElementName.TABLE, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents tbody(TableBodyInstruction... contents) {
    element(StandardElementName.TBODY, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents tbody(String text) {
    element(StandardElementName.TBODY, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents td(TableDataInstruction... contents) {
    element(StandardElementName.TD, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents td(String text) {
    element(StandardElementName.TD, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents template(TemplateInstruction... contents) {
    element(StandardElementName.TEMPLATE, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents template(String text) {
    element(StandardElementName.TEMPLATE, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents textarea(TextAreaInstruction... contents) {
    element(StandardElementName.TEXTAREA, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents textarea(String text) {
    element(StandardElementName.TEXTAREA, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents th(TableHeaderInstruction... contents) {
    element(StandardElementName.TH, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents th(String text) {
    element(StandardElementName.TH, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents thead(TableHeadInstruction... contents) {
    element(StandardElementName.THEAD, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents thead(String text) {
    element(StandardElementName.THEAD, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents title(TitleInstruction... contents) {
    element(StandardElementName.TITLE, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents tr(TableRowInstruction... contents) {
    element(StandardElementName.TR, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents tr(String text) {
    element(StandardElementName.TR, text);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents ul(UnorderedListInstruction... contents) {
    element(StandardElementName.UL, contents);
    return InternalInstruction.INSTANCE;
  }

  public final ElementContents ul(String text) {
    element(StandardElementName.UL, text);
    return InternalInstruction.INSTANCE;
  }

  public final GlobalAttribute accesskey(String value) {
    attribute(StandardAttributeName.ACCESSKEY, value);
    return InternalInstruction.INSTANCE;
  }

  public final FormInstruction action(String value) {
    attribute(StandardAttributeName.ACTION, value);
    return InternalInstruction.INSTANCE;
  }

  public final TableInstruction align(String value) {
    attribute(StandardAttributeName.ALIGN, value);
    return InternalInstruction.INSTANCE;
  }

  public final AlignmentBaselineAttribute alignmentBaseline(String value) {
    attribute(StandardAttributeName.ALIGNMENTBASELINE, value);
    return InternalInstruction.INSTANCE;
  }

  public final ImageInstruction alt(String value) {
    attribute(StandardAttributeName.ALT, value);
    return InternalInstruction.INSTANCE;
  }

  public final GlobalAttribute ariaHidden(String value) {
    attribute(StandardAttributeName.ARIAHIDDEN, value);
    return InternalInstruction.INSTANCE;
  }

  public final ScriptInstruction async() {
    attribute(StandardAttributeName.ASYNC);
    return InternalInstruction.INSTANCE;
  }

  public final AutocompleteAttribute autocomplete(String value) {
    attribute(StandardAttributeName.AUTOCOMPLETE, value);
    return InternalInstruction.INSTANCE;
  }

  public final InputInstruction autofocus() {
    attribute(StandardAttributeName.AUTOFOCUS);
    return InternalInstruction.INSTANCE;
  }

  public final BaselineShiftAttribute baselineShift(String value) {
    attribute(StandardAttributeName.BASELINESHIFT, value);
    return InternalInstruction.INSTANCE;
  }

  public final TableInstruction border(String value) {
    attribute(StandardAttributeName.BORDER, value);
    return InternalInstruction.INSTANCE;
  }

  public final TableInstruction cellpadding(String value) {
    attribute(StandardAttributeName.CELLPADDING, value);
    return InternalInstruction.INSTANCE;
  }

  public final TableInstruction cellspacing(String value) {
    attribute(StandardAttributeName.CELLSPACING, value);
    return InternalInstruction.INSTANCE;
  }

  public final MetaInstruction charset(String value) {
    attribute(StandardAttributeName.CHARSET, value);
    return InternalInstruction.INSTANCE;
  }

  public final BlockquoteInstruction cite(String value) {
    attribute(StandardAttributeName.CITE, value);
    return InternalInstruction.INSTANCE;
  }

  public final GlobalAttribute className(String value) {
    attribute(StandardAttributeName.CLASS, value);
    return InternalInstruction.INSTANCE;
  }

  public final ClipRuleAttribute clipRule(String value) {
    attribute(StandardAttributeName.CLIPRULE, value);
    return InternalInstruction.INSTANCE;
  }

  public final ColorAttribute color(String value) {
    attribute(StandardAttributeName.COLOR, value);
    return InternalInstruction.INSTANCE;
  }

  public final ColorInterpolationAttribute colorInterpolation(String value) {
    attribute(StandardAttributeName.COLORINTERPOLATION, value);
    return InternalInstruction.INSTANCE;
  }

  public final ColorInterpolationFiltersAttribute colorInterpolationFilters(String value) {
    attribute(StandardAttributeName.COLORINTERPOLATIONFILTERS, value);
    return InternalInstruction.INSTANCE;
  }

  public final TextAreaInstruction cols(String value) {
    attribute(StandardAttributeName.COLS, value);
    return InternalInstruction.INSTANCE;
  }

  public final MetaInstruction content(String value) {
    attribute(StandardAttributeName.CONTENT, value);
    return InternalInstruction.INSTANCE;
  }

  public final GlobalAttribute contenteditable(String value) {
    attribute(StandardAttributeName.CONTENTEDITABLE, value);
    return InternalInstruction.INSTANCE;
  }

  public final CrossoriginAttribute crossorigin(String value) {
    attribute(StandardAttributeName.CROSSORIGIN, value);
    return InternalInstruction.INSTANCE;
  }

  public final CursorAttribute cursor(String value) {
    attribute(StandardAttributeName.CURSOR, value);
    return InternalInstruction.INSTANCE;
  }

  public final DAttribute d(String value) {
    attribute(StandardAttributeName.D, value);
    return InternalInstruction.INSTANCE;
  }

  public final ScriptInstruction defer() {
    attribute(StandardAttributeName.DEFER);
    return InternalInstruction.INSTANCE;
  }

  public final GlobalAttribute dir(String value) {
    attribute(StandardAttributeName.DIR, value);
    return InternalInstruction.INSTANCE;
  }

  public final DirectionAttribute direction(String value) {
    attribute(StandardAttributeName.DIRECTION, value);
    return InternalInstruction.INSTANCE;
  }

  public final TextAreaInstruction dirname(String value) {
    attribute(StandardAttributeName.DIRNAME, value);
    return InternalInstruction.INSTANCE;
  }

  public final DisabledAttribute disabled() {
    attribute(StandardAttributeName.DISABLED);
    return InternalInstruction.INSTANCE;
  }

  public final DisplayAttribute display(String value) {
    attribute(StandardAttributeName.DISPLAY, value);
    return InternalInstruction.INSTANCE;
  }

  public final DominantBaselineAttribute dominantBaseline(String value) {
    attribute(StandardAttributeName.DOMINANTBASELINE, value);
    return InternalInstruction.INSTANCE;
  }

  public final GlobalAttribute draggable(String value) {
    attribute(StandardAttributeName.DRAGGABLE, value);
    return InternalInstruction.INSTANCE;
  }

  public final FormInstruction enctype(String value) {
    attribute(StandardAttributeName.ENCTYPE, value);
    return InternalInstruction.INSTANCE;
  }

  public final FillAttribute fill(String value) {
    attribute(StandardAttributeName.FILL, value);
    return InternalInstruction.INSTANCE;
  }

  public final FillOpacityAttribute fillOpacity(String value) {
    attribute(StandardAttributeName.FILLOPACITY, value);
    return InternalInstruction.INSTANCE;
  }

  public final FillRuleAttribute fillRule(String value) {
    attribute(StandardAttributeName.FILLRULE, value);
    return InternalInstruction.INSTANCE;
  }

  public final FilterAttribute filter(String value) {
    attribute(StandardAttributeName.FILTER, value);
    return InternalInstruction.INSTANCE;
  }

  public final FloodColorAttribute floodColor(String value) {
    attribute(StandardAttributeName.FLOODCOLOR, value);
    return InternalInstruction.INSTANCE;
  }

  public final FloodOpacityAttribute floodOpacity(String value) {
    attribute(StandardAttributeName.FLOODOPACITY, value);
    return InternalInstruction.INSTANCE;
  }

  public final FontFamilyAttribute fontFamily(String value) {
    attribute(StandardAttributeName.FONTFAMILY, value);
    return InternalInstruction.INSTANCE;
  }

  public final FontSizeAttribute fontSize(String value) {
    attribute(StandardAttributeName.FONTSIZE, value);
    return InternalInstruction.INSTANCE;
  }

  public final FontSizeAdjustAttribute fontSizeAdjust(String value) {
    attribute(StandardAttributeName.FONTSIZEADJUST, value);
    return InternalInstruction.INSTANCE;
  }

  public final FontStretchAttribute fontStretch(String value) {
    attribute(StandardAttributeName.FONTSTRETCH, value);
    return InternalInstruction.INSTANCE;
  }

  public final FontStyleAttribute fontStyle(String value) {
    attribute(StandardAttributeName.FONTSTYLE, value);
    return InternalInstruction.INSTANCE;
  }

  public final FontVariantAttribute fontVariant(String value) {
    attribute(StandardAttributeName.FONTVARIANT, value);
    return InternalInstruction.INSTANCE;
  }

  public final FontWeightAttribute fontWeight(String value) {
    attribute(StandardAttributeName.FONTWEIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  public final LabelInstruction forAttr(String value) {
    attribute(StandardAttributeName.FOR, value);
    return InternalInstruction.INSTANCE;
  }

  public final LabelInstruction forElement(String value) {
    attribute(StandardAttributeName.FOR, value);
    return InternalInstruction.INSTANCE;
  }

  public final FormAttribute form(String value) {
    attribute(StandardAttributeName.FORM, value);
    return InternalInstruction.INSTANCE;
  }

  public final GlyphOrientationHorizontalAttribute glyphOrientationHorizontal(String value) {
    attribute(StandardAttributeName.GLYPHORIENTATIONHORIZONTAL, value);
    return InternalInstruction.INSTANCE;
  }

  public final GlyphOrientationVerticalAttribute glyphOrientationVertical(String value) {
    attribute(StandardAttributeName.GLYPHORIENTATIONVERTICAL, value);
    return InternalInstruction.INSTANCE;
  }

  public final HeightAttribute height(String value) {
    attribute(StandardAttributeName.HEIGHT, value);
    return InternalInstruction.INSTANCE;
  }

  public final GlobalAttribute hidden() {
    attribute(StandardAttributeName.HIDDEN);
    return InternalInstruction.INSTANCE;
  }

  public final HrefAttribute href(String value) {
    attribute(StandardAttributeName.HREF, value);
    return InternalInstruction.INSTANCE;
  }

  public final MetaInstruction httpEquiv(String value) {
    attribute(StandardAttributeName.HTTPEQUIV, value);
    return InternalInstruction.INSTANCE;
  }

  public final GlobalAttribute id(String value) {
    attribute(StandardAttributeName.ID, value);
    return InternalInstruction.INSTANCE;
  }

  public final ImageRenderingAttribute imageRendering(String value) {
    attribute(StandardAttributeName.IMAGERENDERING, value);
    return InternalInstruction.INSTANCE;
  }

  public final ScriptInstruction integrity(String value) {
    attribute(StandardAttributeName.INTEGRITY, value);
    return InternalInstruction.INSTANCE;
  }

  public final GlobalAttribute lang(String value) {
    attribute(StandardAttributeName.LANG, value);
    return InternalInstruction.INSTANCE;
  }

  public final LetterSpacingAttribute letterSpacing(String value) {
    attribute(StandardAttributeName.LETTERSPACING, value);
    return InternalInstruction.INSTANCE;
  }

  public final LightingColorAttribute lightingColor(String value) {
    attribute(StandardAttributeName.LIGHTINGCOLOR, value);
    return InternalInstruction.INSTANCE;
  }

  public final MarkerEndAttribute markerEnd(String value) {
    attribute(StandardAttributeName.MARKEREND, value);
    return InternalInstruction.INSTANCE;
  }

  public final MarkerMidAttribute markerMid(String value) {
    attribute(StandardAttributeName.MARKERMID, value);
    return InternalInstruction.INSTANCE;
  }

  public final MarkerStartAttribute markerStart(String value) {
    attribute(StandardAttributeName.MARKERSTART, value);
    return InternalInstruction.INSTANCE;
  }

  public final MaskAttribute mask(String value) {
    attribute(StandardAttributeName.MASK, value);
    return InternalInstruction.INSTANCE;
  }

  public final MaskTypeAttribute maskType(String value) {
    attribute(StandardAttributeName.MASKTYPE, value);
    return InternalInstruction.INSTANCE;
  }

  public final TextAreaInstruction maxlength(String value) {
    attribute(StandardAttributeName.MAXLENGTH, value);
    return InternalInstruction.INSTANCE;
  }

  public final LinkInstruction media(String value) {
    attribute(StandardAttributeName.MEDIA, value);
    return InternalInstruction.INSTANCE;
  }

  public final FormInstruction method(String value) {
    attribute(StandardAttributeName.METHOD, value);
    return InternalInstruction.INSTANCE;
  }

  public final TextAreaInstruction minlength(String value) {
    attribute(StandardAttributeName.MINLENGTH, value);
    return InternalInstruction.INSTANCE;
  }

  public final SelectInstruction multiple() {
    attribute(StandardAttributeName.MULTIPLE);
    return InternalInstruction.INSTANCE;
  }

  public final NameAttribute name(String value) {
    attribute(StandardAttributeName.NAME, value);
    return InternalInstruction.INSTANCE;
  }

  public final ScriptInstruction nomodule() {
    attribute(StandardAttributeName.NOMODULE);
    return InternalInstruction.INSTANCE;
  }

  public final BodyInstruction onafterprint(String value) {
    attribute(StandardAttributeName.ONAFTERPRINT, value);
    return InternalInstruction.INSTANCE;
  }

  public final BodyInstruction onbeforeprint(String value) {
    attribute(StandardAttributeName.ONBEFOREPRINT, value);
    return InternalInstruction.INSTANCE;
  }

  public final BodyInstruction onbeforeunload(String value) {
    attribute(StandardAttributeName.ONBEFOREUNLOAD, value);
    return InternalInstruction.INSTANCE;
  }

  public final GlobalAttribute onclick(String value) {
    attribute(StandardAttributeName.ONCLICK, value);
    return InternalInstruction.INSTANCE;
  }

  public final BodyInstruction onhashchange(String value) {
    attribute(StandardAttributeName.ONHASHCHANGE, value);
    return InternalInstruction.INSTANCE;
  }

  public final BodyInstruction onlanguagechange(String value) {
    attribute(StandardAttributeName.ONLANGUAGECHANGE, value);
    return InternalInstruction.INSTANCE;
  }

  public final BodyInstruction onmessage(String value) {
    attribute(StandardAttributeName.ONMESSAGE, value);
    return InternalInstruction.INSTANCE;
  }

  public final BodyInstruction onoffline(String value) {
    attribute(StandardAttributeName.ONOFFLINE, value);
    return InternalInstruction.INSTANCE;
  }

  public final BodyInstruction ononline(String value) {
    attribute(StandardAttributeName.ONONLINE, value);
    return InternalInstruction.INSTANCE;
  }

  public final BodyInstruction onpagehide(String value) {
    attribute(StandardAttributeName.ONPAGEHIDE, value);
    return InternalInstruction.INSTANCE;
  }

  public final BodyInstruction onpageshow(String value) {
    attribute(StandardAttributeName.ONPAGESHOW, value);
    return InternalInstruction.INSTANCE;
  }

  public final BodyInstruction onpopstate(String value) {
    attribute(StandardAttributeName.ONPOPSTATE, value);
    return InternalInstruction.INSTANCE;
  }

  public final BodyInstruction onrejectionhandled(String value) {
    attribute(StandardAttributeName.ONREJECTIONHANDLED, value);
    return InternalInstruction.INSTANCE;
  }

  public final BodyInstruction onstorage(String value) {
    attribute(StandardAttributeName.ONSTORAGE, value);
    return InternalInstruction.INSTANCE;
  }

  public final GlobalAttribute onsubmit(String value) {
    attribute(StandardAttributeName.ONSUBMIT, value);
    return InternalInstruction.INSTANCE;
  }

  public final BodyInstruction onunhandledrejection(String value) {
    attribute(StandardAttributeName.ONUNHANDLEDREJECTION, value);
    return InternalInstruction.INSTANCE;
  }

  public final BodyInstruction onunload(String value) {
    attribute(StandardAttributeName.ONUNLOAD, value);
    return InternalInstruction.INSTANCE;
  }

  public final OpacityAttribute opacity(String value) {
    attribute(StandardAttributeName.OPACITY, value);
    return InternalInstruction.INSTANCE;
  }

  public final DetailsInstruction open() {
    attribute(StandardAttributeName.OPEN);
    return InternalInstruction.INSTANCE;
  }

  public final OverflowAttribute overflow(String value) {
    attribute(StandardAttributeName.OVERFLOW, value);
    return InternalInstruction.INSTANCE;
  }

  public final PaintOrderAttribute paintOrder(String value) {
    attribute(StandardAttributeName.PAINTORDER, value);
    return InternalInstruction.INSTANCE;
  }

  public final PlaceholderAttribute placeholder(String value) {
    attribute(StandardAttributeName.PLACEHOLDER, value);
    return InternalInstruction.INSTANCE;
  }

  public final PointerEventsAttribute pointerEvents(String value) {
    attribute(StandardAttributeName.POINTEREVENTS, value);
    return InternalInstruction.INSTANCE;
  }

  public final MetaInstruction property(String value) {
    attribute(StandardAttributeName.PROPERTY, value);
    return InternalInstruction.INSTANCE;
  }

  public final ReadonlyAttribute readonly() {
    attribute(StandardAttributeName.READONLY);
    return InternalInstruction.INSTANCE;
  }

  public final ReferrerpolicyAttribute referrerpolicy(String value) {
    attribute(StandardAttributeName.REFERRERPOLICY, value);
    return InternalInstruction.INSTANCE;
  }

  public final LinkInstruction rel(String value) {
    attribute(StandardAttributeName.REL, value);
    return InternalInstruction.INSTANCE;
  }

  public final RequiredAttribute required() {
    attribute(StandardAttributeName.REQUIRED);
    return InternalInstruction.INSTANCE;
  }

  public final LinkInstruction rev(String value) {
    attribute(StandardAttributeName.REV, value);
    return InternalInstruction.INSTANCE;
  }

  public final OrderedListInstruction reversed() {
    attribute(StandardAttributeName.REVERSED);
    return InternalInstruction.INSTANCE;
  }

  public final GlobalAttribute role(String value) {
    attribute(StandardAttributeName.ROLE, value);
    return InternalInstruction.INSTANCE;
  }

  public final TextAreaInstruction rows(String value) {
    attribute(StandardAttributeName.ROWS, value);
    return InternalInstruction.INSTANCE;
  }

  public final OptionInstruction selected() {
    attribute(StandardAttributeName.SELECTED);
    return InternalInstruction.INSTANCE;
  }

  public final ShapeRenderingAttribute shapeRendering(String value) {
    attribute(StandardAttributeName.SHAPERENDERING, value);
    return InternalInstruction.INSTANCE;
  }

  public final SelectInstruction size(String value) {
    attribute(StandardAttributeName.SIZE, value);
    return InternalInstruction.INSTANCE;
  }

  public final LinkInstruction sizes(String value) {
    attribute(StandardAttributeName.SIZES, value);
    return InternalInstruction.INSTANCE;
  }

  public final GlobalAttribute spellcheck(String value) {
    attribute(StandardAttributeName.SPELLCHECK, value);
    return InternalInstruction.INSTANCE;
  }

  public final SrcAttribute src(String value) {
    attribute(StandardAttributeName.SRC, value);
    return InternalInstruction.INSTANCE;
  }

  public final ImageInstruction srcset(String value) {
    attribute(StandardAttributeName.SRCSET, value);
    return InternalInstruction.INSTANCE;
  }

  public final OrderedListInstruction start(String value) {
    attribute(StandardAttributeName.START, value);
    return InternalInstruction.INSTANCE;
  }

  public final StopColorAttribute stopColor(String value) {
    attribute(StandardAttributeName.STOPCOLOR, value);
    return InternalInstruction.INSTANCE;
  }

  public final StopOpacityAttribute stopOpacity(String value) {
    attribute(StandardAttributeName.STOPOPACITY, value);
    return InternalInstruction.INSTANCE;
  }

  public final StrokeAttribute stroke(String value) {
    attribute(StandardAttributeName.STROKE, value);
    return InternalInstruction.INSTANCE;
  }

  public final StrokeDasharrayAttribute strokeDasharray(String value) {
    attribute(StandardAttributeName.STROKEDASHARRAY, value);
    return InternalInstruction.INSTANCE;
  }

  public final StrokeDashoffsetAttribute strokeDashoffset(String value) {
    attribute(StandardAttributeName.STROKEDASHOFFSET, value);
    return InternalInstruction.INSTANCE;
  }

  public final StrokeLinecapAttribute strokeLinecap(String value) {
    attribute(StandardAttributeName.STROKELINECAP, value);
    return InternalInstruction.INSTANCE;
  }

  public final StrokeLinejoinAttribute strokeLinejoin(String value) {
    attribute(StandardAttributeName.STROKELINEJOIN, value);
    return InternalInstruction.INSTANCE;
  }

  public final StrokeMiterlimitAttribute strokeMiterlimit(String value) {
    attribute(StandardAttributeName.STROKEMITERLIMIT, value);
    return InternalInstruction.INSTANCE;
  }

  public final StrokeOpacityAttribute strokeOpacity(String value) {
    attribute(StandardAttributeName.STROKEOPACITY, value);
    return InternalInstruction.INSTANCE;
  }

  public final StrokeWidthAttribute strokeWidth(String value) {
    attribute(StandardAttributeName.STROKEWIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  public final GlobalAttribute inlineStyle(String value) {
    attribute(StandardAttributeName.STYLE, value);
    return InternalInstruction.INSTANCE;
  }

  public final GlobalAttribute tabindex(String value) {
    attribute(StandardAttributeName.TABINDEX, value);
    return InternalInstruction.INSTANCE;
  }

  public final TargetAttribute target(String value) {
    attribute(StandardAttributeName.TARGET, value);
    return InternalInstruction.INSTANCE;
  }

  public final TextAnchorAttribute textAnchor(String value) {
    attribute(StandardAttributeName.TEXTANCHOR, value);
    return InternalInstruction.INSTANCE;
  }

  public final TextDecorationAttribute textDecoration(String value) {
    attribute(StandardAttributeName.TEXTDECORATION, value);
    return InternalInstruction.INSTANCE;
  }

  public final TextOverflowAttribute textOverflow(String value) {
    attribute(StandardAttributeName.TEXTOVERFLOW, value);
    return InternalInstruction.INSTANCE;
  }

  public final TextRenderingAttribute textRendering(String value) {
    attribute(StandardAttributeName.TEXTRENDERING, value);
    return InternalInstruction.INSTANCE;
  }

  public final TransformAttribute transform(String value) {
    attribute(StandardAttributeName.TRANSFORM, value);
    return InternalInstruction.INSTANCE;
  }

  public final TransformOriginAttribute transformOrigin(String value) {
    attribute(StandardAttributeName.TRANSFORMORIGIN, value);
    return InternalInstruction.INSTANCE;
  }

  public final GlobalAttribute translate(String value) {
    attribute(StandardAttributeName.TRANSLATE, value);
    return InternalInstruction.INSTANCE;
  }

  public final TypeAttribute type(String value) {
    attribute(StandardAttributeName.TYPE, value);
    return InternalInstruction.INSTANCE;
  }

  public final UnicodeBidiAttribute unicodeBidi(String value) {
    attribute(StandardAttributeName.UNICODEBIDI, value);
    return InternalInstruction.INSTANCE;
  }

  public final ValueAttribute value(String value) {
    attribute(StandardAttributeName.VALUE, value);
    return InternalInstruction.INSTANCE;
  }

  public final VectorEffectAttribute vectorEffect(String value) {
    attribute(StandardAttributeName.VECTOREFFECT, value);
    return InternalInstruction.INSTANCE;
  }

  public final SvgInstruction viewBox(String value) {
    attribute(StandardAttributeName.VIEWBOX, value);
    return InternalInstruction.INSTANCE;
  }

  public final VisibilityAttribute visibility(String value) {
    attribute(StandardAttributeName.VISIBILITY, value);
    return InternalInstruction.INSTANCE;
  }

  public final WhiteSpaceAttribute whiteSpace(String value) {
    attribute(StandardAttributeName.WHITESPACE, value);
    return InternalInstruction.INSTANCE;
  }

  public final WidthAttribute width(String value) {
    attribute(StandardAttributeName.WIDTH, value);
    return InternalInstruction.INSTANCE;
  }

  public final WordSpacingAttribute wordSpacing(String value) {
    attribute(StandardAttributeName.WORDSPACING, value);
    return InternalInstruction.INSTANCE;
  }

  public final TextAreaInstruction wrap(String value) {
    attribute(StandardAttributeName.WRAP, value);
    return InternalInstruction.INSTANCE;
  }

  public final WritingModeAttribute writingMode(String value) {
    attribute(StandardAttributeName.WRITINGMODE, value);
    return InternalInstruction.INSTANCE;
  }

  public final SvgInstruction xmlns(String value) {
    attribute(StandardAttributeName.XMLNS, value);
    return InternalInstruction.INSTANCE;
  }

  abstract void attribute(StandardAttributeName name);

  abstract void attribute(StandardAttributeName name, String value);

  abstract void element(StandardElementName name, String text);

  abstract void element(StandardElementName name, Instruction[] contents);
}
