package dejan.petrovic.hr.mrpantomime;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dejan.mrpantomime.R;


public class MainActivity extends AppCompatActivity {

    private static int endGameScore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInfoShowDialog();
        btnNextStartGame();
        endGameScore = 0;

    }

    /**
     * Exits the game when hardware button back pressed
     */
    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * Creates game info dialog with the description of the game
     */
    private void showInfoDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle(MainActivity.this.getResources().getString(R.string.pravila_igre));
        dialog.setMessage(R.string.pravila1);
        dialog.setNegativeButton(R.string.btnDialogClose_text, null);
        dialog.setIcon(R.mipmap.ic_launcher);
        AlertDialog dlg = dialog.show();
        WindowManager.LayoutParams lp = dlg.getWindow().getAttributes();
        lp.dimAmount = 0.8F;
        dlg.getWindow().setAttributes(lp);
        dlg.getWindow().setBackgroundDrawableResource(R.color.transparent);
        dlg.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(17);
        dlg.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.orange));

    }

    /**
     * Creates setOnClickListener for Button btnInfo
     * and on click shows the infoDialog
     */
    public void btnInfoShowDialog() {
        Button btnInfo = (Button) findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog();
            }
        });
    }

    /**
     *
     */
    public void btnNextStartGame() {
        Button btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etEndGameScore = (EditText) findViewById(R.id.etResult);
                try {
                    // Sets end game score between 10 and 60 points

                    if (!etEndGameScore.getText().toString().isEmpty()) {
                        endGameScore = Integer.parseInt(etEndGameScore.getText().toString());
                        if (endGameScore >= 10 && endGameScore <= 60) {
                            startActivity(new Intent(MainActivity.this, GameActivity.class));
                        } else {
                            Toast.makeText(MainActivity.this, R.string.btnStartToastMsg, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, R.string.btnStartToastMsg, Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, R.string.btnStartToastMsg, Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    public int getEndGameScore() {
        return endGameScore;
    }

}

