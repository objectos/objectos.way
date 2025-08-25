suite("Action::showModal test", function() {

  setup(function() {
    clearWorkArea();
    this.server = makeServer();
  });

  teardown(function() {
    this.server.restore();
  });

  test("it should open the modal", function() {
    const onClick = JSON.stringify([
      ["id-2", "subject", "show-modal-0"]
    ]);

    make(`
	<div>
	<dialog id='subject'>Contents</dialog>
	<button id='a' data-on-click='${onClick}'>open</button>
	</div>
	`);

    byId('a').click();

    const subject = byId("subject");

    assert.isTrue(subject.open);
	
	subject.close();
  });

});