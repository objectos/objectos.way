/*
 * Copyright (C) 2023-2026 Objectos Software LTDA.
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
const way = (function() {

  "use strict";

  const way = {

    on: on

  };

  // ##################################################################
  // # BEGIN: Actions support
  // ##################################################################

  function on(event, action) {
    const ctx = {
      $document: undefined,

      $el: event.currentTarget,

      $evt: event,

      $preventDefault: true,

      $recv: undefined,

      document: function() {
        if (!this.$document) {
          const global = globalThis;

          if (!global.document) {
            throw new Error("Illegal state: global scope has no document");
          }

          this.$document = global.document;
        }

        return this.$document;
      },

      window: function() {
        const global = globalThis;

        if (!(global instanceof Window)) {
          throw new Error("Illegal state: global scope is not a Window instance");
        }

        return global;
      }
    };

    execute(ctx, action);

    if (ctx.$preventDefault) {
      event.preventDefault();
    }
  }

  function execute(ctx, action) {
    checkArray(action, "action");

    const id = checkString(action.shift(), "id");

    const handler = operations[id];

    if (handler) {
      return handler(ctx, action);
    } else {
      throw new Error(`Illegal arg: no handler found for operation=${id}`);
    }
  }

  function nest(ctx, action) {
    checkArray(action, "action");

    return (...args) => {
      const nested = { ...ctx };

      nested.$args = args;

      const nestedAction = structuredClone(action);

      return execute(nested, nestedAction);
    };
  }

  // ##################################################################
  // # END: Actions support
  // ##################################################################

  // ##################################################################
  // # BEGIN: Objectos Way Actions
  // ##################################################################

  const operations = {
    "AX": argsRead,
    "CR": contextRead,
    "cr": contextReadUnchecked,
    "CW": contextWrite,
    "EI": elementById,
    "ET": elementTarget,
    "FE": forEach,
    "FO": follow,
    "FN": functionJs,
    "GR": globalRead,
    "HI": historyUpdate,
    "IF": ifElse,
    "IU": invokeUnchecked,
    "IV": invokeVirtual,
    "JS": jsValue,
    "NO": noop,
    "PO": popstate,
    "PR": propertyRead,
    "pr": propertyReadUnchecked,
    "PW": propertyWrite,
    "RE": render,
    "SU": submit,
    "TE": throwError,
    "TY": typeEnsure,
    "UB": updateBody,
    "UE": updateElements,
    "UH": updateHead,
    "W1": wayOne,
    "WS": waySeq
  };

  function argsRead(ctx, args) {
    const index = checkInteger(args.shift(), "index");

    if (index < 0) {
      throw new Error(`Illegal arg: index must not be negative. index=${index}`);
    }

    const values = ctx.$args;

    if (!values) {
      throw new Error(`Illegal arg: context does not define $args`);
    }

    return values[index];
  }

  function contextRead(ctx, args) {
    const name = contextName(args.shift());

    return ctx[name];
  }

  function contextReadUnchecked(ctx, args) {
    const name = checkString(args.shift(), "name");

    if (name.length === 0) {
      throw new Error("Illegal arg: context variable name must not be empty");
    }

    return ctx[name];
  }

  function contextWrite(ctx, args) {
    const name = contextName(args.shift());

    const val = checkDefined(args.shift(), "value");

    ctx[name] = execute(ctx, val);
  }

  function contextName(maybe) {
    const name = checkString(maybe, "name");

    if (name.length === 0) {
      throw new Error("Illegal arg: context variable name must not be empty");
    }

    if (name.startsWith("$")) {
      throw new Error("Illegal arg: context variable name must not begin with the '$' character");
    }

    return name;
  }

  function elementById(ctx, args) {
    const action = checkDefined(args.shift(), "action");

    const id = execute(ctx, action);

    checkString(id, "id");

    const element = document.getElementById(id);

    if (!element) {
      throw new Error(`Illegal arg: element not found with ID ${id}`);
    }

    return element;
  }

  function elementTarget(ctx, _) {
    return ctx.$el;
  }

  function follow(ctx, args) {
    const el = ctx.$el;

    // validate element

    if (!(el instanceof HTMLAnchorElement)) {
      const actual = el.constructor ? el.constructor.name : "Unknown";

      throw new Error(`Illegal state: follow must be executed on an HTMLAnchorElement but got ${actual}`);
    }

    const href = el.href;

    if (!href) {
      throw new Error(`Illegal state: anchor has no href property`);
    }

    return navigate(ctx, args, href);
  }

  function forEach(ctx, args) {
    const recv = checkRecv(ctx, args.shift());

    const action = checkDefined(args.shift(), "action");

    const fn = nest(ctx, action);

    recv.forEach(fn);
  }

  function functionJs(ctx, args) {
    const action = checkDefined(args.shift(), "action");

    return nest(ctx, action);
  }

  function globalRead() {
    return globalThis;
  }

  let $historyUpdate = 0;

  function historyUpdate(ctx, _args) {
    const global = ctx.window();

    const h = global.history;

    const state = {
      way: true
    };

    const unused = "";

    if ($historyUpdate++ === 0) {
      h.replaceState(state, unused, global.location.href);
    }

    h.pushState(state, unused, ctx.$respUrl);
  }

  function ifElse(ctx, args) {
    const recv = checkRecv(ctx, "boolean");

    const onTrue = checkDefined(args.shift(), "onTrue");

    const onFalse = checkDefined(args.shift(), "onFalse");

    const action = recv ? onTrue : onFalse;

    return execute(ctx, action);
  }

  function invokeUnchecked(ctx, args) {
    const recv = ctx.$recv;

    return invoke0(ctx, args, recv);
  }

  function invokeVirtual(ctx, args) {
    const recv = checkRecv(ctx, args.shift());

    return invoke0(ctx, args, recv);
  }

  function invoke0(ctx, args, recv) {
    const methodName = checkString(args.shift(), "methodName");

    const method = recv[methodName];

    if (!method) {
      throw new Error(`Illegal arg: ${recv} does not declare the ${methodName} method`);
    }

    const encodedArgs = checkArray(args.shift(), "encodedArgs");

    const methodArgs = encodedArgs.map(x => execute(ctx, x));

    return method.apply(recv, methodArgs);
  }

  function jsValue(_, args) {
    return checkDefined(args.shift(), "value");
  }

  async function navigate(ctx, args, url, reqOpts) {
    // TODO validate url
    // - it must be internal

    const opts = navigateOpts(ctx, args);

    if (opts.reqHeaders) {
      if (reqOpts === undefined) {
        reqOpts = {}
      }

      if (reqOpts.headers === undefined) {
        reqOpts.headers = opts.reqHeaders;
      } else {
        const h = new Headers(reqOpts.headers);

        for (const [name, value] of opts.reqHeaders) {
          h.append(name, value);
        }

        reqOpts.headers = h;
      }
    }

    const actions = navigateActions(opts);

    const req = new Request(url, reqOpts);

    req.headers.set("Way-Request", "true");

    const resp = await globalThis.fetch(req);

    const headers = resp.headers;

    const contentType = headers.get("Content-Type");

    if (!contentType) {
      throw new Error("Invalid response: no content-type");
    }

    if (!contentType.startsWith("text/html")) {
      throw new Error(`Invalid response: unsupported content-type ${contentType}`);
    }

    ctx.$respUrl = resp.url;

    const html = await resp.text();

    const parser = new DOMParser();

    ctx.$htmlDoc = parser.parseFromString(html, "text/html");

    return execute(ctx, actions);
  }

  const $navigateOpts = {
    // history
    "HI": function(opts, args) {
      opts.history = checkBoolean(args.shift(), "value");
    },

    // request header
    "RH": function(opts, args) {
      const name = checkString(args.shift(), "name");

      const $value = checkDefined(args.shift(), "value");

      const value = checkString(execute(opts.$ctx, $value), "value");

      const h = opts.reqHeaders !== undefined
        ? opts.reqHeaders
        : opts.reqHeaders = new Headers();

      h.append(name, value);
    },

    // scroll to element
    "SE": function(opts, args) {
      opts.scrollBody = false;
      opts.scrollElem = args;
    },

    // scroll off
    "SO": function(opts) {
      opts.scrollBody = false;
      opts.scrollElem = undefined;
    },

    // updateHead
    "UH": function(opts, args) {
      opts.updateHead = checkBoolean(args.shift(), "value");
    },

    // update
    "UP": function(opts, args) {
      opts.updateBody = false;
      opts.updateElems = [...args];
    },
  };

  function navigateOpts(ctx, args) {
    // initialize with defaults
    const opts = {
      $ctx: ctx,

      history: true,

      reqHeaders: undefined,

      updateHead: true,
      updateBody: true,
      updateElems: undefined,

      scrollBody: true,
      scrollElem: undefined,
    };

    for (const arg of args) {
      const opt = checkArray(arg, "opt");

      const id = checkString(opt.shift(), "id");

      const handler = $navigateOpts[id];

      if (handler) {
        handler(opts, arg);
      } else {
        throw new Error(`Illegal arg: no handler found for option=${id}`);
      }
    }

    return opts;
  }

  function navigateActions(opts) {
    const actions = ["WS"];

    if (opts.updateHead) {
      actions.push(["UH"]);
    }

    if (opts.updateBody) {
      actions.push(["UB"]);
    }

    if (opts.updateElems) {
      actions.push(["UE", opts.updateElems]);
    }

    if (opts.scrollBody) {
      const scroll = ["W1"];
      scroll.push(["GR"]);
      scroll.push(["PR", "Window", "document"]);
      scroll.push(["PR", "Document", "documentElement"]);
      scroll.push(["IV", "Element", "scrollIntoView", []])
      actions.push(scroll);
    }

    if (opts.scrollElem) {
      const scroll = ["W1"];
      scroll.push(...opts.scrollElem);
      scroll.push(["IV", "Element", "scrollIntoView", []])
      actions.push(scroll);
    }

    if (opts.history) {
      actions.push(["HI"]);
    }

    return actions;
  }

  function noop() {}

  function popstate(ctx, args) {
    const event = ctx.$evt;

    if (!(event instanceof PopStateEvent)) {
      const actual = event.constructor ? event.constructor.name : "Unknown";

      throw new Error(`Illegal state: expected PopStateEvent but got ${actual}`);
    }

    const state = event.state;

    if (!state || !state.way) {
      ctx.$preventDefault = false;

      return;
    }

    const win = ctx.window();

    const location = win.location;

    const url = location.href;

    return navigate(ctx, [...(args ?? []), ["HI", false]], url);
  }

  function propertyRead(ctx, args) {
    const recv = checkRecv(ctx, args.shift());

    const propName = checkString(args.shift(), "propName");

    return propertyRead0(recv, propName);
  }

  function propertyReadUnchecked(ctx, args) {
    const recv = ctx.$recv;

    const propName = checkString(args.shift(), "propName");

    return propertyRead0(recv, propName);
  }

  function propertyRead0(recv, propName) {
    const prop = recv[propName];

    if (!prop) {
      throw new Error(`Illegal arg: ${recv} does not declare the ${propName} property`);
    }

    return prop;
  }

  function propertyWrite(ctx, args) {
    const recv = checkRecv(ctx, args.shift());

    const propName = checkString(args.shift(), "propName");

    const val = checkDefined(args.shift(), "value");

    recv[propName] = execute(ctx, val);
  }

  function render(ctx, args) {
    const recv = checkRecv(ctx, "Element");

    const id = recv.id;

    if (!id) {
      throw new Error(`Illegal state: ${recv} does not declare the 'id' property`);
    }

    const $url = checkDefined(args.shift(), "url")

    const url = checkString(execute(ctx, $url), "url");

    const navArgs = [...args];

    // disable history
    navArgs.push(["HI", false]);

    // disable scroll
    navArgs.push(["SO"]);

    // disable updateHead
    navArgs.push(["UH", false]);

    // update only this element
    navArgs.push(["UP", id]);

    return navigate(ctx, navArgs, url);
  }

  function submit(ctx, args) {
    const el = ctx.$el;

    if (!(el instanceof HTMLFormElement)) {
      const actual = element.constructor ? element.constructor.name : "Unknown";

      throw new Error(`Illegal state: submit must be executed on an HTMLFormElement but got ${actual}`);
    }

    const action = el.action;

    if (!action) {
      throw new Error(`Illegal state: form has no action property`);
    }

    const method = el.method;

    if (!method) {
      throw new Error(`Illegal state: form has no method property`);
    }

    let url = action;

    const req = {
      method: method.toUpperCase()
    };

    const formData = new FormData(el);

    if (req.method === "GET") {
      url = new URL(action);

      for (const [key, value] of formData.entries()) {
        url.searchParams.set(key, value);
      }
    }

    else {
      const enctype = el.getAttribute("enctype");

      if (enctype === "multipart/form-data") {
        throw new Error(`Implement me :: enctype=${enctype}`);
      }

      else {
        req.body = new URLSearchParams(formData);

        req.headers = {
          "Content-Type": "application/x-www-form-urlencoded"
        };
      }
    }

    return navigate(ctx, args, url, req);
  }

  function throwError(ctx, args) {
    const $msg = checkDefined(args.shift(), "msg");

    const msg = execute(ctx, $msg);

    checkString(msg, "msg");

    throw new Error(msg);
  }

  function typeEnsure(ctx, args) {
    const typeName = checkString(args.shift(), "typeName");

    return checkType(ctx.$recv, "recv", typeName);
  }

  function updateBody(ctx) {
    const doc = ctx.document();

    const newDoc = checkDefined(ctx.$htmlDoc, "newDoc");

    doc.body = newDoc.body;
  }

  function updateElements(ctx, args) {
    const doc = ctx.document();

    const newDoc = checkDefined(ctx.$htmlDoc, "newDoc");

    for (const id of args) {
      const el = doc.getElementById(id);

      if (!el) {
        continue;
      }

      const newEl = newDoc.getElementById(id);

      if (!newEl) {
        continue;
      }

      el.replaceWith(newEl);
    }
  }

  function updateHead(ctx) {
    const doc = ctx.document();

    const head = doc.head;

    if (!head) {
      return;
    }

    const newDoc = checkDefined(ctx.$htmlDoc, "newDoc");

    const newHead = newDoc.head;

    if (!newHead || !newHead.hasChildNodes()) {
      return;
    }

    const newEls = new Map();

    for (const newChild of newHead.children) {
      const newHtml = newChild.outerHTML;

      newEls.set(newHtml, newChild);
    }

    const remEls = [];

    for (const child of head.children) {
      const html = child.outerHTML;

      const newChild = newEls.get(html);

      if (newChild) {
        // current elem exists in new head
        // => let's keep it
        newEls.delete(html);
      } else {
        // current elem does not exist in new head
        // => let's remove it
        remEls.push(child);
      }
    }

    for (const newChild of newEls.values()) {
      head.appendChild(newChild);
    }

    for (const remChild of remEls) {
      head.removeChild(remChild);
    }
  }

  function wayOne(ctx, action) {
    checkArray(action, "action");

    const old = ctx.$recv;

    for (const op of action) {
      ctx.$recv = execute(ctx, op);
    }

    const res = ctx.$recv;

    ctx.$recv = old;

    return res;
  }

  function waySeq(ctx, actions) {
    checkArray(actions, "actions");

    const old = ctx.$recv;

    for (const action of actions) {
      execute(ctx, action);
    }

    ctx.$recv = old;
  }

  // ##################################################################
  // # END: Objectos Way Actions
  // ##################################################################

  // ##################################################################
  // # BEGIN: private private
  // ##################################################################

  function checkArray(maybe, name) {
    if (!Array.isArray(maybe)) {
      throw new Error(`Illegal arg: ${name} must be an Array value but got ${maybe}`);
    }

    return maybe;
  }

  function checkBoolean(maybe, name) {
    const t = typeof maybe;

    if (t !== "boolean") {
      throw new Error(`Illegal arg: ${name} must be a Boolean value but got ${t}`);
    }

    return maybe;
  }

  function checkDefined(maybe, name) {
    if (maybe === undefined) {
      throw new Error(`Illegal arg: ${name} must be a defined value`);
    }

    return maybe;
  }

  function checkInteger(maybe, name) {
    if (!Number.isInteger(maybe)) {
      throw new Error(`Illegal arg: ${name} must be an integer value but got ${maybe}`);
    }

    return maybe;
  }

  function checkRecv(ctx, tn) {
    const typeName = checkString(tn, "typeName");

    const recv = ctx.$recv;

    checkType(recv, "recv", typeName);

    return recv;
  }

  function checkType(maybe, name, typeName) {
    if (typeName === "boolean") {
      return checkBoolean(maybe, name);
    } else if (typeName === "string") {
      return checkString(maybe, name);
    } else {
      const t = typeof maybe;

      if (t !== "object") {
        throw new Error(`Illegal arg: ${name} must be an Object value but got ${t}`);
      }

      let prototype = Object.getPrototypeOf(maybe);

      while (prototype) {
        const constructor = prototype.constructor;

        if (!constructor) {
          throw new Error(`Illegal arg: expected ${name} to have a constructor, but found ${constructor}`);
        }

        const actualName = constructor.name;

        if (actualName === typeName) {
          return maybe;
        }

        prototype = Object.getPrototypeOf(prototype);
      }

      throw new Error(`Illegal arg: ${name} does not have ${typeName} in its prototype chain`);
    }
  }

  function checkString(maybe, name) {
    const t = typeof maybe;

    if (t !== "string") {
      throw new Error(`Illegal arg: ${name} must be a String value but got ${t}`);
    }

    return maybe;
  }

  // ##################################################################
  // # END: private private
  // ##################################################################

  return way;

})();