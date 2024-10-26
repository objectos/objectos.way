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
VERSION := 0.20-SNAPSHOT
MODULE := $(ARTIFACT_ID)

## Dependencies
SELFGEN := br.com.objectos/objectos.selfgen/0.6-SNAPSHOT
NOTES := br.com.objectos/objectos.notes/0.1

H2 := com.h2database/h2/2.2.224
TESTNG := org.testng/testng/7.9.0

SLF4J_NOP := org.slf4j/slf4j-nop/1.7.36


# Delete the default suffixes
.SUFFIXES:

#
# way
#

.PHONY: all
all: test

include make/java-core.mk

#
# way@clean
#

include make/common-clean.mk

#
# way@selfgen
#

## selfgen target directory
MAIN := main

## selfgen deps
SELFGEN_DEPS := $(SELFGEN)

## selfgen module path
SELFGEN_MODULE_PATH := $(WORK)/selfgen-module-path

## selfgen marker
SELFGEN_MARKER := $(WORK)/selfgen-marker

## selfgen java command
SELFGEN_JAVAX := $(JAVA)
SELFGEN_JAVAX += --module-path @$(SELFGEN_MODULE_PATH)
SELFGEN_JAVAX += --enable-preview
SELFGEN_JAVAX += --module objectos.selfgen/objectos.selfgen.Main
SELFGEN_JAVAX += $(MAIN)

.PHONY: selfgen
selfgen: $(SELFGEN_MARKER)

.PHONY: selfgen@clean
selfgen@clean:
	rm -f $(SELFGEN_MARKER)
	
.PHONY: re-selfgen
re-selfgen: selfgen@clean selfgen 

$(SELFGEN_MODULE_PATH): $(call to-resolution-files,$(SELFGEN_DEPS))
	$(call uniq-resolution-files,$^) > $@.tmp
	cat $@.tmp | paste --delimiter='$(MODULE_PATH_SEPARATOR)' --serial > $@

$(SELFGEN_MARKER): $(SELFGEN_MODULE_PATH)
	$(SELFGEN_JAVAX)
	touch $@ 

#
# way@compile
#

## javac --release option
JAVA_RELEASE := 21

## compile deps
COMPILE_DEPS := $(NOTES)

## resources
RESOURCES := resources

include make/java-compile.mk

#
# way@test-compile
#

## test compile deps
TEST_COMPILE_DEPS := $(H2)
TEST_COMPILE_DEPS += $(TESTNG)

include make/java-test-compile.mk

#
# way@test
#

## test main class
TEST_MAIN := objectos.way.RunTests

## www test runtime dependencies
TEST_RUNTIME_DEPS := $(SLF4J_NOP)

## test modules
TEST_ADD_MODULES := org.testng
TEST_ADD_MODULES += com.h2database
TEST_ADD_MODULES += java.net.http

## test --add-exports
TEST_ADD_EXPORTS := objectos.way/objectos.util=org.testng
TEST_ADD_EXPORTS += objectos.way/objectox.lang=org.testng
TEST_ADD_EXPORTS += objectos.way/testing.site.web=org.testng

## test --add-reads
TEST_ADD_READS := objectos.way=org.testng
TEST_ADD_READS += objectos.way=com.h2database
TEST_ADD_READS += objectos.way=java.compiler
TEST_ADD_READS += objectos.way=java.net.http

include make/java-test.mk

#
# way@dev
#

## dev main class
DEV_MAIN_CLASS := testing.site.TestingSiteDev

## dev deps
DEV_DEPS := $(TEST_COMPILE_MARKER)

## dev module-path
DEV_MODULE_PATH := $(WORK)/dev-module-path

## dev java command
DEV_JAVAX = $(JAVA)
DEV_JAVAX += -Xmx64m
DEV_JAVAX += -XX:+UseSerialGC
DEV_JAVAX += --module-path @$(DEV_MODULE_PATH)
ifeq ($(ENABLE_DEBUG), 1)
DEV_JAVAX += -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=localhost:7000
endif
ifeq ($(ENABLE_PREVIEW), 1)
DEV_JAVAX += --enable-preview
endif
DEV_JAVAX += --patch-module $(MODULE)=$(TEST_CLASS_OUTPUT)
DEV_JAVAX += --add-exports $(MODULE)/objectos.lang.object=ALL-UNNAMED
DEV_JAVAX += --add-exports $(MODULE)/objectos.util.list=ALL-UNNAMED
DEV_JAVAX += --add-exports $(MODULE)/objectos.util.set=ALL-UNNAMED
DEV_JAVAX += --add-exports $(MODULE)/testing.zite=ALL-UNNAMED
DEV_JAVAX += --module $(MODULE)/$(DEV_MAIN_CLASS)
## dev app args
DEV_JAVAX += --class-output $(CLASS_OUTPUT)
DEV_JAVAX += --test-class-output $(TEST_CLASS_OUTPUT)

.PHONY: dev
dev: $(DEV_MODULE_PATH)
	$(DEV_JAVAX)
	
$(DEV_MODULE_PATH): $(DEV_DEPS)
	echo $(CLASS_OUTPUT) > $@.tmp
ifdef COMPILE_RESOLUTION_FILES
	cat $(COMPILE_RESOLUTION_FILES) >> $@.tmp
endif
ifdef TEST_COMPILE_RESOLUTION_FILES
	cat $(TEST_COMPILE_RESOLUTION_FILES) >> $@.tmp
endif
	$(call uniq-resolution-files,$@.tmp) | paste --delimiter='$(MODULE_PATH_SEPARATOR)' --serial > $@

#
# way@npm-install
#

.PHONY: npm-install
npm-install:
	npm install

#
# way@javadoc
#

JAVADOC_SNIPPET_PATH := $(TEST)

include make/java-javadoc.mk

#
# way@jar
#

include make/java-jar.mk

#
# way@pom
#

## pom.xml copyright years
COPYRIGHT_YEARS := 2022-2024

## pom.xml description
DESCRIPTION := Objectos Way allows you to build web applications using only Java. 

#
# way@install
#

include make/java-install.mk
