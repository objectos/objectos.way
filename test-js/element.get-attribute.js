suite("Element::getProperty test", function() {

  setup(function() {
    clearWorkArea();
    this.server = makeServer();
  });

  teardown(function() {
    this.server.restore();
  });

  test("it should get the attribute", function() {
    const clickId = "foo";

    const onClick = JSON.stringify([
      ["request-0", "GET", ["id-1", "foo", "getAttribute", "data-href"], []]
    ]);

    make(`
    <div data-frame='x:foo'>
    <div id='${clickId}' data-on-click='${onClick}' data-href='/foo'>Foo</div>
    </div>
    `);

    const page2 = makeElement("<div data-frame='x:bar'>Bar</div>");

    this.server.respondWith("GET", "/foo", [200, { "Content-Type": "text/html" }, page2.outerHTML]);

    byId(clickId).click();

    this.server.respond();

    assert.equal(workArea().innerHTML, page2.outerHTML);
  });

});