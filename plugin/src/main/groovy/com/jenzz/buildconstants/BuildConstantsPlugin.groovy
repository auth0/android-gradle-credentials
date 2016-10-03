package com.jenzz.buildconstants

import com.android.build.gradle.AppPlugin
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project

class BuildConstantsPlugin implements Plugin<Project> {

  @Override
  void apply(Project project) {
    if (!project.plugins.findPlugin(AppPlugin)) {
      throw new GradleException(
          'You must apply the Android plugin before using the \'com.jenzz.buildconstants\' plugin')
    }

    project.extensions.add 'buildConstants', BuildConstantsExtension

    project.afterEvaluate {

      project.android.applicationVariants.all { variant ->

        def variantName = variant.name.capitalize()
        def generateConstantsTask = project.tasks.create([name       : "generate${variantName}Auth0Credentials",
                                                          description: "Generates an auth0.xml resource file with credentials found in the local.properties file",
                                                          type       : BuildConstantsTask], {
          variantDir = variant.dirName
          if (project.buildConstants.constants){
            println "Using build.gradle constants"
            //Prefer build.gradle defined constants over the local.properties one
            constants = project.buildConstants.constants
          } else {
            println "Using local.properties file"
            constants = parseLocalProperties(project.rootDir.absolutePath)
          }
        })

        def processResourcesTask = project.tasks.find {
          def pattern = ~/process${variantName}Resources$/
          pattern.matcher(it.name).matches()
        }

        processResourcesTask.dependsOn generateConstantsTask
      }
    }
  }


  static def parseLocalProperties(String filePath){
    Properties properties = new Properties()
    properties.load(new File("${filePath}/local.properties").newDataInputStream())
    Map<String, Object> constants = new HashMap<>()
    constants.put("client_id", properties.getProperty("auth0.client_id", "{CLIENT_ID}"))
    constants.put("domain", properties.getProperty("auth0.domain", "{DOMAIN}"))
    return constants
  }

}