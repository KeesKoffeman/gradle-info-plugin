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

package nebula.plugin.info

import nebula.test.IntegrationSpec
import nebula.test.functional.ExecutionResult

class InfoBrokerPluginIntegrationSpec extends IntegrationSpec {

    def 'it returns build reports at the end of the build'() {
        given:
        def report = 'This string may only be retrieved after the build has finished'
        buildFile << """
            ${applyPlugin(InfoBrokerPlugin)}

            def broker = project.plugins.getPlugin(${InfoBrokerPlugin.name})
            
            gradle.buildFinished {
                println broker.buildReports().get('report')
            }

            task createReport {
                doLast {
                    broker.addReport('report', '$report')
                }
            }

        """.stripIndent()

        when:
        ExecutionResult result = runTasksSuccessfully('createReport')

        then:
        result.standardOutput.contains(report)
    }

}
