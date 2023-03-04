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
package objectos.html;

import objectos.html.tmpl.AValue;
import objectos.html.tmpl.AbbrValue;
import objectos.html.tmpl.ArticleValue;
import objectos.html.tmpl.BValue;
import objectos.html.tmpl.BlockquoteValue;
import objectos.html.tmpl.BodyValue;
import objectos.html.tmpl.BrValue;
import objectos.html.tmpl.ButtonValue;
import objectos.html.tmpl.ClipPathValue;
import objectos.html.tmpl.CodeValue;
import objectos.html.tmpl.DdValue;
import objectos.html.tmpl.DefsValue;
import objectos.html.tmpl.DetailsValue;
import objectos.html.tmpl.DivValue;
import objectos.html.tmpl.DlValue;
import objectos.html.tmpl.DtValue;
import objectos.html.tmpl.ElementName;
import objectos.html.tmpl.EmValue;
import objectos.html.tmpl.FieldsetValue;
import objectos.html.tmpl.FigureValue;
import objectos.html.tmpl.FooterValue;
import objectos.html.tmpl.FormValue;
import objectos.html.tmpl.GValue;
import objectos.html.tmpl.H1Value;
import objectos.html.tmpl.H2Value;
import objectos.html.tmpl.H3Value;
import objectos.html.tmpl.H4Value;
import objectos.html.tmpl.H5Value;
import objectos.html.tmpl.H6Value;
import objectos.html.tmpl.HeadValue;
import objectos.html.tmpl.HeaderValue;
import objectos.html.tmpl.HgroupValue;
import objectos.html.tmpl.HrValue;
import objectos.html.tmpl.HtmlValue;
import objectos.html.tmpl.ImgValue;
import objectos.html.tmpl.InputValue;
import objectos.html.tmpl.KbdValue;
import objectos.html.tmpl.LabelValue;
import objectos.html.tmpl.LegendValue;
import objectos.html.tmpl.LiValue;
import objectos.html.tmpl.LinkValue;
import objectos.html.tmpl.MainValue;
import objectos.html.tmpl.MenuValue;
import objectos.html.tmpl.MetaValue;
import objectos.html.tmpl.NavValue;
import objectos.html.tmpl.OlValue;
import objectos.html.tmpl.OptgroupValue;
import objectos.html.tmpl.OptionValue;
import objectos.html.tmpl.PValue;
import objectos.html.tmpl.PathValue;
import objectos.html.tmpl.PreValue;
import objectos.html.tmpl.ProgressValue;
import objectos.html.tmpl.SampValue;
import objectos.html.tmpl.ScriptValue;
import objectos.html.tmpl.SectionValue;
import objectos.html.tmpl.SelectValue;
import objectos.html.tmpl.SmallValue;
import objectos.html.tmpl.SpanValue;
import objectos.html.tmpl.StandardAttributeName;
import objectos.html.tmpl.StandardAttributeName.Accesskey;
import objectos.html.tmpl.StandardAttributeName.Action;
import objectos.html.tmpl.StandardAttributeName.Align;
import objectos.html.tmpl.StandardAttributeName.AlignmentBaseline;
import objectos.html.tmpl.StandardAttributeName.Alt;
import objectos.html.tmpl.StandardAttributeName.AriaHidden;
import objectos.html.tmpl.StandardAttributeName.Async;
import objectos.html.tmpl.StandardAttributeName.Autocomplete;
import objectos.html.tmpl.StandardAttributeName.Autofocus;
import objectos.html.tmpl.StandardAttributeName.BaselineShift;
import objectos.html.tmpl.StandardAttributeName.Border;
import objectos.html.tmpl.StandardAttributeName.Cellpadding;
import objectos.html.tmpl.StandardAttributeName.Cellspacing;
import objectos.html.tmpl.StandardAttributeName.Charset;
import objectos.html.tmpl.StandardAttributeName.Cite;
import objectos.html.tmpl.StandardAttributeName.Class;
import objectos.html.tmpl.StandardAttributeName.ClipRule;
import objectos.html.tmpl.StandardAttributeName.Color;
import objectos.html.tmpl.StandardAttributeName.ColorInterpolation;
import objectos.html.tmpl.StandardAttributeName.ColorInterpolationFilters;
import objectos.html.tmpl.StandardAttributeName.Cols;
import objectos.html.tmpl.StandardAttributeName.Content;
import objectos.html.tmpl.StandardAttributeName.Contenteditable;
import objectos.html.tmpl.StandardAttributeName.Crossorigin;
import objectos.html.tmpl.StandardAttributeName.Cursor;
import objectos.html.tmpl.StandardAttributeName.D;
import objectos.html.tmpl.StandardAttributeName.Defer;
import objectos.html.tmpl.StandardAttributeName.Dir;
import objectos.html.tmpl.StandardAttributeName.Direction;
import objectos.html.tmpl.StandardAttributeName.Dirname;
import objectos.html.tmpl.StandardAttributeName.Disabled;
import objectos.html.tmpl.StandardAttributeName.Display;
import objectos.html.tmpl.StandardAttributeName.DominantBaseline;
import objectos.html.tmpl.StandardAttributeName.Draggable;
import objectos.html.tmpl.StandardAttributeName.Enctype;
import objectos.html.tmpl.StandardAttributeName.Fill;
import objectos.html.tmpl.StandardAttributeName.FillOpacity;
import objectos.html.tmpl.StandardAttributeName.FillRule;
import objectos.html.tmpl.StandardAttributeName.Filter;
import objectos.html.tmpl.StandardAttributeName.FloodColor;
import objectos.html.tmpl.StandardAttributeName.FloodOpacity;
import objectos.html.tmpl.StandardAttributeName.FontFamily;
import objectos.html.tmpl.StandardAttributeName.FontSize;
import objectos.html.tmpl.StandardAttributeName.FontSizeAdjust;
import objectos.html.tmpl.StandardAttributeName.FontStretch;
import objectos.html.tmpl.StandardAttributeName.FontStyle;
import objectos.html.tmpl.StandardAttributeName.FontVariant;
import objectos.html.tmpl.StandardAttributeName.FontWeight;
import objectos.html.tmpl.StandardAttributeName.For;
import objectos.html.tmpl.StandardAttributeName.Form;
import objectos.html.tmpl.StandardAttributeName.GlyphOrientationHorizontal;
import objectos.html.tmpl.StandardAttributeName.GlyphOrientationVertical;
import objectos.html.tmpl.StandardAttributeName.Height;
import objectos.html.tmpl.StandardAttributeName.Hidden;
import objectos.html.tmpl.StandardAttributeName.Href;
import objectos.html.tmpl.StandardAttributeName.HttpEquiv;
import objectos.html.tmpl.StandardAttributeName.Id;
import objectos.html.tmpl.StandardAttributeName.ImageRendering;
import objectos.html.tmpl.StandardAttributeName.Integrity;
import objectos.html.tmpl.StandardAttributeName.Lang;
import objectos.html.tmpl.StandardAttributeName.LetterSpacing;
import objectos.html.tmpl.StandardAttributeName.LightingColor;
import objectos.html.tmpl.StandardAttributeName.MarkerEnd;
import objectos.html.tmpl.StandardAttributeName.MarkerMid;
import objectos.html.tmpl.StandardAttributeName.MarkerStart;
import objectos.html.tmpl.StandardAttributeName.Mask;
import objectos.html.tmpl.StandardAttributeName.MaskType;
import objectos.html.tmpl.StandardAttributeName.Maxlength;
import objectos.html.tmpl.StandardAttributeName.Media;
import objectos.html.tmpl.StandardAttributeName.Method;
import objectos.html.tmpl.StandardAttributeName.Minlength;
import objectos.html.tmpl.StandardAttributeName.Multiple;
import objectos.html.tmpl.StandardAttributeName.Name;
import objectos.html.tmpl.StandardAttributeName.Nomodule;
import objectos.html.tmpl.StandardAttributeName.Onafterprint;
import objectos.html.tmpl.StandardAttributeName.Onbeforeprint;
import objectos.html.tmpl.StandardAttributeName.Onbeforeunload;
import objectos.html.tmpl.StandardAttributeName.Onclick;
import objectos.html.tmpl.StandardAttributeName.Onhashchange;
import objectos.html.tmpl.StandardAttributeName.Onlanguagechange;
import objectos.html.tmpl.StandardAttributeName.Onmessage;
import objectos.html.tmpl.StandardAttributeName.Onoffline;
import objectos.html.tmpl.StandardAttributeName.Ononline;
import objectos.html.tmpl.StandardAttributeName.Onpagehide;
import objectos.html.tmpl.StandardAttributeName.Onpageshow;
import objectos.html.tmpl.StandardAttributeName.Onpopstate;
import objectos.html.tmpl.StandardAttributeName.Onrejectionhandled;
import objectos.html.tmpl.StandardAttributeName.Onstorage;
import objectos.html.tmpl.StandardAttributeName.Onsubmit;
import objectos.html.tmpl.StandardAttributeName.Onunhandledrejection;
import objectos.html.tmpl.StandardAttributeName.Onunload;
import objectos.html.tmpl.StandardAttributeName.Opacity;
import objectos.html.tmpl.StandardAttributeName.Open;
import objectos.html.tmpl.StandardAttributeName.Overflow;
import objectos.html.tmpl.StandardAttributeName.PaintOrder;
import objectos.html.tmpl.StandardAttributeName.Placeholder;
import objectos.html.tmpl.StandardAttributeName.PointerEvents;
import objectos.html.tmpl.StandardAttributeName.Property;
import objectos.html.tmpl.StandardAttributeName.Readonly;
import objectos.html.tmpl.StandardAttributeName.Referrerpolicy;
import objectos.html.tmpl.StandardAttributeName.Rel;
import objectos.html.tmpl.StandardAttributeName.Required;
import objectos.html.tmpl.StandardAttributeName.Rev;
import objectos.html.tmpl.StandardAttributeName.Reversed;
import objectos.html.tmpl.StandardAttributeName.Role;
import objectos.html.tmpl.StandardAttributeName.Rows;
import objectos.html.tmpl.StandardAttributeName.Selected;
import objectos.html.tmpl.StandardAttributeName.ShapeRendering;
import objectos.html.tmpl.StandardAttributeName.Size;
import objectos.html.tmpl.StandardAttributeName.Sizes;
import objectos.html.tmpl.StandardAttributeName.Spellcheck;
import objectos.html.tmpl.StandardAttributeName.Src;
import objectos.html.tmpl.StandardAttributeName.Srcset;
import objectos.html.tmpl.StandardAttributeName.Start;
import objectos.html.tmpl.StandardAttributeName.StopColor;
import objectos.html.tmpl.StandardAttributeName.StopOpacity;
import objectos.html.tmpl.StandardAttributeName.Stroke;
import objectos.html.tmpl.StandardAttributeName.StrokeDasharray;
import objectos.html.tmpl.StandardAttributeName.StrokeDashoffset;
import objectos.html.tmpl.StandardAttributeName.StrokeLinecap;
import objectos.html.tmpl.StandardAttributeName.StrokeLinejoin;
import objectos.html.tmpl.StandardAttributeName.StrokeMiterlimit;
import objectos.html.tmpl.StandardAttributeName.StrokeOpacity;
import objectos.html.tmpl.StandardAttributeName.StrokeWidth;
import objectos.html.tmpl.StandardAttributeName.Style;
import objectos.html.tmpl.StandardAttributeName.Tabindex;
import objectos.html.tmpl.StandardAttributeName.Target;
import objectos.html.tmpl.StandardAttributeName.TextAnchor;
import objectos.html.tmpl.StandardAttributeName.TextDecoration;
import objectos.html.tmpl.StandardAttributeName.TextOverflow;
import objectos.html.tmpl.StandardAttributeName.TextRendering;
import objectos.html.tmpl.StandardAttributeName.Transform;
import objectos.html.tmpl.StandardAttributeName.TransformOrigin;
import objectos.html.tmpl.StandardAttributeName.Translate;
import objectos.html.tmpl.StandardAttributeName.Type;
import objectos.html.tmpl.StandardAttributeName.UnicodeBidi;
import objectos.html.tmpl.StandardAttributeName.Value;
import objectos.html.tmpl.StandardAttributeName.VectorEffect;
import objectos.html.tmpl.StandardAttributeName.ViewBox;
import objectos.html.tmpl.StandardAttributeName.Visibility;
import objectos.html.tmpl.StandardAttributeName.WhiteSpace;
import objectos.html.tmpl.StandardAttributeName.Width;
import objectos.html.tmpl.StandardAttributeName.WordSpacing;
import objectos.html.tmpl.StandardAttributeName.Wrap;
import objectos.html.tmpl.StandardAttributeName.WritingMode;
import objectos.html.tmpl.StandardAttributeName.Xmlns;
import objectos.html.tmpl.StandardElementName;
import objectos.html.tmpl.StrongValue;
import objectos.html.tmpl.StyleValue;
import objectos.html.tmpl.SubValue;
import objectos.html.tmpl.SummaryValue;
import objectos.html.tmpl.SupValue;
import objectos.html.tmpl.SvgValue;
import objectos.html.tmpl.TableValue;
import objectos.html.tmpl.TbodyValue;
import objectos.html.tmpl.TdValue;
import objectos.html.tmpl.TemplateValue;
import objectos.html.tmpl.TextareaValue;
import objectos.html.tmpl.ThValue;
import objectos.html.tmpl.TheadValue;
import objectos.html.tmpl.TitleValue;
import objectos.html.tmpl.TrValue;
import objectos.html.tmpl.UlValue;

abstract class GeneratedAbstractTemplate {
  public final ElementName a(AValue... values) {
    return addStandardElement(StandardElementName.A, values);
  }

  public final ElementName a(String text) {
    return addStandardElement(StandardElementName.A, text);
  }

  public final ElementName abbr(AbbrValue... values) {
    return addStandardElement(StandardElementName.ABBR, values);
  }

  public final ElementName abbr(String text) {
    return addStandardElement(StandardElementName.ABBR, text);
  }

  public final ElementName article(ArticleValue... values) {
    return addStandardElement(StandardElementName.ARTICLE, values);
  }

  public final ElementName article(String text) {
    return addStandardElement(StandardElementName.ARTICLE, text);
  }

  public final ElementName b(BValue... values) {
    return addStandardElement(StandardElementName.B, values);
  }

  public final ElementName b(String text) {
    return addStandardElement(StandardElementName.B, text);
  }

  public final ElementName blockquote(BlockquoteValue... values) {
    return addStandardElement(StandardElementName.BLOCKQUOTE, values);
  }

  public final ElementName blockquote(String text) {
    return addStandardElement(StandardElementName.BLOCKQUOTE, text);
  }

  public final ElementName body(BodyValue... values) {
    return addStandardElement(StandardElementName.BODY, values);
  }

  public final ElementName body(String text) {
    return addStandardElement(StandardElementName.BODY, text);
  }

  public final ElementName br(BrValue... values) {
    return addStandardElement(StandardElementName.BR, values);
  }

  public final ElementName button(ButtonValue... values) {
    return addStandardElement(StandardElementName.BUTTON, values);
  }

  public final ElementName button(String text) {
    return addStandardElement(StandardElementName.BUTTON, text);
  }

  public final ElementName clipPath(ClipPathValue... values) {
    return addStandardElement(StandardElementName.CLIPPATH, values);
  }

  public final ElementName code(CodeValue... values) {
    return addStandardElement(StandardElementName.CODE, values);
  }

  public final ElementName code(String text) {
    return addStandardElement(StandardElementName.CODE, text);
  }

  public final ElementName dd(DdValue... values) {
    return addStandardElement(StandardElementName.DD, values);
  }

  public final ElementName dd(String text) {
    return addStandardElement(StandardElementName.DD, text);
  }

  public final ElementName defs(DefsValue... values) {
    return addStandardElement(StandardElementName.DEFS, values);
  }

  public final ElementName defs(String text) {
    return addStandardElement(StandardElementName.DEFS, text);
  }

  public final ElementName details(DetailsValue... values) {
    return addStandardElement(StandardElementName.DETAILS, values);
  }

  public final ElementName details(String text) {
    return addStandardElement(StandardElementName.DETAILS, text);
  }

  public final ElementName div(DivValue... values) {
    return addStandardElement(StandardElementName.DIV, values);
  }

  public final ElementName div(String text) {
    return addStandardElement(StandardElementName.DIV, text);
  }

  public final ElementName dl(DlValue... values) {
    return addStandardElement(StandardElementName.DL, values);
  }

  public final ElementName dl(String text) {
    return addStandardElement(StandardElementName.DL, text);
  }

  public final ElementName dt(DtValue... values) {
    return addStandardElement(StandardElementName.DT, values);
  }

  public final ElementName dt(String text) {
    return addStandardElement(StandardElementName.DT, text);
  }

  public final ElementName em(EmValue... values) {
    return addStandardElement(StandardElementName.EM, values);
  }

  public final ElementName em(String text) {
    return addStandardElement(StandardElementName.EM, text);
  }

  public final ElementName fieldset(FieldsetValue... values) {
    return addStandardElement(StandardElementName.FIELDSET, values);
  }

  public final ElementName fieldset(String text) {
    return addStandardElement(StandardElementName.FIELDSET, text);
  }

  public final ElementName figure(FigureValue... values) {
    return addStandardElement(StandardElementName.FIGURE, values);
  }

  public final ElementName figure(String text) {
    return addStandardElement(StandardElementName.FIGURE, text);
  }

  public final ElementName footer(FooterValue... values) {
    return addStandardElement(StandardElementName.FOOTER, values);
  }

  public final ElementName footer(String text) {
    return addStandardElement(StandardElementName.FOOTER, text);
  }

  public final ElementName form(FormValue... values) {
    return addStandardElement(StandardElementName.FORM, values);
  }

  public final ElementName g(GValue... values) {
    return addStandardElement(StandardElementName.G, values);
  }

  public final ElementName g(String text) {
    return addStandardElement(StandardElementName.G, text);
  }

  public final ElementName h1(H1Value... values) {
    return addStandardElement(StandardElementName.H1, values);
  }

  public final ElementName h1(String text) {
    return addStandardElement(StandardElementName.H1, text);
  }

  public final ElementName h2(H2Value... values) {
    return addStandardElement(StandardElementName.H2, values);
  }

  public final ElementName h2(String text) {
    return addStandardElement(StandardElementName.H2, text);
  }

  public final ElementName h3(H3Value... values) {
    return addStandardElement(StandardElementName.H3, values);
  }

  public final ElementName h3(String text) {
    return addStandardElement(StandardElementName.H3, text);
  }

  public final ElementName h4(H4Value... values) {
    return addStandardElement(StandardElementName.H4, values);
  }

  public final ElementName h4(String text) {
    return addStandardElement(StandardElementName.H4, text);
  }

  public final ElementName h5(H5Value... values) {
    return addStandardElement(StandardElementName.H5, values);
  }

  public final ElementName h5(String text) {
    return addStandardElement(StandardElementName.H5, text);
  }

  public final ElementName h6(H6Value... values) {
    return addStandardElement(StandardElementName.H6, values);
  }

  public final ElementName h6(String text) {
    return addStandardElement(StandardElementName.H6, text);
  }

  public final ElementName head(HeadValue... values) {
    return addStandardElement(StandardElementName.HEAD, values);
  }

  public final ElementName head(String text) {
    return addStandardElement(StandardElementName.HEAD, text);
  }

  public final ElementName header(HeaderValue... values) {
    return addStandardElement(StandardElementName.HEADER, values);
  }

  public final ElementName header(String text) {
    return addStandardElement(StandardElementName.HEADER, text);
  }

  public final ElementName hgroup(HgroupValue... values) {
    return addStandardElement(StandardElementName.HGROUP, values);
  }

  public final ElementName hgroup(String text) {
    return addStandardElement(StandardElementName.HGROUP, text);
  }

  public final ElementName hr(HrValue... values) {
    return addStandardElement(StandardElementName.HR, values);
  }

  public final ElementName html(HtmlValue... values) {
    return addStandardElement(StandardElementName.HTML, values);
  }

  public final ElementName html(String text) {
    return addStandardElement(StandardElementName.HTML, text);
  }

  public final ElementName img(ImgValue... values) {
    return addStandardElement(StandardElementName.IMG, values);
  }

  public final ElementName input(InputValue... values) {
    return addStandardElement(StandardElementName.INPUT, values);
  }

  public final ElementName kbd(KbdValue... values) {
    return addStandardElement(StandardElementName.KBD, values);
  }

  public final ElementName kbd(String text) {
    return addStandardElement(StandardElementName.KBD, text);
  }

  public final ElementName label(LabelValue... values) {
    return addStandardElement(StandardElementName.LABEL, values);
  }

  public final ElementName legend(LegendValue... values) {
    return addStandardElement(StandardElementName.LEGEND, values);
  }

  public final ElementName legend(String text) {
    return addStandardElement(StandardElementName.LEGEND, text);
  }

  public final ElementName li(LiValue... values) {
    return addStandardElement(StandardElementName.LI, values);
  }

  public final ElementName li(String text) {
    return addStandardElement(StandardElementName.LI, text);
  }

  public final ElementName link(LinkValue... values) {
    return addStandardElement(StandardElementName.LINK, values);
  }

  public final ElementName main(MainValue... values) {
    return addStandardElement(StandardElementName.MAIN, values);
  }

  public final ElementName main(String text) {
    return addStandardElement(StandardElementName.MAIN, text);
  }

  public final ElementName menu(MenuValue... values) {
    return addStandardElement(StandardElementName.MENU, values);
  }

  public final ElementName menu(String text) {
    return addStandardElement(StandardElementName.MENU, text);
  }

  public final ElementName meta(MetaValue... values) {
    return addStandardElement(StandardElementName.META, values);
  }

  public final ElementName nav(NavValue... values) {
    return addStandardElement(StandardElementName.NAV, values);
  }

  public final ElementName nav(String text) {
    return addStandardElement(StandardElementName.NAV, text);
  }

  public final ElementName ol(OlValue... values) {
    return addStandardElement(StandardElementName.OL, values);
  }

  public final ElementName ol(String text) {
    return addStandardElement(StandardElementName.OL, text);
  }

  public final ElementName optgroup(OptgroupValue... values) {
    return addStandardElement(StandardElementName.OPTGROUP, values);
  }

  public final ElementName optgroup(String text) {
    return addStandardElement(StandardElementName.OPTGROUP, text);
  }

  public final ElementName option(OptionValue... values) {
    return addStandardElement(StandardElementName.OPTION, values);
  }

  public final ElementName option(String text) {
    return addStandardElement(StandardElementName.OPTION, text);
  }

  public final ElementName p(PValue... values) {
    return addStandardElement(StandardElementName.P, values);
  }

  public final ElementName p(String text) {
    return addStandardElement(StandardElementName.P, text);
  }

  public final ElementName path(PathValue... values) {
    return addStandardElement(StandardElementName.PATH, values);
  }

  public final ElementName path(String text) {
    return addStandardElement(StandardElementName.PATH, text);
  }

  public final ElementName pre(PreValue... values) {
    return addStandardElement(StandardElementName.PRE, values);
  }

  public final ElementName pre(String text) {
    return addStandardElement(StandardElementName.PRE, text);
  }

  public final ElementName progress(ProgressValue... values) {
    return addStandardElement(StandardElementName.PROGRESS, values);
  }

  public final ElementName progress(String text) {
    return addStandardElement(StandardElementName.PROGRESS, text);
  }

  public final ElementName samp(SampValue... values) {
    return addStandardElement(StandardElementName.SAMP, values);
  }

  public final ElementName samp(String text) {
    return addStandardElement(StandardElementName.SAMP, text);
  }

  public final ElementName script(ScriptValue... values) {
    return addStandardElement(StandardElementName.SCRIPT, values);
  }

  public final ElementName script(String text) {
    return addStandardElement(StandardElementName.SCRIPT, text);
  }

  public final ElementName section(SectionValue... values) {
    return addStandardElement(StandardElementName.SECTION, values);
  }

  public final ElementName section(String text) {
    return addStandardElement(StandardElementName.SECTION, text);
  }

  public final ElementName select(SelectValue... values) {
    return addStandardElement(StandardElementName.SELECT, values);
  }

  public final ElementName select(String text) {
    return addStandardElement(StandardElementName.SELECT, text);
  }

  public final ElementName small(SmallValue... values) {
    return addStandardElement(StandardElementName.SMALL, values);
  }

  public final ElementName small(String text) {
    return addStandardElement(StandardElementName.SMALL, text);
  }

  public final ElementName span(SpanValue... values) {
    return addStandardElement(StandardElementName.SPAN, values);
  }

  public final ElementName span(String text) {
    return addStandardElement(StandardElementName.SPAN, text);
  }

  public final ElementName strong(StrongValue... values) {
    return addStandardElement(StandardElementName.STRONG, values);
  }

  public final ElementName strong(String text) {
    return addStandardElement(StandardElementName.STRONG, text);
  }

  public final ElementName style(StyleValue... values) {
    return addStandardElement(StandardElementName.STYLE, values);
  }

  public final ElementName style(String text) {
    return addStandardElement(StandardElementName.STYLE, text);
  }

  public final ElementName sub(SubValue... values) {
    return addStandardElement(StandardElementName.SUB, values);
  }

  public final ElementName sub(String text) {
    return addStandardElement(StandardElementName.SUB, text);
  }

  public final ElementName summary(SummaryValue... values) {
    return addStandardElement(StandardElementName.SUMMARY, values);
  }

  public final ElementName summary(String text) {
    return addStandardElement(StandardElementName.SUMMARY, text);
  }

  public final ElementName sup(SupValue... values) {
    return addStandardElement(StandardElementName.SUP, values);
  }

  public final ElementName sup(String text) {
    return addStandardElement(StandardElementName.SUP, text);
  }

  public final ElementName svg(SvgValue... values) {
    return addStandardElement(StandardElementName.SVG, values);
  }

  public final ElementName svg(String text) {
    return addStandardElement(StandardElementName.SVG, text);
  }

  public final ElementName table(TableValue... values) {
    return addStandardElement(StandardElementName.TABLE, values);
  }

  public final ElementName table(String text) {
    return addStandardElement(StandardElementName.TABLE, text);
  }

  public final ElementName tbody(TbodyValue... values) {
    return addStandardElement(StandardElementName.TBODY, values);
  }

  public final ElementName tbody(String text) {
    return addStandardElement(StandardElementName.TBODY, text);
  }

  public final ElementName td(TdValue... values) {
    return addStandardElement(StandardElementName.TD, values);
  }

  public final ElementName td(String text) {
    return addStandardElement(StandardElementName.TD, text);
  }

  public final ElementName template(TemplateValue... values) {
    return addStandardElement(StandardElementName.TEMPLATE, values);
  }

  public final ElementName template(String text) {
    return addStandardElement(StandardElementName.TEMPLATE, text);
  }

  public final ElementName textarea(TextareaValue... values) {
    return addStandardElement(StandardElementName.TEXTAREA, values);
  }

  public final ElementName textarea(String text) {
    return addStandardElement(StandardElementName.TEXTAREA, text);
  }

  public final ElementName th(ThValue... values) {
    return addStandardElement(StandardElementName.TH, values);
  }

  public final ElementName th(String text) {
    return addStandardElement(StandardElementName.TH, text);
  }

  public final ElementName thead(TheadValue... values) {
    return addStandardElement(StandardElementName.THEAD, values);
  }

  public final ElementName thead(String text) {
    return addStandardElement(StandardElementName.THEAD, text);
  }

  public final ElementName title(TitleValue... values) {
    return addStandardElement(StandardElementName.TITLE, values);
  }

  public final ElementName tr(TrValue... values) {
    return addStandardElement(StandardElementName.TR, values);
  }

  public final ElementName tr(String text) {
    return addStandardElement(StandardElementName.TR, text);
  }

  public final ElementName ul(UlValue... values) {
    return addStandardElement(StandardElementName.UL, values);
  }

  public final ElementName ul(String text) {
    return addStandardElement(StandardElementName.UL, text);
  }

  public final Accesskey accesskey(String value) {
    return addStandardAttribute(StandardAttributeName.ACCESSKEY, value);
  }

  public final Action action(String value) {
    return addStandardAttribute(StandardAttributeName.ACTION, value);
  }

  public final Align align(String value) {
    return addStandardAttribute(StandardAttributeName.ALIGN, value);
  }

  public final AlignmentBaseline alignmentBaseline(String value) {
    return addStandardAttribute(StandardAttributeName.ALIGNMENTBASELINE, value);
  }

  public final Alt alt(String value) {
    return addStandardAttribute(StandardAttributeName.ALT, value);
  }

  public final AriaHidden ariaHidden(String value) {
    return addStandardAttribute(StandardAttributeName.ARIAHIDDEN, value);
  }

  public final Async async() {
    return addStandardAttribute(StandardAttributeName.ASYNC);
  }

  public final Autocomplete autocomplete(String value) {
    return addStandardAttribute(StandardAttributeName.AUTOCOMPLETE, value);
  }

  public final Autofocus autofocus() {
    return addStandardAttribute(StandardAttributeName.AUTOFOCUS);
  }

  public final BaselineShift baselineShift(String value) {
    return addStandardAttribute(StandardAttributeName.BASELINESHIFT, value);
  }

  public final Border border(String value) {
    return addStandardAttribute(StandardAttributeName.BORDER, value);
  }

  public final Cellpadding cellpadding(String value) {
    return addStandardAttribute(StandardAttributeName.CELLPADDING, value);
  }

  public final Cellspacing cellspacing(String value) {
    return addStandardAttribute(StandardAttributeName.CELLSPACING, value);
  }

  public final Charset charset(String value) {
    return addStandardAttribute(StandardAttributeName.CHARSET, value);
  }

  public final Cite cite(String value) {
    return addStandardAttribute(StandardAttributeName.CITE, value);
  }

  public final Class _class(String value) {
    return addStandardAttribute(StandardAttributeName.CLASS, value);
  }

  public final ClipRule clipRule(String value) {
    return addStandardAttribute(StandardAttributeName.CLIPRULE, value);
  }

  public final Color color(String value) {
    return addStandardAttribute(StandardAttributeName.COLOR, value);
  }

  public final ColorInterpolation colorInterpolation(String value) {
    return addStandardAttribute(StandardAttributeName.COLORINTERPOLATION, value);
  }

  public final ColorInterpolationFilters colorInterpolationFilters(String value) {
    return addStandardAttribute(StandardAttributeName.COLORINTERPOLATIONFILTERS, value);
  }

  public final Cols cols(String value) {
    return addStandardAttribute(StandardAttributeName.COLS, value);
  }

  public final Content content(String value) {
    return addStandardAttribute(StandardAttributeName.CONTENT, value);
  }

  public final Contenteditable contenteditable(String value) {
    return addStandardAttribute(StandardAttributeName.CONTENTEDITABLE, value);
  }

  public final Crossorigin crossorigin(String value) {
    return addStandardAttribute(StandardAttributeName.CROSSORIGIN, value);
  }

  public final Cursor cursor(String value) {
    return addStandardAttribute(StandardAttributeName.CURSOR, value);
  }

  public final D d(String value) {
    return addStandardAttribute(StandardAttributeName.D, value);
  }

  public final Defer defer() {
    return addStandardAttribute(StandardAttributeName.DEFER);
  }

  public final Dir dir(String value) {
    return addStandardAttribute(StandardAttributeName.DIR, value);
  }

  public final Direction direction(String value) {
    return addStandardAttribute(StandardAttributeName.DIRECTION, value);
  }

  public final Dirname dirname(String value) {
    return addStandardAttribute(StandardAttributeName.DIRNAME, value);
  }

  public final Disabled disabled() {
    return addStandardAttribute(StandardAttributeName.DISABLED);
  }

  public final Display display(String value) {
    return addStandardAttribute(StandardAttributeName.DISPLAY, value);
  }

  public final DominantBaseline dominantBaseline(String value) {
    return addStandardAttribute(StandardAttributeName.DOMINANTBASELINE, value);
  }

  public final Draggable draggable(String value) {
    return addStandardAttribute(StandardAttributeName.DRAGGABLE, value);
  }

  public final Enctype enctype(String value) {
    return addStandardAttribute(StandardAttributeName.ENCTYPE, value);
  }

  public final Fill fill(String value) {
    return addStandardAttribute(StandardAttributeName.FILL, value);
  }

  public final FillOpacity fillOpacity(String value) {
    return addStandardAttribute(StandardAttributeName.FILLOPACITY, value);
  }

  public final FillRule fillRule(String value) {
    return addStandardAttribute(StandardAttributeName.FILLRULE, value);
  }

  public final Filter filter(String value) {
    return addStandardAttribute(StandardAttributeName.FILTER, value);
  }

  public final FloodColor floodColor(String value) {
    return addStandardAttribute(StandardAttributeName.FLOODCOLOR, value);
  }

  public final FloodOpacity floodOpacity(String value) {
    return addStandardAttribute(StandardAttributeName.FLOODOPACITY, value);
  }

  public final FontFamily fontFamily(String value) {
    return addStandardAttribute(StandardAttributeName.FONTFAMILY, value);
  }

  public final FontSize fontSize(String value) {
    return addStandardAttribute(StandardAttributeName.FONTSIZE, value);
  }

  public final FontSizeAdjust fontSizeAdjust(String value) {
    return addStandardAttribute(StandardAttributeName.FONTSIZEADJUST, value);
  }

  public final FontStretch fontStretch(String value) {
    return addStandardAttribute(StandardAttributeName.FONTSTRETCH, value);
  }

  public final FontStyle fontStyle(String value) {
    return addStandardAttribute(StandardAttributeName.FONTSTYLE, value);
  }

  public final FontVariant fontVariant(String value) {
    return addStandardAttribute(StandardAttributeName.FONTVARIANT, value);
  }

  public final FontWeight fontWeight(String value) {
    return addStandardAttribute(StandardAttributeName.FONTWEIGHT, value);
  }

  public final For forAttr(String value) {
    return addStandardAttribute(StandardAttributeName.FOR, value);
  }

  public final For forElement(String value) {
    return addStandardAttribute(StandardAttributeName.FOR, value);
  }

  public final Form form(String value) {
    return addStandardAttribute(StandardAttributeName.FORM, value);
  }

  public final GlyphOrientationHorizontal glyphOrientationHorizontal(String value) {
    return addStandardAttribute(StandardAttributeName.GLYPHORIENTATIONHORIZONTAL, value);
  }

  public final GlyphOrientationVertical glyphOrientationVertical(String value) {
    return addStandardAttribute(StandardAttributeName.GLYPHORIENTATIONVERTICAL, value);
  }

  public final Height height(String value) {
    return addStandardAttribute(StandardAttributeName.HEIGHT, value);
  }

  public final Hidden hidden() {
    return addStandardAttribute(StandardAttributeName.HIDDEN);
  }

  public final Href href(String value) {
    return addStandardAttribute(StandardAttributeName.HREF, value);
  }

  public final HttpEquiv httpEquiv(String value) {
    return addStandardAttribute(StandardAttributeName.HTTPEQUIV, value);
  }

  public final Id id(String value) {
    return addStandardAttribute(StandardAttributeName.ID, value);
  }

  public final ImageRendering imageRendering(String value) {
    return addStandardAttribute(StandardAttributeName.IMAGERENDERING, value);
  }

  public final Integrity integrity(String value) {
    return addStandardAttribute(StandardAttributeName.INTEGRITY, value);
  }

  public final Lang lang(String value) {
    return addStandardAttribute(StandardAttributeName.LANG, value);
  }

  public final LetterSpacing letterSpacing(String value) {
    return addStandardAttribute(StandardAttributeName.LETTERSPACING, value);
  }

  public final LightingColor lightingColor(String value) {
    return addStandardAttribute(StandardAttributeName.LIGHTINGCOLOR, value);
  }

  public final MarkerEnd markerEnd(String value) {
    return addStandardAttribute(StandardAttributeName.MARKEREND, value);
  }

  public final MarkerMid markerMid(String value) {
    return addStandardAttribute(StandardAttributeName.MARKERMID, value);
  }

  public final MarkerStart markerStart(String value) {
    return addStandardAttribute(StandardAttributeName.MARKERSTART, value);
  }

  public final Mask mask(String value) {
    return addStandardAttribute(StandardAttributeName.MASK, value);
  }

  public final MaskType maskType(String value) {
    return addStandardAttribute(StandardAttributeName.MASKTYPE, value);
  }

  public final Maxlength maxlength(String value) {
    return addStandardAttribute(StandardAttributeName.MAXLENGTH, value);
  }

  public final Media media(String value) {
    return addStandardAttribute(StandardAttributeName.MEDIA, value);
  }

  public final Method method(String value) {
    return addStandardAttribute(StandardAttributeName.METHOD, value);
  }

  public final Minlength minlength(String value) {
    return addStandardAttribute(StandardAttributeName.MINLENGTH, value);
  }

  public final Multiple multiple() {
    return addStandardAttribute(StandardAttributeName.MULTIPLE);
  }

  public final Name name(String value) {
    return addStandardAttribute(StandardAttributeName.NAME, value);
  }

  public final Nomodule nomodule() {
    return addStandardAttribute(StandardAttributeName.NOMODULE);
  }

  public final Onafterprint onafterprint(String value) {
    return addStandardAttribute(StandardAttributeName.ONAFTERPRINT, value);
  }

  public final Onbeforeprint onbeforeprint(String value) {
    return addStandardAttribute(StandardAttributeName.ONBEFOREPRINT, value);
  }

  public final Onbeforeunload onbeforeunload(String value) {
    return addStandardAttribute(StandardAttributeName.ONBEFOREUNLOAD, value);
  }

  public final Onclick onclick(String value) {
    return addStandardAttribute(StandardAttributeName.ONCLICK, value);
  }

  public final Onhashchange onhashchange(String value) {
    return addStandardAttribute(StandardAttributeName.ONHASHCHANGE, value);
  }

  public final Onlanguagechange onlanguagechange(String value) {
    return addStandardAttribute(StandardAttributeName.ONLANGUAGECHANGE, value);
  }

  public final Onmessage onmessage(String value) {
    return addStandardAttribute(StandardAttributeName.ONMESSAGE, value);
  }

  public final Onoffline onoffline(String value) {
    return addStandardAttribute(StandardAttributeName.ONOFFLINE, value);
  }

  public final Ononline ononline(String value) {
    return addStandardAttribute(StandardAttributeName.ONONLINE, value);
  }

  public final Onpagehide onpagehide(String value) {
    return addStandardAttribute(StandardAttributeName.ONPAGEHIDE, value);
  }

  public final Onpageshow onpageshow(String value) {
    return addStandardAttribute(StandardAttributeName.ONPAGESHOW, value);
  }

  public final Onpopstate onpopstate(String value) {
    return addStandardAttribute(StandardAttributeName.ONPOPSTATE, value);
  }

  public final Onrejectionhandled onrejectionhandled(String value) {
    return addStandardAttribute(StandardAttributeName.ONREJECTIONHANDLED, value);
  }

  public final Onstorage onstorage(String value) {
    return addStandardAttribute(StandardAttributeName.ONSTORAGE, value);
  }

  public final Onsubmit onsubmit(String value) {
    return addStandardAttribute(StandardAttributeName.ONSUBMIT, value);
  }

  public final Onunhandledrejection onunhandledrejection(String value) {
    return addStandardAttribute(StandardAttributeName.ONUNHANDLEDREJECTION, value);
  }

  public final Onunload onunload(String value) {
    return addStandardAttribute(StandardAttributeName.ONUNLOAD, value);
  }

  public final Opacity opacity(String value) {
    return addStandardAttribute(StandardAttributeName.OPACITY, value);
  }

  public final Open open() {
    return addStandardAttribute(StandardAttributeName.OPEN);
  }

  public final Overflow overflow(String value) {
    return addStandardAttribute(StandardAttributeName.OVERFLOW, value);
  }

  public final PaintOrder paintOrder(String value) {
    return addStandardAttribute(StandardAttributeName.PAINTORDER, value);
  }

  public final Placeholder placeholder(String value) {
    return addStandardAttribute(StandardAttributeName.PLACEHOLDER, value);
  }

  public final PointerEvents pointerEvents(String value) {
    return addStandardAttribute(StandardAttributeName.POINTEREVENTS, value);
  }

  public final Property property(String value) {
    return addStandardAttribute(StandardAttributeName.PROPERTY, value);
  }

  public final Readonly readonly() {
    return addStandardAttribute(StandardAttributeName.READONLY);
  }

  public final Referrerpolicy referrerpolicy(String value) {
    return addStandardAttribute(StandardAttributeName.REFERRERPOLICY, value);
  }

  public final Rel rel(String value) {
    return addStandardAttribute(StandardAttributeName.REL, value);
  }

  public final Required required() {
    return addStandardAttribute(StandardAttributeName.REQUIRED);
  }

  public final Rev rev(String value) {
    return addStandardAttribute(StandardAttributeName.REV, value);
  }

  public final Reversed reversed() {
    return addStandardAttribute(StandardAttributeName.REVERSED);
  }

  public final Role role(String value) {
    return addStandardAttribute(StandardAttributeName.ROLE, value);
  }

  public final Rows rows(String value) {
    return addStandardAttribute(StandardAttributeName.ROWS, value);
  }

  public final Selected selected() {
    return addStandardAttribute(StandardAttributeName.SELECTED);
  }

  public final ShapeRendering shapeRendering(String value) {
    return addStandardAttribute(StandardAttributeName.SHAPERENDERING, value);
  }

  public final Size size(String value) {
    return addStandardAttribute(StandardAttributeName.SIZE, value);
  }

  public final Sizes sizes(String value) {
    return addStandardAttribute(StandardAttributeName.SIZES, value);
  }

  public final Spellcheck spellcheck(String value) {
    return addStandardAttribute(StandardAttributeName.SPELLCHECK, value);
  }

  public final Src src(String value) {
    return addStandardAttribute(StandardAttributeName.SRC, value);
  }

  public final Srcset srcset(String value) {
    return addStandardAttribute(StandardAttributeName.SRCSET, value);
  }

  public final Start start(String value) {
    return addStandardAttribute(StandardAttributeName.START, value);
  }

  public final StopColor stopColor(String value) {
    return addStandardAttribute(StandardAttributeName.STOPCOLOR, value);
  }

  public final StopOpacity stopOpacity(String value) {
    return addStandardAttribute(StandardAttributeName.STOPOPACITY, value);
  }

  public final Stroke stroke(String value) {
    return addStandardAttribute(StandardAttributeName.STROKE, value);
  }

  public final StrokeDasharray strokeDasharray(String value) {
    return addStandardAttribute(StandardAttributeName.STROKEDASHARRAY, value);
  }

  public final StrokeDashoffset strokeDashoffset(String value) {
    return addStandardAttribute(StandardAttributeName.STROKEDASHOFFSET, value);
  }

  public final StrokeLinecap strokeLinecap(String value) {
    return addStandardAttribute(StandardAttributeName.STROKELINECAP, value);
  }

  public final StrokeLinejoin strokeLinejoin(String value) {
    return addStandardAttribute(StandardAttributeName.STROKELINEJOIN, value);
  }

  public final StrokeMiterlimit strokeMiterlimit(String value) {
    return addStandardAttribute(StandardAttributeName.STROKEMITERLIMIT, value);
  }

  public final StrokeOpacity strokeOpacity(String value) {
    return addStandardAttribute(StandardAttributeName.STROKEOPACITY, value);
  }

  public final StrokeWidth strokeWidth(String value) {
    return addStandardAttribute(StandardAttributeName.STROKEWIDTH, value);
  }

  public final Style inlineStyle(String value) {
    return addStandardAttribute(StandardAttributeName.STYLE, value);
  }

  public final Tabindex tabindex(String value) {
    return addStandardAttribute(StandardAttributeName.TABINDEX, value);
  }

  public final Target target(String value) {
    return addStandardAttribute(StandardAttributeName.TARGET, value);
  }

  public final TextAnchor textAnchor(String value) {
    return addStandardAttribute(StandardAttributeName.TEXTANCHOR, value);
  }

  public final TextDecoration textDecoration(String value) {
    return addStandardAttribute(StandardAttributeName.TEXTDECORATION, value);
  }

  public final TextOverflow textOverflow(String value) {
    return addStandardAttribute(StandardAttributeName.TEXTOVERFLOW, value);
  }

  public final TextRendering textRendering(String value) {
    return addStandardAttribute(StandardAttributeName.TEXTRENDERING, value);
  }

  public final Transform transform(String value) {
    return addStandardAttribute(StandardAttributeName.TRANSFORM, value);
  }

  public final TransformOrigin transformOrigin(String value) {
    return addStandardAttribute(StandardAttributeName.TRANSFORMORIGIN, value);
  }

  public final Translate translate(String value) {
    return addStandardAttribute(StandardAttributeName.TRANSLATE, value);
  }

  public final Type type(String value) {
    return addStandardAttribute(StandardAttributeName.TYPE, value);
  }

  public final UnicodeBidi unicodeBidi(String value) {
    return addStandardAttribute(StandardAttributeName.UNICODEBIDI, value);
  }

  public final Value value(String value) {
    return addStandardAttribute(StandardAttributeName.VALUE, value);
  }

  public final VectorEffect vectorEffect(String value) {
    return addStandardAttribute(StandardAttributeName.VECTOREFFECT, value);
  }

  public final ViewBox viewBox(String value) {
    return addStandardAttribute(StandardAttributeName.VIEWBOX, value);
  }

  public final Visibility visibility(String value) {
    return addStandardAttribute(StandardAttributeName.VISIBILITY, value);
  }

  public final WhiteSpace whiteSpace(String value) {
    return addStandardAttribute(StandardAttributeName.WHITESPACE, value);
  }

  public final Width width(String value) {
    return addStandardAttribute(StandardAttributeName.WIDTH, value);
  }

  public final WordSpacing wordSpacing(String value) {
    return addStandardAttribute(StandardAttributeName.WORDSPACING, value);
  }

  public final Wrap wrap(String value) {
    return addStandardAttribute(StandardAttributeName.WRAP, value);
  }

  public final WritingMode writingMode(String value) {
    return addStandardAttribute(StandardAttributeName.WRITINGMODE, value);
  }

  public final Xmlns xmlns(String value) {
    return addStandardAttribute(StandardAttributeName.XMLNS, value);
  }

  abstract <N extends StandardAttributeName> N addStandardAttribute(N name);

  abstract <N extends StandardAttributeName> N addStandardAttribute(N name, String value);

  abstract ElementName addStandardElement(StandardElementName name, String text);

  abstract ElementName addStandardElement(StandardElementName name, objectos.html.tmpl.Value[] values);
}
