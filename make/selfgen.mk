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
# selfgen source generation
#

## selfgen source path
SELFGEN_SOURCE_PATH := $(SELFGEN_MODULE)/main

## selfgen source files
SELFGEN_SOURCES := $(shell find ${SELFGEN_SOURCE_PATH} -type f -name '*.java' -print)

## selfgen source files modified since last compilation
SELFGEN_MODIFIED_SOURCES :=

## selfgen work dir
SELFGEN_WORK := $(SELFGEN_MODULE)/work

## selfgen class output path
SELFGEN_CLASS_OUTPUT := $(SELFGEN_WORK)/main

## selfgen compiled classes
SELFGEN_CLASSES := $(SELFGEN_SOURCES:$(SELFGEN_SOURCE_PATH)/%.java=$(SELFGEN_CLASS_OUTPUT)/%.class)

## selfgen compile-time dependencies
# SELFGEN_COMPILE_DEPS = 

## selfgen compile-time module-path
SELFGEN_COMPILE_MODULE_PATH = $(call module-path,$(SELFGEN_COMPILE_DEPS))
 
## selfgen javac command
SELFGEN_JAVACX = $(JAVAC)
SELFGEN_JAVACX += -d $(SELFGEN_CLASS_OUTPUT)
SELFGEN_JAVACX += -Xlint:all
ifdef SELFGEN_ENABLE_PREVIEW
SELFGEN_JAVACX += -Xmaxwarns 500
SELFGEN_JAVACX += --enable-preview
endif
SELFGEN_JAVACX += --module-path $(SELFGEN_COMPILE_MODULE_PATH)
SELFGEN_JAVACX += --module-version $(VERSION)
SELFGEN_JAVACX += --release $(SELFGEN_JAVAC_RELEASE)
SELFGEN_JAVACX += $(SELFGEN_MODIFIED_SOURCES)

## selfgen pseudo artifact
SELFGEN_ARTIFACT := $(SELFGEN_WORK)/artifact

## selfgen java command
SELFGEN_JAVAX = $(JAVA)
ifdef SELFGEN_ENABLE_PREVIEW
SELFGEN_JAVAX += --enable-preview
endif
SELFGEN_JAVAX += --module-path $(SELFGEN_CLASS_OUTPUT)$(MODULE_PATH_SEPARATOR)$(SELFGEN_COMPILE_MODULE_PATH)
SELFGEN_JAVAX += --module $(SELFGEN_MODULE)/$(SELFGEN_MAIN)
SELFGEN_JAVAX += $(MAIN)

#
# jar target
#
.PHONY: selfgen
selfgen: $(SELFGEN_ARTIFACT)
	$(SELFGEN_JAVAX)

$(SELFGEN_ARTIFACT): $(SELFGEN_COMPILE_DEPS) $(SELFGEN_CLASSES)
	if [ -n "$(SELFGEN_MODIFIED_SOURCES)" ]; then \
		$(SELFGEN_JAVACX); \
	fi
	touch $(SELFGEN_ARTIFACT)

$(SELFGEN_CLASSES): $(SELFGEN_CLASS_OUTPUT)/%.class: $(SELFGEN_SOURCE_PATH)/%.java
	$(eval SELFGEN_MODIFIED_SOURCES += $$<)
