package com.swapnil.lostnfound.Views.Fragments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.swapnil.lostnfound.R;
import com.swapnil.lostnfound.Utils.LFUtils;
import com.swapnil.lostnfound.Views.Activity.LFBaseActivity;
import com.swapnil.lostnfound.Views.CustomViews.LFCustomToast;

public class LFSignUpFragment extends Fragment implements OnClickListener {
	private static View view;
	private static EditText fullName, emailId, mobileNumber, location,
			password, confirmPassword;
	private static TextView login , signuptitle;
	private static Button signUpButton;
	private static CheckBox terms_conditions;
    Typeface face;

	public LFSignUpFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.signup_layout, container, false);
        face = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(),
                "font/RobotoCondensed-Regular.ttf");
		initViews();
		setListeners();
		return view;
	}

	// Initialize all views
	private void initViews() {
        signuptitle = (TextView) view.findViewById(R.id.signup_title);
		fullName = (EditText) view.findViewById(R.id.fullName);
		emailId = (EditText) view.findViewById(R.id.userEmailId);
		mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
		location = (EditText) view.findViewById(R.id.location);
		password = (EditText) view.findViewById(R.id.password);
		confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
		signUpButton = (Button) view.findViewById(R.id.signUpBtn);
		login = (TextView) view.findViewById(R.id.already_user);
		terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);

        signuptitle.setTypeface(face);
        fullName.setTypeface(face);
        emailId.setTypeface(face);
        mobileNumber.setTypeface(face);
        location.setTypeface(face);
        password.setTypeface(face);
        confirmPassword.setTypeface(face);
        signUpButton.setTypeface(face);
        login.setTypeface(face);
        terms_conditions.setTypeface(face);

		// Setting text selector over textviews
		XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
		try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(),
					xrp);

			login.setTextColor(csl);
			terms_conditions.setTextColor(csl);
		} catch (Exception e) {
		}
	}

	// Set Listeners
	private void setListeners() {
		signUpButton.setOnClickListener(this);
		login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signUpBtn:

			// Call checkValidation method
			checkValidation();
			break;

		case R.id.already_user:

			// Replace login fragment
			new LFBaseActivity().replaceLoginFragment();
			break;
		}

	}

	// Check Validation Method
	private void checkValidation() {

		// Get all edittext texts
		String getFullName = fullName.getText().toString();
		String getEmailId = emailId.getText().toString();
		String getMobileNumber = mobileNumber.getText().toString();
		String getLocation = location.getText().toString();
		String getPassword = password.getText().toString();
		String getConfirmPassword = confirmPassword.getText().toString();

		// Pattern match for email id
		Pattern p = Pattern.compile(LFUtils.regEx);
		Matcher m = p.matcher(getEmailId);

		// Check if all strings are null or not
		if (getFullName.equals("") || getFullName.length() == 0
				|| getEmailId.equals("") || getEmailId.length() == 0
				|| getMobileNumber.equals("") || getMobileNumber.length() == 0
				|| getLocation.equals("") || getLocation.length() == 0
				|| getPassword.equals("") || getPassword.length() == 0
				|| getConfirmPassword.equals("")
				|| getConfirmPassword.length() == 0)

			new LFCustomToast().Show_Toast(getActivity(), view,
					"All fields are required.");

		// Check if email id valid or not
		else if (!m.find())
			new LFCustomToast().Show_Toast(getActivity(), view,
					"Your Email Id is Invalid.");

		// Check if both password should be equal
		else if (!getConfirmPassword.equals(getPassword))
			new LFCustomToast().Show_Toast(getActivity(), view,
					"Both password doesn't match.");

		// Make sure user should check Terms and Conditions checkbox
		else if (!terms_conditions.isChecked())
			new LFCustomToast().Show_Toast(getActivity(), view,
					"Please select Terms and Conditions.");

		// Else do signup or do your stuff
		else
			Toast.makeText(getActivity(), "Do SignUp.", Toast.LENGTH_SHORT)
					.show();

	}
}
