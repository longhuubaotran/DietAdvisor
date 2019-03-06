package hfad.com.dietadvisor;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class AdviseActivity extends AppCompatActivity {

    double weightWithIdealBMI;
    double weightAchieve;
    boolean checkUSSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advise);
        Toolbar toolbar = findViewById(R.id.toolbar_advise);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get data in intent from MainActivity
        weightWithIdealBMI = Double.parseDouble(getIntent().getExtras().get("weightWithIdealBMI").toString());
        weightAchieve = Double.parseDouble(getIntent().getExtras().get("weightAchieve").toString());
        checkUSSystem = Boolean.parseBoolean(getIntent().getExtras().get("checkUSSystem").toString());

        // send data from activity to fragment
        Bundle bundle = new Bundle();
        bundle.putDouble("weightWithIdealBMI", weightWithIdealBMI);
        bundle.putDouble("weightAchieve", weightAchieve);
        bundle.putBoolean("checkUSSystem",checkUSSystem);

        advise_fragment adviseFragment = new advise_fragment();
        adviseFragment.setArguments(bundle);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.advise_container,adviseFragment);
        ft.commit();
    }
}
