package com.cyberclub.portal.dtos;

public record SettingData(
    String theme,
    boolean notifications_enabled,
    String language_code
){}