package app.k9mail.feature.account.setup.domain.entity.oldmail

/**
 * Created by Hungnd on 4/20/2017.
 */
class OldAccount {
    var accountEmail: String = ""

    var accountType: Int = 0
    var signedInTime: Long = 0
    var fullName: String? = ""
    var firstName: String? = ""
    var lastName: String? = ""
    var thumbnailUrl: String? = ""
    var password: String? = ""
    private var signature = "a"
    var folderNameInbox: String = "INBOX"
    var folderNameSent: String? = ""
    var folderNameDraft: String? = ""
    var folderNameSpam: String? = ""
    var folderNameTrash: String? = ""
        private set
    var isStartTls: Int = 1

    var listAnotherFolder: ArrayList<EmailFolder>? = null

    private val childFoldersInInbox: ArrayList<String>? = null

    constructor()

    constructor(
        accountEmail: String, fullName: String?, firstName: String?,
        lastName: String?, accountType: Int, thumbnailUrl: String?, password: String?,
        signature: String, folderNameInbox: String, folderNameSent: String?,
        folderNameDraft: String?, folderNameSpam: String?, folderNameTrash: String?
    ) {
        this.accountEmail = accountEmail
        this.fullName = fullName
        this.firstName = firstName
        this.lastName = lastName
        this.accountType = accountType
        this.thumbnailUrl = thumbnailUrl
        this.password = password
        this.signature = signature
        this.folderNameInbox = folderNameInbox
        this.folderNameSent = folderNameSent
        this.folderNameDraft = folderNameDraft
        this.folderNameSpam = folderNameSpam
        this.folderNameTrash = folderNameTrash
    }


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
