suite("Element::close test", function() {

  setup(function() {
    clearWorkArea();
    this.server = makeServer();
  });

  teardown(function() {
    this.server.restore();
  });

  test("it should close the dialog", function() {
    const onClick = JSON.stringify([
      ["id-2", "subject", "close-0"]
    ]);

    make(`
	<div>
	<dialog id='subject'>Contents</dialog>
	<button id='a' data-on-click='${onClick}'>open</button>
	</div>
	`);

    const subject = byId("subject");
	
	subject.showModal();

    byId('a').click();

    assert.isFalse(subject.open);
  });

});