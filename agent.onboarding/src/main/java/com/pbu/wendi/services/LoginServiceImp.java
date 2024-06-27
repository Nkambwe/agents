package com.pbu.wendi.services;

import com.pbu.wendi.requests.helpers.dto.LoginModel;
import com.pbu.wendi.requests.helpers.dto.SettingsModel;
import com.pbu.wendi.utils.helpers.SamConstant;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class LoginServiceImp implements LoginService {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public LoginServiceImp(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.baseUrl = SamConstant.SAM_BASEURL;
    }

    //TODO - add end points for pending applications and rejected for user with Id @id

    @Override
    public List<SettingsModel> getLoginSettings() {
        String url = String.format("%s/%s", baseUrl, "getLoginSettings");
        ParameterizedTypeReference<List<SettingsModel>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<SettingsModel>> response = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
        return response.getBody();
    }

    @Override
    public LoginModel LoginUser(String username) {
        String loginUrl = String.format("%s/%s/%s", baseUrl, "getLoginInfo", username);
        LoginModel model = restTemplate.getForObject(loginUrl, LoginModel.class);

        if (model == null) {
            return null;
        }

        //...get a list of configurations
        List<SettingsModel> settings = getLoginSettings();

        // Check if passwords should expire
        settings.stream()
                .filter(m -> (SamConstant.EXPRIREPWD).equals(m.getParamName()))
                .findFirst().ifPresent(s ->
                        model.setExpirePassword(Boolean.parseBoolean(s.getParamValue())));

        // Check for timeout
        SettingsModel timeoutSetting = settings.stream()
                .filter(s -> (SamConstant.TIMEOUT).equals(s.getParamName()))
                .findFirst()
                .orElse(null);

        if (timeoutSetting != null) {
            try {
                int timeout = Integer.parseInt(timeoutSetting.getParamValue());
                model.setTimeout(timeout);
            } catch (NumberFormatException e) {
                model.setTimeout(0);
            }
        }

        // Calculate days left for password to expire
        int expireDays = 30; // Default days
        LocalDateTime lastDate = model.getLastPasswordChange();

        if (lastDate != null) {
            SettingsModel daysSetting = settings.stream()
                    .filter(s -> (SamConstant.EXPRIREPWD_DAYS).equals(s.getParamName()))
                    .findFirst()
                    .orElse(null);

            if (daysSetting != null) {
                try {
                    int setDays = Integer.parseInt(daysSetting.getParamValue());
                    LocalDateTime now = LocalDateTime.now();
                    int daysPassed = (int) Duration.between(lastDate, now).toDays();
                    expireDays = Math.max(setDays - daysPassed, 0);
                } catch (NumberFormatException e) {
                    // Log or handle the exception
                }
            }
        }

        model.setExpiresIn(expireDays);

        return model;
    }
}
