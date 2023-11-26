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

define COMPILE_TASK

## source directory
$(1)MAIN = $$($(1)MODULE)/main

## source files
$(1)SOURCES = $$(shell find $${$(1)MAIN} -type f -name '*.java' -print)

## source files modified since last compilation
$(1)DIRTY :=

## class output path
$(1)CLASS_OUTPUT = $$($(1)WORK)/main

## compiled classes
$(1)CLASSES = $$($(1)SOURCES:$$($(1)MAIN)/%.java=$$($(1)CLASS_OUTPUT)/%.class)

## compile-time dependencies
# $(1)COMPILE_DEPS = 

## compile-time required resolutions
$(1)COMPILE_RESOLUTIONS = $$(call to-resolutions,$$($(1)COMPILE_DEPS))

## compile-time required jars
$(1)COMPILE_JARS = $$(call to-jars,$$($(1)COMPILE_DEPS))

## compile-time module-path
$(1)COMPILE_MODULE_PATH = $$(call module-path,$$($(1)COMPILE_JARS))
 
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
$(1)JAVACX += --module-path $$($(1)COMPILE_MODULE_PATH)
endif
$(1)JAVACX += --module-version $$($(1)VERSION)
$(1)JAVACX += --release $$($(1)JAVA_RELEASE)
$(1)JAVACX += $$($(1)DIRTY)

## resources
# $(1)RESOURCES =

## compilation marker
$(1)COMPILE_MARKER = $$($(1)WORK)/compile-marker

## compilation requirements
$(1)COMPILE_REQS  = $$($(1)COMPILE_RESOLUTIONS)
$(1)COMPILE_REQS += $$($(1)CLASSES)
ifdef $(1)RESOURCES
$(1)COMPILE_REQS += $$($(1)RESOURCES)
endif
ifdef $(1)COMPILE_REQS_MORE
$(1)COMPILE_REQS += $$($(1)COMPILE_REQS_MORE)
endif

#
# compilation deps generation
#

## resolution cache file
$(1)RESOLUTION = $$(RESOLUTION_DIR)/$$($(1)GROUP_ID)/$$($(1)ARTIFACT_ID)/$$($(1)VERSION)

## resolution cache file reqs
ifndef $(1)RESOLUTION_REQS
$(1)RESOLUTION_REQS = $$($(1)MODULE)/$$($(1)MODULE).mk
endif

$(1)RESOLUTION_DEPS  = $$($(1)GROUP_ID)/$$($(1)ARTIFACT_ID)/$$($(1)VERSION)
$(1)RESOLUTION_DEPS += $$($(1)COMPILE_DEPS)

$(1)RESOLUTION_JARS  = $$(call mk-dependency,$$($(1)GROUP_ID),$$($(1)ARTIFACT_ID),$$($(1)VERSION),jar)
$(1)RESOLUTION_JARS += $$(call to-jars-paths,$$($(1)COMPILE_DEPS))

$$($(1)RESOLUTION): $$($(1)RESOLUTION_REQS)
	mkdir --parents $$(@D)
	echo "$$(sort $$($(1)RESOLUTION_JARS))" | $(TR) ' ' '\n' > $$($(1)RESOLUTION)

#
# compilation targets
#

.PHONY: $(2)compile
$(2)compile: $$($(1)COMPILE_MARKER)

.PHONY: $(2)compile-jars
$(2)compile-jars: $$($(1)COMPILE_JARS) $$($(1)RESOLUTION)

$$($(1)COMPILE_MARKER): $$($(1)COMPILE_REQS)
	if [ -n "$$($(1)DIRTY)" ]; then \
		$(MAKE) $(2)compile-jars; \
		$$($(1)JAVACX); \
	fi
	touch $$@

$$($(1)CLASSES): $$($(1)CLASS_OUTPUT)/%.class: $$($(1)MAIN)/%.java
	$$(eval $(1)DIRTY += $$$$<)

endef
