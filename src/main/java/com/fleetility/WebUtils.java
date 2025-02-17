package com.fleetility;

import java.util.Locale;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebUtils {

	private final HttpServletRequest request;
	
	public Locale getRequestLocale() {
	    String acceptLanguage = request.getHeader("Accept-Language");
	    return parseAcceptLanguageHeader(acceptLanguage);
	}
	
    public Locale parseAcceptLanguageHeader(String acceptLanguage) {
        if (acceptLanguage == null || acceptLanguage.isEmpty()) {
            return Locale.getDefault();
        }

        // Extract the first locale (before ";")
        String languageTag = acceptLanguage.split(",")[0].split(";")[0];
        return Locale.forLanguageTag(languageTag);
    }
    
}
