package dejan.petrovic.hr.mrpantomime.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dejan.mrpantomime.R;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import dejan.petrovic.hr.mrpantomime.database.DbAccess;
import dejan.petrovic.hr.mrpantomime.dialogs.InfoDialog;
import dejan.petrovic.hr.mrpantomime.util.CountDownTimerPausable;

public class GameActivity extends AppCompatActivity {

    // TODO rijesit warninge

    private CountDownTimerPausable cdTimer;
    private long countDowntimeLeft; // shows time left in milliseconds
    private boolean nextTeamPlaying = false;
    private CheckBox[] chkList; // List that contains all declared CheckBoxes
    private int team1Score = 0; // Score for team 1
    private int team2Score = 0; // Score for team 2
    private int state = 0;  // If state = 0 Team 1 is playing, if state = 1 Team 2 is playing.
    private List<String> wordsList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initWidgets();
        cdTimer().create();
        getWordsFromDbToArray();
        getRandomWordsFromArray();
        btnNextListener();
        btnInfoShowDialog();
        onClickCheckBoxHandler();

    }

    /**
     * Disables the back button, so no one can accidentally exit the game
     */
    @Override
    public void onBackPressed() {
        Toast.makeText(this, R.string.onBackPressed_text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Pauses the {@link #cdTimer()} if someone calls you during the game
     */
    @Override
    protected void onPause() {
        super.onPause();
        cdTimer.pause();
    }

    /**
     * Resumes the {@link #cdTimer()} if Home button pressed
     * so no one could cheat the time
     */
    @Override
    protected void onStop() {
        super.onStop();
        cdTimer.resume();
    }

    /**
     * Resumes the {@link #cdTimer()}
     * when you get back from phone call to game
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (cdTimer.isPaused()) {
            cdTimer.resume();
        }
    }

    /**
     * Creates CheckBox setOnClickListeners for every CheckBox
     * from CheckBox list (chkList)
     */
    public void onClickCheckBoxHandler() {
        for (final CheckBox chk : chkList) {
            chk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (chk.isChecked()) { // if CheckBox is checked (that means the word is guessed) increment score
                        incrementScore();
                    } else {
                        decrementScore();
                    }
                    whenAllWordsGuessedCountDownTimerPause();
                    setTextScore();
                }
            });
        }
    }

    /**
     * Creates the btnNext Listener and listens for click
     * if clicked first shows the nextTeamPlaying text
     * then if clicked again checks which team is playing changes the text color
     * and calls the method continuePlaying
     */
    public void btnNextListener() {
        Button btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextTeamPlaying) {
                    if (state == 0) {
                        ekipa2TxtHighlighted();
                        state = 1;
                    } else {
                        ekipa1TxtHighlighted();
                        state = 0;
                    }
                    nextTeamPlaying = false;
                    getRandomWordsFromArray();
                    continuePlaying();
                    return;
                }
                ekipa1ScoreCounter();
                setTextScore();
                // Checks if someone has enough points to win if no moves to other team
                if (winnerDeclare()) {
                    return;
                }
                nextTeamPlaying();
            }
        });
    }

    /**
     * If allWordsGuessed adds time left bonus score.
     * Every 20 seconds time left counts for 1 point
     */
    private void ekipa1ScoreCounter() {
        if (allWordsGuessed()) {
            int timeLeftScore = (int) (countDowntimeLeft / 20000);
            if (state == 0) {
                team1Score = team1Score + timeLeftScore;
            } else {
                team2Score = team2Score + timeLeftScore;
            }
        }
    }

    /**
     * Pauses the countDownTimer when all checkboxes checked(all words guessed)
     * and resumes if checkboxes checked than unchecked
     */
    private void whenAllWordsGuessedCountDownTimerPause() {
        if (allWordsGuessed()) {
            cdTimer.pause();
        } else {
            cdTimer.resume();
        }
    }

    /**
     * if all checkboxes checked that means all words are guessed
     */
    private boolean allWordsGuessed() {
        for (CheckBox chk : chkList) {
            if (!chk.isChecked()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Initializes layout Widgets from {@link R.layout#activity_game}
     */
    private void initWidgets() {
        CheckBox chk1 = (CheckBox) findViewById(R.id.chk1);
        CheckBox chk2 = (CheckBox) findViewById(R.id.chk2);
        CheckBox chk3 = (CheckBox) findViewById(R.id.chk3);
        CheckBox chk4 = (CheckBox) findViewById(R.id.chk4);
        CheckBox chk5 = (CheckBox) findViewById(R.id.chk5);
        chkList = new CheckBox[]{chk1, chk2, chk3, chk4, chk5};
    }

    /**
     * Deletes the word from ArrayList, than shows the deleted word in checkbox(1,2,3,4 and 5)
     * so words wouldn't repeat
     * And checks if the words list is smaller than 6, if it is,
     * adds the words again from database through {@link #getWordsFromDbToArray()} method
     */
    private void getRandomWordsFromArray() {
        if (wordsList.size() < 6) {
            getWordsFromDbToArray();
        }
        for (int i = 0; i < 5; i++) {
            chkList[i].setText(wordsList.remove(i));
        }
    }

    /**
     * Reads from database to ArrayList {@link #wordsList}
     * and shuffles the list
     */
    private void getWordsFromDbToArray() {
        DbAccess dbAccess = DbAccess.getInstance(this);
        dbAccess.open();
        wordsList = dbAccess.getWords();
        Collections.shuffle(wordsList); //randomize the list
        dbAccess.close();
    }

    /**
     * if btnHome pressed, goes to MainActivity
     */
    public void goToMainActivity(View view) {
        finish();
        cdTimer.cancel();
        startActivity(new Intent(this, MainActivity.class));
    }

    /**
     * Sets score to tvScoreTeam1 and tvScoreTeam2 TextViews
     */
    private void setTextScore() {
        TextView tvScoreTeam1 = (TextView)findViewById(R.id.tvTeam1Score);
        TextView tvScoreTeam2 = (TextView) findViewById(R.id.tvTeam2Score);
        tvScoreTeam1.setText(String.valueOf(team1Score));
        tvScoreTeam2.setText(String.valueOf(team2Score));
    }

    /**
     * Creates ContDownTimerWithPause ands sets minutes and seconds
     */
    public CountDownTimerPausable cdTimer() {
        cdTimer = new CountDownTimerPausable(181000, 1000, true) {
            TextView tvClock = (TextView) findViewById(R.id.tvClock);
            @Override
            public void onTick(long millisUntilFinished) {

                long minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished);
                long seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished));

                tvClock.setText(String.format(Locale.US, "%02d:%02d", minutes, seconds));
                countDowntimeLeft = millisUntilFinished;

            }

            @Override
            public void onFinish() {
                tvClock.setText(R.string.tvClock_setText);
            }
        };
        return cdTimer;
    }

    /**
     * Checks if someone has won
     * and declares the winner
     */
    private boolean winnerDeclare() {
        MainActivity mainActivity = new MainActivity();
        int endGameScore = mainActivity.getEndGameScore();
        int maxScorePerRound = 13;
        Button btnNext = (Button) findViewById(R.id.btnNext);
        TextView tvNoteTxt = (TextView) findViewById(R.id.tvNoteTxt);

        // Since team 1 is playing first we need to give team 2 a chance to get higher score or equal to team 1,
        // lets say they play to 10 and team 1 is first to play as always, team 1 gathers at start maxScorePerRound
        // than team 1 would win without giving team 2 a chance to play and get better or equal score.
        if ((team1Score >= endGameScore && team2Score < team1Score - maxScorePerRound && state == 0) ||
                (team2Score >= endGameScore && team2Score > team1Score) || // team 2 is playing last in the round so if they win, team 1 had their chance
                (team1Score >= endGameScore && team1Score > team2Score && state == 1)){ // if team 2 fails to get better or equal score, than team 1 wins
            cdTimer.cancel();
            for (CheckBox chk : chkList) {
                chk.setVisibility(View.INVISIBLE);
            }
            btnNext.setVisibility(View.GONE);
            tvNoteTxt.setVisibility(View.VISIBLE);
            if (team2Score > team1Score) {
                tvNoteTxt.setText(R.string.tvNoteTxt_congzTeam2Txt);

            } else {
                tvNoteTxt.setText(R.string.tvNoteTxt_congzTeam1Text);
                ekipa1TxtHighlighted();
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * adds +1 to {@link #team1Score} or {@link #team2Score}
     */
    private void incrementScore() {
        if (state == 0) {
            team1Score++;
        } else {
            team2Score++;
        }
    }

    /**
     * decrements -1 to {@link #team1Score} or {@link #team2Score}
     */
    private void decrementScore() {
        if (state == 0) {
            team1Score--;
        } else {
            team2Score--;
        }
    }

    /**
     * Hides all CheckBoxes and shows the nextTeamPlaying text when other team is next playing
     */
    private void nextTeamPlaying() {
        TextView tvNoteTxt = (TextView) findViewById(R.id.tvNoteTxt);
        nextTeamPlaying = true;
        cdTimer.cancel();
        for (CheckBox chk : chkList) {
            chk.setVisibility(View.INVISIBLE);
        }
        if (state == 0) {
            tvNoteTxt.setText(R.string.tvNoteTxt_team2playingTxt);
        } else {
            tvNoteTxt.setText(R.string.tvNoteTxt_team1playingTxt);
        }
        tvNoteTxt.setVisibility(View.VISIBLE);
    }

    /**
     * Changes tvTeam1 color to orange and tvTeam2 color to white
     */
    private void ekipa1TxtHighlighted() {
        TextView tvTeam2 = (TextView) findViewById(R.id.tvTeam2);
        TextView tvTeam1 = (TextView) findViewById(R.id.tvTeam1);
        TextView tvScoreTeam1 = (TextView)findViewById(R.id.tvTeam1Score);
        TextView tvScoreTeam2 = (TextView) findViewById(R.id.tvTeam2Score);

        tvScoreTeam1.setTextColor(getResources().getColor(R.color.orange));
        tvTeam1.setTextColor(getResources().getColor(R.color.orange));
        tvScoreTeam2.setTextColor(getResources().getColor(R.color.white));
        tvTeam2.setTextColor(getResources().getColor(R.color.white));
    }

    /**
     * Changes tvTeam2 color to orange and tvTeam1 color to white
     */
    private void ekipa2TxtHighlighted() {
        TextView tvTeam2 = (TextView) findViewById(R.id.tvTeam2);
        TextView tvTeam1 = (TextView) findViewById(R.id.tvTeam1);
        TextView tvScoreTeam1 = (TextView)findViewById(R.id.tvTeam1Score);
        TextView tvScoreTeam2 = (TextView) findViewById(R.id.tvTeam2Score);

        tvScoreTeam2.setTextColor(getResources().getColor(R.color.orange));
        tvTeam2.setTextColor(getResources().getColor(R.color.orange));
        tvScoreTeam1.setTextColor(getResources().getColor(R.color.white));
        tvTeam1.setTextColor(getResources().getColor(R.color.white));
    }

    /**
     * Starts the {@link #cdTimer} shows the CheckBoxes and makes them unchecked
     *
     */
    private void continuePlaying() {
        TextView tvNoteTxt = (TextView) findViewById(R.id.tvNoteTxt);
        cdTimer.create();
        tvNoteTxt.setVisibility(View.GONE);
        for (int i = 0; i < 5; i++) {
            chkList[i].setChecked(false);
            chkList[i].setVisibility(View.VISIBLE);
        }
    }

    /**
     * Creates setOnClickListener for Button btnInfo
     * and on click shows the infoDialog
     */
    private void btnInfoShowDialog() {
        Button btnInfo = (Button) findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InfoDialog infoDialog = new InfoDialog(getResources().getString(R.string.pravila2));
                infoDialog.show(getFragmentManager(),"tag");
            }
        });
    }

}

