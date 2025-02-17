/*
 * Copyright 2014-2019 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nebula.plugin.info.reporting

import nebula.plugin.info.InfoBrokerPlugin
import nebula.plugin.info.InfoReporterPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.internal.file.copy.CopySpecWrapper
import org.gradle.api.tasks.bundling.Jar

/**
 * Inject a properties file into the jar file will the info values, using the InfoPropertiesFilePlugin
 */
class InfoJarPropertiesFilePlugin implements Plugin<Project>, InfoReporterPlugin {

    void apply(Project project) {

        project.plugins.withType(InfoBrokerPlugin) { manifestPlugin ->
            InfoPropertiesFilePlugin propFilePlugin = project.plugins.apply(InfoPropertiesFilePlugin) as InfoPropertiesFilePlugin
            InfoPropertiesFile manifestTask = propFilePlugin.getManifestTask()

            project.tasks.withType(Jar) { Jar jarTask ->
                jarTask.from(manifestTask.outputs) { CopySpecWrapper copySpecWrapper ->
                    copySpecWrapper.into "META-INF"
                }
            }
        }
    }
}