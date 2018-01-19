package dejan.petrovic.hr.mrpantomime.dialogs;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import com.example.dejan.mrpantomime.R;

public class InfoDialog extends DialogFragment {

    String message;

    public InfoDialog(){
    }

    @SuppressLint("ValidFragment")
    public InfoDialog(String mesage){
        this.message = mesage;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(R.string.pravila_igre);
        dialog.setMessage(message);
        dialog.setNegativeButton(R.string.btnDialogClose_text, null);
        dialog.setIcon(R.mipmap.ic_launcher);

        AlertDialog dlg = dialog.show();
        WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
        lp.dimAmount = 0.8F;
        dlg.getWindow().setAttributes(lp);
        dlg.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dlg.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(17);
        dlg.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.orange));

        return dlg;
    }
}
