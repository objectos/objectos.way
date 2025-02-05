suite("Action::setProperty test", function() {

  setup(function() {
    clearWorkArea();
    this.server = makeServer();
  });

  teardown(function() {
    this.server.restore();
  });

  test("it should set the attribute with a string value", function() {
    const clickMe = "click-me";

    const action = JSON.stringify([
      ["id-1", "subject", "setAttribute", "value", "bar"]
    ]);

    make(`
		<div id='${clickMe}' data-on-click='${action}'>
		<input id='subject' value='foo'></div>
		</div>
		`);

    const subject = byId("subject");

    assert.equal(subject.getAttribute("value"), "foo");

    byId(clickMe).click();

    assert.equal(subject.getAttribute("value"), "bar");
  });

  test("it should set the attribute with a string query", function() {
    const clickMe = "click-me";

    const action = JSON.stringify([
      ["id-2", "subject", "attr-0", "value", ["id-1", "subject", "getAttribute", "data-item"]]
    ]);

    make(`
    <div id='${clickMe}' data-on-click='${action}'>
    <input id='subject' value='foo' data-item='bar'></div>
    </div>
    `);

    const subject = byId("subject");

    assert.equal(subject.getAttribute("value"), "foo");

    byId(clickMe).click();

    assert.equal(subject.getAttribute("value"), "bar");
  });

});