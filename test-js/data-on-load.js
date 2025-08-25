suite("data-on-load test", function() {

  setup(function() {
    clearWorkArea();
    this.server = makeServer();
  });

  teardown(function() {
    this.server.restore();
  });

  test("it should fire after frame update", function() {
    make(`
    <div>
    <a id='a' href='/foo' data-on-click='[["navigate-0"]]'>Foo</a>
	<div data-frame='x:foo'></div>
	<div id='subject'></div>
    </div>`);

    const onLoad = JSON.stringify([
      ["id-2", "subject", "toggle-class-0", "add-me"]
    ]);

    const page2 = makeElement(`
	<html><div data-frame='x:bar' data-on-load='${onLoad}'>Bar</div></html>
    `);

    this.server.respondWith("GET", "/foo", [200, { "Content-Type": "text/html" }, page2.outerHTML]);

    const a = byId("a");

    a.click();

    this.server.respond();

    const subject = byId("subject");

    const l = subject.classList;

    assert.equal(l.length, 1);
    assert.isTrue(l.contains("add-me"));
  });

  test("it should fire only on updated frames", function() {
    make(`
    <div>
    <a id='a' href='/foo' data-on-click='[["navigate-0"]]'>Foo</a>
	<div data-frame='x:aaa'></div>
	<div data-frame='y:aaa'></div>
	<div id='subject'></div>
    </div>`);

    const onLoadX = JSON.stringify([
      ["id-2", "subject", "toggle-class-0", "new-x"]
    ]);
    const onLoadY = JSON.stringify([
      ["id-2", "subject", "toggle-class-0", "new-y"]
    ]);

    const page2 = makeElement(`
	<html>
	<div data-frame='x:ccc' data-on-load='${onLoadX}'></div>
	<div data-frame='y:aaa' data-on-load='${onLoadY}'></div>
	</html>
    `);

    this.server.respondWith("GET", "/foo", [200, { "Content-Type": "text/html" }, page2.outerHTML]);

    const a = byId("a");

    a.click();

    this.server.respond();

    const subject = byId("subject");

    const l = subject.classList;

    assert.equal(l.length, 1);
    assert.isTrue(l.contains("new-x"));
  });

  test("it should fire children elements", function() {
    make(`
    <div>
    <a id='a' href='/foo' data-on-click='[["navigate-0"]]'>Foo</a>
	<div data-frame='x:aaa'></div>
	<div id='subject'></div>
    </div>`);

    const onLoadX = JSON.stringify([
      ["id-2", "subject", "toggle-class-0", "new-x"]
    ]);
    const onLoadY = JSON.stringify([
      ["id-2", "subject", "toggle-class-0", "new-y"]
    ]);

    const page2 = makeElement(`
	<html>
	<div data-frame='x:ccc' data-on-load='${onLoadX}'>
	<div>
	<div data-frame='y:ccc' data-on-load='${onLoadY}'></div>
	</div>
	</div>
	</html>
    `);

    this.server.respondWith("GET", "/foo", [200, { "Content-Type": "text/html" }, page2.outerHTML]);

    const a = byId("a");

    a.click();

    this.server.respond();

    const subject = byId("subject");

    const l = subject.classList;

    assert.equal(l.length, 2);
    assert.isTrue(l.contains("new-x"));
    assert.isTrue(l.contains("new-y"));
  });

});