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
# Makefile for libraries options
#

MAKEFILE_LIBRARY_PARTS := common-tools.mk
MAKEFILE_LIBRARY_PARTS += common-deps.mk
MAKEFILE_LIBRARY_PARTS += common-jar.mk
MAKEFILE_LIBRARY_PARTS += common-test.mk
#MAKEFILE_LIBRARY_PARTS += common-install.mk
#MAKEFILE_LIBRARY_PARTS += common-source-jar.mk
#MAKEFILE_LIBRARY_PARTS += common-javadoc.mk
#MAKEFILE_LIBRARY_PARTS += common-pom.mk
#MAKEFILE_LIBRARY_PARTS += common-ossrh.mk
#MAKEFILE_LIBRARY_PARTS += common-release.mk

MAKEFILE_LIBRARY = $(foreach part, $(MAKEFILE_LIBRARY_PARTS), make/$(part))
