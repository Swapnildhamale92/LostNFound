package com.swapnil.lostnfound.Views.Fragments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swapnil.lostnfound.Models.Message;
import com.swapnil.lostnfound.Models.User;
import com.swapnil.lostnfound.R;
import com.swapnil.lostnfound.Utils.LFApiClient;
import com.swapnil.lostnfound.Utils.LFApiService;
import com.swapnil.lostnfound.Utils.LFUtils;
import com.swapnil.lostnfound.Views.CustomViews.LFCustomToast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LFLoginFragment extends Fragment implements OnClickListener {
	private static View view;

	private static EditText emailid, password;
	private static Button loginButton;
	private static TextView forgotPassword, signUp,loginTitle;
	private static CheckBox show_hide_password;
	private static LinearLayout loginLayout;
	private static Animation shakeAnimation;
	private static FragmentManager fragmentManager;
	private ProgressDialog pDialog;
	private String email,pass;
	Typeface face;

	public LFLoginFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.login_layout, container, false);
		face = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(),
				"font/RobotoCondensed-Regular.ttf");
		initViews();
		setListeners();
		return view;
	}

	// Initiate Views
	private void initViews() {
		fragmentManager = getActivity().getSupportFragmentManager();

		loginTitle = (TextView) view.findViewById(R.id.login_title);
		emailid = (EditText) view.findViewById(R.id.login_emailid);
		password = (EditText) view.findViewById(R.id.login_password);
		loginButton = (Button) view.findViewById(R.id.loginBtn);
		forgotPassword = (TextView) view.findViewById(R.id.forgot_password);
		signUp = (TextView) view.findViewById(R.id.createAccount);
		show_hide_password = (CheckBox) view
				.findViewById(R.id.show_hide_password);
		loginLayout = (LinearLayout) view.findViewById(R.id.login_layout);

		loginTitle.setTypeface(face);
		emailid.setTypeface(face);
		password.setTypeface(face);
		loginButton.setTypeface(face);
		forgotPassword.setTypeface(face);
		signUp.setTypeface(face);
		show_hide_password.setTypeface(face);

		// Load ShakeAnimation
		shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.shake);

		// Setting text selector over textviews
		XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
		try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(),
					xrp);

			forgotPassword.setTextColor(csl);
			show_hide_password.setTextColor(csl);
			signUp.setTextColor(csl);
		} catch (Exception e) {
		}
	}

	// Set Listeners
	private void setListeners() {
		loginButton.setOnClickListener(this);
		forgotPassword.setOnClickListener(this);
		signUp.setOnClickListener(this);

		// Set check listener over checkbox for showing and hiding password
		show_hide_password
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton button,
							boolean isChecked) {

						// If it is checkec then show password else hide
						// password
						if (isChecked) {

							show_hide_password.setText(R.string.hide_pwd);// change
																			// checkbox
																			// text

							password.setInputType(InputType.TYPE_CLASS_TEXT);
							password.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());// show password
						} else {
							show_hide_password.setText(R.string.show_pwd);// change
																			// checkbox
																			// text

							password.setInputType(InputType.TYPE_CLASS_TEXT
									| InputType.TYPE_TEXT_VARIATION_PASSWORD);
							password.setTransformationMethod(PasswordTransformationMethod
									.getInstance());// hide password

						}

					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginBtn:
			checkValidation();
			break;

		case R.id.forgot_password:

			// Replace forgot password fragment with animation
			fragmentManager
					.beginTransaction()
					.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
					.replace(R.id.frameContainer,
							new LFForgotPasswordFragment(),
							LFUtils.ForgotPassword_Fragment).commit();
			break;
		case R.id.createAccount:

			// Replace signup frgament with animation
			fragmentManager
					.beginTransaction()
					.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
					.replace(R.id.frameContainer, new LFSignUpFragment(),
							LFUtils.SignUp_Fragment).commit();
			break;
		}

	}

	// Check Validation before login
	private void checkValidation() {
		// Get email id and password
		email = emailid.getText().toString();
		pass = password.getText().toString();

		// Check patter for email id
		Pattern p = Pattern.compile(LFUtils.regEx);

		Matcher m = p.matcher(email);

		// Check for both field is empty or not
		if (email.equals("") || email.length() == 0
				|| pass.equals("") || pass.length() == 0) {
			loginLayout.startAnimation(shakeAnimation);
			new LFCustomToast().Show_Toast(getActivity(), view,
					"Enter both credentials.");

		}
		// Check if email id is valid or not
		else if (!m.find())
			new LFCustomToast().Show_Toast(getActivity(), view,
					"Your Email Id is Invalid.");
		// Else do login and do your stuff
		else
			loginByServer();

	}

	private void loginByServer() {
		pDialog = new ProgressDialog(getContext());
		pDialog.setIndeterminate(true);
		pDialog.setMessage("Log In...");
		pDialog.setCancelable(false);

		showpDialog();

		email = emailid.getText().toString();
		pass = password.getText().toString();

		User user = new User(
				email ,
				pass

		);


		LFApiService service = LFApiClient.getClient().create(LFApiService.class);

		Call<Message> userCall = service.userLogIn(user);

		userCall.enqueue(new Callback<Message>() {
			@Override
			public void onResponse(Call<Message> call, Response<Message> response) {
				hidepDialog();
				//onSignupSuccess();
				Log.d("onResponse", "" + response.body().getMessage());


				if(response.body().getMessage()!= null) {
					Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();

				}else {
					Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure(Call<Message> call, Throwable t) {
				hidepDialog();
				Log.d("onFailure", t.toString());
			}
		});
	}

	private void showpDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hidepDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

}
