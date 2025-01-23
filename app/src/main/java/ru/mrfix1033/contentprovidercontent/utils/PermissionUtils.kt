package ru.mrfix1033.contentprovidercontent.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat

class PermissionUtils(activityResultCaller: ActivityResultCaller) {
    private var lambda: (Boolean) -> Unit = {}
    private var launcher: ActivityResultLauncher<Array<String>> =
        activityResultCaller.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { isGrantedMap ->
            lambda(isGrantedMap.values.all { it })
        }

    fun request(vararg permissions: String, lambda: (Boolean) -> Unit) {
        this.lambda = lambda
        launcher.launch(permissions.asList().toTypedArray())
    }

    fun Check_Request_Execute(
        context: Context,
        vararg permissions: String,
        lambda: (Boolean) -> Unit
    ) {
        val toCheck = mutableListOf<String>()
        for (permission in permissions)
            if (ActivityCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED
            ) toCheck.add(permission)
        request(permissions = toCheck.toTypedArray(), lambda)
    }
}