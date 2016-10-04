/*
 * CredentialsPlugin.groovy
 *
 * Copyright (c) 2016 Auth0 (http://auth0.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.auth0.android.gradle.credentials

import com.android.build.gradle.AppPlugin
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class CredentialsPlugin implements Plugin<Project> {

    private static final String RES_CLIENT_ID_KEY = "com_auth0_client_id"
    private static final String RES_DOMAIN_KEY = "com_auth0_domain"
    private static final String LOCAL_PROPERTIES_CLIENT_ID_KEY = "auth0.clientId"
    private static final String LOCAL_PROPERTIES_DOMAIN_KEY = "auth0.domain"

    @Override
    void apply(Project project) {
        if (!project.plugins.findPlugin(AppPlugin)) {
            throw new GradleException(
                    'You must apply the Android Application plugin before using the \'com.auth0.android.gradle-credentials\' plugin')
        }

        project.extensions.add 'auth0', CredentialsExtension

        project.afterEvaluate {

            project.android.applicationVariants.all { variant ->

                def variantName = variant.name.capitalize()
                def generateCredentialsTask = project.tasks.create(
                        [name       : "generate${variantName}Auth0Credentials",
                         description: "Generates an auth0.xml resource file with credentials found in the build.gradle:auth0 closure or the local.properties file",
                         type       : CredentialsTask], {
                    variantDir = variant.dirName
                    if (!project.auth0) {
                        println "Auth0 extension doesn't exists in the build.gradle file. Creating a default one.."
                        project.auth0 = new CredentialsExtension();
                    }
                    def localPropertiesPath = project.rootDir.absolutePath

                    if (hasLocalProperties(localPropertiesPath)) {
                        println "Auth0Credentials: Credentials found in the Project's local.properties file. Overriding build.gradle:auth0 settings.."
                        auth0 = parseLocalProperties(localPropertiesPath)
                    } else {
                        println "Auth0Credentials: Searching in the Application's build.gradle:auth0 closure.."
                        auth0 = parseCredentials(project.auth0)
                    }

                    println "Auth0Credentials: Using ClientID=${auth0.get(RES_CLIENT_ID_KEY)} and Domain=${auth0.get(RES_DOMAIN_KEY)}"
                })

                def processResourcesTask = project.tasks.find {
                    def pattern = ~/process${variantName}Resources$/
                    pattern.matcher(it.name).matches()
                }

                processResourcesTask.dependsOn generateCredentialsTask
            }
        }
    }

    static def hasLocalProperties(String filePath) {
        Properties properties = new Properties()
        properties.load(new File("${filePath}/local.properties").newDataInputStream())
        String clientId = properties.getProperty(LOCAL_PROPERTIES_CLIENT_ID_KEY)
        String domain = properties.getProperty(LOCAL_PROPERTIES_DOMAIN_KEY)

        return isValidString(clientId) || isValidString(domain)
    }

    static def parseLocalProperties(String filePath) {
        Properties properties = new Properties()
        properties.load(new File("${filePath}/local.properties").newDataInputStream())
        String clientId = properties.getProperty(LOCAL_PROPERTIES_CLIENT_ID_KEY)
        String domain = properties.getProperty(LOCAL_PROPERTIES_DOMAIN_KEY)

        Map<String, Object> auth0 = new HashMap<>()
        if (isValidString(clientId) && !isValidString(domain)) {
            throw new GradleException('Missing \'' + LOCAL_PROPERTIES_DOMAIN_KEY + '\' value in the Project\'s local.properties file');
        }
        if (!isValidString(clientId) && isValidString(domain)) {
            throw new GradleException('Missing \'' + LOCAL_PROPERTIES_CLIENT_ID_KEY + '\' value in the Project\'s local.properties file');
        }
        println "Auth0Credentials: About to parse local.properties file"


        auth0.put(RES_CLIENT_ID_KEY, clientId)
        auth0.put(RES_DOMAIN_KEY, domain)
        return auth0
    }

    static def parseCredentials(CredentialsExtension ext) {
        String clientId = ext.getClientId()
        String domain = ext.getDomain()

        Map<String, Object> auth0 = new HashMap<>()
        if (isValidString(clientId) && !isValidString(domain)) {
            throw new GradleException('Missing \'auth0:domain\' value in the Application\'s build.gradle file');
        }
        if (!isValidString(clientId) && isValidString(domain)) {
            throw new GradleException('Missing \'auth0:clientId\' value in the Application\'s build.gradle file');
        }
        auth0.put(RES_CLIENT_ID_KEY, clientId)
        auth0.put(RES_DOMAIN_KEY, domain)
        return auth0
    }

    static def isValidString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false
        }
        return true
    }

}