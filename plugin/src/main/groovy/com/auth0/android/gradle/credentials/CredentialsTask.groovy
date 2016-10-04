/*
 * CredentialsTask.groovy
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