package com.hungbang.email2018.data.entity;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by Hungnd on 4/20/2017.
 */

public class Account {
    @NonNull
    private String accountEmail;

    private int accountType;
    public long signedInTime;
    private String fullName;
    private String firstName;
    private String lastName;
    private String thumbnailUrl;
    private String password;
    private String signature = "a";
    private String folderNameInbox = "INBOX";
    private String folderNameSent;
    private String folderNameDraft;
    private String folderNameSpam;
    private String folderNameTrash;
    public int isStartTls = 1;

    public ArrayList<EmailFolder> listAnotherFolder;

    private ArrayList<String> childFoldersInInbox;

    public String getFolderNameInbox() {
        return folderNameInbox;
    }

    public void setFolderNameInbox(String folderNameInbox) {
        this.folderNameInbox = folderNameInbox;
    }

    public String getFolderNameSent() {
        return folderNameSent;
    }

    public void setFolderNameSent(String folderNameSent) {
        this.folderNameSent = folderNameSent;
    }

    public String getFolderNameDraft() {
        return folderNameDraft;
    }

    public void setFolderNameDraft(String folderNameDraft) {
        this.folderNameDraft = folderNameDraft;
    }

    public String getFolderNameSpam() {
        return folderNameSpam;
    }

    public void setFolderNameSpam(String folderNameSpam) {
        this.folderNameSpam = folderNameSpam;
    }

    public String getFolderNameTrash() {
        return folderNameTrash;
    }

    public Account() {
    }

    public Account(@NonNull String accountEmail, String fullName, String firstName,
                   String lastName, int accountType, String thumbnailUrl, String password,
                   String signature, String folderNameInbox, String folderNameSent,
                   String folderNameDraft, String folderNameSpam, String folderNameTrash) {
        this.accountEmail = accountEmail;
        this.fullName = fullName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountType = accountType;
        this.thumbnailUrl = thumbnailUrl;
        this.password = password;
        this.signature = signature;
        this.folderNameInbox = folderNameInbox;
        this.folderNameSent = folderNameSent;
        this.folderNameDraft = folderNameDraft;
        this.folderNameSpam = folderNameSpam;
        this.folderNameTrash = folderNameTrash;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(@NonNull String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public String getFullName() {
        return fullName;
    }


    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Get the real folder name of an account corresponding to a label
     *
     * @param label example : label = SPAM --> return Junk
     * @return
     */
    public String getRealFolderName(String label) {
        if (label == null) {
            return "";
        }
        // TODO: 2/7/2018
//        switch (label) {
//            case MailHelper.label.INBOX:
//                return folderNameInbox;
//            case MailHelper.label.SENT:
//                return folderNameSent;
//            case MailHelper.label.SPAM:
//                return folderNameSpam;
//            case MailHelper.label.DRAFT:
//                return folderNameDraft;
//            case MailHelper.label.TRASH:
//                return folderNameTrash;
//        }
        return label;
    }

    /**
     * Return the label corresponding to the folder
     * Example : folder = Junk --> label = SPAM
     * <p>
     * param realFolderName example : Junk, Deleted...
     *
     * @return
     */
//    public String getFolderLabel(String realFolderName) {
//        if(realFolderName == null){
//            return "";
//        }
//        if (realFolderName.equals(folderNameInbox)) {
//            return MailHelper.label.INBOX;
//        } else if (realFolderName.equals(folderNameSent)) {
//            return MailHelper.label.SENT;
//        } else if (realFolderName.equals(folderNameDraft)) {
//            return MailHelper.label.DRAFT;
//        } else if (realFolderName.equals(folderNameSpam)) {
//            return MailHelper.label.SPAM;
//        } else if (realFolderName.equals(folderNameTrash)) {
//            return MailHelper.label.TRASH;
//        }
//        return realFolderName;
//    }

}
