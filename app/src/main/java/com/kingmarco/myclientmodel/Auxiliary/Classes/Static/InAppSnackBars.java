package com.kingmarco.myclientmodel.Auxiliary.Classes.Static;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentActivity;

import com.google.android.material.snackbar.Snackbar;
import com.kingmarco.myclientmodel.Auxiliary.Enums.SnackBarsInfo;
import com.kingmarco.myclientmodel.R;

public class InAppSnackBars {
    private static void showSnackBar(View contentView,
                                     Context context,
                                     int textColor,
                                     int backgroundColor,
                                     String text) {
        Snackbar snackbar = Snackbar.make(contentView,text, Snackbar.LENGTH_LONG)
                .setTextColor(context.getColor(textColor))
                .setBackgroundTint(context.getColor(backgroundColor));
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbar.getView().getLayoutParams();
        params.gravity = Gravity.TOP;
        snackbar.getView().setLayoutParams(params);
        snackbar.show();
    }

    public static void defineSnackBarInfo(SnackBarsInfo snackBarsInfo,
                                          View contentView,
                                          Context context,
                                          FragmentActivity fragmentActivity,
                                          boolean canGoBack){
        if (context == null){return;}
        switch (snackBarsInfo){
            case DATA_ERROR:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.red,
                        "Hubo un error al subir la informacion, intenta otra vez");
                break;
            case CARTS_ERROR:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.red,
                        "No se puedo cargar tus pedidos, intenta mas tarde");
                break;
            case EXIST_ERROR:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.red,
                        "Ya estas registrado en la base de datos");
                break;
            case DELETE_ERROR:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.red,
                        "Hubo un error al borrar la informacion, intenta otra vez");
                break;
            case IMAGES_ERROR:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.red,
                        "Hubo en error al subir las imagenes, intenta otra vez");
                break;
            case LOGIN_ERROR:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.red,
                        "Lo siento, pero no estas registrado");
                break;
            case REGISTER_ERROR:
                InAppSnackBars.showSnackBar(contentView,context,R.color.white,R.color.red,
                        "Lo siento registro fallido, intentalo otra vez");
                break;
            case DISABLE_ERROR:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.red,
                        "No estas autorizado para hacer esta operacion");
                break;
            case CHATS_ERROR:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.red,
                        "No se puedo cargar los chats");
                break;
            case EMAIL_SERVER_ERROR:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.red,
                        "El email antiguo no concuerda con el tuyo");
                break;
            case EMAILS_EQUALS_ERROR:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.red,
                        "Los emails no concuerdan");
                break;
            case EMAIL_INCORRECT_ERROR:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.red,
                        "No es un email valido");
                break;
            case PASSWORD_EQUALS_ERROR:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.red,
                        "Las contraseñas no concuerdan");
                break;
            case PASSWORD_LENGTH_ERROR:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.red,
                        "La contraseña es muy corta");
                break;
            case INCOMPLETE_INFO_ERROR:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.red,
                        "Esta incompleta la informacion");
                break;
            case EMAIL_SUCCESS:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.light_green,
                        "Tu correo ha sido actualizado correctamente");
                if(fragmentActivity == null || !canGoBack){return;}
                fragmentActivity.onBackPressed();
                break;
            case PASSWORD_SUCCESS:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.light_green,
                        "Tu contraseña ha sido actualizado correctamente");
                if(fragmentActivity == null || !canGoBack){return;}
                fragmentActivity.onBackPressed();
                break;
            case LOGIN_SUCCESS:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.light_green,
                        "Has Iniciado Sesion");
                break;
            case LOG_OUT_SUCCESS:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.light_green,
                        "Has Cerrado Sesion");
                break;
            case UPLOAD_SUCCESS:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.light_green,
                        "Se subio con exito la informacion");
                if(fragmentActivity == null || !canGoBack){return;}
                fragmentActivity.onBackPressed();
                break;
            case UPDATE_SUCCESS:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.light_green,
                        "Se actualizo con exito la informacion");
                if(fragmentActivity == null || !canGoBack){return;}
                fragmentActivity.onBackPressed();
                break;
            case DELETE_SUCCESS:
                InAppSnackBars.showSnackBar(contentView, context, R.color.white, R.color.light_green,
                        "Se borro exitosamente la informacion");
                if(fragmentActivity == null|| !canGoBack){return;}
                fragmentActivity.onBackPressed();
                break;
        }
    }
}
