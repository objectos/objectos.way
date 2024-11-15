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
VERSION := 0.1.8-SNAPSHOT
MODULE := $(ARTIFACT_ID)

## Dependencies
H2 := com.h2database/h2/2.2.224
SLF4J_NOP := org.slf4j/slf4j-nop/1.7.36
TESTNG := org.testng/testng/7.10.2

## Maven interop
REMOTE_REPOS := https://repo.maven.apache.org/maven2
OSSRH_SERVER := https://oss.sonatype.org

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
# way@compile
#

## javac --release option
JAVA_RELEASE := 21

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
# OSSRH secrets
#

## - OSSRH_GPG_KEY
## - OSSRH_GPG_PASSPHRASE
## - OSSRH_USERNAME
## - OSSRH_PASSWORD
-include $(HOME)/.config/objectos/ossrh-config.mk

#
# way@ossrh
#

include make/java-ossrh.mk

#
# way@ossrh-snapshots
#

include make/java-ossrh-snapshots.mk

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
