#
# Copyright (C) 2023 Objectos Software LTDA.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#
# objectos.html.style options
#

## module directory
HTML_STYLE = objectos.html.style

## module
HTML_STYLE_MODULE = $(HTML_STYLE)

## module version
HTML_STYLE_VERSION = $(VERSION)

## javac --release option
HTML_STYLE_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
HTML_STYLE_ENABLE_PREVIEW = 0

## compile deps
HTML_STYLE_COMPILE_DEPS  = $(call module-gav,$(HTML))
HTML_STYLE_COMPILE_DEPS += $(call module-gav,$(NOTES))
HTML_STYLE_COMPILE_DEPS += $(call module-gav,$(UTIL_MAP))
HTML_STYLE_COMPILE_DEPS += $(call module-gav,$(UTIL_SET))

## marker to indicate when selfgen was last run
HTML_STYLE_SELFGEN_MARKER = $(HTML_STYLE)/work/selfgen-marker

## compilation depends on selfgen
HTML_STYLE_COMPILE_REQS_MORE = $(HTML_STYLE_SELFGEN_MARKER)

## make selfgen a req for compilation
HTML_STYLE_RESOURCES = $(HTML_STYLE_SELFGEN_MARKER)

## test compile deps
HTML_STYLE_TEST_COMPILE_DEPS  = $(HTML_STYLE_COMPILE_DEPS)
HTML_STYLE_TEST_COMPILE_DEPS += $(call module-gav,$(HTML_STYLE))
HTML_STYLE_TEST_COMPILE_DEPS += $(call module-gav,$(NOTES_CONSOLE))
HTML_STYLE_TEST_COMPILE_DEPS += $(TESTNG)

## test runtime dependencies
HTML_STYLE_TEST_RUNTIME_DEPS  = $(HTML_STYLE_TEST_COMPILE_DEPS)
HTML_STYLE_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## test runtime modules
HTML_STYLE_TEST_JAVAX_MODULES = org.testng
HTML_STYLE_TEST_JAVAX_MODULES += objectos.html
HTML_STYLE_TEST_JAVAX_MODULES += objectos.notes.console

## way test runtime reads
HTML_STYLE_TEST_JAVAX_READS = objectos.html
HTML_STYLE_TEST_JAVAX_READS += objectos.notes.console

## test runtime exports
HTML_STYLE_TEST_JAVAX_EXPORTS = objectox.html.style

## install coordinates
HTML_STYLE_GROUP_ID = $(GROUP_ID)
HTML_STYLE_ARTIFACT_ID = $(HTML_STYLE_MODULE)

## copyright years for javadoc
HTML_STYLE_COPYRIGHT_YEARS := 2022-2023

## pom description
HTML_STYLE_DESCRIPTION = Utility-first styling for Objectos HTML templates

#
# eval tasks
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),HTML_STYLE_,html.style@)))

#
# objectos.html.style selfgen
#

## html selfgen java command
HTML_STYLE_SELFGEN_JAVAX = $(JAVA)
HTML_STYLE_SELFGEN_JAVAX += --module-path $(call module-path,$(SELFGEN_RUNTIME_JARS))
ifeq ($(SELFGEN_ENABLE_PREVIEW), 1)
HTML_STYLE_SELFGEN_JAVAX += --enable-preview
endif
HTML_STYLE_SELFGEN_JAVAX += --module $(SELFGEN_MODULE)/$(SELFGEN_MODULE).CssUtilSpec
HTML_STYLE_SELFGEN_JAVAX += $(HTML_STYLE_MAIN)

$(HTML_STYLE_SELFGEN_MARKER): $(SELFGEN_JAR_FILE)
	$(HTML_STYLE_SELFGEN_JAVAX)
	mkdir --parents $(@D)
	touch $@

#
# objectos.html.style targets
#

.PHONY: html.style@install
html.style@install: $(HTML_STYLE_INSTALL)

.PHONY: html.style@source-jar
html.style@source-jar: $(HTML_STYLE_SOURCE_JAR_FILE)

.PHONY: html.style@javadoc
html.style@javadoc: $(HTML_STYLE_JAVADOC_JAR_FILE)

.PHONY: html.style@pom
html.style@pom: $(HTML_STYLE_POM_FILE)

.PHONY: html.style@ossrh-prepare
html.style@ossrh-prepare: $(HTML_STYLE_OSSRH_PREPARE)
