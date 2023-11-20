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
# objectos.css options
#

## module directory
CSS = objectos.css

## module
CSS_MODULE = $(CSS)

## module version
CSS_VERSION = $(VERSION)

## javac --release option
CSS_JAVA_RELEASE = $(JAVA_RELEASE)

## --enable-preview ?
CSS_ENABLE_PREVIEW = 0

## compile deps
CSS_COMPILE_DEPS  = $(call module-gav,$(UTIL_ARRAY))

## marker to indicate when selfgen was last run
CSS_SELFGEN_MARKER = $(CSS)/work/selfgen-marker

## compile reqs
CSS_COMPILE_REQS_MORE = $(CSS_SELFGEN_MARKER)

## make selfgen a req for html compilation
CSS_RESOURCES = $(CSS_SELFGEN_MARKER)

## test compile deps
CSS_TEST_COMPILE_DEPS  = $(CSS_COMPILE_DEPS)
CSS_TEST_COMPILE_DEPS += $(call module-gav,$(CSS))
CSS_TEST_COMPILE_DEPS += $(TESTNG)

## test runtime dependencies
CSS_TEST_RUNTIME_DEPS  = $(CSS_TEST_COMPILE_DEPS)
CSS_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## test runtime exports
CSS_TEST_JAVAX_EXPORTS = objectox.css

## install coordinates
CSS_GROUP_ID = $(GROUP_ID)
CSS_ARTIFACT_ID = $(CSS_MODULE)

## copyright years for javadoc
CSS_COPYRIGHT_YEARS := 2022-2023

## pom description
CSS_DESCRIPTION = Generate CSS using pure Java

#
# eval tasks
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),CSS_,css@)))

#
# objectos.css selfgen
#

## html selfgen java command
CSS_SELFGEN_JAVAX = $(JAVA)
CSS_SELFGEN_JAVAX += --module-path $(call module-path,$(SELFGEN_RUNTIME_JARS))
ifeq ($(SELFGEN_ENABLE_PREVIEW), 1)
CSS_SELFGEN_JAVAX += --enable-preview
endif
CSS_SELFGEN_JAVAX += --module $(SELFGEN_MODULE)/$(SELFGEN_MODULE).CssSpec
CSS_SELFGEN_JAVAX += $(CSS_MAIN)

$(CSS_SELFGEN_MARKER): $(SELFGEN_RUNTIME_JARS)
	$(CSS_SELFGEN_JAVAX)
	mkdir --parents $(@D)
	touch $@

#
# objectos.css targets
#

.PHONY: css@test
css@test: $(CSS_TEST_RUN_MARKER)

.PHONY: css@install
css@install: $(CSS_INSTALL)

.PHONY: css@source-jar
css@source-jar: $(CSS_SOURCE_JAR_FILE)

.PHONY: css@javadoc
css@javadoc: $(CSS_JAVADOC_JAR_FILE)

.PHONY: css@pom
css@pom: $(CSS_POM_FILE)

.PHONY: css@ossrh-prepare
css@ossrh-prepare: $(CSS_OSSRH_PREPARE)
