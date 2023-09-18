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

# library.mk

LIBRARY_HEAD := library-head.mk

LIBRARY_BODY := common-tools.mk
LIBRARY_BODY += common-deps.mk
LIBRARY_BODY += common-jar.mk
LIBRARY_BODY += common-test.mk
LIBRARY_BODY += common-install.mk
LIBRARY_BODY += common-source-jar.mk
LIBRARY_BODY += common-javadoc.mk
LIBRARY_BODY += common-pom.mk
LIBRARY_BODY += common-ossrh.mk
LIBRARY_BODY += common-release.mk

LIBRARY_ARTIFACT := library.mk

# library-with-selfgen.mk

LIBRARY_SELFGEN_HEAD := library-with-selfgen-head.mk

LIBRARY_SELFGEN_BODY := $(LIBRARY_BODY)
LIBRARY_SELFGEN_BODY += selfgen.mk
LIBRARY_SELFGEN_BODY += selfgen-test.mk

LIBRARY_SELFGEN_ARTIFACT := library-with-selfgen.mk

# library-with-demo.mk 

LIBRARY_DEMO_HEAD := library-with-demo-head.mk

LIBRARY_DEMO_BODY := $(LIBRARY_BODY)
LIBRARY_DEMO_BODY += demo.mk

LIBRARY_DEMO_ARTIFACT := library-with-demo.mk

.PHONY: all
all: clean library library-demo library-selfgen

.PHONY: clean
clean:
	rm -f $(LIBRARY_ARTIFACT) $(LIBRARY_DEMO_ARTIFACT) $(LIBRARY_SELFGEN_ARTIFACT) 

.PHONY: library
library: $(LIBRARY_ARTIFACT)

$(LIBRARY_ARTIFACT): $(LIBRARY_HEAD) $(LIBRARY_BODY)
	 echo $(LIBRARY_BODY) | xargs tail -n +16 --quiet | cat $(LIBRARY_HEAD) - > $(LIBRARY_ARTIFACT)

.PHONY: library-demo
library-demo: $(LIBRARY_DEMO_ARTIFACT)

$(LIBRARY_DEMO_ARTIFACT): $(LIBRARY_DEMO_HEAD) $(LIBRARY_DEMO_BODY)
	 echo $(LIBRARY_DEMO_BODY) | xargs tail -n +16 --quiet | cat $(LIBRARY_DEMO_HEAD) - > $(LIBRARY_DEMO_ARTIFACT)
	 
.PHONY: library-selfgen
library-selfgen: $(LIBRARY_SELFGEN_ARTIFACT)

$(LIBRARY_SELFGEN_ARTIFACT): $(LIBRARY_SELFGEN_HEAD) $(LIBRARY_SELFGEN_BODY)
	 echo $(LIBRARY_SELFGEN_BODY) | xargs tail -n +16 --quiet | cat $(LIBRARY_SELFGEN_HEAD) - > $(LIBRARY_SELFGEN_ARTIFACT)
