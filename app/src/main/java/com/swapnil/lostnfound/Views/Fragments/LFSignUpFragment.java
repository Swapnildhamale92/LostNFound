package com.swapnil.lostnfound.Views.Fragments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.swapnil.lostnfound.Models.LoginResponse;
import com.swapnil.lostnfound.Models.SignupResponse;
import com.swapnil.lostnfound.Models.UserLogin;
import com.swapnil.lostnfound.Models.UserSignUp;
import com.swapnil.lostnfound.R;
import com.swapnil.lostnfound.Utils.LFApiClient;
import com.swapnil.lostnfound.Utils.LFApiService;
import com.swapnil.lostnfound.Utils.LFUtils;
import com.swapnil.lostnfound.Views.Activity.LFBaseActivity;
import com.swapnil.lostnfound.Views.CustomViews.LFCustomToast;
import com.swapnil.lostnfound.Views.CustomViews.LFProgressDialog;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class LFSignUpFragment extends Fragment implements OnClickListener,PlaceSelectionListener {
	private static View view;
	private static EditText fullName, emailId, mobileNumber, location,
			password, confirmPassword;
	private static TextView login , signuptitle;
	private static Button signUpButton;
	private static CheckBox terms_conditions;
	private LFProgressDialog        pDialog = null;
	private String name,email,phone,userlocation,signup_lat,signup_long,pass;
    private Context context;
	Typeface face;
	private int              PLACE_AUTOCOMPLETE_REQUEST_CODE = 99;
	private double           lat,lng;

	public LFSignUpFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.signup_layout, container, false);
        context = this.getActivity().getWindow().getContext();
        face = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(),
                "font/RobotoCondensed-Regular.ttf");
		initViews();
		setListeners();
		return view;
	}

	// Initialize all views
	@SuppressWarnings("ResourceType")
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

		location.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
					callPlaceAutocompleteActivityIntent();
				}
				return false;
			}
		});

		location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View view, boolean hasFocus) {
				if(hasFocus) callPlaceAutocompleteActivityIntent();
			}
		});

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
            signUpByServer();

	}

	private void signUpByServer() {
		getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
		pDialog = new LFProgressDialog(getActivity(), getResources().getString(R.string.prog_msg_SIGNUP));
		pDialog.setCancelable(false);
		pDialog.show();

		name = fullName.getText().toString();
		email = emailId.getText().toString();
		phone = mobileNumber.getText().toString();
		userlocation = location.getText().toString();
		pass = password.getText().toString();

		UserSignUp userSignUp = new UserSignUp(
				name,
				email,
				phone,
				userlocation,
				signup_lat,
				signup_long,
				pass
		);


		LFApiService service = LFApiClient.getClient().create(LFApiService.class);

		Call<SignupResponse> userCall = service.userSignUp(userSignUp);

		userCall.enqueue(new Callback<SignupResponse>() {
			@Override
			public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {

				if (null != pDialog) {
					pDialog.dismiss();
					getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
				}


				if(response.body() != null) {
					if (response.body().getSuccessCode() == 201) {
						//Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
						new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
								.setTitleText("Message")
								.setConfirmText("OK")
								.setContentText(response.body().getMessage())
								.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
									@Override
									public void onClick(SweetAlertDialog sDialog) {
										sDialog.dismissWithAnimation();
										new LFBaseActivity().replaceLoginFragment();
									}
								})
								.show();

					}
				} else if(response.errorBody()!=null) {
					try {
						JSONObject jObjError = new JSONObject(response.errorBody().string());
						//Toast.makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
						new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
								.setTitleText("Message")
								.setContentText(jObjError.getString("message"))
								.setConfirmText("Try Again")
								.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
									@Override
									public void onClick(SweetAlertDialog sDialog) {
										sDialog.dismissWithAnimation();
									}
								})
								.show();
					} catch (Exception e) {
						Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			}

			@Override
			public void onFailure(Call<SignupResponse> call, Throwable t) {
				if (null != pDialog) {
					pDialog.dismiss();
					getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
				}
				Log.d("onFailure", t.toString());
			}
		});
	}

	private void callPlaceAutocompleteActivityIntent() {
		try {
			Intent intent =
					new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
							.build(getActivity());
			startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
			//PLACE_AUTOCOMPLETE_REQUEST_CODE is integer for request code
		} catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
			// TODO: Handle the error.
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//autocompleteFragment.onActivityResult(requestCode, resultCode, data);
		if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Place place = PlaceAutocomplete.getPlace(getActivity(), data);
				this.onPlaceSelected(place);
				Log.i("LOSTNFOUND", "Place:" + place.toString());
			} else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
				Status status = PlaceAutocomplete.getStatus(getActivity(), data);
				Log.i("LOSTNFOUND", status.getStatusMessage());
			} else if (requestCode == RESULT_CANCELED) {

			}
		}
	}

	@Override
	public void onPlaceSelected(Place place) {
		if(place.getName() != null) {
			location.setText(place.getName());
			LatLng latLng = place.getLatLng();
			lat = latLng.latitude;
			lng = latLng.longitude;
			signup_lat = String.valueOf(lat);
			signup_long = String.valueOf(lng);
		}
	}

	@Override
	public void onError(Status status) {

	}
}
