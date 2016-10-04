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

                    def homeLocalPropertiesPath = System.getProperty("user.home")
                    def projectLocalPropertiesPath = project.rootDir.absolutePath

                    auth0 = parseLocalProperties(homeLocalPropertiesPath)
                    auth0 = pickFirstValid(auth0, parseLocalProperties(projectLocalPropertiesPath))
                    auth0 = pickFirstValid(auth0, parseCredentials(project.auth0))

                    if (!hasCredentials(auth0)) {
                        throw new GradleException("Auth0 Credentials not found! Make sure to define both the 'clientId' and the 'domain' values. I've searched for them in this order: 1) 'local.properties' file located in the User's Home directory. " +
                                "2) 'local.properties' file located in the Project's directory. 3) 'build.gradle' file located in the Application's directory.")
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

    static def pickFirstValid(Map a, Map b) {
        return hasCredentials(a) ? a : b;
    }

    static def hasCredentials(Map auth0) {
        return auth0 != null && isValidString(auth0.get(RES_CLIENT_ID_KEY)) && isValidString(auth0.get(RES_DOMAIN_KEY))
    }

    static def isValidString(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false
        }
        return true
    }

    static def parseLocalProperties(String filePath) {
        File file = new File("${filePath}/local.properties");
        if (!file.exists()) {
            return null
        }
        Properties properties = new Properties()
        properties.load(file.newDataInputStream())
        Map<String, Object> auth0 = new HashMap<>()
        auth0.put(RES_CLIENT_ID_KEY, properties.getProperty(LOCAL_PROPERTIES_CLIENT_ID_KEY))
        auth0.put(RES_DOMAIN_KEY, properties.getProperty(LOCAL_PROPERTIES_DOMAIN_KEY))
        return auth0
    }

    static def parseCredentials(CredentialsExtension ext) {
        Map<String, Object> auth0 = new HashMap<>()
        auth0.put(RES_CLIENT_ID_KEY, ext.getClientId())
        auth0.put(RES_DOMAIN_KEY, ext.getDomain())
        return auth0
    }

}