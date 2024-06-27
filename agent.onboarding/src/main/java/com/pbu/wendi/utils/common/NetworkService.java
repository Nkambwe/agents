package com.pbu.wendi.utils.common;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class NetworkService {
    public String getIncomingIpAddress(HttpServletRequest request) {
        return request.getRemoteAddr();
    }
}
