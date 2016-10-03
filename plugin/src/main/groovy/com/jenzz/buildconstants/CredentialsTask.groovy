package com.jenzz.buildconstants

import com.android.annotations.NonNull
import com.jenzz.buildconstants.factories.XmlFileFactory
import com.jenzz.buildconstants.sanitizers.XmlFileNameSanitizer
import com.jenzz.buildconstants.writers.XmlConstantsWriter
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class CredentialsTask extends DefaultTask {

  @Input String variantDir
  @Input Map<String, Object> auth0

  @OutputFile File xmlFile

  @Override
  Task configure(Closure closure) {
    Task configure = super.configure(closure)
    xmlFile = createXmlFile()
    configure
  }

  @SuppressWarnings("GroovyUnusedDeclaration")
  @TaskAction
  void run() {
    brewXml();
  }

  @NonNull
  private File createXmlFile() {
    String fileNameInput = "auth0"
    String fileName = new XmlFileNameSanitizer().sanitize fileNameInput
    new XmlFileFactory(project.buildDir.path, variantDir).create fileName
  }

  private void brewXml() {
    new XmlConstantsWriter(xmlFile).write auth0
  }
}