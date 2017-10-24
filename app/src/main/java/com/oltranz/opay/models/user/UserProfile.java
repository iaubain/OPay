package com.oltranz.opay.models.user;

/**
 * Created by ISHIMWE Aubain Consolateur. email: iaubain@yahoo.fr / aubain.c.ishimwe@oltranz.com Tel: +250 785 534 672 / +250 736 864 662 on 10/10/2017.
 */

public class UserProfile {
    private String file;
    private String extension;
    private String folder;

    public UserProfile() {
    }

    public UserProfile(String file, String extension, String folder) {
        this.setFile(file);
        this.setExtension(extension);
        this.setFolder(folder);
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }
}
