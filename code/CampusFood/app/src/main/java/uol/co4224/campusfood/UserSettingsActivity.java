package uol.co4224.campusfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class UserSettingsActivity extends AppCompatActivity {

    Switch celery;
    Switch cereals;
    Switch crustaceans;
    Switch eggs;
    Switch fish;
    Switch lupin;
    Switch milk;
    Switch molluscs;
    Switch mustard;
    Switch nuts;
    Switch peanuts;
    Switch sesame;
    Switch soya;
    Switch sulphur;
    Switch vegetarian;
    Switch vegan;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        getSupportActionBar().setTitle("CampusFood | Your Allergens");
        celery = (Switch) findViewById(R.id.swiCelery);
        cereals = (Switch) findViewById(R.id.swiCereals);
        crustaceans = (Switch) findViewById(R.id.swiCrustaceans);
        eggs = (Switch) findViewById(R.id.swiEggs);
        fish = (Switch) findViewById(R.id.swiFish);
        lupin = (Switch) findViewById(R.id.swiLupin);
        milk = (Switch) findViewById(R.id.swiMilk);
        molluscs = (Switch) findViewById(R.id.swiMolluscs);
        mustard = (Switch) findViewById(R.id.swiMustard);
        nuts = (Switch) findViewById(R.id.swiNuts);
        peanuts = (Switch) findViewById(R.id.swiPeanuts);
        sesame = (Switch) findViewById(R.id.swiSesame);
        soya = (Switch) findViewById(R.id.swiSoya);
        sulphur = (Switch) findViewById(R.id.swiSulphur);
        vegetarian = (Switch) findViewById(R.id.swiVegetarian);
        vegan = (Switch) findViewById(R.id.swiVegan);
        save = (Button) findViewById(R.id.btnSave);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAllergens(v);
            }
        });

        getFromAllergenMatrix(getIntent().getIntExtra("ALLERGEN", 0));

    }

    public void saveAllergens(View v) {
        Intent i = new Intent();
        i.putExtra("ALLERGEN", generateAllergenMatrix());
        setResult(RESULT_OK, i);
        finish();
    }

    public int generateAllergenMatrix() {
        return (celery.isChecked() ? 1 : 0) +
                (cereals.isChecked() ? 2 : 0) +
                (crustaceans.isChecked() ? 4 : 0) +
                (eggs.isChecked() ? 8 : 0) +
                (fish.isChecked() ? 16 : 0) +
                (lupin.isChecked() ? 32 : 0) +
                (milk.isChecked() ? 64 : 0) +
                (molluscs.isChecked() ? 128 : 0) +
                (mustard.isChecked() ? 256 : 0) +
                (nuts.isChecked() ? 512 : 0) +
                (peanuts.isChecked() ? 1024 : 0) +
                (sesame.isChecked() ? 2048 : 0) +
                (soya.isChecked() ? 4096 : 0) +
                (sulphur.isChecked() ? 8192 : 0) +
                (vegetarian.isChecked() ? 16384 : 0) +
                (vegan.isChecked() ? 32768 : 0);
    }
    public void getFromAllergenMatrix(int allergen) {
        int[] matrix = new int[16];
        for (int i = 0; i < 16; i++) {
            matrix[i] = allergen % 2;
            allergen /= 2;
        }
        celery.setChecked(matrix[0] == 1);
        cereals.setChecked(matrix[1] == 1);
        crustaceans.setChecked(matrix[2] == 1);
        eggs.setChecked(matrix[3] == 1);
        fish.setChecked(matrix[4] == 1);
        lupin.setChecked(matrix[5] == 1);
        milk.setChecked(matrix[6] == 1);
        molluscs.setChecked(matrix[7] == 1);
        mustard.setChecked(matrix[8] == 1);
        nuts.setChecked(matrix[9] == 1);
        peanuts.setChecked(matrix[10] == 1);
        sesame.setChecked(matrix[11] == 1);
        soya.setChecked(matrix[12] == 1);
        sulphur.setChecked(matrix[13] == 1);
        vegetarian.setChecked(matrix[14] == 1);
        vegan.setChecked(matrix[15] == 1);
    }
}