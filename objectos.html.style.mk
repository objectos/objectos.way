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
HTML_STYLE_COMPILE_DEPS = $(LANG_OBJECT_JAR_FILE)
HTML_STYLE_COMPILE_DEPS += $(NOTES_JAR_FILE)
HTML_STYLE_COMPILE_DEPS += $(UTIL_ARRAY_JAR_FILE)
HTML_STYLE_COMPILE_DEPS += $(UTIL_COLLECTION_JAR_FILE)
HTML_STYLE_COMPILE_DEPS += $(UTIL_MAP_JAR_FILE)
HTML_STYLE_COMPILE_DEPS += $(UTIL_SET_JAR_FILE)
HTML_STYLE_COMPILE_DEPS += $(HTML_TMPL_JAR_FILE)

## marker to indicate when selfgen was last run
HTML_STYLE_SELFGEN_MARKER = $(HTML_STYLE)/work/selfgen-marker

## make selfgen a req for compilation
HTML_STYLE_RESOURCES = $(HTML_STYLE_SELFGEN_MARKER)

## jar name
HTML_STYLE_JAR_NAME = $(HTML_STYLE)

## test compile deps
HTML_STYLE_TEST_COMPILE_DEPS = $(HTML_STYLE_COMPILE_DEPS)
HTML_STYLE_TEST_COMPILE_DEPS += $(HTML_JAR_FILE)
HTML_STYLE_TEST_COMPILE_DEPS += $(NOTES_BASE_JAR_FILE)
HTML_STYLE_TEST_COMPILE_DEPS += $(NOTES_CONSOLE_JAR_FILE)
HTML_STYLE_TEST_COMPILE_DEPS += $(HTML_STYLE_JAR_FILE)
HTML_STYLE_TEST_COMPILE_DEPS += $(call dependency,org.testng,testng,$(TESTNG_VERSION))

## test runtime dependencies
HTML_STYLE_TEST_RUNTIME_DEPS = $(HTML_STYLE_TEST_COMPILE_DEPS)
HTML_STYLE_TEST_RUNTIME_DEPS += $(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
HTML_STYLE_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
HTML_STYLE_TEST_RUNTIME_DEPS += $(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

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

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),HTML_STYLE_)))

#
# objectos.html.style selfgen
#

## html selfgen java command
HTML_STYLE_SELFGEN_JAVAX = $(JAVA)
HTML_STYLE_SELFGEN_JAVAX += --module-path $(call module-path,$(SELFGEN_RUNTIME_DEPS))
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

.PHONY: html.style@clean
html.style@clean:
	rm -rf $(HTML_STYLE_WORK)/*

.PHONY: html.style@compile
html.style@compile: $(HTML_STYLE_SELFGEN_MARKER) $(HTML_STYLE_COMPILE_MARKER)

.PHONY: html.style@jar
html.style@jar: $(HTML_STYLE_JAR_FILE)

.PHONY: html.style@test
html.style@test: $(HTML_STYLE_TEST_RUN_MARKER)

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