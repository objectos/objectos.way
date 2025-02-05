suite("Action::html test", function() {

  setup(function() {
    clearWorkArea();
    this.server = makeServer();
  });

  teardown(function() {
    this.server.restore();
  });

  test("single data-frame: same name different value", function() {
    make("<form data-frame='x:1' method='post' action='/test'><button id='b' type='submit'>Before</button></form>");
    const frame2 = makeElement("<form data-frame='x:2' method='post' action='/test'><button type='submit'>After</button></form>");

    this.server.respondWith(
      "POST",
      "/test", [
      200,
      { "Content-Type": "application/json" },
      JSON.stringify([
        ["html-0", frame2.outerHTML]
      ])
    ]);

    const btn = byId("b");

    btn.click();

    this.server.respond();

    assert.equal(workArea().innerHTML, frame2.outerHTML);

    const requests = this.server.requests;

    assert.equal(requests.length, 1);
    assert.propertyVal(requests[0].requestHeaders, "Way-Request", "true");
  });
 
  test("single data-frame: same name same value", function() {
    const frame1 = make("<form data-frame='x:1' method='post' action='/test'><button id='b' type='submit'>Before</button></form>");
    const frame2 = makeElement("<form data-frame='x:1' method='post' action='/test'><button type='submit'>Ignored</button></form>");

    this.server.respondWith(
      "POST",
      "/test", [
      200,
      { "Content-Type": "application/json" },
      JSON.stringify([
        ["html-0", frame2.outerHTML]
      ])
    ]);

    const btn = byId("b");

    btn.click();

    this.server.respond();

    assert.equal(workArea().innerHTML, frame1.outerHTML);
  });

  test("nested data-frames (1 child): morph inner only", function() {
    make("<div data-frame='root:x'>A<form data-frame='x:1' method='post' action='/test'><button id='b' type='submit'>Before</button></form></div>");
    const frame2 = makeElement("<div data-frame='root:x'>B<form data-frame='x:2' method='post' action='/test'><button type='submit'>After</button></form></div>");
    const expect = makeElement("<div data-frame='root:x'>A<form data-frame='x:2' method='post' action='/test'><button type='submit'>After</button></form></div>");

    this.server.respondWith(
      "POST",
      "/test", [
      200,
      { "Content-Type": "application/json" },
      JSON.stringify([
        ["html-0", frame2.outerHTML]
      ])
    ]);

    const btn = byId("b");

    btn.click();

    this.server.respond();

    assert.equal(workArea().innerHTML, expect.outerHTML);
  });

  test("nested data-frames (1 child): morph outer only", function() {
    make("<div data-frame='root:x'>A<form data-frame='x:1' method='post' action='/test'><button id='b' type='submit'>Before</button></form></div>");
    const frame2 = makeElement("<div data-frame='root:y'>B</div>");

    this.server.respondWith(
      "POST",
      "/test", [
      200,
      { "Content-Type": "application/json" },
      JSON.stringify([
        ["html-0", frame2.outerHTML]
      ])
    ]);

    const btn = byId("b");

    btn.click();

    this.server.respond();

    assert.equal(workArea().innerHTML, frame2.outerHTML);
  });

  test("nested data-frames (2 children): morph one child only", function() {
    make(`
    <div data-frame='root:x'>
      <div data-frame='hd:foo'>Foo</div>
    <span>should remain</span>
      <form data-frame='x:1' method='post' action='/test'>
      <button id='b' type='submit'>Before</button>
      </form>
      </div>`);
    const page2 = makeElement(`
    <div data-frame='root:x'>
      <div data-frame='hd:bar'>Bar</div>
      <form data-frame='x:1' method='post' action='/test'>
      <button id='b' type='submit'>After</button>
      </form>
      </div>`);
    const expect = makeElement(`
    <div data-frame='root:x'>
      <div data-frame='hd:bar'>Bar</div>
    <span>should remain</span>
      <form data-frame='x:1' method='post' action='/test'>
      <button id='b' type='submit'>Before</button>
      </form>
      </div>`);

    this.server.respondWith(
      "POST",
      "/test", [
      200,
      { "Content-Type": "application/json" },
      JSON.stringify([
        ["html-0", page2.outerHTML]
      ])
    ]);

    const btn = byId("b");

    btn.click();

    this.server.respond();

    assert.equal(workArea().innerHTML, expect.outerHTML);
  });

  test("nested data-frames (2 children): morph both children", function() {
    make(`
    <div data-frame='root:x'>
      <div data-frame='hd:foo'>Foo</div>
    <span>should remain</span>
      <form data-frame='x:1' method='post' action='/test'>
      <button id='b' type='submit'>Before</button>
      </form>
      </div>`);
    const page2 = makeElement(`
    <div data-frame='root:x'>
      <div data-frame='hd:bar'>Bar</div>
      <div data-frame='x:2'>From form to div</div>
      </div>`);
    const expect = makeElement(`
    <div data-frame='root:x'>
      <div data-frame='hd:bar'>Bar</div>
    <span>should remain</span>
      <div data-frame='x:2'>From form to div</div>
      </div>`);

    this.server.respondWith(
      "POST",
      "/test", [
      200,
      { "Content-Type": "application/json" },
      JSON.stringify([
        ["html-0", page2.outerHTML]
      ])
    ]);

    const btn = byId("b");

    btn.click();

    this.server.respond();

    assert.equal(workArea().innerHTML, expect.outerHTML);
  });

  test("nested data-frames (2 children): morph parent only", function() {
    make(`
    <div data-frame='root:x'>
      <div data-frame='hd:foo'>Foo</div>
    <span>should not remain</span>
      <form data-frame='x:1' method='post' action='/test'>
      <button id='b' type='submit'>Before</button>
      </form>
      </div>`);
    const page2 = makeElement(`
    <div data-frame='root:y'>
      <div data-frame='hd:foo'>Bar</div>
      <div data-frame='x:1'>From form to div</div>
      </div>`);

    this.server.respondWith(
      "POST",
      "/test", [
      200,
      { "Content-Type": "application/json" },
      JSON.stringify([
        ["html-0", page2.outerHTML]
      ])
    ]);

    const btn = byId("b");

    btn.click();

    this.server.respond();

    assert.equal(workArea().innerHTML, page2.outerHTML);
  });

  test("name-only data-frame: always update if same name", function() {
    make(`
    <div data-frame='main:first'>
      <h1>Do not change</h1>
      <div data-frame='name-only'>
      <a id='a' href='/test' data-on-click='[["navigate-0"]]'>Click me</a>
      </div>
      </div>`);
    const page2 = makeElement(`
    <div data-frame='main:first'>
      <h1>It is different but shouldn't change</h1>
      <div data-frame='name-only'>
      Success
      </div>
      </div>`);
    const expect = makeElement(`
    <div data-frame='main:first'>
      <h1>Do not change</h1>
      <div data-frame='name-only'>
      Success
      </div>
      </div>`);

    this.server.respondWith("GET", "/test", [200, { "Content-Type": "text/html" }, page2.outerHTML]);

    const a = byId("a");

    a.click();

    this.server.respond();

    assert.equal(workArea().innerHTML, expect.outerHTML);
  });

  test("name-only data-frame: do not update if names are different", function() {
    make(`
    <div data-frame='main:first'><h1>Do not change</h1><div data-frame='name-only-1'>
      <a id='a' href='/test' data-on-click='[["navigate-0"]]'>Click me</a>
      </div><div data-frame='name-only-2'>will remove...</div></div>`);
    const page2 = makeElement("<div data-frame='main:first'><h1>It is different but shouldn't change</h1><div data-frame='name-only-1'>Success</div><div data-frame='name-only-x'>Different name</div></div>");
    const expect = makeElement("<div data-frame='main:first'><h1>Do not change</h1><div data-frame='name-only-1'>Success</div></div>");

    this.server.respondWith("GET", "/test", [200, { "Content-Type": "text/html" }, page2.outerHTML]);

    const a = byId("a");

    a.click();

    this.server.respond();

    assert.equal(workArea().innerHTML, expect.outerHTML);
  });

  test("accept text/html response", function() {
    make("<form data-frame='x:1' method='post' action='/test'><button id='b' type='submit'>Before</button></form>");
    const frame2 = makeElement("<form data-frame='x:2' method='post' action='/test'><button type='submit'>After</button></form>");

    this.server.respondWith(
      "POST",
      "/test", [
      200,
      { "Content-Type": "text/html" },
      frame2.outerHTML
    ]);

    const btn = byId("b");

    btn.click();

    this.server.respond();

    assert.equal(workArea().innerHTML, frame2.outerHTML);
  });

  test("accept text/html charset response", function() {
    make("<form data-frame='x:1' method='post' action='/test'><button id='b' type='submit'>Before</button></form>");
    const frame2 = makeElement("<form data-frame='x:2' method='post' action='/test'><button type='submit'>After</button></form>");

    this.server.respondWith(
      "POST",
      "/test", [
      200,
      { "Content-Type": "text/html; charset=utf-8" },
      frame2.outerHTML
    ]);

    const btn = byId("b");

    btn.click();

    this.server.respond();

    assert.equal(workArea().innerHTML, frame2.outerHTML);
  });

  test("head/title update", function() {
    make(`
    <div data-frame='x:x'>
    <a id='a' href='/foo' data-on-click='[["navigate-0"]]'>Foo</a>
    </div>`);
    const page2 = makeElement("<html><head><title>Updated page title</title></head></html>");

    assert.notEqual(document.title, "Updated page title");

    this.server.respondWith("GET", "/foo", [200, { "Content-Type": "text/html" }, page2.outerHTML]);

    const a = byId("a");

    a.click();

    this.server.respond();

    assert.equal(document.title, "Updated page title");
  });

  test("anchor click", function() {
    make(`
    <div data-frame='x:foo'>
    <a id='a' href='/foo' data-on-click='[["navigate-0"]]'>Foo</a>
    </div>`);
    const page2 = makeElement("<html><div data-frame='x:bar'>Bar</div></html>");

    this.server.respondWith("GET", "/foo", [200, { "Content-Type": "text/html" }, page2.outerHTML]);

    const a = byId("a");

    a.click();

    this.server.respond();

    assert.equal(workArea().innerHTML, page2.outerHTML);
  });

  test("anchor click child", function() {
    make(`
    <div data-frame='x:foo'>
    <a href='/foo' data-on-click='[["navigate-0"]]'><span id='a'>Foo</span></a>
    </div>`);
    const page2 = makeElement("<html><div data-frame='x:bar'>Bar</div></html>");

    this.server.respondWith("GET", "/foo", [200, { "Content-Type": "text/html" }, page2.outerHTML]);

    const a = byId("a");

    a.click();

    this.server.respond();

    assert.equal(workArea().innerHTML, page2.outerHTML);
  });
  
});