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
# test compilation options
#

define TEST_COMPILE_TASK

## test source directory
$(1)TEST = $$($(1)BASEDIR)/test

## test source files 
$(1)TEST_SOURCES = $$(shell find $${$(1)TEST} -type f -name '*.java' -print)

## test class output path
$(1)TEST_CLASS_OUTPUT = $$($(1)WORK)/test

## test compile-time dependencies
# $(1)TEST_COMPILE_DEPS =

## test compile-time class-path
$(1)TEST_COMPILE_CLASS_PATH = $$($(1)WORK)/test-compile-class-path

## test javac command
$(1)TEST_JAVACX = $$(JAVAC)
$(1)TEST_JAVACX += -d $$($(1)TEST_CLASS_OUTPUT)
$(1)TEST_JAVACX += -g
$(1)TEST_JAVACX += -Xlint:all
$(1)TEST_JAVACX += --class-path @$$($(1)TEST_COMPILE_CLASS_PATH)
ifeq ($$($(1)ENABLE_PREVIEW),1)
$(1)TEST_JAVACX += --enable-preview
endif
$(1)TEST_JAVACX += --release $$($(1)JAVA_RELEASE)
$(1)TEST_JAVACX += $$($(1)TEST_SOURCES)

## test resources directory
# $(1)TEST_RESOURCES = $$($(1)BASEDIR)/test-resources

ifdef $(1)TEST_RESOURCES
## test resources "source"
$(1)TEST_RESOURCES_SRC = $$(shell find $${$(1)TEST_RESOURCES} -type f -print)

## test resources "output"
$(1)TEST_RESOURCES_OUT = $$($(1)TEST_RESOURCES_SRC:$$($(1)TEST_RESOURCES)/%=$$($(1)TEST_CLASS_OUTPUT)/%)

## target to copy test resources
$$($(1)TEST_RESOURCES_OUT): $$($(1)TEST_CLASS_OUTPUT)/%: $$($(1)TEST_RESOURCES)/%
	mkdir --parents $$(@D)
	cp $$< $$@
endif

## test compilation marker
$(1)TEST_COMPILE_MARKER = $$($(1)WORK)/test-compile-marker

## test compilation requirements
$(1)TEST_COMPILE_REQS := $$($(1)TEST_COMPILE_CLASS_PATH)
$(1)TEST_COMPILE_REQS += $$($(1)TEST_SOURCES)
$(1)TEST_COMPILE_REQS += $$($(1)TEST_RESOURCES_OUT)

#
# test compilation targets
#

.PHONY: $(2)test-compile
$(2)test-compile: $$($(1)TEST_COMPILE_MARKER)

$$($(1)TEST_COMPILE_CLASS_PATH): $$($(1)TEST_COMPILE_DEPS)
ifneq ($$($(1)TEST_COMPILE_DEPS),)
	cat $$^ | sort | uniq | paste --delimiter='$$(CLASS_PATH_SEPARATOR)' --serial > $$@
else
	touch $$@
endif

$$($(1)TEST_COMPILE_MARKER): $$($(1)TEST_COMPILE_REQS) 
	$$($(1)TEST_JAVACX)
ifneq ($$($(1)TEST_COMPILE_DEPS),)
	cat $$($(1)TEST_COMPILE_DEPS) | sort | uniq >> $$@
endif

endef
