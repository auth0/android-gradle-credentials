package com.jenzz.buildconstants

import com.android.annotations.NonNull

@SuppressWarnings("GroovyUnusedDeclaration")
public class CredentialsExtension {

    String domain
    String clientId

    @NonNull
    String getDomain(@NonNull String defValue) {
        return domain ? domain : defValue
    }

    @NonNull
    String getClientId(@NonNull String defValue) {
        return clientId ? clientId : defValue
    }
}