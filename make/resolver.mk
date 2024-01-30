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
# Dependencies related options & functions
#

ifndef RESOLUTION_DIR
$(error The required variable RESOLUTION_DIR was not defined)
endif

## Resolver.java path
ifndef RESOLVER_JAVA
RESOLVER_JAVA := Resolver.java
endif

## Where to find our Resolver.java source 
RESOLVER_URL := https://raw.githubusercontent.com/objectos/objectos.mk/main/resolver/src/main/java/Resolver.java

## Resolver.java deps
RESOLVER_DEPS := commons-codec/commons-codec/1.16.0
RESOLVER_DEPS += org.apache.commons/commons-lang3/3.12.0
RESOLVER_DEPS += org.apache.httpcomponents/httpclient/4.5.14
RESOLVER_DEPS += org.apache.httpcomponents/httpcore/4.4.16
RESOLVER_DEPS += org.apache.maven.resolver/maven-resolver-api/1.9.16
RESOLVER_DEPS += org.apache.maven.resolver/maven-resolver-connector-basic/1.9.16
RESOLVER_DEPS += org.apache.maven.resolver/maven-resolver-impl/1.9.16
RESOLVER_DEPS += org.apache.maven.resolver/maven-resolver-named-locks/1.9.16
RESOLVER_DEPS += org.apache.maven.resolver/maven-resolver-spi/1.9.16
RESOLVER_DEPS += org.apache.maven.resolver/maven-resolver-supplier/1.9.16
RESOLVER_DEPS += org.apache.maven.resolver/maven-resolver-transport-file/1.9.16
RESOLVER_DEPS += org.apache.maven.resolver/maven-resolver-transport-http/1.9.16
RESOLVER_DEPS += org.apache.maven.resolver/maven-resolver-util/1.9.16
RESOLVER_DEPS += org.apache.maven/maven-artifact/3.9.4
RESOLVER_DEPS += org.apache.maven/maven-builder-support/3.9.4
RESOLVER_DEPS += org.apache.maven/maven-model-builder/3.9.4
RESOLVER_DEPS += org.apache.maven/maven-model/3.9.4
RESOLVER_DEPS += org.apache.maven/maven-repository-metadata/3.9.4
RESOLVER_DEPS += org.apache.maven/maven-resolver-provider/3.9.4
RESOLVER_DEPS += org.codehaus.plexus/plexus-interpolation/1.26
RESOLVER_DEPS += org.codehaus.plexus/plexus-utils/3.5.1
RESOLVER_DEPS += org.slf4j/jcl-over-slf4j/1.7.36
RESOLVER_DEPS += org.slf4j/slf4j-api/1.7.36
RESOLVER_DEPS += org.slf4j/slf4j-nop/1.7.36

## dep-to-jar
word-solidus = $(word $(2), $(subst $(solidus),$(space),$(1)))
mk-resolved-jar = $(call mk-dependency,$(call word-solidus,$(1),1),$(call word-solidus,$(1),2),$(call word-solidus,$(1),3),jar)
dep-to-jar = $(foreach dep,$(1),$(LOCAL_REPO_PATH)/$(call mk-resolved-jar,$(dep)))

## Resolver.java jars
RESOLVER_DEPS_JARS = $(call dep-to-jar,$(RESOLVER_DEPS))

## resolve java command
RESOLVEX  = $(JAVA)
RESOLVEX += --class-path $(call class-path,$(RESOLVER_DEPS_JARS))
RESOLVEX += $(RESOLVER_JAVA)
RESOLVEX += --local-repo $(LOCAL_REPO_PATH)
RESOLVEX += --resolution-dir $(RESOLUTION_DIR)

#
# resolver rules
#

$(RESOLVER_JAVA):
	wget --no-verbose $(RESOLVER_URL) 

$(RESOLUTION_DIR)/%: $(RESOLVER_JAVA) $(RESOLVER_DEPS_JARS)
	$(RESOLVEX) $(@:$(RESOLUTION_DIR)/%=%)
