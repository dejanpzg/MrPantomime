package dejan.petrovic.hr.mrpantomime.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.dejan.mrpantomime.R;

import dejan.petrovic.hr.mrpantomime.interfaces.HomeDialogBtnListener;

public class ReturnHomeDialog extends DialogFragment {

    HomeDialogBtnListener listener;

    public void setListener(HomeDialogBtnListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(getActivity());
        dialog.setTitle("Return to Home Screen");
        dialog.setMessage("Are you sure you want to end the game and return to Home Screen?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.positiveBtnListener();
            }
        });
        dialog.setNegativeButton("No", null);
        dialog.setIcon(R.mipmap.ic_launcher);

        android.support.v7.app.AlertDialog dlg = dialog.show();
        WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
        lp.dimAmount = 0.8F;
        dlg.getWindow().setAttributes(lp);
        dlg.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dlg.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(17);
        dlg.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.orange));
        dlg.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.orange));
        dlg.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(17);

        return dlg;
    }

}
