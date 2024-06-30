package com.pbu.wendi.utils.helpers;


import com.pbu.wendi.requests.agents.dto.SettingsRequest;

import java.util.ArrayList;
import java.util.List;

public class ApplicationSettings {
    public static List<SettingsRequest> getSettingRequests(){
        List<SettingsRequest> settings = new ArrayList<>();
        SettingsRequest s1;
        s1 = new SettingsRequest();
        s1.setDeleted(false);
        s1.setDescription("Last used sort code assigned to an agent");
        s1.setParamName(AppConstants.SORT_CODE);
        s1.setParamValue("0");
        s1.setDeleted(false);
        settings.add(s1);

        SettingsRequest s2;
        s2 = new SettingsRequest();
        s2.setDeleted(false);
        s2.setDescription("Storage URL for individual agent documents and attachments");
        s2.setParamName(AppConstants.IND_URL);
        s2.setParamValue("");
        s2.setDeleted(false);
        settings.add(s2);

        SettingsRequest s3;
        s3 = new SettingsRequest();
        s3.setDeleted(false);
        s3.setDescription("Storage folder for individual agent documents");
        s3.setParamName(AppConstants.IND_FOLDER);
        s3.setParamValue("");
        s3.setDeleted(false);
        settings.add(s3);

        SettingsRequest s4;
        s4 = new SettingsRequest();
        s4.setDeleted(false);
        s4.setDescription("Storage URL for business agent documents and attachments");
        s4.setParamName(AppConstants.BIZ_URL);
        s4.setParamValue("");
        s4.setDeleted(false);
        settings.add(s4);

        SettingsRequest s5;
        s5 = new SettingsRequest();
        s5.setDeleted(false);
        s5.setDescription("Storage folder for business agent documents");
        s5.setParamName(AppConstants.BIZ_FOLDER);
        s5.setParamValue("");
        s5.setDeleted(false);
        settings.add(s5);

        SettingsRequest s6;
        s6 = new SettingsRequest();
        s6.setDeleted(false);
        s6.setDescription("Enable password expiration");
        s6.setParamName(AppConstants.EXPRIREPWD);
        s6.setParamValue("true");
        s6.setDeleted(false);
        settings.add(s6);

        SettingsRequest s7;//LOGIN_ATTEMPTS
        s7 = new SettingsRequest();
        s7.setDeleted(false);
        s7.setDescription("Number of days it takes for a password to expire");
        s7.setParamName(AppConstants.EXPRIREPWD_DAYS);
        s7.setParamValue("30");
        s7.setDeleted(false);
        settings.add(s7);

        SettingsRequest s8;
        s8 = new SettingsRequest();
        s8.setDeleted(false);
        s8.setDescription("Number of attempts a user is allowed to retry before deactivating their account");
        s8.setParamName(AppConstants.LOGIN_ATTEMPTS);
        s8.setParamValue("30");
        s8.setDeleted(false);
        settings.add(s7);
        return settings;
    }

    public static SettingsRequest findByParamName(String paramName) {
        for (SettingsRequest setting : getSettingRequests()) {
            if (setting.getParamName().equals(paramName)) {
                return setting;
            }
        }
        return null; // or throw an exception if preferred
    }
}