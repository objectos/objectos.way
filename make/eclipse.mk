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
# Eclipse project targets
#

define ECLIPSE_CLASSPATH
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
	<classpathentry kind="src" output="work/main" path="main"/>
	<classpathentry kind="src" output="work/test" path="test">
		<attributes>
			<attribute name="test" value="true"/>
		</attributes>
	</classpathentry>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-21">
		<attributes>
			<attribute name="module" value="true"/>
		</attributes>
	</classpathentry>
	<classpathentry kind="var" path="M2_REPO/org/testng/testng/7.7.1/testng-7.7.1.jar"/>
	<classpathentry kind="var" path="M2_REPO/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar"/>
	<classpathentry kind="var" path="M2_REPO/org/slf4j/slf4j-nop/1.7.36/slf4j-nop-1.7.36.jar"/>
	<classpathentry kind="var" path="M2_REPO/com/beust/jcommander/1.82/jcommander-1.82.jar"/>
	<classpathentry kind="output" path="eclipse-bin"/>
</classpath>
endef

define ECLIPSE_GITIGNORE
/eclipse-bin/
/work/
endef

define ECLIPSE_PROJECT
<?xml version="1.0" encoding="UTF-8"?>
<projectDescription>
	<name>objectos.way:$(ECLIPSE_MODULE_NAME)</name>
	<comment></comment>
	<projects>
	</projects>
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

define ECLIPSE_SETTINGS_CORE_RESOURCES
eclipse.preferences.version=1
encoding/<project>=UTF-8
endef

define ECLIPSE_SETTINGS_JDT_CORE
eclipse.preferences.version=1
org.eclipse.jdt.core.compiler.codegen.inlineJsrBytecode=enabled
org.eclipse.jdt.core.compiler.codegen.targetPlatform=21
org.eclipse.jdt.core.compiler.codegen.unusedLocal=preserve
org.eclipse.jdt.core.compiler.compliance=21
org.eclipse.jdt.core.compiler.debug.lineNumber=generate
org.eclipse.jdt.core.compiler.debug.localVariable=generate
org.eclipse.jdt.core.compiler.debug.sourceFile=generate
org.eclipse.jdt.core.compiler.problem.assertIdentifier=error
org.eclipse.jdt.core.compiler.problem.enablePreviewFeatures=disabled
org.eclipse.jdt.core.compiler.problem.enumIdentifier=error
org.eclipse.jdt.core.compiler.problem.reportPreviewFeatures=warning
org.eclipse.jdt.core.compiler.release=enabled
org.eclipse.jdt.core.compiler.source=21
endef

define ECLIPSE_SETTINGS_JDT_UI
eclipse.preferences.version=1
org.eclipse.jdt.ui.javadoc=true
org.eclipse.jdt.ui.text.custom_code_templates=<?xml version\="1.0" encoding\="UTF-8" standalone\="no"?><templates><template autoinsert\="false" context\="gettercomment_context" deleted\="false" description\="Comment for getter function" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.gettercomment" name\="gettercomment"/><template autoinsert\="false" context\="settercomment_context" deleted\="false" description\="Comment for setter function" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.settercomment" name\="settercomment"/><template autoinsert\="false" context\="constructorcomment_context" deleted\="false" description\="Comment for created constructors" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.constructorcomment" name\="constructorcomment"/><template autoinsert\="false" context\="filecomment_context" deleted\="false" description\="Comment for created Java files" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.filecomment" name\="filecomment">/*\n * Copyright (C) 2023 Objectos Software LTDA.\n *\n * Licensed under the Apache License, Version 2.0 (the "License");\n * you may not use this file except in compliance with the License.\n * You may obtain a copy of the License at\n *\n * http\://www.apache.org/licenses/LICENSE-2.0\n *\n * Unless required by applicable law or agreed to in writing, software\n * distributed under the License is distributed on an "AS IS" BASIS,\n * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n * See the License for the specific language governing permissions and\n * limitations under the License.\n */</template><template autoinsert\="false" context\="typecomment_context" deleted\="false" description\="Comment for created types" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.typecomment" name\="typecomment"/><template autoinsert\="true" context\="fieldcomment_context" deleted\="false" description\="Comment for fields" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.fieldcomment" name\="fieldcomment">/**\n * \n */</template><template autoinsert\="false" context\="methodcomment_context" deleted\="false" description\="Comment for non-overriding function" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.methodcomment" name\="methodcomment"/><template autoinsert\="true" context\="modulecomment_context" deleted\="false" description\="Comment for modules" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.modulecomment" name\="modulecomment">/**\n * ${tags}\n */</template><template autoinsert\="false" context\="overridecomment_context" deleted\="false" description\="Comment for overriding functions" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.overridecomment" name\="overridecomment"/><template autoinsert\="false" context\="delegatecomment_context" deleted\="false" description\="Comment for delegate methods" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.delegatecomment" name\="delegatecomment"/><template autoinsert\="true" context\="newtype_context" deleted\="false" description\="Newly created files" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.newtype" name\="newtype">${filecomment}\n${package_declaration}\n\n${typecomment}\n${type_declaration}</template><template autoinsert\="true" context\="classbody_context" deleted\="false" description\="Code in new class type bodies" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.classbody" name\="classbody">\n</template><template autoinsert\="true" context\="interfacebody_context" deleted\="false" description\="Code in new interface type bodies" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.interfacebody" name\="interfacebody">\n</template><template autoinsert\="true" context\="enumbody_context" deleted\="false" description\="Code in new enum type bodies" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.enumbody" name\="enumbody">\n</template><template autoinsert\="true" context\="recordbody_context" deleted\="false" description\="Code in new record type bodies" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.recordbody" name\="recordbody">\n</template><template autoinsert\="true" context\="annotationbody_context" deleted\="false" description\="Code in new annotation type bodies" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.annotationbody" name\="annotationbody">\n</template><template autoinsert\="true" context\="catchblock_context" deleted\="false" description\="Code in new catch blocks" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.catchblock" name\="catchblock">// ${todo} Auto-generated catch block\n${exception_var}.printStackTrace();</template><template autoinsert\="false" context\="methodbody_context" deleted\="false" description\="Code in created function stubs" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.methodbody" name\="methodbody">${body_statement}</template><template autoinsert\="false" context\="constructorbody_context" deleted\="false" description\="Code in created constructor stubs" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.constructorbody" name\="constructorbody">${body_statement}</template><template autoinsert\="true" context\="getterbody_context" deleted\="false" description\="Code in created getters" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.getterbody" name\="getterbody">return ${field};</template><template autoinsert\="true" context\="setterbody_context" deleted\="false" description\="Code in created setters" enabled\="true" id\="org.eclipse.jdt.ui.text.codetemplates.setterbody" name\="setterbody">$${field} \= $${param};</template></templates>
endef

.PHONY: eclipse-gen
eclipse-gen: export CLASSPATH = $(ECLIPSE_CLASSPATH)
eclipse-gen: export GITIGNORE = $(ECLIPSE_GITIGNORE)
eclipse-gen: export PROJECT = $(ECLIPSE_PROJECT)
eclipse-gen: export SETTINGS_CORE_RESOURCES = $(ECLIPSE_SETTINGS_CORE_RESOURCES)
eclipse-gen: export SETTINGS_JDT_CORE = $(ECLIPSE_SETTINGS_JDT_CORE)
eclipse-gen: export SETTINGS_JDT_UI = $(ECLIPSE_SETTINGS_JDT_UI)
eclipse-gen:
	mkdir --parents $(ECLIPSE_MODULE_NAME)/main
	mkdir --parents $(ECLIPSE_MODULE_NAME)/test
	echo "$$CLASSPATH" > $(ECLIPSE_MODULE_NAME)/.classpath
	echo "$$GITIGNORE" > $(ECLIPSE_MODULE_NAME)/.gitignore
	echo "$$PROJECT" > $(ECLIPSE_MODULE_NAME)/.project
	mkdir --parents $(ECLIPSE_MODULE_NAME)/.settings
	echo "$$SETTINGS_CORE_RESOURCES" > $(ECLIPSE_MODULE_NAME)/.settings/org.eclipse.core.resources.prefs
	echo "$$SETTINGS_JDT_CORE" > $(ECLIPSE_MODULE_NAME)/.settings/org.eclipse.jdt.core.prefs
	echo "$$SETTINGS_JDT_UI" > $(ECLIPSE_MODULE_NAME)/.settings/org.eclipse.jdt.ui.prefs
