/*
 * XmlFileNameSanitizer.groovy
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

package com.auth0.android.gradle.credentials.sanitizers

import com.android.annotations.NonNull
import com.android.annotations.Nullable
import org.gradle.api.InvalidUserDataException
import org.slf4j.Logger

import static org.slf4j.LoggerFactory.getLogger

public class XmlFileNameSanitizer implements FileNameSanitizer {

    private static final String SUFFIX = ".xml"
    private static final Logger log = getLogger(XmlFileNameSanitizer.class)

    @NonNull
    @Override
    public String sanitize(@Nullable String fileName) {
        if (fileName == null) {
            throw new InvalidUserDataException("XML file name must not be null.")
        }

        if (fileName.isEmpty()) {
            throw new InvalidUserDataException("XML file name must not be empty.")
        }

        if (fileName.endsWith(SUFFIX)) { // remove potential suffix
            fileName = fileName.substring 0, fileName.length() - SUFFIX.length()
            log.warn("XML file name extension not required. Ignoring " + SUFFIX + " suffix.")
        }

        if (!fileName.matches("^[a-z0-9_]+\$")) {
            throw new InvalidUserDataException("XML file name must contain only lowercase a-z, 0-9, or _.")
        }

        fileName
    }
}
