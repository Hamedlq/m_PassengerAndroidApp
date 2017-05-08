package com.mibarim.main.services;

import android.graphics.Bitmap;

import com.mibarim.main.RestInterfaces.GetUserInfoService;
import com.mibarim.main.models.AboutMeModel;
import com.mibarim.main.models.ApiResponse;
import com.mibarim.main.models.CarInfoModel;
import com.mibarim.main.models.ContactStateModel;
import com.mibarim.main.models.ImageResponse;
import com.mibarim.main.models.InviteModel;
import com.mibarim.main.models.LicenseInfoModel;
import com.mibarim.main.models.PersonalInfoModel;
import com.mibarim.main.models.ScoreModel;
import com.mibarim.main.models.UserInfoModel;
import com.mibarim.main.models.UserInitialModel;

import retrofit.RestAdapter;

/**
 * Created by Hamed on 3/10/2016.
 */
public class UserInfoService {

    private RestAdapter restAdapter;

    public UserInfoService(RestAdapter restAdapter) {
        this.restAdapter = restAdapter;
    }

    public RestAdapter getRestAdapter() {
        return restAdapter;
    }

    private GetUserInfoService getService() {
        return getRestAdapter().create(GetUserInfoService.class);
    }

    public LicenseInfoModel getLicenseInfo(String authToken) {
        LicenseInfoModel res = getService().getLicenseInfo("Bearer " + authToken, "7");//7 is for retrofit problem
        return res;
    }

    public CarInfoModel getCarInfo(String authToken) {
        CarInfoModel res = getService().getCarInfo("Bearer " + authToken, "7");
        return res;
    }

    public UserInfoModel getUserInfo(String authToken) {
        UserInfoModel res = getService().getUserInfo("Bearer " + authToken, "7");
        return res;
    }

    public ApiResponse SaveUserPersonalInfo(String authToken, UserInfoModel personalInfoModel) {
        ApiResponse res = getService().SaveUserPersonalInfo("Bearer " + authToken,
                personalInfoModel.Name,
                personalInfoModel.Family,
                personalInfoModel.NationalCode,
                personalInfoModel.Gender,
                personalInfoModel.Email,
                personalInfoModel.Code
        );
        return res;
    }

    public ApiResponse registerUser(String authToken, UserInfoModel personalInfoModel) {
        ApiResponse res = getService().registerUser("Bearer " + authToken,
                personalInfoModel.Name,
                personalInfoModel.Family,
                personalInfoModel.NationalCode,
                personalInfoModel.Gender,
                personalInfoModel.Email,
                personalInfoModel.Code
        );
        return res;
    }

    public ApiResponse SaveLicenseInfo(String authToken, LicenseInfoModel licenseInfoModel) {
        ApiResponse res = getService().SaveLicenseInfo("Bearer " + authToken,
                licenseInfoModel.LicenseNo);
        return res;
    }

    public ApiResponse SaveCarInfo(String authToken, UserInfoModel carInfoModel) {
        ApiResponse res = getService().SaveCarInfo("Bearer " + authToken,
                carInfoModel.CarType,
                carInfoModel.CarColor,
                carInfoModel.CarPlateNo);
        return res;
    }

    public ApiResponse SaveBankInfo(String authToken, UserInfoModel bankInfoModel) {
        ApiResponse res = getService().SaveBankInfo("Bearer " + authToken,
                bankInfoModel.BankName,
                bankInfoModel.BankAccountNo,
                bankInfoModel.BankShaba);
        return res;
    }

    public ApiResponse SaveUserInfo(String authToken, UserInfoModel userInfoModel) {
        ApiResponse res = getService().SaveUserInfo("Bearer " + authToken,
                userInfoModel.Name,
                userInfoModel.Family,
                userInfoModel.NationalCode,
                userInfoModel.Gender,
                userInfoModel.Email,
                userInfoModel.Code,
                userInfoModel.CarType,
                userInfoModel.CarPlateNo,
                userInfoModel.CarColor,
                userInfoModel.BankShaba,
                userInfoModel.BankName,
                userInfoModel.BankAccountNo
        );
        return res;
    }

    public ApiResponse AppVersion() {
        ApiResponse res = getService().GetVersion("7");
        return res;
    }

    public ApiResponse GetTripId(String authToken) {
        ApiResponse res = getService().GetTripId("Bearer " + authToken, "7");
        return res;
    }


    public ApiResponse SaveUserEmailInfo(String authToken, PersonalInfoModel personalInfoModel) {
        ApiResponse res = getService().SaveUserEmailInfo("Bearer " + authToken,
                personalInfoModel.Email
        );
        return res;
    }


    public ScoreModel getUserScores(String authToken) {
        ScoreModel res = getService().getUserScores("Bearer " + authToken, "7");
        return res;

    }

    public ApiResponse SaveGoogleToken(String authToken,String Token) {
        ApiResponse res=getService().SaveGoogleToken("Bearer " + authToken, Token);
        return res;
    }

    public ImageResponse GetImageById(String token, String imageId) {
        ImageResponse res = getService().GetImageById("Bearer " + token, imageId);
        return res;
    }

    public ApiResponse submitDiscount(String authToken, String discountCode) {
        ApiResponse res=getService().submitDiscount("Bearer " + authToken, discountCode);
        return res;
    }

    public ApiResponse getDiscounts(String authToken) {
        ApiResponse res=getService().getDiscounts("Bearer " + authToken, "7");
        return res;
    }

    public ApiResponse getWithdrawRequests(String authToken) {
        ApiResponse res=getService().getWithdrawRequests("Bearer " + authToken, "7");
        return res;
    }

    public ApiResponse saveAboutMe(String authToken, AboutMeModel a) {
        ApiResponse res=getService().saveAboutMe("Bearer " + authToken, a.Desc);
        return res;
    }

    public AboutMeModel getAboutMe(String authToken) {
        AboutMeModel res = getService().getAboutMe("Bearer " + authToken, "7");
        return res;
    }
    public InviteModel getInvite(String authToken) {
        InviteModel res = getService().getInvite("Bearer " + authToken, "7");
        return res;
    }

    public ApiResponse submitWithdrawRequest(String authToken, String withdrawAmount) {
        ApiResponse res=getService().submitWithdrawRequest("Bearer " + authToken, withdrawAmount);
        return res;
    }

    public ApiResponse toggleTripState(String authToken, String contactId,boolean state) {
        ApiResponse res=getService().toggleContactState("Bearer " + authToken, contactId,state);
        return res;
    }


    public ScoreModel getUserScoresByContact(String authToken, long contactId) {
        ScoreModel res = getService().getUserScoresByContact("Bearer " + authToken, contactId);
        return res;
    }

    public ApiResponse SetUserScore(String authToken, long contactId, float rating) {
        ApiResponse res=getService().SetUserScore("Bearer " + authToken, contactId,rating);
        return res;
    }

    public ScoreModel getUserScoresByRoute(String authToken, long routeId) {
        ScoreModel res = getService().getUserScoresByRoute("Bearer " + authToken, routeId);
        return res;
    }

    public UserInitialModel getUserInitialInfo(String authToken) {
        UserInitialModel res = getService().getUserInitialInfo("Bearer " + authToken, "7");
        return res;
    }

    public ApiResponse sendFeedback(String mobile,String txt) {
        ApiResponse res = getService().sendFeedback(mobile,"mobile@mibarim.com", txt);
        return res;
    }
}
