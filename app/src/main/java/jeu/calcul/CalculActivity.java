package jeu.calcul;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.etumoov.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import BD_Utilisateur.Helper_Utilisateur.DataBaseHelper;
import BD_Utilisateur.Models_Utilisateur.Profil;


public class CalculActivity extends AppCompatActivity {

    Button btn_rep0, btn_rep1, btn_rep2, btn_rep3;
    TextView tv_score, tv_questions, tv_timer, tv_bottommessage;
    ProgressBar prog_timer;
    DatabaseReference reference;
    private DataBaseHelper db;
    //FirebaseUser user;
    int meilleurScore;

    Jeu g = new Jeu();
    int secondsRemaining = 30;

    // Initialisation du timer(30s)
    CountDownTimer timer = new CountDownTimer(30000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            secondsRemaining--;
            tv_timer.setText(Integer.toString(secondsRemaining) + "sec");
            prog_timer.setProgress(30 - secondsRemaining);
        }
        @Override
        public void onFinish() {
            finDeGame();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcul);
        db = new DataBaseHelper(this);
        reference = FirebaseDatabase.getInstance().getReference("Utilisateur");
       /* user = FirebaseAuth.getInstance().getCurrentUser();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
           reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("scoreCalcul").get;
        }
        meilleurScore = user.*/


        btn_rep0 = findViewById(R.id.btn_rep0);
        btn_rep1 = findViewById(R.id.btn_rep1);
        btn_rep2 = findViewById(R.id.btn_rep2);
        btn_rep3 = findViewById(R.id.btn_rep3);

        tv_score = findViewById(R.id.tv_score);
        tv_bottommessage = findViewById(R.id.tv_bottommessage);
        tv_questions = findViewById(R.id.tv_questions);
        tv_timer = findViewById(R.id.tv_timer);
        prog_timer=findViewById(R.id.prog_timer);

        tv_timer.setText("0 Sec");
        tv_questions.setText("");
        tv_bottommessage.setText("Démarrez la partie !");
        tv_score.setText("0 pts");
        prog_timer.setMax(100);
        nextTurn();
        timer.start();

        View.OnClickListener answerButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button buttonClicked = (Button) v;
                int answerSelected = Integer.parseInt(buttonClicked.getText().toString());
                g.checkAnswer(answerSelected);
                tv_score.setText(Integer.toString(g.getScore()));
                nextTurn();
            }
        };

        btn_rep0.setOnClickListener(answerButtonClickListener);
        btn_rep1.setOnClickListener(answerButtonClickListener);
        btn_rep2.setOnClickListener(answerButtonClickListener);
        btn_rep3.setOnClickListener(answerButtonClickListener);
    }

    // Fonction fin de jeu, si score inférieur à 70 le jeu recommence en boucle
    private void finDeGame(){
        if( g.getScore() < 70){
            Intent intent = new Intent(getApplicationContext(),CalculActivity.class);
            startActivity(intent);
            finish();
        }else{
            btn_rep0.setEnabled(false);btn_rep1.setEnabled(false);
            btn_rep2.setEnabled(false);btn_rep3.setEnabled(false);
            // Pop up qui affiche le nombre de points et le nombre de bonne réponse
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CalculActivity.this);
            alertDialogBuilder
                    .setMessage("Vous avez " + g.getScore() + " pts" + "\net " + g.getNumberCorrect() + " bonnes réponses sur : " + (g.getTotalQuestions()-1))
                    .setCancelable(false)
                    .setPositiveButton("QUITTER", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {finish();}
                    });
            Profil profil = db.getProfils();
            if (g.getScore() > profil.getScore() || profil.getScore() == 0)
                db.updateScore(profil.getId_profil(), g.getScore());
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
          //  stopService(new Intent(this, AlarmService.class));
        }
        db.close();
       // reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("scoreCalcul").setValue(g.getScore());
    }

    // Change de calcul à chaque fois que l'on appuie sur une réponse
    private void nextTurn() {
        g.makeNewQuestion();
        int [] answer = g.getCurrentQuestion().getAnswerArray();

        btn_rep0.setText(Integer.toString(answer[0]));
        btn_rep1.setText(Integer.toString(answer[1]));
        btn_rep2.setText(Integer.toString(answer[2]));
        btn_rep3.setText(Integer.toString(answer[3]));

        btn_rep0.setEnabled(true);
        btn_rep1.setEnabled(true);
        btn_rep2.setEnabled(true);
        btn_rep3.setEnabled(true);

        tv_questions.setText(g.getCurrentQuestion().getQuestionPhrase());
        tv_bottommessage.setText(g.getNumberCorrect() + "/" + (g.getTotalQuestions() -1 ));
    }

}
