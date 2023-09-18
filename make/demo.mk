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
# demo application
#

## demo source path
DEMO_SOURCE_PATH := $(DEMO_MODULE)/main

## demo source files
DEMO_SOURCES := $(shell find ${DEMO_SOURCE_PATH} -type f -name '*.java' -print)

## demo source files modified since last compilation
DEMO_MODIFIED_SOURCES :=

## demo work dir
DEMO_WORK := $(DEMO_MODULE)/work

## demo class output path
DEMO_CLASS_OUTPUT := $(DEMO_WORK)/main

## demo compiled classes
DEMO_CLASSES := $(DEMO_SOURCES:$(DEMO_SOURCE_PATH)/%.java=$(DEMO_CLASS_OUTPUT)/%.class)

## demo compile-time dependencies
# DEMO_COMPILE_DEPS = 

## demo compile-time module-path
DEMO_COMPILE_MODULE_PATH = $(call module-path,$(DEMO_COMPILE_DEPS))
 
## demo javac command
DEMO_JAVACX = $(JAVAC)
DEMO_JAVACX += -d $(DEMO_CLASS_OUTPUT)
DEMO_JAVACX += -Xlint:all
ifdef DEMO_ENABLE_PREVIEW
DEMO_JAVACX += -Xmaxwarns 500
DEMO_JAVACX += --enable-preview
endif
DEMO_JAVACX += --module-path $(CLASS_OUTPUT):$(DEMO_COMPILE_MODULE_PATH)
DEMO_JAVACX += --module-version $(VERSION)
DEMO_JAVACX += --release $(DEMO_JAVAC_RELEASE)
DEMO_JAVACX += $(DEMO_MODIFIED_SOURCES)

## demo pseudo artifact
DEMO_ARTIFACT := $(DEMO_WORK)/artifact

## demo java command
DEMO_JAVAX = $(JAVA)
ifdef DEMO_ENABLE_PREVIEW
DEMO_JAVAX += --enable-preview
endif
DEMO_JAVAX += --module-path $(CLASS_OUTPUT):$(DEMO_CLASS_OUTPUT):$(DEMO_COMPILE_MODULE_PATH)
DEMO_JAVAX += --module $(DEMO_MODULE)/$(DEMO_MAIN)

#
# jar target
#
.PHONY: demo
demo: $(DEMO_ARTIFACT)
	$(DEMO_JAVAX)

$(DEMO_ARTIFACT): $(DEMO_COMPILE_DEPS) $(DEMO_CLASSES)
	if [ -n "$(DEMO_MODIFIED_SOURCES)" ]; then \
		$(DEMO_JAVACX); \
	fi
	touch $(DEMO_ARTIFACT)

$(DEMO_CLASSES): $(DEMO_CLASS_OUTPUT)/%.class: $(DEMO_SOURCE_PATH)/%.java
	$(eval DEMO_MODIFIED_SOURCES += $$<)
