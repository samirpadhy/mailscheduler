package com.mailingapp.domain;

import java.io.File;

/**
 * @author: Samir
 * @since 1.0 17/03/2015
 */
public class EmailAttachment {

    private String name;

    private File file;

    private String contentType = "application/pdf";

    public EmailAttachment(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }

    public String getContentType() {
        return contentType;
    }
}
