#
# Copyright (C) 2022-2025 Objectos Software LTDA.
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
#VERSION := 0.3.0-SNAPSHOT
VERSION := 0.2.9-20251215
MODULE := $(ARTIFACT_ID)

## javac --release option
JAVA_RELEASE := 25

## Maven interop
REMOTE_REPOS := https://repo.maven.apache.org/maven2

## Dependencies
H2 := com.h2database/h2/2.3.232
MARIADB := org.mariadb.jdbc/mariadb-java-client/3.5.3
SLF4J_NOP := org.slf4j/slf4j-nop/2.0.17
TESTNG := org.testng/testng/7.11.0

PLAYWRIGHT := com.microsoft.playwright/playwright/1.56.0
OPENTEST4J := org.opentest4j/opentest4j/1.3.0

# Delete the default suffixes
.SUFFIXES:

#
# way
#

.PHONY: all
all: mysql7-start test test-js mysql7-stop

include make/java-core.mk

#
# way@clean
#

include make/common-clean.mk

#
# way@compile
#

## compilation requirements 
COMPILE_REQS = $(SCRIPT_GEN) $(SCRIPT2_GEN)

include make/java-compile.mk

#
# way@css-props-gen
#

css_props_json := $(WORK)/css-props.json

css_props_txt := $(WORK)/css-props.txt

.PHONY: css-props-gen
css-props-gen: $(css_props_json)
	jq -r 'to_entries | map(.key) | join("\n")' $(<) > $(css_props_txt)
	$(JAVA) CssPropsGen.java

$(css_props_json): | $(WORK)
	curl -o $(@) https://raw.githubusercontent.com/mdn/data/refs/heads/main/css/properties.json

#
# way@script-gen
#

## script-gen
SCRIPT_GEN := $(MAIN)/objectos/way/ScriptLibrary.java

## script-gen java command
SCRIPT_GEN_JAVAX := $(JAVA)
SCRIPT_GEN_JAVAX += ScriptLibraryGen.java

## when to update
SCRIPT_GEN_REQS := ScriptLibraryGen.java
SCRIPT_GEN_REQS += main-js/way.js

.PHONY: script-gen
script-gen: $(SCRIPT_GEN)

.PHONY: script-gen@clean
script-gen@clean:
	rm -f $(SCRIPT_GEN)

$(SCRIPT_GEN): $(SCRIPT_GEN_REQS)
	$(SCRIPT_GEN_JAVAX)

#
# way@script2-gen
#

## script2-gen
SCRIPT2_GEN := $(MAIN)/objectos/way/ScriptLibrary2.java

## script2-gen java command
SCRIPT2_GEN_JAVAX := $(JAVA)
SCRIPT2_GEN_JAVAX += ScriptLibraryGen2.java

## when to update
SCRIPT2_GEN_REQS := ScriptLibraryGen2.java
SCRIPT2_GEN_REQS += main-js/way2.js

.PHONY: script2-gen
script2-gen: $(SCRIPT2_GEN)

.PHONY: script2-gen@clean
script2-gen@clean:
	rm -f $(SCRIPT2_GEN)

$(SCRIPT2_GEN): $(SCRIPT2_GEN_REQS)
	$(SCRIPT2_GEN_JAVAX)

#
# way@dev
#

## dev main class
DEV_MAIN := objectos.way.dev.DevStart

## dev jvm opts
ifeq ($(ENABLE_DEBUG),1)
DEV_JVM_OPTS += -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=localhost:7000
endif

include make/java-dev.mk

#
# way@test-compile
#

## test compile deps
TEST_COMPILE_DEPS := $(H2)
TEST_COMPILE_DEPS += $(MARIADB)
TEST_COMPILE_DEPS += $(PLAYWRIGHT)
TEST_COMPILE_DEPS += $(TESTNG)

include make/java-test-compile.mk

#
# way@test
#

## test main class
TEST_MAIN := objectos.way.RunTests

## www test runtime dependencies
TEST_RUNTIME_DEPS := $(SLF4J_NOP)
TEST_RUNTIME_DEPS += $(OPENTEST4J)

## test JVM opts
TEST_JVM_OPTS := -Dplaywright.headless=true

## test modules
TEST_ADD_MODULES := org.testng
TEST_ADD_MODULES += org.slf4j
TEST_ADD_MODULES += com.h2database
TEST_ADD_MODULES += org.mariadb.jdbc
TEST_ADD_MODULES += java.net.http
TEST_ADD_MODULES += com.google.gson
TEST_ADD_MODULES += jdk.unsupported
TEST_ADD_MODULES += org.opentest4j

## test --add-exports
TEST_ADD_EXPORTS := objectos.way/objectos.util=org.testng

## test --add-reads
TEST_ADD_READS := objectos.way=org.testng
TEST_ADD_READS += objectos.way=org.slf4j
TEST_ADD_READS += objectos.way=com.h2database
TEST_ADD_READS += objectos.way=org.mariadb.jdbc
TEST_ADD_READS += objectos.way=java.compiler
TEST_ADD_READS += objectos.way=java.net.http
TEST_ADD_READS += objectos.way=playwright

include make/java-test.mk

#
# way@npm-install
#

.PHONY: npm-install
npm-install: node_modules/marker

node_modules/marker: package.json
	npm install
	touch $@
	
#
# way@test-js
#

.PHONY: test-js
test-js: node_modules/marker
	node_modules/mocha-chrome/cli.js test-js/test.html

#
# way@javadoc
#

JAVADOC_SNIPPET_PATH := $(TEST)

include make/java-javadoc.mk

#
# way@jar
#

## exclude dev
JAR_EXCLUDES := objectos/way/DevBoot.class
JAR_EXCLUDES += objectos/way/dev
JAR_EXCLUDES += objectos/way/dev/*

include make/java-jar.mk

#
# way@install
#

include make/java-install.mk

#
# way@source-jar
#

include make/java-source-jar.mk

#
# way@pom
#

## pom template
define POM_TMPL =
<?xml version="1.0" encoding="UTF-8"?>
<!--

    Copyright (C) 2022-2024 Objectos Software LTDA.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

-->
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">

	<modelVersion>4.0.0</modelVersion>
	
	<groupId>$(GROUP_ID)</groupId>
	<artifactId>$(ARTIFACT_ID)</artifactId>
	<version>$(VERSION)</version>
	<name>$(GROUP_ID):$(ARTIFACT_ID)</name>

	<description>
	Objectos Way allows you to build web applications using only Java.
	</description>

	<url>https://www.objectos.com.br/</url>

	<inceptionYear>2022</inceptionYear>

	<licenses>
		<license>
			<name>The Apache License, Version 2.0</name>
			<url>https://www.apache.org/licenses/LICENSE-2.0</url>
			<distribution>repo</distribution>
		</license>
	</licenses>

	<scm>
		<connection>scm:git:git://github.com/objectos/objectos.way.git</connection>
		<developerConnection>scm:git:ssh://git@github.com/objectos/objectos.way.git</developerConnection>
		<url>https://github.com/objectos/objectos.way</url>
		<tag>HEAD</tag>
	</scm>

	<organization>
		<name>Objectos Software LTDA</name>
		<url>https://www.objectos.com.br/</url>
	</organization>

	<developers>
		<developer>
			<id>objectos</id>
			<name>Objectos Software LTDA</name>
			<email>opensource@objectos.com.br</email>
			<organization>Objectos Software LTDA</organization>
			<organizationUrl>https://www.objectos.com.br/</organizationUrl>
		</developer>
	</developers>
	
</project>
endef

## mk-pom function
mk-pom = $(call POM_TMPL)

include make/java-pom.mk

#
# Publisher API secrets
#

## - PUBLISHER_API_GPG_KEY
## - PUBLISHER_API_GPG_PASSPHRASE
## - PUBLISHER_API_USERNAME
## - PUBLISHER_API_PASSWORD
-include $(HOME)/.config/objectos/publisher-api-config.mk

#
# way@publish
#

include make/java-publisher-api.mk

#
# GH secrets
#

## - GH_TOKEN
-include $(HOME)/.config/objectos/gh-config.mk

#
# way@gh-release
#

include make/gh-release.mk

#
# way@eclipse
#

include make/java-eclipse.mk

#
# way@mysql
#

include make/mysql7.mk

#
# www sub-project
#

## www directory
WWW := www

## www make directory
WWW_MK_DIR := $(WWW)/make

## www required makefiles
WWW_MK := $(WWW_MK_DIR)/common-clean.mk
WWW_MK += $(WWW_MK_DIR)/java-compile.mk
WWW_MK += $(WWW_MK_DIR)/java-core.mk
WWW_MK += $(WWW_MK_DIR)/java-dev.mk
WWW_MK += $(WWW_MK_DIR)/java-eclipse.mk
WWW_MK += $(WWW_MK_DIR)/java-test.mk
WWW_MK += $(WWW_MK_DIR)/java-test-compile.mk

## exported variables to WWW sub-project
export GROUP_ID
export VERSION
export TESTNG
export JAVA_RELEASE

.PHONY: www
www: $(WWW_MK)
	$(MAKE) -C $(WWW)

.PHONY: www-%
www-%: $(WWW_MK)
	$(MAKE) -C $(WWW) $*

$(WWW_MK_DIR)/%: make/% | $(WWW_MK_DIR)
	cp $< $@

$(WWW_MK_DIR):
	mkdir --parents $@