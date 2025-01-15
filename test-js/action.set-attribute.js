suite("Action::setProperty test", function() {

  setup(function() {
    clearWorkArea();
    this.server = makeServer();
  });

  teardown(function() {
    this.server.restore();
  });

  test("it should set the attribute", function() {
    const clickMe = "click-me";

    const action = JSON.stringify([
      ["set-attribute-0", "subject", "value", "bar"]
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

});