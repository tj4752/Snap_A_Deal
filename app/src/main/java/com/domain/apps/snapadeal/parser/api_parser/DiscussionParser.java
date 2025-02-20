package com.domain.apps.snapadeal.parser.api_parser;

import com.domain.apps.snapadeal.classes.Discussion;
import com.domain.apps.snapadeal.classes.Message;
import com.domain.apps.snapadeal.classes.User;
import com.domain.apps.snapadeal.parser.Parser;
import com.domain.apps.snapadeal.parser.tags.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import io.realm.RealmList;

/**
 * Created by Droideve on 1/12/2016.
 */
public class DiscussionParser extends Parser {

    public DiscussionParser(JSONObject json) {
        super(json);
    }

    public RealmList<Discussion> getDiscussion() {

        RealmList<Discussion> list = new RealmList<Discussion>();

        try {

            JSONObject jsonResult = this.json.getJSONObject(Tags.RESULT);


            for (int i = 0; i < jsonResult.length(); i++) {

                JSONObject jsonRow = jsonResult.getJSONObject(String.valueOf(i));

                JSONObject json_user_sender = new JSONObject(jsonRow.getString(Tags.SENDER));
                UserParser mUserParserSender = new UserParser(json_user_sender);
                User sender = mUserParserSender.getUser().get(0);

                JSONObject json_mesg = new JSONObject(jsonRow.getString(Tags.MESSAGES));
                MessageParser mMessageParser = new MessageParser(json_mesg);
                RealmList<Message> messages = mMessageParser.getMessages();

                Discussion mDiscussion = new Discussion();
                mDiscussion.setDiscussionId(jsonRow.getInt("id_discussion"));
                mDiscussion.setSenderUser(sender);
                mDiscussion.setSystem(false);

                mDiscussion.setNbrMessage(jsonRow.getInt("nbrMessageNotSeen"));
                mDiscussion.setCreatedAt(jsonRow.getString("created_at"));
                mDiscussion.setStatus(jsonRow.getInt("status"));


                mDiscussion.setMessages(messages);

                list.add(mDiscussion);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return list;
    }


}
