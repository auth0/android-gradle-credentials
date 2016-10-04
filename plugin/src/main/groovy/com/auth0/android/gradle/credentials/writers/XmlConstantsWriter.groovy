/*
 * XmlConstantsWriter.groovy
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

package com.auth0.android.gradle.credentials.writers

import com.android.annotations.NonNull

import static java.util.Locale.ENGLISH

public class XmlConstantsWriter extends BaseConstantsWriter {

    XmlConstantsWriter(@NonNull File file) {
        super(file)
    }

    @Override
    String writeHeader() {
        //@formatter:off
        "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + "\n" +
        "<resources>" + "\n" +
        "\n" +
        "\t" + "<!-- Automatically generated file. DO NOT MODIFY. -->" + "\n" +
        "\t" + "<!-- Check your build.gradle for buildConstants to edit these values. -->" + "\n" +
        "\n"
        //@formatter:on
    }

    @NonNull
    @Override
    String writeString(@NonNull Map.Entry<String, Object> constant) {
        write "string", constant
    }

    @NonNull
    @Override
    String writeInteger(@NonNull Map.Entry<String, Object> constant) {
        write "integer", constant
    }

    @NonNull
    @Override
    String writeBoolean(@NonNull Map.Entry<String, Object> constant) {
        write "bool", constant
    }

    @NonNull
    @Override
    String writeFooter() {
        //@formatter:off
        "\n" +
        "</resources>"
        //@formatter:on
    }

    @NonNull
    private static String write(@NonNull String dataType, @NonNull Map.Entry<String, Object> constant) {
        "<$dataType name=\"${constant.key.toLowerCase(ENGLISH)}\">$constant.value</$dataType>"
    }
}
