package hfad.com.dietadvisor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class advise_fragment extends Fragment {

    double weightAchieve;
    double weightWithIdealBMI;
    boolean checkUSSystem;
    ImageView image;
    TextView adviseText;
    EditText daysEditText;
    Button doneBtn;
    TextView possibleDaysText;
    int days;
    int possibleDay;
    String unit = " kg";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.advise_fragment_layout, container, false);
        weightWithIdealBMI = getArguments().getDouble("weightWithIdealBMI");
        weightAchieve = getArguments().getDouble("weightAchieve");
        checkUSSystem = getArguments().getBoolean("checkUSSystem");
        DecimalFormat df = new DecimalFormat("#.##"); // round bmi to 2 decimal
        weightWithIdealBMI = Double.valueOf(df.format(weightWithIdealBMI));
        weightAchieve = Double.valueOf(df.format(weightAchieve));

        image = view.findViewById(R.id.fitness_image);
        adviseText = view.findViewById(R.id.advise_text);
        possibleDaysText = view.findViewById(R.id.possibleDays);
        daysEditText = view.findViewById(R.id.daysEditText);
        doneBtn = view.findViewById(R.id.btn_done);

        image.setImageResource(R.drawable.fitness);
        if (checkUSSystem) {
            unit = " lbs";
            weightAchieve = weightAchieve / 0.4535923;
            weightWithIdealBMI = weightWithIdealBMI / 0.4535923;
            weightWithIdealBMI = Double.valueOf(df.format(weightWithIdealBMI));
            weightAchieve = Double.valueOf(df.format(weightAchieve));
        }
        adviseText.setText(weightWithIdealBMI + unit + " " + getString(R.string.advise));
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateUserDaysGoal(weightAchieve);
                daysEditText.getText().clear();
            }
        });
        return view;
    }

    private void calculateUserDaysGoal(double weightAchieve) {
        try {
            days = Integer.parseInt(daysEditText.getText().toString());
        } catch (NumberFormatException e) {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.mustBeANum), Toast.LENGTH_SHORT);
            toast.show();
        }
        if (days < 0) {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.cantBeNegative), Toast.LENGTH_SHORT);
            toast.show();
        } else {
            if (checkUSSystem) {
                possibleDay = (int) (weightAchieve * 7 / 2.2)  ;
            } else {
                possibleDay = (int) weightAchieve * 7;
            }
            if (days < (possibleDay - 14)) {
                possibleDaysText.setText("You have to lose "+ weightAchieve +  unit  + " .For your healthy, you could achieve the ideal weight within " + possibleDay + " days " +
                        getString(R.string.shortTime));
            } else if (days > (possibleDay + 14)) {
                possibleDaysText.setText("You have to lose "+ weightAchieve +  unit  + " .For your healthy, you could achieve the ideal weight within " + possibleDay + " days " +
                        getString(R.string.longTime));
            }
        }
    }
}
