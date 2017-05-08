package com.mibarim.main.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.mibarim.main.models.AboutMeModel;
import com.mibarim.main.models.Address.LocationPoint;
import com.mibarim.main.models.Address.PathPoint;
import com.mibarim.main.models.CarInfoModel;
import com.mibarim.main.models.ContactModel;
import com.mibarim.main.models.ImageResponse;
import com.mibarim.main.models.InviteModel;
import com.mibarim.main.models.LicenseInfoModel;
import com.mibarim.main.models.MessageModel;
import com.mibarim.main.models.PersonalInfoModel;
import com.mibarim.main.models.Route.RouteResponse;
import com.mibarim.main.models.RouteRequest;
import com.mibarim.main.models.UserInfoModel;
import com.mibarim.main.models.enums.ImageTypes;
import com.mibarim.main.models.enums.ServiceTypes;
import com.mibarim.main.models.enums.TimingOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Hamed on 4/2/2015.
 */
public class UserData {
    public static final String DB_NAME = "UserData.db";
    public static final int DB_VERSION = 10;
    public static final String NEW_USER_INFO_TABLE = "newUserInfo";
    public static final String C_USER_MOBILE = "userMobile";
    public static final String C_USER_GENDER = "userGender";
    public static final String C_USER_NAME = "userName";
    public static final String C_USER_FAMILY = "userFamily";
    public static final String C_USER_NATIONAL_CODE = "userNationalCode";
    public static final String C_USER_EMAIL = "userEmail";
    public static final String C_USER_CODE = "userCode";
    public static final String C_COMPANY_NAME = "companyName";
    public static final String C_USER_IMAGE_ID = "userImageId";
    public static final String C_USER_NATIONAL_CARD_IMAGE_ID = "userNationalCardImageId";
    public static final String C_LICENSE_IMAGE_ID = "licenseImageId";
    public static final String C_CAR_TYPE = "car_type";
    public static final String C_CAR_COLOR = "car_color";
    public static final String C_CAR_PLATE_NO = "car_plate";
    public static final String C_CAR_IMAGE_ID_1 = "carImageId1";
    public static final String C_CAR_IMAGE_ID_2 = "carImageId2";
    public static final String C_BANK_SHABA = "bankShaba";
    public static final String C_BANK_ACCOUNT = "bankAccount";
    public static final String C_BANK_NAME = "bankName";
    public static final String C_BANK_CARD_IMAGE_ID = "bankImageId";
    public static final String C_COMPANY_IMAGE_ID = "companyImageId";

    public static final String USER_INFO_TEMP_TABLE = "userInfoTemp";
    public static final String CAR_TEMP_TABLE = "carTemp";
    public static final String BANK_TEMP_TABLE = "bankTemp";


    public static final String NEW_REQUEST_TABLE = "routeRequest";
    public static final String C_NR_EVENT_ID = "eventId";
    public static final String C_NR_SERVICE_TYPE = "serviceType";
    public static final String C_NR_SRC_LAT = "srcLat";
    public static final String C_NR_SRC_LNG = "srcLng";
    public static final String C_NR_SRC_ADD = "srcAdd";
    public static final String C_NR_DST_LAT = "dstLat";
    public static final String C_NR_DST_LNG = "dstLng";
    public static final String C_NR_DST_ADD = "dstAdd";
    public static final String C_NR_TIMING_OPTION = "timingOption";
    public static final String C_NR_THE_DATE = "theDate";
    public static final String C_NR_THE_TIME = "theTime";
    public static final String C_NR_THE_RETURN_TIME = "theReturnTime";
    public static final String C_NR_IS_RETURN = "isReturn";
    public static final String C_NR_SAT = "satTime";
    public static final String C_NR_SUN = "sunTime";
    public static final String C_NR_MON = "monTime";
    public static final String C_NR_TUE = "tueTime";
    public static final String C_NR_WED = "wedTime";
    public static final String C_NR_THU = "thuTime";
    public static final String C_NR_FRI = "friTime";
    public static final String C_NR_DRIVE = "IsDrive";
    public static final String C_NR_PRICE = "Price";
    public static final String C_NR_RECOMMEND_ID = "recommendPathId";

    public static final String MESSAGING_TABLE = "messaging";
    public static final String C_MSG_CONTACT_ID = "contactId";
    public static final String C_MSG_MESSAGE_ID = "commentId";
    public static final String C_MSG_NAME_FAMILY = "name";
    public static final String C_MSG_TIME_STRING = "time";
    public static final String C_MSG_MESSAGE = "msg";
    public static final String C_MSG_IS_DELETABLE = "isDeletable";
    public static final String C_MSG_USER_PIC = "userPic";

    public static final String IMAGE_TABLE = "Images";
    public static final String C_IMAGE_ID = "ImageId";
    public static final String C_IMAGE_FILE = "ImageFile";
    public static final String C_IMAGE_TYPE = "ImageType";
    public static final String C_IMAGE_FILE_PATH = "ImageFilePath";

    public static final String NEW_RESPONSE_TABLE = "routeResponse";
    public static final String C_ROUTE_ID = "routeId";
    public static final String C_NRR_SRC_LAT = "srcLat";
    public static final String C_NRR_SRC_LNG = "srcLng";
    public static final String C_NRR_SRC_ADD = "srcAdd";
    public static final String C_NRR_DST_LAT = "dstLat";
    public static final String C_NRR_DST_LNG = "dstLng";
    public static final String C_NRR_DST_ADD = "dstAdd";
    public static final String C_NRR_ACCOMPANY = "accompanyCount";
    public static final String C_NRR_DRIVE = "IsDrive";
    public static final String C_NRR_TIMING_STR = "timingString";
    public static final String C_NRR_DATE_STR = "dateString";
    public static final String C_NRR_PRICING_STR = "pricingString";
    public static final String C_NRR_SUGGEST_COUNT = "suggestCount";
    public static final String C_NRR_STATE = "routeRequestState";
    public static final String C_NRR_ROUTE_UID = "routeUId";
    public static final String C_NRR_NEW_SUGGEST_COUNT= "newsuggestCount";
    public static final String C_NRR_IS_SAT= "sat";
    public static final String C_NRR_IS_SUN= "sun";
    public static final String C_NRR_IS_MON= "mon";
    public static final String C_NRR_IS_TUE= "tue";
    public static final String C_NRR_IS_WED= "wed";
    public static final String C_NRR_IS_THU= "thu";
    public static final String C_NRR_IS_FRI= "fri";

    public static final String NEW_CONTACT_TABLE = "contact";
    public static final String C_C_CONTACT_ID = "contactId";
    public static final String C_C_Name = "name";
    public static final String C_C_FAMILY= "family";
    public static final String C_C_GENDER= "gender";
    public static final String C_C_LAST_MSG_TIME= "lastMsgTime";
    public static final String C_C_LAST_MSG= "lastMsg";
    public static final String C_C_SUPPORT= "isSupport";
    public static final String C_C_DRIVER= "isDriver";
    public static final String C_C_RIDE_ACCEPT= "isRideAccepted";
    public static final String C_C_PASSENGER_ACCEPT= "isPassengerAccepted";
    public static final String C_C_IMAGE_ID= "userImageId";
    public static final String C_C_ABOUT_USER= "aboutuser";

    public static final String ABOUT_ME_TABLE = "aboutMe";
    public static final String C_AM_DESC = "aboutMeDesc";

    public static final String INVITE_TABLE = "invite";
    public static final String C_I_INVITE_CODE = "inviteCode";
    public static final String C_I_INVITE_LINK = "inviteLink";


    static final String TAG = "UserData";
    Context context;
    DbHelper dbHelper;
    SQLiteDatabase db;

    public UserData(Context context) {
        this.context = context;
        dbHelper = new DbHelper();
    }

    public long insertAboutMeInfo(AboutMeModel model) {
        db = dbHelper.getWritableDatabase();
        db.delete(ABOUT_ME_TABLE, null, null);
        ContentValues values = new ContentValues();
        values.put(C_AM_DESC, model.Desc);
        long res = db.insertWithOnConflict(ABOUT_ME_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d(TAG, "About Me Info Inserted");
        db.close();
        return res;
    }


    public long insertInvite(InviteModel model) {
        db = dbHelper.getWritableDatabase();
        db.delete(INVITE_TABLE, null, null);
        ContentValues values = new ContentValues();
        values.put(C_I_INVITE_CODE, model.InviteCode);
        values.put(C_I_INVITE_LINK, model.InviteLink);
        long res = db.insertWithOnConflict(INVITE_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d(TAG, "Invite Inserted");
        db.close();
        return res;
    }


    public long insertUserInfo(UserInfoModel user) {
        //DeleteUserInfo();
        db = dbHelper.getWritableDatabase();
        db.delete(NEW_USER_INFO_TABLE, null, null);
        ContentValues values = new ContentValues();
        values.put(C_USER_MOBILE, user.Mobile);
        values.put(C_USER_GENDER, user.Gender);
        values.put(C_USER_NAME, user.Name);
        values.put(C_USER_FAMILY, user.Family);
        values.put(C_USER_NATIONAL_CODE, user.NationalCode);
        values.put(C_USER_NATIONAL_CARD_IMAGE_ID, user.NationalCardImageId);
        values.put(C_USER_EMAIL, user.Email);
        values.put(C_USER_CODE, user.Code);
        values.put(C_COMPANY_NAME, user.CompanyName);
        values.put(C_USER_IMAGE_ID, user.UserImageId);
        values.put(C_LICENSE_IMAGE_ID, user.LicenseImageId);
        values.put(C_CAR_TYPE, user.CarType);
        values.put(C_CAR_COLOR, user.CarColor);
        values.put(C_CAR_PLATE_NO, user.CarPlateNo);
        values.put(C_CAR_IMAGE_ID_1, user.CarCardImageId);
        values.put(C_CAR_IMAGE_ID_2, user.CarCardBckImageId);
        values.put(C_BANK_ACCOUNT, user.BankAccountNo);
        values.put(C_BANK_NAME, user.BankName);
        values.put(C_BANK_SHABA, user.BankShaba);
        values.put(C_BANK_CARD_IMAGE_ID, user.BankImageId);
        values.put(C_COMPANY_IMAGE_ID, user.CompanyImageId);
        long res = db.insertWithOnConflict(NEW_USER_INFO_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d(TAG, "User Info Inserted");
        db.close();
        return res;
    }

    public long insertTempUserInfo(UserInfoModel user) {
        db = dbHelper.getWritableDatabase();
        db.delete(USER_INFO_TEMP_TABLE, null, null);
        ContentValues values = new ContentValues();
        values.put(C_USER_GENDER, user.Gender);
        values.put(C_USER_NAME, user.Name);
        values.put(C_USER_FAMILY, user.Family);
        values.put(C_USER_NATIONAL_CODE, user.NationalCode);
        values.put(C_USER_EMAIL, user.Email);
        values.put(C_USER_CODE, user.Code);
        values.put(C_COMPANY_NAME, user.CompanyName);
        long res = db.insertWithOnConflict(USER_INFO_TEMP_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d(TAG, "Temp User Info Inserted");
        db.close();
        return res;
    }
    public long insertTempCarInfo(UserInfoModel user) {
        db = dbHelper.getWritableDatabase();
        db.delete(CAR_TEMP_TABLE, null, null);
        ContentValues values = new ContentValues();
        values.put(C_CAR_TYPE, user.CarType);
        values.put(C_CAR_COLOR, user.CarColor);
        values.put(C_CAR_PLATE_NO, user.CarPlateNo);
        long res = db.insertWithOnConflict(CAR_TEMP_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d(TAG, "Temp Car Info Inserted");
        db.close();
        return res;
    }

    public long insertTempBankInfo(UserInfoModel user) {
        db = dbHelper.getWritableDatabase();
        db.delete(BANK_TEMP_TABLE, null, null);
        ContentValues values = new ContentValues();
        values.put(C_BANK_ACCOUNT, user.BankAccountNo);
        values.put(C_BANK_NAME, user.BankName);
        values.put(C_BANK_SHABA, user.BankShaba);
        long res = db.insertWithOnConflict(BANK_TEMP_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d(TAG, "Temp Bank Info Inserted");
        db.close();
        return res;
    }


    public long insertImage(ImageResponse img, String imageFilePath) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_IMAGE_ID, img.ImageId);
        values.put(C_IMAGE_TYPE, img.ImageType.toString());
        values.put(C_IMAGE_FILE_PATH, imageFilePath);
        values.put(C_IMAGE_FILE, img.Base64ImageFile);
        long res = db.insertWithOnConflict(IMAGE_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d(TAG, "Image Info Inserted");
        db.close();
        return res;
    }

    public void insertRouteResponse(List<RouteResponse> routeResponseList) {
        db = dbHelper.getWritableDatabase();
        db.delete(NEW_RESPONSE_TABLE, null, null);
        ContentValues values;
        for (RouteResponse routeResponse:routeResponseList){
            values = new ContentValues();
            values.put(C_ROUTE_ID, routeResponse.RouteId);
            values.put(C_NRR_SRC_LAT, routeResponse.SrcLatitude);
            values.put(C_NRR_SRC_LNG, routeResponse.SrcLongitude);
            values.put(C_NRR_SRC_ADD, routeResponse.SrcAddress);
            values.put(C_NRR_DST_LAT, routeResponse.DstLatitude);
            values.put(C_NRR_DST_LNG, routeResponse.DstLongitude);
            values.put(C_NRR_DST_ADD, routeResponse.DstAddress);
            values.put(C_NRR_ACCOMPANY, routeResponse.AccompanyCount);
            values.put(C_NRR_DRIVE, routeResponse.IsDrive);
            values.put(C_NRR_TIMING_STR, routeResponse.TimingString);
            values.put(C_NRR_DATE_STR, routeResponse.DateString);
            values.put(C_NRR_PRICING_STR, routeResponse.PricingString);
            values.put(C_NRR_SUGGEST_COUNT, routeResponse.SuggestCount);
            values.put(C_NRR_STATE, routeResponse.RouteRequestState);
            values.put(C_NRR_ROUTE_UID, routeResponse.RouteUId);
            values.put(C_NRR_NEW_SUGGEST_COUNT, routeResponse.NewSuggestCount);
            values.put(C_NRR_IS_SAT, routeResponse.Sat);
            values.put(C_NRR_IS_SUN, routeResponse.Sun);
            values.put(C_NRR_IS_MON, routeResponse.Mon);
            values.put(C_NRR_IS_TUE, routeResponse.Tue);
            values.put(C_NRR_IS_WED, routeResponse.Wed);
            values.put(C_NRR_IS_THU, routeResponse.Thu);
            values.put(C_NRR_IS_FRI, routeResponse.Fri);
            db.insertWithOnConflict(NEW_RESPONSE_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            Log.d(TAG, "routeRequest info inserted");
        }
        db.close();
    }

    public void insertContacts(List<ContactModel> contactModelList) {
        db = dbHelper.getWritableDatabase();
        db.delete(NEW_CONTACT_TABLE, null, null);
        ContentValues values;
        for (ContactModel contactModel:contactModelList){
            values = new ContentValues();
            values.put(C_C_CONTACT_ID, contactModel.ContactId);
            values.put(C_C_Name, contactModel.Name);
            values.put(C_C_FAMILY, contactModel.Family);
            values.put(C_C_GENDER, contactModel.Gender);
            values.put(C_C_LAST_MSG_TIME, contactModel.LastMsgTime);
            values.put(C_C_LAST_MSG, contactModel.LastMsg);
            values.put(C_C_SUPPORT, contactModel.IsSupport);
            values.put(C_C_DRIVER, contactModel.IsDriver);
            values.put(C_C_RIDE_ACCEPT, contactModel.IsRideAccepted);
            values.put(C_C_PASSENGER_ACCEPT, contactModel.IsPassengerAccepted);
            values.put(C_C_IMAGE_ID, contactModel.UserImageId);
            values.put(C_C_ABOUT_USER, contactModel.AboutUser);
            db.insertWithOnConflict(NEW_CONTACT_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            Log.d(TAG, "contactModel info inserted");
        }
        db.close();
    }

    public long insertNewRouteSrc(RouteRequest routeRequest) {
        db = dbHelper.getWritableDatabase();
        db.delete(NEW_REQUEST_TABLE, null, null);
        ContentValues values = new ContentValues();
        values.put(C_NR_SERVICE_TYPE, routeRequest.ServiceType.toString());
        values.put(C_NR_EVENT_ID, routeRequest.EventId);
        values.put(C_NR_SRC_LAT, routeRequest.SrcLatitude);
        values.put(C_NR_SRC_LNG, routeRequest.SrcLongitude);
        values.put(C_NR_SRC_ADD, routeRequest.SrcGAddress);
        //null problem
        values.put(C_NR_TIMING_OPTION, TimingOptions.Weekly.toString());
        values.put(C_NR_PRICE, routeRequest.CostMinMax);
        long res = db.insertWithOnConflict(NEW_REQUEST_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d(TAG, "New Route Request Src Inserted");
        db.close();
        return res;
    }

    public long insertNewRouteDst(RouteRequest routeRequest) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_NR_DST_LAT, routeRequest.DstLatitude);
        values.put(C_NR_DST_LNG, routeRequest.DstLongitude);
        values.put(C_NR_DST_ADD, routeRequest.DstGAddress);
        values.put(C_NR_PRICE, routeRequest.CostMinMax);
        values.put(C_NR_DRIVE, routeRequest.IsDrive);
        long res = db.update(NEW_REQUEST_TABLE, values, null, null);
        Log.d(TAG, "New Route Request Dst Inserted");
        db.close();
        return res;
    }

    public long insertNewRouteEventDst(RouteRequest routeRequest) {
        long res =0;
        RouteRequest rr= routeRequestQuery();
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_NR_SERVICE_TYPE, routeRequest.ServiceType.toString());
        values.put(C_NR_EVENT_ID, routeRequest.EventId);
        values.put(C_NR_DST_LAT, routeRequest.DstLatitude);
        values.put(C_NR_DST_LNG, routeRequest.DstLongitude);
        values.put(C_NR_DST_ADD, routeRequest.DstGAddress);
        values.put(C_NR_PRICE, routeRequest.CostMinMax);
        values.put(C_NR_DRIVE, routeRequest.IsDrive);
        //null problem
        values.put(C_NR_TIMING_OPTION, TimingOptions.Weekly.toString());
        values.put(C_NR_PRICE, routeRequest.CostMinMax);
        if(rr.EventId>0){
           res = db.update(NEW_REQUEST_TABLE, values, null, null);
        }else {
            res = db.insertWithOnConflict(NEW_REQUEST_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }

        Log.d(TAG, "New Route Request Dst Inserted");
        db.close();
        return res;
    }


    public long insertNewRouteIndex(String index) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_NR_RECOMMEND_ID, index);
        long res = db.update(NEW_REQUEST_TABLE, values, null, null);
        Log.d(TAG, "New Route Request Path Id Inserted");
        db.close();
        return res;
    }

    public long insertNewRouteDate(RouteRequest routeRequest) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(C_NR_TIMING_OPTION, routeRequest.TimingOption.toString());
        values.put(C_NR_THE_DATE, routeRequest.TheDateString());
        values.put(C_NR_THE_TIME, routeRequest.TheTimeString());
        values.put(C_NR_THE_RETURN_TIME, routeRequest.TheReturnTimeString());
        values.put(C_NR_IS_RETURN, routeRequest.IsReturn);
        values.put(C_NR_SAT, routeRequest.SatDatetimeString());
        values.put(C_NR_SUN, routeRequest.SunDatetimeString());
        values.put(C_NR_MON, routeRequest.MonDatetimeString());
        values.put(C_NR_TUE, routeRequest.TueDatetimeString());
        values.put(C_NR_WED, routeRequest.WedDatetimeString());
        values.put(C_NR_THU, routeRequest.ThuDatetimeString());
        values.put(C_NR_FRI, routeRequest.FriDatetimeString());
        long res = db.update(NEW_REQUEST_TABLE, values, null, null);
        Log.d(TAG, "New Route Request Date Inserted");
        db.close();
        return res;
    }

    public long insertMessages(List<MessageModel> msgs,long contactId) {
        db = dbHelper.getWritableDatabase();
        int res = db.delete(MESSAGING_TABLE, C_MSG_CONTACT_ID + " =? ", new String[]{String.valueOf(contactId)});
        long longRes=0;
        if(res>0){
            for (MessageModel msg:msgs){
                ContentValues values = new ContentValues();
                values.put(C_MSG_CONTACT_ID, msg.GroupId);
                values.put(C_MSG_MESSAGE_ID, msg.CommentId);
                values.put(C_MSG_NAME_FAMILY, msg.NameFamily);
                values.put(C_MSG_TIME_STRING, msg.TimingString);
                values.put(C_MSG_MESSAGE, msg.Comment);
                values.put(C_MSG_USER_PIC, msg.Base64UserPic);
                longRes =+ db.insertWithOnConflict(MESSAGING_TABLE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
        }
        Log.d(TAG, "Messages Inserted");
        db.close();
        return longRes;
    }


    public List<MessageModel> messagesQuery(long contactId) {
        db = dbHelper.getReadableDatabase();
        List<MessageModel> messageModelList = new ArrayList<>();
        Cursor cursor = db.query(MESSAGING_TABLE, null, C_MSG_CONTACT_ID + " = ?", new String[]{String.valueOf(contactId)}, null, null, C_MSG_MESSAGE_ID);
        while (cursor.moveToNext()) {
            MessageModel messageModel = new MessageModel();
            messageModel.GroupId=cursor.getInt(cursor.getColumnIndex(UserData.C_MSG_CONTACT_ID));
            messageModel.CommentId=cursor.getLong(cursor.getColumnIndex(UserData.C_MSG_MESSAGE_ID));
            messageModel.NameFamily=cursor.getString(cursor.getColumnIndex(UserData.C_MSG_NAME_FAMILY));
            messageModel.TimingString=cursor.getString(cursor.getColumnIndex(UserData.C_MSG_TIME_STRING));
            messageModel.Comment=cursor.getString(cursor.getColumnIndex(UserData.C_MSG_MESSAGE));
            messageModel.IsDeletable=cursor.getInt(cursor.getColumnIndex(UserData.C_MSG_IS_DELETABLE))>0;
            messageModel.Base64UserPic=cursor.getString(cursor.getColumnIndex(UserData.C_MSG_USER_PIC));
            messageModelList.add(messageModel);
        }
        db.close();
        return messageModelList;
    }


    public RouteRequest routeRequestQuery() {
        db = dbHelper.getReadableDatabase();
        RouteRequest routeRequest = new RouteRequest();
        Cursor cursor = db.query(NEW_REQUEST_TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            routeRequest.ServiceType = ServiceTypes.valueOf(cursor.getString(cursor.getColumnIndex(UserData.C_NR_SERVICE_TYPE)));
            routeRequest.EventId = cursor.getLong(cursor.getColumnIndex(UserData.C_NR_EVENT_ID));
            routeRequest.SrcLatitude = cursor.getString(cursor.getColumnIndex(UserData.C_NR_SRC_LAT));
            routeRequest.SrcLongitude = cursor.getString(cursor.getColumnIndex(UserData.C_NR_SRC_LNG));
            routeRequest.SrcGAddress = cursor.getString(cursor.getColumnIndex(UserData.C_NR_SRC_ADD));
            routeRequest.DstLatitude = cursor.getString(cursor.getColumnIndex(UserData.C_NR_DST_LAT));
            routeRequest.DstLongitude = cursor.getString(cursor.getColumnIndex(UserData.C_NR_DST_LNG));
            routeRequest.DstGAddress = cursor.getString(cursor.getColumnIndex(UserData.C_NR_DST_ADD));
            routeRequest.TimingOption = TimingOptions.valueOf(cursor.getString(cursor.getColumnIndex(UserData.C_NR_TIMING_OPTION)));
            routeRequest.TheTime = castStringToCal(cursor.getString(cursor.getColumnIndex(UserData.C_NR_THE_TIME)));
            routeRequest.TheReturnTime = castStringToCal(cursor.getString(cursor.getColumnIndex(UserData.C_NR_THE_RETURN_TIME)));
            routeRequest.IsReturn = cursor.getInt(cursor.getColumnIndex(UserData.C_NR_IS_RETURN))>0;
            routeRequest.TheDate = castStringToDate(cursor.getString(cursor.getColumnIndex(UserData.C_NR_THE_DATE)));
            routeRequest.SatDatetime = castStringToCal(cursor.getString(cursor.getColumnIndex(UserData.C_NR_SAT)));
            routeRequest.SunDatetime = castStringToCal(cursor.getString(cursor.getColumnIndex(UserData.C_NR_SUN)));
            routeRequest.MonDatetime = castStringToCal(cursor.getString(cursor.getColumnIndex(UserData.C_NR_MON)));
            routeRequest.TueDatetime = castStringToCal(cursor.getString(cursor.getColumnIndex(UserData.C_NR_TUE)));
            routeRequest.WedDatetime = castStringToCal(cursor.getString(cursor.getColumnIndex(UserData.C_NR_WED)));
            routeRequest.ThuDatetime = castStringToCal(cursor.getString(cursor.getColumnIndex(UserData.C_NR_THU)));
            routeRequest.FriDatetime = castStringToCal(cursor.getString(cursor.getColumnIndex(UserData.C_NR_FRI)));
            routeRequest.IsDrive = cursor.getInt(cursor.getColumnIndex(UserData.C_NR_DRIVE))>0;
            routeRequest.CostMinMax = Float.parseFloat(cursor.getString(cursor.getColumnIndex(UserData.C_NR_PRICE)));
            routeRequest.RecommendPathId = cursor.getLong(cursor.getColumnIndex(UserData.C_NR_RECOMMEND_ID));
        }
        db.close();
        return routeRequest;
    }


    public List<RouteResponse> routeResponseListQuery() {
        db = dbHelper.getReadableDatabase();
        RouteResponse routeResponse = new RouteResponse();
        List<RouteResponse> routeResponseList = new ArrayList<RouteResponse>();
        Cursor cursor = db.query(NEW_RESPONSE_TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            routeResponse = new RouteResponse();
            routeResponse.RouteId = cursor.getInt(cursor.getColumnIndex(UserData.C_ROUTE_ID));
            routeResponse.SrcLatitude = cursor.getString(cursor.getColumnIndex(UserData.C_NRR_SRC_LAT));
            routeResponse.SrcLongitude = cursor.getString(cursor.getColumnIndex(UserData.C_NRR_SRC_LNG));
            routeResponse.SrcAddress = cursor.getString(cursor.getColumnIndex(UserData.C_NRR_SRC_ADD));
            routeResponse.DstLatitude = cursor.getString(cursor.getColumnIndex(UserData.C_NRR_DST_LAT));
            routeResponse.DstLongitude = cursor.getString(cursor.getColumnIndex(UserData.C_NRR_DST_LNG));
            routeResponse.DstAddress = cursor.getString(cursor.getColumnIndex(UserData.C_NRR_DST_ADD));
            routeResponse.AccompanyCount = cursor.getInt(cursor.getColumnIndex(UserData.C_NRR_ACCOMPANY));
            routeResponse.IsDrive = cursor.getInt(cursor.getColumnIndex(UserData.C_NRR_DRIVE))>0;
            routeResponse.TimingString = cursor.getString(cursor.getColumnIndex(UserData.C_NRR_TIMING_STR));
            routeResponse.DateString = cursor.getString(cursor.getColumnIndex(UserData.C_NRR_DATE_STR));
            routeResponse.PricingString = cursor.getString(cursor.getColumnIndex(UserData.C_NRR_PRICING_STR));
            routeResponse.SuggestCount = cursor.getInt(cursor.getColumnIndex(UserData.C_NRR_SUGGEST_COUNT));
            routeResponse.RouteRequestState = cursor.getString(cursor.getColumnIndex(UserData.C_NRR_STATE));
            routeResponse.NewSuggestCount = cursor.getInt(cursor.getColumnIndex(UserData.C_NRR_NEW_SUGGEST_COUNT));
            routeResponse.RouteUId = cursor.getString(cursor.getColumnIndex(UserData.C_NRR_ROUTE_UID));
            routeResponse.Sat = cursor.getInt(cursor.getColumnIndex(UserData.C_NRR_IS_SAT))>0;
            routeResponse.Sun = cursor.getInt(cursor.getColumnIndex(UserData.C_NRR_IS_SUN))>0;
            routeResponse.Mon = cursor.getInt(cursor.getColumnIndex(UserData.C_NRR_IS_MON))>0;
            routeResponse.Tue = cursor.getInt(cursor.getColumnIndex(UserData.C_NRR_IS_TUE))>0;
            routeResponse.Wed = cursor.getInt(cursor.getColumnIndex(UserData.C_NRR_IS_WED))>0;
            routeResponse.Thu = cursor.getInt(cursor.getColumnIndex(UserData.C_NRR_IS_THU))>0;
            routeResponse.Fri = cursor.getInt(cursor.getColumnIndex(UserData.C_NRR_IS_FRI))>0;
            routeResponseList.add(routeResponse);
        }
        db.close();
        return routeResponseList;
    }

    public List<ContactModel> contactModelListQuery() {
        db = dbHelper.getReadableDatabase();
        ContactModel contactModel = new ContactModel();
        List<ContactModel> contactModelList = new ArrayList<ContactModel>();
        Cursor cursor = db.query(NEW_CONTACT_TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            contactModel = new ContactModel();
            contactModel.ContactId = cursor.getInt(cursor.getColumnIndex(UserData.C_C_CONTACT_ID));
            contactModel.Name = cursor.getString(cursor.getColumnIndex(UserData.C_C_Name));
            contactModel.Family = cursor.getString(cursor.getColumnIndex(UserData.C_C_FAMILY));
            contactModel.Gender = cursor.getString(cursor.getColumnIndex(UserData.C_C_GENDER));
            contactModel.LastMsgTime = cursor.getString(cursor.getColumnIndex(UserData.C_C_LAST_MSG_TIME));
            contactModel.LastMsg = cursor.getString(cursor.getColumnIndex(UserData.C_C_LAST_MSG));
            contactModel.IsSupport = cursor.getInt(cursor.getColumnIndex(UserData.C_C_SUPPORT));
            contactModel.IsDriver = cursor.getInt(cursor.getColumnIndex(UserData.C_C_DRIVER));
            contactModel.IsRideAccepted = cursor.getInt(cursor.getColumnIndex(UserData.C_C_RIDE_ACCEPT));
            contactModel.IsPassengerAccepted = cursor.getInt(cursor.getColumnIndex(UserData.C_C_PASSENGER_ACCEPT));
            contactModel.UserImageId = cursor.getString(cursor.getColumnIndex(UserData.C_C_IMAGE_ID));
            contactModel.AboutUser = cursor.getString(cursor.getColumnIndex(UserData.C_C_ABOUT_USER));
            contactModelList.add(contactModel);
        }
        db.close();
        return contactModelList;
    }


    public List<RouteRequest> routeRequestListQuery() {
        db = dbHelper.getReadableDatabase();
        RouteRequest routeRequest = new RouteRequest();
        List<RouteRequest> routeRequestList = new ArrayList<RouteRequest>();
        Cursor cursor = db.query(NEW_REQUEST_TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            routeRequest = new RouteRequest();
            routeRequest.ServiceType = ServiceTypes.valueOf(cursor.getString(cursor.getColumnIndex(UserData.C_NR_SERVICE_TYPE)));
            routeRequest.EventId = cursor.getLong(cursor.getColumnIndex(UserData.C_NR_EVENT_ID));
            routeRequest.SrcLatitude = cursor.getString(cursor.getColumnIndex(UserData.C_NR_SRC_LAT));
            routeRequest.SrcLongitude = cursor.getString(cursor.getColumnIndex(UserData.C_NR_SRC_LNG));
            routeRequest.SrcGAddress = cursor.getString(cursor.getColumnIndex(UserData.C_NR_SRC_ADD));
            routeRequest.DstLatitude = cursor.getString(cursor.getColumnIndex(UserData.C_NR_DST_LAT));
            routeRequest.DstLongitude = cursor.getString(cursor.getColumnIndex(UserData.C_NR_DST_LNG));
            routeRequest.DstGAddress = cursor.getString(cursor.getColumnIndex(UserData.C_NR_DST_ADD));
            routeRequest.TimingOption = TimingOptions.valueOf(cursor.getString(cursor.getColumnIndex(UserData.C_NR_TIMING_OPTION)));
            routeRequest.TheTime = castStringToCal(cursor.getString(cursor.getColumnIndex(UserData.C_NR_THE_TIME)));
            routeRequest.TheDate = castStringToDate(cursor.getString(cursor.getColumnIndex(UserData.C_NR_THE_DATE)));
            routeRequest.SatDatetime = castStringToCal(cursor.getString(cursor.getColumnIndex(UserData.C_NR_SAT)));
            routeRequest.SunDatetime = castStringToCal(cursor.getString(cursor.getColumnIndex(UserData.C_NR_SUN)));
            routeRequest.MonDatetime = castStringToCal(cursor.getString(cursor.getColumnIndex(UserData.C_NR_MON)));
            routeRequest.TueDatetime = castStringToCal(cursor.getString(cursor.getColumnIndex(UserData.C_NR_TUE)));
            routeRequest.WedDatetime = castStringToCal(cursor.getString(cursor.getColumnIndex(UserData.C_NR_WED)));
            routeRequest.ThuDatetime = castStringToCal(cursor.getString(cursor.getColumnIndex(UserData.C_NR_THU)));
            routeRequest.FriDatetime = castStringToCal(cursor.getString(cursor.getColumnIndex(UserData.C_NR_FRI)));
            routeRequest.IsDrive = cursor.getInt(cursor.getColumnIndex(UserData.C_NR_DRIVE))>0;
            routeRequest.CostMinMax = Float.parseFloat(cursor.getString(cursor.getColumnIndex(UserData.C_NR_PRICE)));
            routeRequest.RecommendPathId = cursor.getLong(cursor.getColumnIndex(UserData.C_NR_RECOMMEND_ID));
            routeRequestList.add(routeRequest);
        }
        db.close();
        return routeRequestList;
    }


    private Calendar castStringToCal(String dateString) {
        Calendar cal = Calendar.getInstance();
        if (dateString == null || dateString.equals("")) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        try {
            Date date = format.parse(dateString);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal;
    }

    private Date castStringToDate(String dateString) {
        if (dateString == null || dateString.equals("")) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    public UserInfoModel userInfoQuery() {
        db = dbHelper.getReadableDatabase();
        UserInfoModel userInfoModel = new UserInfoModel();
        Cursor cursor = db.query(NEW_USER_INFO_TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            userInfoModel.Gender = cursor.getString(cursor.getColumnIndex(UserData.C_USER_GENDER));
            userInfoModel.Name = cursor.getString(cursor.getColumnIndex(UserData.C_USER_NAME));
            userInfoModel.Family = cursor.getString(cursor.getColumnIndex(UserData.C_USER_FAMILY));
            userInfoModel.Mobile = cursor.getString(cursor.getColumnIndex(UserData.C_USER_MOBILE));
            userInfoModel.Email = cursor.getString(cursor.getColumnIndex(UserData.C_USER_EMAIL));
            userInfoModel.Code = cursor.getString(cursor.getColumnIndex(UserData.C_USER_CODE));
            userInfoModel.CompanyName = cursor.getString(cursor.getColumnIndex(UserData.C_COMPANY_NAME));
            userInfoModel.NationalCode = cursor.getString(cursor.getColumnIndex(UserData.C_USER_NATIONAL_CODE));
            userInfoModel.NationalCardImageId = cursor.getString(cursor.getColumnIndex(UserData.C_USER_NATIONAL_CARD_IMAGE_ID));
            userInfoModel.UserImageId = cursor.getString(cursor.getColumnIndex(UserData.C_USER_IMAGE_ID));
            userInfoModel.LicenseImageId = cursor.getString(cursor.getColumnIndex(UserData.C_LICENSE_IMAGE_ID));
            userInfoModel.CarType = cursor.getString(cursor.getColumnIndex(UserData.C_CAR_TYPE));
            userInfoModel.CarColor = cursor.getString(cursor.getColumnIndex(UserData.C_CAR_COLOR));
            userInfoModel.CarPlateNo = cursor.getString(cursor.getColumnIndex(UserData.C_CAR_PLATE_NO));
            userInfoModel.CarCardImageId = cursor.getString(cursor.getColumnIndex(UserData.C_CAR_IMAGE_ID_1));
            userInfoModel.CarCardBckImageId = cursor.getString(cursor.getColumnIndex(UserData.C_CAR_IMAGE_ID_2));
            userInfoModel.BankName = cursor.getString(cursor.getColumnIndex(UserData.C_BANK_NAME));
            userInfoModel.BankShaba = cursor.getString(cursor.getColumnIndex(UserData.C_BANK_SHABA));
            userInfoModel.BankAccountNo = cursor.getString(cursor.getColumnIndex(UserData.C_BANK_ACCOUNT));
            userInfoModel.BankImageId = cursor.getString(cursor.getColumnIndex(UserData.C_BANK_CARD_IMAGE_ID));
            userInfoModel.CompanyImageId= cursor.getString(cursor.getColumnIndex(UserData.C_COMPANY_IMAGE_ID));
        }
        db.close();
        return userInfoModel;
    }

    public UserInfoModel tempUserInfoQuery() {
        db = dbHelper.getReadableDatabase();
        UserInfoModel userInfoModel = new UserInfoModel();
        Cursor cursor = db.query(USER_INFO_TEMP_TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            userInfoModel.Gender = cursor.getString(cursor.getColumnIndex(UserData.C_USER_GENDER));
            userInfoModel.Name = cursor.getString(cursor.getColumnIndex(UserData.C_USER_NAME));
            userInfoModel.Family = cursor.getString(cursor.getColumnIndex(UserData.C_USER_FAMILY));
            //userInfoModel.Mobile = cursor.getString(cursor.getColumnIndex(UserData.C_USER_MOBILE));
            userInfoModel.Email = cursor.getString(cursor.getColumnIndex(UserData.C_USER_EMAIL));
            userInfoModel.Code = cursor.getString(cursor.getColumnIndex(UserData.C_USER_CODE));
            userInfoModel.CompanyName = cursor.getString(cursor.getColumnIndex(UserData.C_COMPANY_NAME));
            userInfoModel.NationalCode = cursor.getString(cursor.getColumnIndex(UserData.C_USER_NATIONAL_CODE));
        }
        db.close();
        return userInfoModel;
    }

    public UserInfoModel tempCarInfoQuery() {
        db = dbHelper.getReadableDatabase();
        UserInfoModel userInfoModel = new UserInfoModel();
        Cursor cursor = db.query(CAR_TEMP_TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            userInfoModel.CarType = cursor.getString(cursor.getColumnIndex(UserData.C_CAR_TYPE));
            userInfoModel.CarColor = cursor.getString(cursor.getColumnIndex(UserData.C_CAR_COLOR));
            userInfoModel.CarPlateNo = cursor.getString(cursor.getColumnIndex(UserData.C_CAR_PLATE_NO));
        }
        db.close();
        return userInfoModel;
    }

    public AboutMeModel aboutMeQuery() {
        db = dbHelper.getReadableDatabase();
        AboutMeModel aboutMeModel= new AboutMeModel();
        Cursor cursor = db.query(ABOUT_ME_TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            aboutMeModel.Desc=cursor.getString(cursor.getColumnIndex(UserData.C_AM_DESC));
        }
        db.close();
        return aboutMeModel;
    }

    public InviteModel inviteQuery() {
        db = dbHelper.getReadableDatabase();
        InviteModel inviteModel= new InviteModel();
        Cursor cursor = db.query(INVITE_TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            inviteModel.InviteCode=cursor.getString(cursor.getColumnIndex(UserData.C_I_INVITE_CODE));
            inviteModel.InviteLink=cursor.getString(cursor.getColumnIndex(UserData.C_I_INVITE_LINK));
        }
        db.close();
        return inviteModel;
    }


    public UserInfoModel tempBankInfoQuery() {
        db = dbHelper.getReadableDatabase();
        UserInfoModel userInfoModel = new UserInfoModel();
        Cursor cursor = db.query(BANK_TEMP_TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            userInfoModel.BankName = cursor.getString(cursor.getColumnIndex(UserData.C_BANK_NAME));
            userInfoModel.BankShaba = cursor.getString(cursor.getColumnIndex(UserData.C_BANK_SHABA));
            userInfoModel.BankAccountNo = cursor.getString(cursor.getColumnIndex(UserData.C_BANK_ACCOUNT));
        }
        db.close();
        return userInfoModel;
    }


    public ImageResponse imageQuery(String imageId) {
        db = dbHelper.getReadableDatabase();
        ImageResponse imageModel = null;
        Cursor cursor = db.query(IMAGE_TABLE, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String image = cursor.getString(cursor.getColumnIndex(UserData.C_IMAGE_ID));
            if(imageId.equals(image)){
                imageModel = new ImageResponse();
                imageModel.ImageId=cursor.getString(cursor.getColumnIndex(UserData.C_IMAGE_ID));
                imageModel.ImageType= ImageTypes.valueOf(cursor.getString(cursor.getColumnIndex(UserData.C_IMAGE_TYPE)));
                imageModel.ImageFilePath = cursor.getString(cursor.getColumnIndex(UserData.C_IMAGE_FILE_PATH));
                imageModel.Base64ImageFile=cursor.getString(cursor.getColumnIndex(UserData.C_IMAGE_FILE));
                break;
            }
        }
        db.close();
        return imageModel;
    }

    public void DeleteRouteRequest() {
        db = dbHelper.getWritableDatabase();
        db.delete(NEW_REQUEST_TABLE, null, null);
        db.close();
    }

    public void DeleteUserInfo() {
        db = dbHelper.getWritableDatabase();
        db.delete(NEW_USER_INFO_TABLE, null, null);
        db.close();
    }

    public void DeleteTempUserInfo() {
        db = dbHelper.getWritableDatabase();
        db.delete(USER_INFO_TEMP_TABLE, null, null);
        db.close();
    }

    public void DeleteAboutMe() {
        db = dbHelper.getWritableDatabase();
        db.delete(ABOUT_ME_TABLE, null, null);
        db.close();
    }

    public void DeleteTempCarInfo() {
        db = dbHelper.getWritableDatabase();
        db.delete(CAR_TEMP_TABLE, null, null);
        db.close();
    }

    public void DeleteTempBankInfo() {
        db = dbHelper.getWritableDatabase();
        db.delete(BANK_TEMP_TABLE, null, null);
        db.close();
    }

    class DbHelper extends SQLiteOpenHelper {
        public DbHelper() {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            /*String sql = String.format("Create table %s" + "(%s text ,%s text ,%s text,%s text,%s text,%s text,%s text,%s integer)",
                    USER_INFO_TABLE, C_USER_MOBILE, C_USER_GENDER, C_USER_NAME, C_USER_FAMILY, C_USER_EMAIL, C_USER_NATIONAL_CODE, C_USER_PIC, C_USER_UPDATE_FLAG);
            Log.d(TAG, String.format("onCreate table %s with sql %s", USER_INFO_TABLE, sql));
            db.execSQL(sql);
            sql = String.format("Create table %s" + "(%s text,%s text,%s integer)",
                    LICENSE_TABLE, C_LICENSE_NO, C_LICENSE_PIC, C_LICENSE_UPDATE_FLAG);
            Log.d(TAG, String.format("onCreate table %s with sql %s", LICENSE_TABLE, sql));
            db.execSQL(sql);
            sql = String.format("Create table %s" + "(%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s integer)",
                    CAR_TABLE, C_CAR_TYPE, C_CAR_COLOR, C_CAR_PLATE_NO, C_CAR_PIC_1, C_CAR_PIC_2, C_CAR_UPDATE_FLAG);
            Log.d(TAG, String.format("onCreate table %s with sql %s", CAR_TABLE, sql));
            db.execSQL(sql);*/
            String sql = String.format("Create table %s" + "(%s text ,%s text ,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text)",
                    NEW_USER_INFO_TABLE, C_USER_MOBILE, C_USER_GENDER, C_USER_NAME, C_USER_FAMILY, C_USER_EMAIL,C_USER_CODE, C_USER_NATIONAL_CODE, C_USER_NATIONAL_CARD_IMAGE_ID, C_USER_IMAGE_ID,
                    C_LICENSE_IMAGE_ID,
                    C_CAR_COLOR,C_CAR_PLATE_NO,C_CAR_TYPE,C_CAR_IMAGE_ID_1,C_CAR_IMAGE_ID_2,
                    C_BANK_ACCOUNT,C_BANK_NAME,C_BANK_SHABA,C_BANK_CARD_IMAGE_ID,C_COMPANY_NAME,C_COMPANY_IMAGE_ID);
            Log.d(TAG, String.format("onCreate table %s with sql %s", NEW_USER_INFO_TABLE, sql));
            db.execSQL(sql);
            sql = String.format("Create table %s" + "(%s text ,%s text ,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text)",
                    NEW_REQUEST_TABLE, C_NR_SERVICE_TYPE, C_NR_EVENT_ID, C_NR_SRC_LAT, C_NR_SRC_LNG, C_NR_SRC_ADD, C_NR_DST_LAT, C_NR_DST_LNG, C_NR_DST_ADD, C_NR_TIMING_OPTION, C_NR_THE_TIME, C_NR_THE_DATE, C_NR_SAT, C_NR_SUN, C_NR_MON, C_NR_TUE, C_NR_WED, C_NR_THU, C_NR_FRI, C_NR_DRIVE, C_NR_PRICE, C_NR_RECOMMEND_ID,C_NR_THE_RETURN_TIME,C_NR_IS_RETURN);
            Log.d(TAG, String.format("onCreate table %s with sql %s", NEW_REQUEST_TABLE, sql));
            db.execSQL(sql);
            sql = String.format("Create table %s" + "(%s text ,%s text ,%s text,%s text,%s text,%s text)",
                    MESSAGING_TABLE, C_MSG_CONTACT_ID, C_MSG_MESSAGE_ID, C_MSG_NAME_FAMILY, C_MSG_TIME_STRING, C_MSG_MESSAGE, C_MSG_IS_DELETABLE);
            Log.d(TAG, String.format("onCreate table %s with sql %s", NEW_REQUEST_TABLE, sql));
            db.execSQL(sql);
            sql = String.format("Create table %s" + "(%s text ,%s text ,%s text,%s text)",
                    IMAGE_TABLE, C_IMAGE_ID, C_IMAGE_TYPE, C_IMAGE_FILE ,C_IMAGE_FILE_PATH);
            Log.d(TAG, String.format("onCreate table %s with sql %s", IMAGE_TABLE, sql));
            db.execSQL(sql);
            sql = String.format("Create table %s" + "(%s text ,%s text ,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text)",
                    NEW_RESPONSE_TABLE, C_ROUTE_ID,C_NRR_SRC_LAT,C_NRR_SRC_LNG,C_NRR_SRC_ADD,C_NRR_DST_LAT,C_NRR_DST_LNG,C_NRR_DST_ADD,
                    C_NRR_ACCOMPANY,C_NRR_DRIVE,C_NRR_TIMING_STR,C_NRR_PRICING_STR,C_NRR_SUGGEST_COUNT,C_NRR_STATE,C_NRR_DATE_STR,
                    C_NRR_ROUTE_UID,C_NRR_NEW_SUGGEST_COUNT,C_NRR_IS_SAT,C_NRR_IS_SUN,C_NRR_IS_MON,C_NRR_IS_TUE,C_NRR_IS_WED,C_NRR_IS_THU,C_NRR_IS_FRI);
            Log.d(TAG, String.format("onCreate table %s with sql %s", NEW_RESPONSE_TABLE, sql));
            db.execSQL(sql);
            sql = String.format("Create table %s" + "(%s text ,%s text ,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text)",
                    NEW_CONTACT_TABLE, C_C_CONTACT_ID,C_C_Name,C_C_FAMILY,C_C_GENDER,C_C_LAST_MSG_TIME,C_C_LAST_MSG,C_C_SUPPORT,C_C_DRIVER,C_C_RIDE_ACCEPT,C_C_IMAGE_ID,C_C_ABOUT_USER,C_C_PASSENGER_ACCEPT);
            Log.d(TAG, String.format("onCreate table %s with sql %s", NEW_CONTACT_TABLE, sql));
            db.execSQL(sql);
            sql = String.format("Create table %s" + "(%s text ,%s text ,%s text,%s text,%s text,%s text,%s text)",
                    USER_INFO_TEMP_TABLE, C_USER_GENDER,C_USER_NAME,C_USER_FAMILY,C_USER_NATIONAL_CODE,C_USER_EMAIL,C_USER_CODE,C_COMPANY_NAME);
            Log.d(TAG, String.format("onCreate table %s with sql %s", USER_INFO_TEMP_TABLE, sql));
            db.execSQL(sql);
            sql = String.format("Create table %s" + "(%s text ,%s text ,%s text)",
                    CAR_TEMP_TABLE, C_CAR_TYPE,C_CAR_COLOR,C_CAR_PLATE_NO);
            Log.d(TAG, String.format("onCreate table %s with sql %s", CAR_TEMP_TABLE, sql));
            db.execSQL(sql);
            sql = String.format("Create table %s" + "(%s text ,%s text ,%s text)",
                    BANK_TEMP_TABLE, C_BANK_ACCOUNT,C_BANK_NAME,C_BANK_SHABA);
            Log.d(TAG, String.format("onCreate table %s with sql %s", BANK_TEMP_TABLE, sql));
            db.execSQL(sql);
            sql = String.format("Create table %s" + "(%s text)",
                    ABOUT_ME_TABLE, C_AM_DESC);
            Log.d(TAG, String.format("onCreate table %s with sql %s", ABOUT_ME_TABLE, sql));
            db.execSQL(sql);
            sql = String.format("Create table %s" + "(%s text,%s text)",
                    INVITE_TABLE, C_I_INVITE_CODE,C_I_INVITE_LINK);
            Log.d(TAG, String.format("onCreate table %s with sql %s", INVITE_TABLE, sql));
            db.execSQL(sql);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion == 1) {
                String sql = String.format("Create table %s" + "(%s text ,%s text ,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text)",
                        NEW_REQUEST_TABLE, C_NR_SERVICE_TYPE, C_NR_EVENT_ID, C_NR_SRC_LAT, C_NR_SRC_LNG, C_NR_SRC_ADD, C_NR_DST_LAT, C_NR_DST_LNG, C_NR_DST_ADD, C_NR_TIMING_OPTION, C_NR_THE_TIME, C_NR_THE_DATE, C_NR_SAT, C_NR_SUN, C_NR_MON, C_NR_TUE, C_NR_WED, C_NR_THU, C_NR_FRI, C_NR_DRIVE, C_NR_PRICE, C_NR_RECOMMEND_ID);
                Log.d(TAG, String.format("onCreate table %s with sql %s", NEW_REQUEST_TABLE, sql));
                db.execSQL(sql);
                sql = String.format("Create table %s" + "(%s text ,%s text ,%s text,%s text,%s text,%s text,%s text)",
                        MESSAGING_TABLE, C_MSG_CONTACT_ID, C_MSG_MESSAGE_ID, C_MSG_NAME_FAMILY, C_MSG_TIME_STRING, C_MSG_MESSAGE, C_MSG_IS_DELETABLE, C_MSG_USER_PIC);
                Log.d(TAG, String.format("onCreate table %s with sql %s", NEW_REQUEST_TABLE, sql));
                db.execSQL(sql);
            }
            if (oldVersion <= 2) {
                String sql = String.format("Create table %s" + "(%s text ,%s text ,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text)",
                        NEW_USER_INFO_TABLE, C_USER_MOBILE, C_USER_GENDER, C_USER_NAME, C_USER_FAMILY, C_USER_EMAIL, C_USER_NATIONAL_CODE, C_USER_NATIONAL_CARD_IMAGE_ID, C_USER_IMAGE_ID,
                        C_LICENSE_IMAGE_ID,
                        C_CAR_COLOR,C_CAR_PLATE_NO,C_CAR_TYPE,C_CAR_IMAGE_ID_1,C_CAR_IMAGE_ID_2,
                        C_BANK_ACCOUNT,C_BANK_NAME,C_BANK_SHABA,C_BANK_CARD_IMAGE_ID);
                Log.d(TAG, String.format("onCreate table %s with sql %s", NEW_USER_INFO_TABLE, sql));
                db.execSQL(sql);
                sql = String.format("Create table %s" + "(%s text ,%s text ,%s text)",
                        IMAGE_TABLE, C_IMAGE_ID, C_IMAGE_TYPE, C_IMAGE_FILE);
                Log.d(TAG, String.format("onCreate table %s with sql %s", IMAGE_TABLE, sql));
                db.execSQL(sql);
            }
            if (oldVersion <= 3) {
                String sql = String.format("Create table %s" + "(%s text ,%s text ,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text)",
                        NEW_RESPONSE_TABLE, C_ROUTE_ID,C_NRR_SRC_LAT,C_NRR_SRC_LNG,C_NRR_SRC_ADD,C_NRR_DST_LAT,C_NRR_DST_LNG,C_NRR_DST_ADD,C_NRR_ACCOMPANY,C_NRR_DRIVE,C_NRR_TIMING_STR,C_NRR_PRICING_STR,C_NRR_SUGGEST_COUNT,C_NRR_STATE);
                Log.d(TAG, String.format("onCreate table %s with sql %s", NEW_RESPONSE_TABLE, sql));
                db.execSQL(sql);
                sql = String.format("Create table %s" + "(%s text ,%s text ,%s text,%s text,%s text,%s text,%s text,%s text,%s text,%s text)",
                        NEW_CONTACT_TABLE, C_C_CONTACT_ID,C_C_Name,C_C_FAMILY,C_C_GENDER,C_C_LAST_MSG_TIME,C_C_LAST_MSG,C_C_SUPPORT,C_C_DRIVER,C_C_RIDE_ACCEPT,C_C_IMAGE_ID);
                Log.d(TAG, String.format("onCreate table %s with sql %s", NEW_CONTACT_TABLE, sql));
                db.execSQL(sql);
            }
            if (oldVersion <= 4){
                String sql = String.format("ALTER TABLE %s" + " ADD COLUMN %s text",
                        NEW_RESPONSE_TABLE, C_NRR_DATE_STR);
                Log.d(TAG, String.format("alter table %s with sql %s", NEW_RESPONSE_TABLE, sql));
                db.execSQL(sql);
                sql = String.format("ALTER TABLE %s" + " ADD COLUMN %s text",
                        NEW_REQUEST_TABLE, C_NR_THE_RETURN_TIME);
                Log.d(TAG, String.format("alter table %s with sql %s", NEW_REQUEST_TABLE, sql));
                db.execSQL(sql);
                sql = String.format("ALTER TABLE %s" + " ADD COLUMN %s text",
                        NEW_REQUEST_TABLE, C_NR_IS_RETURN);
                Log.d(TAG, String.format("alter table %s with sql %s", NEW_REQUEST_TABLE, sql));
                db.execSQL(sql);
            }
            if (oldVersion <= 5){
                String sql = String.format("ALTER TABLE %s" + " ADD COLUMN %s text",
                        NEW_USER_INFO_TABLE, C_USER_CODE);
                Log.d(TAG, String.format("alter table %s with sql %s", NEW_USER_INFO_TABLE, sql));
                db.execSQL(sql);
                sql = String.format("ALTER TABLE %s" + " ADD COLUMN %s text",
                        NEW_USER_INFO_TABLE, C_COMPANY_NAME);
                Log.d(TAG, String.format("alter table %s with sql %s", NEW_USER_INFO_TABLE, sql));
                db.execSQL(sql);
                sql = String.format("ALTER TABLE %s" + " ADD COLUMN %s text",
                        NEW_USER_INFO_TABLE, C_COMPANY_IMAGE_ID);
                Log.d(TAG, String.format("alter table %s with sql %s", NEW_USER_INFO_TABLE, sql));
                db.execSQL(sql);
            }
            if (oldVersion <= 6){
                String sql = String.format("ALTER TABLE %s" + " ADD COLUMN %s text",
                        IMAGE_TABLE, C_IMAGE_FILE_PATH);
                Log.d(TAG, String.format("alter table %s with sql %s", IMAGE_TABLE, sql));
                db.execSQL(sql);
                sql = String.format("Create table %s" + "(%s text ,%s text ,%s text,%s text,%s text,%s text,%s text)",
                        USER_INFO_TEMP_TABLE, C_USER_GENDER,C_USER_NAME,C_USER_FAMILY,C_USER_NATIONAL_CODE,C_USER_EMAIL,C_USER_CODE,C_COMPANY_NAME);
                Log.d(TAG, String.format("onCreate table %s with sql %s", USER_INFO_TEMP_TABLE, sql));
                db.execSQL(sql);
                sql = String.format("Create table %s" + "(%s text ,%s text ,%s text)",
                        CAR_TEMP_TABLE, C_CAR_TYPE,C_CAR_COLOR,C_CAR_PLATE_NO);
                Log.d(TAG, String.format("onCreate table %s with sql %s", CAR_TEMP_TABLE, sql));
                db.execSQL(sql);
                sql = String.format("Create table %s" + "(%s text ,%s text ,%s text)",
                        BANK_TEMP_TABLE, C_BANK_ACCOUNT,C_BANK_NAME,C_BANK_SHABA);
                Log.d(TAG, String.format("onCreate table %s with sql %s", BANK_TEMP_TABLE, sql));
                db.execSQL(sql);
                sql = String.format("Create table %s" + "(%s text)",
                        ABOUT_ME_TABLE, C_AM_DESC);
                Log.d(TAG, String.format("onCreate table %s with sql %s", ABOUT_ME_TABLE, sql));
                db.execSQL(sql);
            }
            if (oldVersion <= 7){
                String sql = String.format("Create table %s" + "(%s text,%s text)",
                        INVITE_TABLE, C_I_INVITE_CODE,C_I_INVITE_LINK);
                Log.d(TAG, String.format("onCreate table %s with sql %s", INVITE_TABLE, sql));
                db.execSQL(sql);
                sql = String.format("ALTER TABLE %s" + " ADD COLUMN %s text",
                        NEW_CONTACT_TABLE, C_C_ABOUT_USER);
                Log.d(TAG, String.format("alter table %s with sql %s", NEW_CONTACT_TABLE, sql));
                db.execSQL(sql);
            }
            if (oldVersion <= 8){
                String sql = String.format("ALTER TABLE %s" + " ADD COLUMN %s text",
                        NEW_CONTACT_TABLE, C_C_PASSENGER_ACCEPT);
                Log.d(TAG, String.format("alter table %s with sql %s", NEW_CONTACT_TABLE, sql));
                db.execSQL(sql);
            }
            if (oldVersion <= 9){
                String sql = String.format("ALTER TABLE %s" + " ADD COLUMN %s text",
                        NEW_RESPONSE_TABLE, C_NRR_ROUTE_UID);
                Log.d(TAG, String.format("alter table %s with sql %s", NEW_RESPONSE_TABLE, sql));
                db.execSQL(sql);
                sql = String.format("ALTER TABLE %s" + " ADD COLUMN %s text",
                        NEW_RESPONSE_TABLE, C_NRR_NEW_SUGGEST_COUNT);
                Log.d(TAG, String.format("alter table %s with sql %s", NEW_RESPONSE_TABLE, sql));
                db.execSQL(sql);
                sql = String.format("ALTER TABLE %s" + " ADD COLUMN %s text",
                        NEW_RESPONSE_TABLE, C_NRR_IS_SAT);
                Log.d(TAG, String.format("alter table %s with sql %s", NEW_RESPONSE_TABLE, sql));
                db.execSQL(sql);
                sql = String.format("ALTER TABLE %s" + " ADD COLUMN %s text",
                        NEW_RESPONSE_TABLE, C_NRR_IS_SUN);
                Log.d(TAG, String.format("alter table %s with sql %s", NEW_RESPONSE_TABLE, sql));
                db.execSQL(sql);
                sql = String.format("ALTER TABLE %s" + " ADD COLUMN %s text",
                        NEW_RESPONSE_TABLE, C_NRR_IS_MON);
                Log.d(TAG, String.format("alter table %s with sql %s", NEW_RESPONSE_TABLE, sql));
                db.execSQL(sql);
                sql = String.format("ALTER TABLE %s" + " ADD COLUMN %s text",
                        NEW_RESPONSE_TABLE,C_NRR_IS_TUE);
                Log.d(TAG, String.format("alter table %s with sql %s", NEW_RESPONSE_TABLE, sql));
                db.execSQL(sql);
                sql = String.format("ALTER TABLE %s" + " ADD COLUMN %s text",
                        NEW_RESPONSE_TABLE,C_NRR_IS_WED);
                Log.d(TAG, String.format("alter table %s with sql %s", NEW_RESPONSE_TABLE, sql));
                db.execSQL(sql);
                sql = String.format("ALTER TABLE %s" + " ADD COLUMN %s text",
                        NEW_RESPONSE_TABLE,C_NRR_IS_THU);
                Log.d(TAG, String.format("alter table %s with sql %s", NEW_RESPONSE_TABLE, sql));
                db.execSQL(sql);
                sql = String.format("ALTER TABLE %s" + " ADD COLUMN %s text",
                        NEW_RESPONSE_TABLE,C_NRR_IS_FRI);
                Log.d(TAG, String.format("alter table %s with sql %s", NEW_RESPONSE_TABLE, sql));
                db.execSQL(sql);
            }
        }
    }
}
