package com.auth0.android.gradle.credentials.writers

import com.android.annotations.NonNull

interface ConstantsWriter {

    void write(@NonNull Map<String, Object> constants)
}