package com.swapnil.lostnfound.Views.Fragments;

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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.swapnil.lostnfound.Models.ChangeForgotPassword;
import com.swapnil.lostnfound.Models.ChangeForgotPasswordResponse;
import com.swapnil.lostnfound.Models.ForgotPasswordMail;
import com.swapnil.lostnfound.Models.ForgotPasswordMailResponse;
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


/**
 * Created by swapnil on 16/9/17.
 */

public class LFNewPasswordFragment extends Fragment implements
        View.OnClickListener {
    private static View view;

    private static EditText tempPass,newPass;
    private static TextView submit,new_password_hint;
    private LFProgressDialog pDialog;
    private Context context;
    private FragmentManager fragmentManager;
    private String tempPassword,newPassword;
    private String email;
    Typeface face;

    public LFNewPasswordFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.newpassword_layout, container,
                false);
        face = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(),
                "font/RobotoCondensed-Regular.ttf");
        context = this.getActivity().getWindow().getContext();
        if(getArguments()!=null) {
            email = getArguments().getString("email");
        }
        initViews();
        setListeners();
        return view;
    }

    // Initialize the views
    @SuppressWarnings("ResourceType")
    private void initViews() {
        new_password_hint = (TextView) view.findViewById(R.id.new_password_hint);
        tempPass = (EditText) view.findViewById(R.id.temporaryPassword);
        newPass = (EditText) view.findViewById(R.id.newPassword);
        submit = (TextView) view.findViewById(R.id.forgot_button);

        new_password_hint.setTypeface(face);
        tempPass.setTypeface(face);
        newPass.setTypeface(face);
        submit.setTypeface(face);

        // Setting text selector over textviews
        XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
        try {
            ColorStateList csl = ColorStateList.createFromXml(getResources(),
                    xrp);

            submit.setTextColor(csl);

        } catch (Exception e) {
        }

    }

    // Set Listeners over buttons
    private void setListeners() {
        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.forgot_button:

                // Call Submit button task
                checkValidation();
                break;

        }

    }

    private void checkValidation() {
        // Get email id and password
        tempPassword = tempPass.getText().toString();
        newPassword = newPass.getText().toString();

        // Check for both field is empty or not
        if (tempPassword.equals("") || tempPassword.length() == 0
                || newPassword.equals("") || newPassword.length() == 0) {
           new LFCustomToast().Show_Toast(getActivity(), view,
                    "Enter both Passwords.");

        }
        else
            changePasswordByServer();

    }

    private void changePasswordByServer() {
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        pDialog = new LFProgressDialog(getActivity(), getResources().getString(R.string.prog_msg_SENDINGMAIL));
        pDialog.setCancelable(false);
        pDialog.show();

        ChangeForgotPassword changeForgotPassword = new ChangeForgotPassword(
                email,
                tempPassword,
                newPassword
        );


        LFApiService service = LFApiClient.getClient().create(LFApiService.class);

        Call<ChangeForgotPasswordResponse> userCall = service.changePassword(changeForgotPassword);

        userCall.enqueue(new Callback<ChangeForgotPasswordResponse>() {
            @Override
            public void onResponse(Call<ChangeForgotPasswordResponse> call, Response<ChangeForgotPasswordResponse> response) {

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
                                        new LFBaseActivity().replaceLoginFragment();
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
            public void onFailure(Call<ChangeForgotPasswordResponse> call, Throwable t) {
                if (null != pDialog) {
                    pDialog.dismiss();
                    getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                }
                Log.d("onFailure", t.toString());
            }
        });
    }
}
