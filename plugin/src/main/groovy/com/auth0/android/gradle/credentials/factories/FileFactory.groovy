package com.auth0.android.gradle.credentials.factories

import com.android.annotations.NonNull

public interface FileFactory {

    @NonNull
    File create(@NonNull String fileName)
}
