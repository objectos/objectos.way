#
# Copyright (C) 2022-2024 Objectos Software LTDA.
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

## Coordinates
GROUP_ID := br.com.objectos
ARTIFACT_ID := objectos.way
VERSION := 0.13-SNAPSHOT
MODULE := $(ARTIFACT_ID)

## Resolution dir (required)
RESOLUTION_DIR := work/resolution

## Deps versions
SELFGEN_VERSION := 0.3
NOTES_VERSION := 0.1
SLF4J_VERSION := 1.7.36
TESTNG_VERSION := 7.9.0

# Delete the default suffixes
.SUFFIXES:

#
# Default target
#

.PHONY: all
all: test

#
# way@clean
#

## basedir
BASEDIR := .

include make/tools.mk
include make/deps.mk
include make/resolver.mk
include make/clean.mk
$(eval $(call CLEAN_TASK,,))

#
# way@selfgen
#

## selfgen deps
SELFGEN_DEPS := $(RESOLUTION_DIR)/br.com.objectos/objectos.selfgen/$(SELFGEN_VERSION)

## selfgen module path
SELFGEN_MODULE_PATH := $(WORK)/selfgen-module-path

## selfgen marker
SELFGEN_MARKER := $(WORK)/selfgen-marker

## selfgen java command
SELFGEN_JAVAX  = $(JAVA)
SELFGEN_JAVAX += --module-path @$(SELFGEN_MODULE_PATH)
SELFGEN_JAVAX += --enable-preview
SELFGEN_JAVAX += --module objectos.selfgen/objectos.selfgen.Main
SELFGEN_JAVAX += $(MAIN)

.PHONY: selfgen
selfgen: $(SELFGEN_MARKER)

$(SELFGEN_MODULE_PATH): $(SELFGEN_DEPS)
	cat $^ | sort -u | paste --delimiter='$(MODULE_PATH_SEPARATOR)' --serial > $@

$(SELFGEN_MARKER): $(SELFGEN_MODULE_PATH)
	$(SELFGEN_JAVAX)
	touch $@ 

#
# way@compile
#

## javac --release option
JAVA_RELEASE = 21

## --enable-preview ?
ENABLE_PREVIEW = 0

## compile deps
COMPILE_DEPS := $(RESOLUTION_DIR)/br.com.objectos/objectos.notes/$(NOTES_VERSION)

## compilation depends on selfgen
COMPILE_REQS_MORE := $(SELFGEN_MARKER)

## resources
RESOURCES := resources

## resolution trigger
RESOLUTION_REQS := Makefile

include make/compile.mk
$(eval $(call COMPILE_TASK,,))

#
# way@test-compile
#

## test compile deps
TEST_COMPILE_DEPS := $(COMPILE_MARKER)
TEST_COMPILE_DEPS += $(RESOLUTION_DIR)/org.testng/testng/$(TESTNG_VERSION)

include make/test-compile.mk
$(eval $(call TEST_COMPILE_TASK,,))

#
# way@test
#

## www test runtime dependencies
TEST_RUNTIME_DEPS := $(TEST_COMPILE_DEPS)
TEST_RUNTIME_DEPS += $(RESOLUTION_DIR)/org.slf4j/slf4j-nop/$(SLF4J_VERSION)

## test runtime exports
TEST_JAVAX_EXPORTS := objectos.lang.object
TEST_JAVAX_EXPORTS += objectos.notes.base
TEST_JAVAX_EXPORTS += objectos.notes.console
TEST_JAVAX_EXPORTS += objectos.notes.file
TEST_JAVAX_EXPORTS += objectos.ui
TEST_JAVAX_EXPORTS += objectos.util.array
TEST_JAVAX_EXPORTS += objectos.util.collection
TEST_JAVAX_EXPORTS += objectos.util.list
TEST_JAVAX_EXPORTS += objectos.util.map
TEST_JAVAX_EXPORTS += objectos.util.set
TEST_JAVAX_EXPORTS += objectox.css
TEST_JAVAX_EXPORTS += objectox.html.style
TEST_JAVAX_EXPORTS += objectox.http
TEST_JAVAX_EXPORTS += objectox.http.server
TEST_JAVAX_EXPORTS += objectox.lang

## test runtime reads
TEST_JAVAX_READS := java.compiler

include make/test-run.mk
$(eval $(call TEST_RUN_TASK,,))

#
# way@jar
#

include make/jar.mk
$(eval $(call JAR_TASK,,))

#
# way@pom
#

## pom.xml copyright years
COPYRIGHT_YEARS := 2022-2024

## pom.xml description
DESCRIPTION := Objectos Code is a Java library for generating Java source code. 

include pom.mk
include make/pom.mk
$(eval $(call POM_TASK,,))

#
# way@install
#

include make/install.mk
$(eval $(call INSTALL_TASK,,))
