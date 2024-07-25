suite("Action::setProperty test", function() {

	setup(function() {
		clearWorkArea();
		this.server = makeServer();
	});

	teardown(function() {
		this.server.restore();
	});

	test("it should set the property", function() {
		const clickId = "f";
		const onClick = JSON.stringify([{
			cmd: "set-property",
			args: ["subject", "background-color", "teal"]
		}]);
		make(`
		<div id='${clickId}' data-on-click='${onClick}'>
		<div id='subject'></div>
		</div>		
		`);

		const subject = byId("subject");

		assert.equal(subject.style.getPropertyValue("background-color"), "");

		byId(clickId).click();

		assert.equal(subject.style.getPropertyValue("background-color"), "teal");
	});

});