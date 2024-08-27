package com.hungbang.email2018.data.entity

/**
 * Created by Hungnd on 4/20/2017.
 */
enum class OldMailAccountType(val value: Int) {
    GOOGLE(1), OUTLOOK(2), YANDEX(3);

    companion object {
        fun fromInt(value: Int) = entries.first { it.value == value }
    }
}

data class Account(
    var accountEmail: String = "",
    var accountType: Int = 0,
    var signedInTime: Long = 0,
    var fullName: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var thumbnailUrl: String? = null,
    var password: String? = null,
    private var signature: String = "a",
    var folderNameInbox: String = "INBOX",
    var folderNameSent: String? = null,
    var folderNameDraft: String? = null,
    var folderNameSpam: String? = null,
    var folderNameTrash: String? = null,
    var isStartTls: Int = 1,

    var listAnotherFolder: ArrayList<EmailFolder>? = null,

    private val childFoldersInInbox: ArrayList<String>? = null,

    ) {

    fun getAccountTypeFromInt() = OldMailAccountType.fromInt(accountType)

    /**
     * Get the real folder name of an account corresponding to a label
     *
     * @param label example : label = SPAM --> return Junk
     * @return
     */
    fun getRealFolderName(label: String?): String {
        if (label == null) {
            return ""
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
        return label
    }
    /**
     * Return the label corresponding to the folder
     * Example : folder = Junk --> label = SPAM
     *
     *
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
