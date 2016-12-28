package com.ahiho.apps.beeenglish.util.deserialize;

import com.ahiho.apps.beeenglish.model.realm_object.CommunicationObject;
import com.ahiho.apps.beeenglish.model.realm_object.CommunicationSubObject;
import com.ahiho.apps.beeenglish.model.realm_object.CommunicationSubSecondDetailObject;
import com.ahiho.apps.beeenglish.model.realm_object.CommunicationSubSecondObject;
import com.ahiho.apps.beeenglish.model.realm_object.SubDetailObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import io.realm.RealmList;

/**
 * Created by theptokim on 12/14/16.
 */

public class CommunicationDeserializer implements JsonDeserializer<CommunicationObject> {

    @Override
    public CommunicationObject deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();

        final int id = jsonObject.get("id").getAsInt();

        final String name = jsonObject.get("name").getAsString();
        final String description = jsonObject.get("description").getAsString();
        final String icon = jsonObject.get("icon").getAsString();


        final JsonArray data = jsonObject.get("data").getAsJsonArray();
        RealmList<CommunicationSubObject> subObjects =new RealmList<>();
        for(int i=0;i<data.size();i++){
            final JsonObject subData =data.get(i).getAsJsonObject();
            final JsonArray jsonSubSecondData = subData.get("data").getAsJsonArray();
            RealmList<CommunicationSubSecondObject> subSecondObjects = new RealmList<>();
            for(int j=0;j<jsonSubSecondData.size();j++){
                final JsonObject jsonSubSecond = jsonSubSecondData.get(j).getAsJsonObject();
                final JsonArray jsonSubSecondDetail = jsonSubSecond.get("data").getAsJsonArray();
                RealmList<CommunicationSubSecondDetailObject> communicationSubSecondDetailObjects = new RealmList<>();

                for(int k=0;k<jsonSubSecondDetail.size();k++){
                    final JsonObject jsonDetail = jsonSubSecondDetail.get(k).getAsJsonObject();
                    communicationSubSecondDetailObjects.add(new CommunicationSubSecondDetailObject(
                            jsonDetail.get("id").getAsInt()
                            , jsonDetail.get("name").getAsString()
                            , jsonDetail.get("description").getAsString()));
                }

                subSecondObjects.add(new CommunicationSubSecondObject(jsonSubSecond.get("id").getAsInt(),
                        jsonSubSecond.get("name").getAsString(),
                        jsonSubSecond.get("description").getAsString(),
                        jsonSubSecond.get("icon").getAsString(),communicationSubSecondDetailObjects)
                        );
            }

            subObjects.add(new CommunicationSubObject(subData.get("id").getAsInt(),
                    subData.get("name").getAsString(),
                    subData.get("description").getAsString(),
                    subData.get("icon").getAsString(),
                    subSecondObjects));
        }
        //The deserialisation code is missing

        final CommunicationObject communicationObject = new CommunicationObject();
        communicationObject.setId(id);
        communicationObject.setName(name);
        communicationObject.setDescription(description);
        communicationObject.setIcon(icon);
        communicationObject.setData(subObjects);
        return communicationObject;
    }
}

