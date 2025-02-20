package com.domain.apps.snapadeal.parser.api_parser;


import com.domain.apps.snapadeal.appconfig.AppContext;
import com.domain.apps.snapadeal.classes.Guest;
import com.domain.apps.snapadeal.parser.Parser;
import com.domain.apps.snapadeal.parser.tags.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmList;

/**
 * Created by Droideve on 1/12/2016.
 */
public class GuestParser extends Parser {

    public GuestParser(JSONObject json) {
        super(json);
    }

    public RealmList<Guest> getData() {

        RealmList<Guest> list = new RealmList<Guest>();

        try {

            JSONObject json_array = json.getJSONObject(Tags.RESULT);

            for (int i = 0; i < json_array.length(); i++) {

                JSONObject json_user = json_array.getJSONObject(i + "");
                Guest user = new Guest();

                user.setId(json_user.getInt("id"));
                user.setFcmId(json_user.getString("fcm_id"));

                try {
                    user.setLng(json_user.getDouble("lng"));
                } catch (Exception e) {
                }
                try {
                    user.setLat(json_user.getDouble("lng"));
                } catch (Exception e) {
                }


                user.setSenderId(json_user.getString("sender_id"));

                list.add(user);

            }

        } catch (JSONException e) {
            if (AppContext.DEBUG)
                e.printStackTrace();
        }


        return list;
    }


}
