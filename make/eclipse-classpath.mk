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
# eclipse-classpath rule
#

## Eclipse classpath file
ECLIPSE_CLASSPATH := .classpath

## Eclipse classpath template
define eclipse_classpath_tmpl
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
	<classpathentry kind="src" output="work/main" path="main"/>
	$(1)<classpathentry kind="src" output="work/test" path="test">
		<attributes>
			<attribute name="test" value="true"/>
		</attributes>
	</classpathentry>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-21">
		<attributes>
			<attribute name="module" value="true"/>
		</attributes>
	</classpathentry>
	<classpathentry kind="output" path="eclipse-bin"/>
$(2)$(3)</classpath>
endef

## Eclipse classpath entry template
define eclipse_classpath_lib
	<classpathentry kind="lib" path="$(1)"/>

endef

## Eclipse main generated sources source folder
define eclipse_main_generated_sources
<classpathentry kind="src" output="work/main" path="work/main-generated-sources"/>
	$(empty)
endef

## Eclipse modulepath entry template
define eclipse_modulepath_lib
	<classpathentry kind="lib" path="$(1)">
		<attributes>
			<attribute name="module" value="true"/>
		</attributes>
	</classpathentry>

endef

define eclipse_classpath_testlib
	<classpathentry kind="lib" path="$(1)">
		<attributes>
			<attribute name="test" value="true"/>
		</attributes>
	</classpathentry>

endef

ifdef COMPILE_SOURCE_OUTPUT
ECLIPSE_MAIN_GENERATED_SOURCES := $(eclipse_main_generated_sources)
endif

## Eclipse compile module path
ifdef ECLIPSE_COMPILE_DEPS
ECLIPSE_COMPILE_RESOLUTION_FILES := $(call to-resolution-files,$(ECLIPSE_COMPILE_DEPS))

ECLIPSE_COMPILE_PATHS := $(sort $(foreach f,$(ECLIPSE_COMPILE_RESOLUTION_FILES),$(file < $(f))))

ECLIPSE_COMPILE_LIBS = $(foreach jar,$(ECLIPSE_COMPILE_PATHS),$(call eclipse_modulepath_lib,$(jar)))
endif

## test classpath libraries
ifdef ECLIPSE_TEST_COMPILE_DEPS
ECLIPSE_TEST_COMPILE_RESOLUTION_FILES := $(call to-resolution-files,$(ECLIPSE_TEST_COMPILE_DEPS))

ECLIPSE_TEST_COMPILE_PATHS := $(sort $(foreach f,$(ECLIPSE_TEST_COMPILE_RESOLUTION_FILES),$(file < $(f))))

ECLIPSE_TEST_COMPILE_LIBS = $(foreach jar,$(ECLIPSE_TEST_COMPILE_PATHS),$(call eclipse_classpath_testlib,$(jar)))
endif

.PHONY: eclipse-classpath
eclipse-classpath:
	$(file > $(ECLIPSE_CLASSPATH),$(call eclipse_classpath_tmpl,$(ECLIPSE_MAIN_GENERATED_SOURCES),$(ECLIPSE_COMPILE_LIBS),$(ECLIPSE_TEST_COMPILE_LIBS)))
