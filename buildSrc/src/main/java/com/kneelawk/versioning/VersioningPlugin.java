package com.kneelawk.versioning;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.plugins.ExtensionContainer;

public class VersioningPlugin implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        ExtensionContainer ext = target.getExtensions();

        String releaseTag = System.getenv("RELEASE_TAG");
        String modVersion;
        if (releaseTag != null) {
            modVersion = releaseTag.substring(1);
            System.out.println("Detected Release Version: " + modVersion);
        } else {
            modVersion = (String) target.property("mod_version");
            if (modVersion == null) throw new IllegalStateException("No property 'mod_version' found");
            System.out.println("Detected Local Version: " + modVersion);
        }

        if (modVersion.isBlank()) throw new IllegalStateException("Invalid blank mod version detected: " + modVersion);

        ext.getExtraProperties().set("modVersion", modVersion);
        target.setVersion(modVersion);
    }
}
