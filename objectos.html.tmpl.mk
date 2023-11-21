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
# objectos.html.tmpl
#

## module directory
HTML_TMPL = objectos.html.tmpl

## module
HTML_TMPL_MODULE = $(HTML_TMPL)

## module coordinates
HTML_TMPL_GROUP_ID = $(GROUP_ID)
HTML_TMPL_ARTIFACT_ID = $(HTML_TMPL)
HTML_TMPL_VERSION = $(VERSION)

## javac --release option
HTML_TMPL_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
HTML_TMPL_ENABLE_PREVIEW = 0

## marker to indicate when selfgen was last run
HTML_TMPL_SELFGEN_MARKER = $(HTML_TMPL)/work/selfgen-marker

## compilation depends on selfgen
HTML_TMPL_COMPILE_REQS_MORE = $(HTML_TMPL_SELFGEN_MARKER)

## copyright years for javadoc
HTML_TMPL_COPYRIGHT_YEARS := 2022-2023

## pom description
HTML_TMPL_DESCRIPTION = Defines the types of the Objectos HTML domain specific language

#
# eval tasks
#

HTML_TMPL_TASKS = $(filter-out $(TEST_TASKS), $(MODULE_TASKS))

$(foreach task,$(HTML_TMPL_TASKS),$(eval $(call $(task),HTML_TMPL_,html.tmpl@)))

#
# objectos.html.tmpl selfgen
#

## html selfgen java command
HTML_TMPL_SELFGEN_JAVAX = $(JAVA)
HTML_TMPL_SELFGEN_JAVAX += --module-path $(call module-path,$(SELFGEN_RUNTIME_JARS))
ifeq ($(SELFGEN_ENABLE_PREVIEW), 1)
HTML_TMPL_SELFGEN_JAVAX += --enable-preview
endif
HTML_TMPL_SELFGEN_JAVAX += --module $(SELFGEN_MODULE)/$(SELFGEN_MODULE).HtmlSpec
HTML_TMPL_SELFGEN_JAVAX += $(HTML_TMPL_MAIN)
HTML_TMPL_SELFGEN_JAVAX += api

$(HTML_TMPL_SELFGEN_MARKER): $(SELFGEN_RUNTIME_JARS)
	$(HTML_TMPL_SELFGEN_JAVAX)
	mkdir --parents $(@D)
	touch $@

#
# objectos.html.tmpl targets
#

.PHONY: html.tmpl@test-compile
html.tmpl@test-compile:

.PHONY: html.tmpl@test
html.tmpl@test:

.PHONY: html.tmpl@source-jar
html.tmpl@source-jar: $(HTML_TMPL_SOURCE_JAR_FILE)

.PHONY: html.tmpl@javadoc
html.tmpl@javadoc: $(HTML_TMPL_JAVADOC_JAR_FILE)

.PHONY: html.tmpl@pom
html.tmpl@pom: $(HTML_TMPL_POM_FILE)

.PHONY: html.tmpl@ossrh-prepare
html.tmpl@ossrh-prepare: $(HTML_TMPL_OSSRH_PREPARE)
