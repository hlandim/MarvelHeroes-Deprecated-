package com.hlandim.marvelheroes.util

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat


class AppPermissionManager(private val activity: Activity) {

    companion object {
        private val permissions = listOf(
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        const val PERMISSION_REQUEST_CODE = 1899
    }

    fun checkAndRequestPermission(): Boolean {

        val permissionsNeeded = permissions.filter {
            ContextCompat.checkSelfPermission(activity, it) != PackageManager.PERMISSION_GRANTED
        }

        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity, permissionsNeeded.toTypedArray(), PERMISSION_REQUEST_CODE)
            return false
        }

        return true

    }

    fun handlePermissionResponse(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        onSuccess: () -> Unit,
        onError: () -> Unit
    ) {
        if (requestCode == AppPermissionManager.PERMISSION_REQUEST_CODE) {
            val permissionsResult = mutableMapOf<String, Int>()
            var deniedCount = 0

            grantResults.forEachIndexed { index, i ->
                if (i == PackageManager.PERMISSION_DENIED) {
                    permissionsResult.put(permissions[index], i)
                    deniedCount++
                }
            }

            if (deniedCount == 0) {
                onSuccess()
            } else {
                for ((perName, _) in permissionsResult) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity, perName)) {
                        showDialog("", "Esse app precisa de permissões para funcionar corretamente"
                            , "Permitir", DialogInterface.OnClickListener { dialog, _ ->
                                dialog.dismiss()
                                checkAndRequestPermission()
                            }, "Negar", DialogInterface.OnClickListener { dialog, _ ->
                                dialog.dismiss()
                                activity.finish()
                            }, false
                        )
                    } else {
                        showDialog(
                            "",
                            "Você negou algumas permissões. Habilite as permissões em [Configurações] -> [Permissions]",
                            "Configurações",
                            DialogInterface.OnClickListener { _, _ ->
                                val intent = Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", activity.packageName, null)
                                )
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                activity.startActivity(intent)
                                activity.finish()
                            },
                            "Sair",
                            DialogInterface.OnClickListener { dialog, _ ->
                                dialog.dismiss()
                                activity.finish()
                            },
                            false
                        )
                    }
                }
                onError()
            }
        }
    }

    private fun showDialog(
        title: String,
        msg: String,
        positiveLabel: String,
        positiveClick: DialogInterface.OnClickListener,
        negativeLabel: String,
        negativeClick: DialogInterface.OnClickListener,
        isCancelable: Boolean
    ): AlertDialog? {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle(title)
        builder.setMessage(msg)
        builder.setCancelable(isCancelable)
        builder.setPositiveButton(positiveLabel, positiveClick)
        builder.setNegativeButton(negativeLabel, negativeClick)
        val dialog = builder.create()
        dialog.show()
        return dialog
    }

}