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
# Objectos Way
#

GROUP_ID := br.com.objectos
ARTIFACT_ID := objectos.way
VERSION := 0.3.0-SNAPSHOT

JAVA_RELEASE := 21

## Deps versions

SLF4J_VERSION := 1.7.36
TESTNG_VERSION := 7.7.1

## Deps artifacts

SLF4J_NOP := org.slf4j/slf4j-nop/$(SLF4J_VERSION)
TESTNG := org.testng/testng/$(TESTNG_VERSION)

EXTERNAL_DEPS := $(SLF4J_NOP) $(TESTNG) 

# Delete the default suffixes
.SUFFIXES:

#
# Default target
#

.PHONY: all
all: resolve-external-deps test install

## include make stuff
INCLUDES := tools.mk
INCLUDES += deps.mk
INCLUDES += resolver.mk
INCLUDES += clean.mk
INCLUDES += compile.mk
INCLUDES += jar.mk
INCLUDES += test-compile.mk
INCLUDES += test-run.mk
INCLUDES += source-jar.mk
INCLUDES += mk-pom.mk
INCLUDES += pom.mk
INCLUDES += install.mk
INCLUDES += ossrh-config.mk
INCLUDES += ossrh-prepare.mk
INCLUDES += ossrh-bundle.mk
INCLUDES += gh-config.mk
INCLUDES += eclipse.mk

include $(foreach inc,$(INCLUDES),make/$(inc))

.PHONY: jar
jar: way@jar

print-%::
	@echo $* = $($*)

#
# Dependency mgmt
#

.PHONY: resolve-external-deps
resolve-external-deps: export deps = $(EXTERNAL_DEPS) 
resolve-external-deps: $(RESOLVER_JAVA) $(RESOLVER_DEPS_JARS)
	for dep in $${deps}; do $(RESOLVEX) $${dep}; done

#
# objectos.way modules section
#

## all of the modules
MODULES := objectos.lang.object
MODULES += objectos.notes
MODULES += objectos.notes.base
MODULES += objectos.notes.console
MODULES += objectos.notes.file
MODULES += objectos.util.array
MODULES += objectos.util.collection
MODULES += objectos.util.list
MODULES += objectos.util.set
MODULES += objectos.util.map
MODULES += objectos.core.service
MODULES += objectos.core.io
MODULES += objectos.core.testing
MODULES += objectos.fs
MODULES += objectos.fs.testing
MODULES += objectos.fs.zip
MODULES += objectos.concurrent
MODULES += objectos.git
MODULES += objectos.mysql
MODULES += objectos.code
MODULES += objectos.selfgen
MODULES += objectos.html.tmpl
MODULES += objectos.html
MODULES += objectos.html.icon
MODULES += objectos.html.script
MODULES += objectos.html.style
MODULES += objectos.css
MODULES += objectos.http
MODULES += objectos.http.server
MODULES += objectos.http.session
MODULES += objectos.lang.classloader
MODULES += objectos.lang.runtime

## common module tasks
MODULE_TASKS  = CLEAN_TASK
MODULE_TASKS += COMPILE_TASK
MODULE_TASKS += JAR_TASK
MODULE_TASKS += TEST_COMPILE_TASK
MODULE_TASKS += TEST_RUN_TASK
MODULE_TASKS += SOURCE_JAR_TASK
MODULE_TASKS += JAVADOC_TASK
MODULE_TASKS += POM_TASK
MODULE_TASKS += INSTALL_TASK
MODULE_TASKS += OSSRH_PREPARE_TASK

## test-related tasks
TEST_TASKS  = TEST_COMPILE_TASK
TEST_TASKS += TEST_RUN_TASK

## @ names
AT_MODULES := $(foreach mod,$(MODULES),$(subst objectos.,,$(mod)))
AT_MODULES += way

## generate module gav
module-gav = $(GROUP_ID)/$(1)/$(VERSION)

## generate common module values
LOWER_MODULES := $(foreach mod,$(AT_MODULES),$(subst .,_,$(mod)_))
UPPER_MODULES := $(shell echo $(LOWER_MODULES) | tr a-z A-Z)

## include each modules's makefile

include $(foreach mod,$(MODULES),$(mod)/$(mod).mk)

#
# objectos.way options
# 

## way directory
WAY := objectos.way

## way module
WAY_MODULE := $(WAY)

## way module version
WAY_GROUP_ID = $(GROUP_ID)
WAY_ARTIFACT_ID = $(ARTIFACT_ID)
WAY_VERSION = $(VERSION)

## way javac --release option
WAY_JAVA_RELEASE := 21

## way --enable-preview ?
WAY_ENABLE_PREVIEW := 0

## way compile deps
WAY_COMPILE_DEPS  = $(call module-gav,$(CSS))
WAY_COMPILE_DEPS += $(call module-gav,$(HTML))
WAY_COMPILE_DEPS += $(call module-gav,$(NOTES))
WAY_COMPILE_DEPS += $(call module-gav,$(UTIL_LIST))
WAY_COMPILE_DEPS += $(call module-gav,$(UTIL_SET))

## way resolution reqs
WAY_RESOLUTION_REQS = Makefile

## way test compile-time dependencies
WAY_TEST_COMPILE_DEPS  = $(WAY_COMPILE_DEPS)
WAY_TEST_COMPILE_DEPS += $(call module-gav,$(WAY))
WAY_TEST_COMPILE_DEPS += $(call module-gav,$(NOTES_CONSOLE))
WAY_TEST_COMPILE_DEPS += $(TESTNG)

## way test runtime dependencies
WAY_TEST_RUNTIME_DEPS  = $(WAY_TEST_COMPILE_DEPS)
WAY_TEST_RUNTIME_DEPS += $(SLF4J_NOP)

## way test runtime modules
WAY_TEST_JAVAX_MODULES = org.testng
WAY_TEST_JAVAX_MODULES += objectos.notes.console

## way test runtime reads
WAY_TEST_JAVAX_READS = java.compiler
WAY_TEST_JAVAX_READS += objectos.notes.console

## way test runtime exports
WAY_TEST_JAVAX_EXPORTS = objectox.css
WAY_TEST_JAVAX_EXPORTS += objectox.css.util
WAY_TEST_JAVAX_EXPORTS += objectox.http
WAY_TEST_JAVAX_EXPORTS += objectox.lang

## way copyright years for javadoc
WAY_COPYRIGHT_YEARS := 2022-2023

## way javadoc snippet path
WAY_JAVADOC_SNIPPET_PATH := WAY_TEST

## way sub modules
WAY_SUBMODULES = lang.object
WAY_SUBMODULES += notes
WAY_SUBMODULES += notes.base
WAY_SUBMODULES += notes.console
WAY_SUBMODULES += notes.file
WAY_SUBMODULES += util.array
WAY_SUBMODULES += util.collection
WAY_SUBMODULES += util.list
WAY_SUBMODULES += util.set
WAY_SUBMODULES += util.map
WAY_SUBMODULES += html.tmpl
WAY_SUBMODULES += html
WAY_SUBMODULES += html.icon
WAY_SUBMODULES += html.script
WAY_SUBMODULES += html.style
WAY_SUBMODULES += css
WAY_SUBMODULES += http
WAY_SUBMODULES += http.server
WAY_SUBMODULES += lang.classloader
WAY_SUBMODULES += lang.runtime

## way bundle contents
WAY_OSSRH_BUNDLE_CONTENTS = $(LANG_OBJECT_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(NOTES_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(NOTES_BASE_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(NOTES_CONSOLE_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(NOTES_FILE_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(UTIL_ARRAY_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(UTIL_COLLECTION_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(UTIL_LIST_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(UTIL_SET_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(UTIL_MAP_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(HTML_TMPL_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(HTML_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(HTML_ICON_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(HTML_SCRIPT_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(HTML_STYLE_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(CSS_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(HTTP_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(HTTP_SERVER_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(LANG_CLASSLOADER_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(LANG_RUNTIME_OSSRH_PREPARE)
WAY_OSSRH_BUNDLE_CONTENTS += $(WAY_OSSRH_PREPARE)

#
# objectos.way targets
#

$(foreach task,$(MODULE_TASKS),$(eval $(call $(task),WAY_,way@)))
$(eval $(call OSSRH_BUNDLE_TASK,WAY_))

#
# Targets section
#

.PHONY: clean
clean: $(foreach mod,$(AT_MODULES),$(mod)@clean)

.PHONY: clean-install
clean-install: $(foreach mod,$(AT_MODULES),$(foreach t,clean-install clean-install-pom,$(mod)@$(t)))

.PHONY: compile
compile: $(foreach mod,$(AT_MODULES),$(mod)@compile)

.PHONY: jar
jar: $(foreach mod,$(AT_MODULES),$(mod)@jar)

.PHONY: test-compile
test-compile: $(foreach mod,$(AT_MODULES),$(mod)@test-compile)

.PHONY: test
test: $(foreach mod,$(AT_MODULES),$(mod)@test)

.PHONY: install
install: $(foreach mod,$(AT_MODULES),$(mod)@install)

.PHONY: source-jar
source-jar: $(foreach mod,$(WAY_SUBMODULES),$(mod)@source-jar) way@source-jar 

.PHONY: javadoc
javadoc: $(foreach mod,$(WAY_SUBMODULES),$(mod)@javadoc) way@javadoc 

.PHONY: pom
pom: $(foreach mod,$(AT_MODULES),$(mod)@pom) 

.PHONY: ossrh-prepare
ossrh-prepare: $(foreach mod,$(WAY_SUBMODULES),$(mod)@ossrh-prepare) way@ossrh-prepare

.PHONY: ossrh-bundle
ossrh-bundle: way@ossrh-bundle

.PHONY: ossrh
ossrh: way@ossrh

.PHONY: gh-release
gh-release: way@gh-release

.PHONY: way@source-jar
way@source-jar: $(WAY_SOURCE_JAR_FILE)

.PHONY: way@javadoc way@clean-javadoc
way@javadoc: $(WAY_JAVADOC_JAR_FILE)

way@clean-javadoc:
	rm -r $(WAY_JAVADOC_OUTPUT)

.PHONY: way@pom
way@pom: $(WAY_POM_FILE)

.PHONY: way@ossrh-prepare
way@ossrh-prepare: $(WAY_OSSRH_PREPARE)

.PHONY: way@ossrh way@ossrh-bundle
way@ossrh: $(WAY_OSSRH_MARKER)

way@ossrh-bundle: $(WAY_OSSRH_BUNDLE)

.PHONY: way@gh-release way@gh-release-body
way@gh-release: $(WAY_GH_RELEASE_MARKER)

way@gh-release-body: $(WAY_GH_RELEASE_BODY)
