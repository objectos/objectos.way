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

# Objectos

VERSION := 0.9.0-SNAPSHOT

## Deps versions

TESTNG_VERSION := 7.7.1
JCOMMANDER_VERSION := 1.82
SLF4J_VERSION := 1.7.36

## Compile options

JAVA_RELEASE = 21

ENABLE_PREVIEW = yes

#
# Makefile for libraries options
#

LIBRARY_HEAD := library-head.mk

MAKEFILE_LIBRARY_PARTS := common-tools.mk
MAKEFILE_LIBRARY_PARTS += common-deps.mk
MAKEFILE_LIBRARY_PARTS += common-jar.mk
#LIBRARY_BODY += common-test.mk
#LIBRARY_BODY += common-install.mk
#LIBRARY_BODY += common-source-jar.mk
#LIBRARY_BODY += common-javadoc.mk
#LIBRARY_BODY += common-pom.mk
#LIBRARY_BODY += common-ossrh.mk
#LIBRARY_BODY += common-release.mk

MAKEFILE_LIBRARY = $(foreach part, $(MAKEFILE_LIBRARY_PARTS), make/$(part))

#
# Objectos Lang
#

## Objectos Lang module name
OBJECTOS_LANG = objectos.lang

## Objectos Lang Makefile
OBJECTOS_LANG_MAKEFILE = $(OBJECTOS_LANG)/Makefile

## Objectos Lang Makefile header : start
## -------------------------------------
define OBJECTOS_LANG_MAKEFILE_HEADER :=
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

# Objectos Lang

MODULE = $(OBJECTOS_LANG)
VERSION = $(VERSION)

# Compile Options

JAVA_RELEASE = $(JAVA_RELEASE)

COMPILE_DEPS =

## Test options

TEST_COMPILE_DEPS = $$(call dependency,org.testng,testng,$(TESTNG_VERSION))

TEST_RUNTIME_DEPS = $$(TEST_COMPILE_DEPS)
TEST_RUNTIME_DEPS += $$(call dependency,com.beust,jcommander,$(JCOMMANDER_VERSION))
TEST_RUNTIME_DEPS += $$(call dependency,org.slf4j,slf4j-api,$(SLF4J_VERSION))
TEST_RUNTIME_DEPS += $$(call dependency,org.slf4j,slf4j-nop,$(SLF4J_VERSION))

## POM generation options

POM_VARIABLES = DESCRIPTION
endef
## -------------------------------------
## Objectos Lang Makefile header : end

objectos.lang@jar: $(OBJECTOS_LANG_MAKEFILE)
	$(MAKE) -C $(OBJECTOS_LANG) jar

$(OBJECTOS_LANG_MAKEFILE): export HEADER := $(OBJECTOS_LANG_MAKEFILE_HEADER)
$(OBJECTOS_LANG_MAKEFILE): $(MAKEFILE_LIBRARY) Makefile
	@echo "$$HEADER" > $@
	@echo $(MAKEFILE_LIBRARY) | xargs tail -n +16 --quiet | cat - >> $@