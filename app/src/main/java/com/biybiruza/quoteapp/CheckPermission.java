package com.biybiruza.quoteapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class CheckPermission extends Activity {
    private final Context context;
    private static final String MNC = "MNC";
    int permReq = 0;
    String permMan = "";

    public CheckPermission(Context context) {
        this.context = context;
    }

    //perMan can be any code number higher than 0
    public void requestPermission(String permRequested){
        switch (permRequested) {
            case "CAMERA":
                //Request for Camera
                this.permReq = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
                this.permMan = Manifest.permission.CAMERA;
                break;
            case "INTERNET":
                //Requesr for Internet
                this.permReq = ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET);
                this.permMan = Manifest.permission.INTERNET;
                break;
            case "STORAGE":
                //Request for group Storage - Read_External_Storage & Write_External_Storage
                this.permReq = ContextCompat.checkSelfPermission(context, Manifest.permission_group.STORAGE);
                this.permMan = Manifest.permission_group.STORAGE;
                break;
            case "MICROPHONE":
                //Request for group Microphone - Record_Audio
                this.permReq = ContextCompat.checkSelfPermission(context, Manifest.permission_group.MICROPHONE);
                this.permMan = Manifest.permission_group.MICROPHONE;
                break;
            case "LOCATION":
                //Request for group Location - Acess_Fine_Location & Acess_Coarse_Location
                this.permReq = ContextCompat.checkSelfPermission(context, Manifest.permission_group.LOCATION);
                this.permMan = Manifest.permission_group.LOCATION;
                break;
            case "CALL":
                //Requesr for call
                this.permReq = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
                this.permMan = Manifest.permission.CALL_PHONE;
                break;
            default:
                break;
        }
    }

    public boolean hasPermission( String permRequested){
        final PackageManager pm = context.getPackageManager();

        if(isMNC_Or_Higher()) {
            requestPermission(permRequested);
            Toast.makeText(this.context, "Is MNC - permMan: " + this.permMan + " Perm required: " + permReq, Toast.LENGTH_SHORT).show();

            if (permReq != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{this.permMan}, this.permReq);
                return false;
            }
        }
        return true;
    }

    //check if is 6.0 or higher
    public boolean isMNC_Or_Higher(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == this.permReq) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissão concedida", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}