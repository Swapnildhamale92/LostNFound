package com.swapnil.lostnfound.Views.Fragments;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.swapnil.lostnfound.Models.ForgotPasswordMail;
import com.swapnil.lostnfound.Models.ForgotPasswordMailResponse;
import com.swapnil.lostnfound.Models.LoginResponse;
import com.swapnil.lostnfound.Models.UserLogin;
import com.swapnil.lostnfound.R;
import com.swapnil.lostnfound.Utils.LFApiClient;
import com.swapnil.lostnfound.Utils.LFApiService;
import com.swapnil.lostnfound.Utils.LFUtils;
import com.swapnil.lostnfound.Views.Activity.LFBaseActivity;
import com.swapnil.lostnfound.Views.CustomViews.LFCustomToast;
import com.swapnil.lostnfound.Views.CustomViews.LFProgressDialog;
import com.swapnil.lostnfound.Views.CustomViews.LFProgressView;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LFForgotPasswordFragment extends Fragment implements
		OnClickListener {
	private static View view;

	private static EditText emailId;
	private static TextView submit, back , regitsterEmailHint;
	private static FragmentManager fragmentManager;

	private LFProgressDialog pDialog;
	private String email;
	private Context context;
	Typeface face;

	public LFForgotPasswordFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.forgotpassword_layout, container,
				false);
		context = this.getActivity().getWindow().getContext();
		face = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(),
				"font/RobotoCondensed-Regular.ttf");
		initViews();
		setListeners();
		return view;
	}

	// Initialize the views
	@SuppressWarnings("ResourceType")
	private void initViews() {
		fragmentManager = getActivity().getSupportFragmentManager();

		regitsterEmailHint = (TextView) view.findViewById(R.id.registered_emailid_hint);
		emailId = (EditText) view.findViewById(R.id.registered_emailid);
		submit = (TextView) view.findViewById(R.id.forgot_button);
		back = (TextView) view.findViewById(R.id.backToLoginBtn);

		regitsterEmailHint.setTypeface(face);
		emailId.setTypeface(face);
		submit.setTypeface(face);
		back.setTypeface(face);

		// Setting text selector over textviews
		XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
		try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(),
					xrp);

			back.setTextColor(csl);
			submit.setTextColor(csl);

		} catch (Exception e) {
		}

	}

	// Set Listeners over buttons
	private void setListeners() {
		back.setOnClickListener(this);
		submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backToLoginBtn:

			// Replace Login Fragment on Back Presses
			new LFBaseActivity().replaceLoginFragment();
			break;

		case R.id.forgot_button:

			// Call Submit button task
			submitButtonTask();
			break;

		}

	}

	private void submitButtonTask() {
		String getEmailId = emailId.getText().toString();

		// Pattern for email id validation
		Pattern p = Pattern.compile(LFUtils.regEx);

		// Match the pattern
		Matcher m = p.matcher(getEmailId);

		// First check if email id is not null else show error toast
		if (getEmailId.equals("") || getEmailId.length() == 0)

			new LFCustomToast().Show_Toast(getActivity(), view,
					"Please enter your Email Id.");

		// Check if email id is valid or not
		else if (!m.find())
			new LFCustomToast().Show_Toast(getActivity(), view,
					"Your Email Id is Invalid.");

		// Else submit email id and fetch passwod or do your stuff
		else
			getPasswordFromServer();
	}

	private void getPasswordFromServer() {
		getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
		pDialog = new LFProgressDialog(getActivity(), getResources().getString(R.string.prog_msg_SENDINGMAIL));
		pDialog.setCancelable(false);
		pDialog.show();

		email = emailId.getText().toString();

		ForgotPasswordMail forgotPasswordMail = new ForgotPasswordMail(
				email
		);


		LFApiService service = LFApiClient.getClient().create(LFApiService.class);

		Call<ForgotPasswordMailResponse> userCall = service.forgotPassword(forgotPasswordMail);

		userCall.enqueue(new Callback<ForgotPasswordMailResponse>() {
			@Override
			public void onResponse(Call<ForgotPasswordMailResponse> call, Response<ForgotPasswordMailResponse> response) {

				if (null != pDialog) {
					pDialog.dismiss();
					getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
				}


				if(response.body() != null) {
					if (response.body().getMessage()!=null) {
						//Toast.makeText(getActivity(), "" + response.body().getMessage(), Toast.LENGTH_SHORT).show();
						new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
								.setTitleText("Message")
								.setConfirmText("OK")
								.setContentText(response.body().getMessage())
								.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
									@Override
									public void onClick(SweetAlertDialog sDialog) {
										sDialog.dismissWithAnimation();
										Fragment lfNewPasswordFragment = new LFNewPasswordFragment();
										Bundle args = new Bundle();
										args.putString("email", email);
										lfNewPasswordFragment.setArguments(args);
										fragmentManager
												.beginTransaction()
												.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
												.replace(R.id.frameContainer,
														lfNewPasswordFragment,
														LFUtils.NewPassword_Fragment).commit();
									}
								})
								.show();
					}
				} else if(response.errorBody()!=null) {
					try {
						JSONObject jObjError = new JSONObject(response.errorBody().string());
						Toast.makeText(getContext(), jObjError.getString("message"), Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
					}
				}
			}

			@Override
			public void onFailure(Call<ForgotPasswordMailResponse> call, Throwable t) {
				if (null != pDialog) {
					pDialog.dismiss();
					getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
				}
				Log.d("onFailure", t.toString());
			}
		});
	}
}