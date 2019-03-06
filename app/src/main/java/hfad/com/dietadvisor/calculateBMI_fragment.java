package hfad.com.dietadvisor;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class calculateBMI_fragment extends Fragment {
    double weight;
    double height; // use double because division of 2 int always get 0
    double in;
    String resultMessage;
    String bmi;
    EditText weightText;
    EditText heightText;
    EditText inText;
    TextView resultText;
    Button showAdviseBtn;
    Button calculateBtn;
    double result;
    int redColor;
    int blackColor;
    final int IDEALBMI = 23;
    double weightWithIdealBmi;
    double weightAchieve;
    boolean checkSkinny = false;
    boolean checkUSSystem = false;
    RadioGroup rdGroup;

    public calculateBMI_fragment() {
        // public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calculatebmi_layout, container, false);

        weightText = view.findViewById(R.id.edit_weight);
        heightText = view.findViewById(R.id.edit_height);
        inText = view.findViewById(R.id.edit_in);
        resultText = view.findViewById(R.id.resultBMI);
        showAdviseBtn = view.findViewById(R.id.btn_showAdvise);
        calculateBtn = view.findViewById(R.id.btn_calculateBMI);
        rdGroup = view.findViewById(R.id.radio_group);
        inText.setFocusable(false); //disable the "in" Edittext first, because Metric radio btn is checked by default

        rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.us_system:
                        checkUSSystem = true;
//                        weightText.getText().clear(); // clear the edittext
//                        heightText.getText().clear(); // clear the edittext
                        inText.setFocusableInTouchMode(true); // enable "in" Edittext
                        inText.setFocusable(true);// enable "in" Edittext
                        break;
                    case R.id.metric_system:
                        checkUSSystem = false;
                        inText.setFocusable(false);
                        break;
                }
            }
        });

        calculateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    calculateBMI(weight, height);
                }
            }
        });

        showAdviseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSkinny == true) {
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.tooSkinny), Toast.LENGTH_SHORT);
                    toast.show();
                } else if (weightWithIdealBmi != 0) { // can't show adviseActivity because too skinny
                    Intent intent = new Intent(getActivity(), AdviseActivity.class);
                    intent.putExtra("weightWithIdealBMI", weightWithIdealBmi);
                    intent.putExtra("weightAchieve", weightAchieve);
                    intent.putExtra("checkUSSystem",checkUSSystem);
                    startActivity(intent);
                } else { // can't show adviseAcitivity because no input
                    Toast toast = Toast.makeText(getActivity(), getString(R.string.cantClick), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        return view;
    }


    //     calculate BMI
    private void calculateBMI(double weight, double height) {
        if (checkWeightHeightValid(weight, height)) {
            checkSkinny = false; // reset this to be able to click showAdviceBtn
            bmi = getString(R.string.show_bmi); // assign the string "Your bmi is: "
            redColor = getResources().getColor(R.color.redWarning);
            blackColor = getResources().getColor(R.color.black);
            height = height / 100; // convert cm to m
            result = weight / (height * height);
            DecimalFormat df = new DecimalFormat("#.##"); // round bmi to 2 decimal
            result = Double.valueOf(df.format(result)); // round bmi to 2 decimal
            resultMessage = bmi + " " + result + " "; // set up the result with bmi number
            if (result >= 19 && result <= 24.9) {
                resultMessage += getString(R.string.normal_bmi);
                weightWithIdealBmi = 0;
                resultText.setTextColor(blackColor);
            } else if (result >= 25 && result <= 29) {
                weightWithIdealBmi = IDEALBMI * (height * height);
                weightAchieve = weight - weightWithIdealBmi;
                resultMessage += getString(R.string.overweight_bmi);
                resultText.setTextColor(redColor);
            } else if (result > 29) {
                weightWithIdealBmi = IDEALBMI * (height * height);
                weightAchieve = weight - weightWithIdealBmi;
                resultMessage += getString(R.string.extreme_bmi);
                resultText.setTextColor(redColor);
            } else {
                weightWithIdealBmi = IDEALBMI * (height * height);
                weightAchieve = weightWithIdealBmi - weight;
                checkSkinny = true;
                resultMessage += getString(R.string.skinny_bmi);
                resultText.setTextColor(redColor); // set red color for text
            }
            resultText.setText(resultMessage);
        } else {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.invalid_input), Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // check valid of weight and height
    private boolean checkWeightHeightValid(double weight, double height) {
        if (weight < 0 || height < 0) {
            return false;
        } else if (weight > 185 || height > 230) {
            return false;
        }
        return true;
    }

    //check input not null and must be  integer
    private boolean checkInput() {
        if (weightText.getText().toString().equals("") || heightText.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.null_input), Toast.LENGTH_SHORT);
            toast.show();
            return false;
        } else {
            try {
                // if it can't cast to double, it's not a number
                weight = Double.parseDouble(weightText.getText().toString());
                height = Double.parseDouble(heightText.getText().toString());
                in = 0; // assign in = 0, so in can't be null or string, this check valid for in
                if (checkUSSystem) { // convert lbs -> kg, ft -> cm
                    in = Double.parseDouble(inText.getText().toString());
                    height = (height * 30.48) + (in * 2.54);
                    weight = weight * 0.4535923;
                }
                return true;
            } catch (NumberFormatException e) {
                Toast toast = Toast.makeText(getActivity(), getString(R.string.not_integer), Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        return false;
    }
}

