package com.auth0.android.gradle.credentials

import com.android.annotations.NonNull
import com.auth0.android.gradle.credentials.factories.XmlFileFactory
import com.auth0.android.gradle.credentials.sanitizers.XmlFileNameSanitizer
import com.auth0.android.gradle.credentials.writers.XmlConstantsWriter
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class CredentialsTask extends DefaultTask {

    @Input
    String variantDir
    @Input
    Map<String, Object> auth0

    @OutputFile
    File xmlFile

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