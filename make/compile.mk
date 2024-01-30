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
# compilation options
#

ifndef RESOLUTION_DIR
$(error The required variable RESOLUTION_DIR was not defined)
endif

define COMPILE_TASK

## source directory
$(1)MAIN = $$($(1)BASEDIR)/main

## source files
$(1)SOURCES = $$(shell find $${$(1)MAIN} -type f -name '*.java' -print)

## class output path
$(1)CLASS_OUTPUT = $$($(1)WORK)/main

## compile-time dependencies
# $(1)COMPILE_DEPS = 

## compile-time module-path
$(1)COMPILE_MODULE_PATH = $$($(1)WORK)/compile-module-path

## javac command
$(1)JAVACX = $$(JAVAC)
$(1)JAVACX += -d $$($(1)CLASS_OUTPUT)
$(1)JAVACX += -g
$(1)JAVACX += -Xlint:all
$(1)JAVACX += -Xpkginfo:always
ifeq ($$($(1)ENABLE_PREVIEW),1)
$(1)JAVACX += --enable-preview
endif
ifneq ($$($(1)COMPILE_DEPS),)
$(1)JAVACX += --module-path @$$($(1)COMPILE_MODULE_PATH)
endif
$(1)JAVACX += --module-version $$($(1)VERSION)
$(1)JAVACX += --release $$($(1)JAVA_RELEASE)
$(1)JAVACX += $$($(1)SOURCES)

## resources
# $(1)RESOURCES =

ifdef $(1)RESOURCES
## test resources "source"
$(1)RESOURCES_SRC = $$(shell find $${$(1)RESOURCES} -type f -print)

## test resources "output"
$(1)RESOURCES_OUT = $$($(1)RESOURCES_SRC:$$($(1)RESOURCES)/%=$$($(1)CLASS_OUTPUT)/%)

## target to copy test resources
$$($(1)RESOURCES_OUT): $$($(1)CLASS_OUTPUT)/%: $$($(1)RESOURCES)/%
	mkdir --parents $$(@D)
	cp $$< $$@
endif

## compilation marker
$(1)COMPILE_MARKER = $$(RESOLUTION_DIR)/$$($(1)GROUP_ID)/$$($(1)ARTIFACT_ID)/$$($(1)VERSION)

## compilation requirements
$(1)COMPILE_REQS  = $$($(1)COMPILE_MODULE_PATH)
$(1)COMPILE_REQS += $$($(1)SOURCES)
ifdef $(1)RESOURCES
$(1)COMPILE_REQS += $$($(1)RESOURCES_OUT)
endif
ifdef $(1)COMPILE_REQS_MORE
$(1)COMPILE_REQS += $$($(1)COMPILE_REQS_MORE)
endif

#
# compilation targets
#

.PHONY: $(2)compile
$(2)compile: $$($(1)COMPILE_MARKER)

$$($(1)COMPILE_MODULE_PATH): $$($(1)COMPILE_DEPS) | $$($(1)WORK)
ifneq ($$($(1)COMPILE_DEPS),)
	cat $$^ | sort -u | paste --delimiter='$$(MODULE_PATH_SEPARATOR)' --serial > $$@
else
	touch $$@
endif

$$($(1)COMPILE_MARKER): $$($(1)COMPILE_REQS)
	$$($(1)JAVACX)
	mkdir --parents $$(@D)
	echo "$$($(1)CLASS_OUTPUT)" > $$@
ifneq ($$($(1)COMPILE_DEPS),)
	cat $$($(1)COMPILE_DEPS) | sort -u >> $$@
endif

endef
