package com.jenzz.buildconstants

import com.android.build.gradle.AppPlugin
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildConstantsPlugin implements Plugin<Project> {

  static final String CLIENT_ID_KEY = "com_auth0_client_id"
  static final String DOMAIN_KEY = "com_auth0_domain"
  static final String DOMAIN_DEFAULT_VALUE = "{DOMAIN}"
  static final String CLIENT_ID_DEFAULT_VALUE = "{CLIENT_ID}"

  @Override
  void apply(Project project) {
    if (!project.plugins.findPlugin(AppPlugin)) {
      throw new GradleException(
          'You must apply the Android Application plugin before using the \'com.jenzz.buildconstants\' plugin')
    }

    project.extensions.add 'auth0', Auth0CredentialsExtension

    project.afterEvaluate {

      project.android.applicationVariants.all { variant ->

        def variantName = variant.name.capitalize()
        def generateCredentialsTask = project.tasks.create(
                [name       : "generate${variantName}Auth0Credentials",
                description: "Generates an auth0.xml resource file with credentials found in the local.properties file",
                type       : BuildConstantsTask], {
          variantDir = variant.dirName
          if (project.auth0){
            println "Auth0Credentials: Searching in build.gradle:auth0 closure"
            auth0 = parseCredentials(project.auth0)
          } else {
            println "Auth0Credentials: Searching in local.properties file"
            auth0 = parseLocalProperties(project.rootDir.absolutePath)
          }

          println "Auth0Credentials: Using ClientID=${auth0.get(CLIENT_ID_KEY)} and Domain=${auth0.get(DOMAIN_KEY)}"
        })

        def processResourcesTask = project.tasks.find {
          def pattern = ~/process${variantName}Resources$/
          pattern.matcher(it.name).matches()
        }

        processResourcesTask.dependsOn generateCredentialsTask
      }
    }
  }

  static def parseCredentials(Auth0CredentialsExtension map){
    Map<String, Object> auth0 = new HashMap<>()
    auth0.put(CLIENT_ID_KEY, map.getClientId())
    auth0.put(DOMAIN_KEY, map.getDomain())
    return auth0
  }

  static def parseLocalProperties(String filePath){
    Properties properties = new Properties()
    properties.load(new File("${filePath}/local.properties").newDataInputStream())
    Map<String, Object> auth0 = new HashMap<>()
    auth0.put(CLIENT_ID_KEY, properties.getProperty("auth0.client_id", CLIENT_ID_DEFAULT_VALUE))
    auth0.put(DOMAIN_KEY, properties.getProperty("auth0.domain", DOMAIN_DEFAULT_VALUE))
    return auth0
  }

}