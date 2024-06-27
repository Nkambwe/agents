package com.pbu.wendi.services;

import com.pbu.wendi.requests.helpers.dto.LoginModel;
import com.pbu.wendi.requests.helpers.dto.SettingsModel;

import java.util.List;

public interface LoginService {
    List<SettingsModel> getLoginSettings();
    LoginModel LoginUser(String username);
}
