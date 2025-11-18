package com.pedrorok.rpm.services;

import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * @author Rok, Pedro Lucas nmm. Created on 17/11/2025
 * @project ResourcePackManager
 */
@Service
public class VersionService {

    private static final Pattern VERSION_PATTERN = Pattern.compile("^\\d+\\.\\d+\\.\\d+$");

    public String generateNextVersion(String currentVersion) {
        if (currentVersion == null || currentVersion.isEmpty()) {
            return "1.0.0";
        }

        String[] parts = currentVersion.split("\\.");
        int patch = Integer.parseInt(parts[2]);
        return parts[0] + "." + parts[1] + "." + (patch + 1);
    }

    public boolean isValidVersion(String version) {
        return VERSION_PATTERN.matcher(version).matches();
    }

    public int compareVersions(String v1, String v2) {
        String[] parts1 = v1.split("\\.");
        String[] parts2 = v2.split("\\.");

        for (int i = 0; i < 3; i++) {
            int num1 = Integer.parseInt(parts1[i]);
            int num2 = Integer.parseInt(parts2[i]);

            if (num1 != num2) {
                return Integer.compare(num1, num2);
            }
        }

        return 0;
    }
}