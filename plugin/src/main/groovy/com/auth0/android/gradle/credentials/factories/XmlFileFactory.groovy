/*
 * XmlFileFactory.groovy
 *
 * Copyright (c) 2016 Jens Driller
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

package com.auth0.android.gradle.credentials.factories

import com.android.annotations.NonNull

public class XmlFileFactory implements FileFactory {

    @NonNull
    private final String buildDir
    @NonNull
    private final String variantDir

    XmlFileFactory(@NonNull String buildDir, @NonNull String variantDir) {
        this.buildDir = buildDir
        this.variantDir = variantDir
    }

    @NonNull
    @Override
    File create(@NonNull String fileName) {
        new File("${buildDir}/generated/res/resValues/${variantDir}/values/${fileName}.xml")
    }
}
