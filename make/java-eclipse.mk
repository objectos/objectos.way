#
# Copyright (C) 2023-2024 Objectos Software LTDA.
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
# Allow importing a Java project in Eclipse IDE
#

ifndef COMPILE_MARKER
$(error Required java-compile.mk was not included)
endif

ifndef TEST_COMPILE_MARKER
$(error Required java-test-compile.mk was not included)
endif

## Generate classpath file at the root
## - always generate the file
ECLIPSE_CLASSPATH := .classpath
.PHONY: $(ECLIPSE_CLASSPATH)

## Eclipse main generated sources source folder
ifdef PROCESSING_OUTPUT
define ECLIPSE_CLASSPATH_PROCESSING_OUTPUT =
	<classpathentry kind="src" output="$(MAIN)" path="$(PROCESSING_OUTPUT)"/>

endef
endif

## compile deps
ifdef COMPILE_RESOLUTION_FILES
ECLIPSE_CLASSPATH_COMPILE := $(sort $(foreach f,$(COMPILE_RESOLUTION_FILES),$(file < $(f))))
endif

## test-compile deps
ifdef TEST_COMPILE_RESOLUTION_FILES
ECLIPSE_CLASSPATH_TEST_COMPILE := $(sort $(foreach f,$(TEST_COMPILE_RESOLUTION_FILES),$(file < $(f))))

ifdef ECLIPSE_CLASSPATH_COMPILE
ECLIPSE_CLASSPATH_TEST_COMPILE := $(filter-out $(ECLIPSE_CLASSPATH_COMPILE),$(ECLIPSE_CLASSPATH_TEST_COMPILE))
endif
endif

## generate deps contents
ECLIPSE_CLASSPATH_DEPS :=

## Eclipse modulepath entry template
define ECLIPSE_CLASSPATH_COMPILE_ENTRY =
	<classpathentry kind="lib" path="$(1)">
		<attributes>
			<attribute name="module" value="true"/>
		</attributes>
	</classpathentry>

endef

ifdef ECLIPSE_CLASSPATH_COMPILE
ECLIPSE_CLASSPATH_DEPS += $(foreach jar,$(ECLIPSE_CLASSPATH_COMPILE),$(call ECLIPSE_CLASSPATH_COMPILE_ENTRY,$(jar)))
endif

## Eclipse test entry template
define ECLIPSE_CLASSPATH_TEST_ENTRY =
	<classpathentry kind="lib" path="$(1)">
		<attributes>
			<attribute name="test" value="true"/>
		</attributes>
	</classpathentry>

endef

ifdef ECLIPSE_CLASSPATH_TEST_COMPILE
ECLIPSE_CLASSPATH_DEPS += $(foreach jar,$(ECLIPSE_CLASSPATH_TEST_COMPILE),$(call ECLIPSE_CLASSPATH_TEST_ENTRY,$(jar)))
endif

## Eclipse classpath template
define ECLIPSE_CLASSPATH_CONTENTS :=
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
	<classpathentry kind="src" output="$(CLASS_OUTPUT)" path="$(MAIN)"/>
$(ECLIPSE_CLASSPATH_PROCESSING_OUTPUT)	<classpathentry kind="src" output="$(TEST_CLASS_OUTPUT)" path="$(TEST)">
		<attributes>
			<attribute name="test" value="true"/>
		</attributes>
	</classpathentry>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER">
		<attributes>
			<attribute name="module" value="true"/>
		</attributes>
	</classpathentry>
	<classpathentry kind="output" path="eclipse-bin"/>
$(ECLIPSE_CLASSPATH_DEPS)</classpath>
endef

## force dep resolving
ECLIPSE_CLASSPATH_REQS := $(COMPILE_RESOLUTION_FILES)
ECLIPSE_CLASSPATH_REQS += $(TEST_COMPILE_RESOLUTION_FILES)

## Generate project file at the root
ECLIPSE_PROJECT := .project
.PHONY: $(ECLIPSE_PROJECT)

## Eclipse project name
ifndef ECLIPSE_PROJECT_NAME
ECLIPSE_PROJECT_NAME := $(ARTIFACT_ID)
endif

define ECLIPSE_PROJECT_CONTENTS :=
<?xml version="1.0" encoding="UTF-8"?>
<projectDescription>
	<name>$(ECLIPSE_PROJECT_NAME)</name>
	<comment></comment>
	<buildSpec>
		<buildCommand>
			<name>org.eclipse.jdt.core.javabuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
	</buildSpec>
	<natures>
		<nature>org.eclipse.jdt.core.javanature</nature>
	</natures>
</projectDescription>
endef

.PHONY: eclipse
eclipse: $(ECLIPSE_CLASSPATH) $(ECLIPSE_PROJECT)

$(ECLIPSE_CLASSPATH): $(ECLIPSE_CLASSPATH_REQS)
	$(file > $@,$(ECLIPSE_CLASSPATH_CONTENTS))

$(ECLIPSE_PROJECT):
	$(file > $@,$(ECLIPSE_PROJECT_CONTENTS))
