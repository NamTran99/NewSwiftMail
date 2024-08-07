package com.fsck.k9.view;


import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.TooltipCompat;
import androidx.recyclerview.widget.RecyclerView;
import app.k9mail.core.ui.legacy.designsystem.atom.icon.Icons;
import com.fsck.k9.Account;
import com.fsck.k9.DI;
import com.fsck.k9.FontSizes;
import com.fsck.k9.K9;
import com.fsck.k9.activity.misc.ContactPicture;
import com.fsck.k9.contacts.ContactPictureLoader;
import com.fsck.k9.helper.ClipboardManager;
import com.fsck.k9.helper.MessageHelper;
import com.fsck.k9.mail.Address;
import com.fsck.k9.mail.Flag;
import com.fsck.k9.mail.Message;
import com.fsck.k9.mailstore.LocalMessage;
import com.fsck.k9.mailstore.MessageDetails;
import com.fsck.k9.mailstore.MessageRepository;
import com.fsck.k9.message.ReplyAction;
import com.fsck.k9.message.ReplyActionStrategy;
import com.fsck.k9.message.ReplyActions;
import com.fsck.k9.ui.R;
import com.fsck.k9.ui.compose.view.ViewUserComposePreview;
import com.fsck.k9.ui.helper.BottomBaselineTextView;
import com.fsck.k9.ui.helper.RelativeDateTimeFormatter;
import com.fsck.k9.ui.messageview.DisplayRecipients;
import com.fsck.k9.ui.messageview.DisplayRecipientsExtractor;
import com.fsck.k9.ui.messageview.MessageHeaderClickListener;
import com.fsck.k9.ui.messageview.MessageViewRecipientFormatter;
import com.fsck.k9.ui.messageview.RecipientNamesView;
import com.fsck.k9.view.adapter.ListMailReceiverAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import timber.log.Timber;


public class MessageHeader extends LinearLayout implements OnClickListener, OnLongClickListener {
    private static final int DEFAULT_SUBJECT_LINES = 3;

    private final MessageViewRecipientFormatter recipientFormatter = DI.get(MessageViewRecipientFormatter.class);
    private final ReplyActionStrategy replyActionStrategy = DI.get(ReplyActionStrategy.class);
    private final MessageHelper messageHelper = DI.get(MessageHelper.class);
    private final MessageRepository messageRepository = DI.get(MessageRepository.class);
    private final FontSizes fontSizes = K9.getFontSizes();

//    private RecyclerView listMailRecyclerView;
//    private ListMailReceiverAdapter listMailReceiverAdapter;
    private BottomBaselineTextView subjectView;
//    private ImageView starView;
    private ImageView contactPictureView;
    private MaterialTextView fromView;
//    private ImageView cryptoStatusIcon;
    private RecipientNamesView recipientNamesView;
    private MaterialTextView dateView;
//    private ImageView menuPrimaryActionView;

//    private ViewUserComposePreview viewSenderUser;

    private RelativeDateTimeFormatter relativeDateTimeFormatter;

    private MessageHeaderClickListener messageHeaderClickListener;
    private ReplyActions replyActions;

//    private MaterialButton btDetails;

    private Boolean isShowDetailUser = false;


    public MessageHeader(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            relativeDateTimeFormatter = DI.get(RelativeDateTimeFormatter.class);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        subjectView = findViewById(R.id.subject);
//        starView = findViewById(R.id.flagged);
        contactPictureView = findViewById(R.id.contact_picture);
        fromView = findViewById(R.id.from);
//        cryptoStatusIcon = findViewById(R.id.crypto_status_icon);
        recipientNamesView = findViewById(R.id.recipients);
        dateView = findViewById(R.id.date);
//        viewSenderUser = findViewById(R.id.viewSenderUser);
//        listMailRecyclerView = findViewById(R.id.rvListMailTo);
//        btDetails = findViewById(R.id.btDetail);
//        listMailReceiverAdapter = new ListMailReceiverAdapter();

        fontSizes.setViewTextSize(subjectView, fontSizes.getMessageViewSubject());
        fontSizes.setViewTextSize(dateView, fontSizes.getMessageViewDate());
        fontSizes.setViewTextSize(fromView, fontSizes.getMessageViewSender());
//        listMailRecyclerView.setAdapter(listMailReceiverAdapter);

        int recipientTextSize = fontSizes.getMessageViewRecipients();
        if (recipientTextSize != FontSizes.FONT_DEFAULT) {
            recipientNamesView.setTextSize(recipientTextSize);
        }

        subjectView.setOnClickListener(this);
        subjectView.setOnLongClickListener(this);
//        btDetails.setOnClickListener(this);
//
//        menuPrimaryActionView = findViewById(R.id.menu_primary_action);
//        menuPrimaryActionView.setOnClickListener(this);

//        View menuOverflowView = findViewById(R.id.menu_overflow);
//        menuOverflowView.setOnClickListener(this);
//        String menuOverflowDescription =
//                getContext().getString(androidx.appcompat.R.string.abc_action_menu_overflow_description);
//        TooltipCompat.setTooltipText(menuOverflowView, menuOverflowDescription);

        findViewById(R.id.participants_container).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.subject) {
            toggleSubjectViewMaxLines();
        }
         else if (id == R.id.menu_overflow) {
            showOverflowMenu(view);
        } else if (id == R.id.participants_container) {
            messageHeaderClickListener.onParticipantsContainerClick();
        }
    }

    private void performPrimaryReplyAction() {
        ReplyAction defaultAction = replyActions.getDefaultAction();
        if (defaultAction == null) {
            return;
        }

        switch (defaultAction) {
            case REPLY: {
                messageHeaderClickListener.onMenuItemClick(R.id.reply);
                break;
            }
            case REPLY_ALL: {
                messageHeaderClickListener.onMenuItemClick(R.id.reply_all);
                break;
            }
            default: {
                throw new IllegalStateException("Unknown reply action: " + defaultAction);
            }
        }
    }

    private void showOverflowMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.setOnMenuItemClickListener(item -> {
            messageHeaderClickListener.onMenuItemClick(item.getItemId());
            return true;
        });
        popupMenu.inflate(R.menu.single_message_options);
        setAdditionalReplyActions(popupMenu);
        popupMenu.show();
    }

    @Override
    public boolean onLongClick(View view) {
        int id = view.getId();

        if (id == R.id.subject) {
            onAddSubjectToClipboard(subjectView.getText().toString());
        }

        return true;
    }

    private void toggleSubjectViewMaxLines() {
        if (subjectView.getMaxLines() == DEFAULT_SUBJECT_LINES) {
            subjectView.setMaxLines(Integer.MAX_VALUE);
        } else {
            subjectView.setMaxLines(DEFAULT_SUBJECT_LINES);
        }
    }

    private void onAddSubjectToClipboard(String subject) {
        ClipboardManager clipboardManager = DI.get(ClipboardManager.class);
        clipboardManager.setText("subject", subject);

        Toast.makeText(getContext(), createMessageForSubject(), Toast.LENGTH_LONG).show();
    }

    public String createMessageForSubject() {
        return getResources().getString(R.string.copy_subject_to_clipboard);
    }

    public void setOnFlagListener(OnClickListener listener) {
//        starView.setOnClickListener(listener);
    }


    public void populate(final LocalMessage message, final Account account, boolean showStar, boolean showAccountChip) {
//        MessageDetails messageDetail  = messageRepository.getMessageDetails(message.makeMessageReference());

        Address fromAddress = null;
        Address[] fromAddresses = message.getFrom();
        if (fromAddresses.length > 0) {
            fromAddress = fromAddresses[0];
        }

        if (K9.isShowContactPicture()) {
            contactPictureView.setVisibility(View.VISIBLE);
            if (fromAddress != null) {
                ContactPictureLoader contactsPictureLoader = ContactPicture.getContactPictureLoader();
                contactsPictureLoader.setContactPicture(contactPictureView, fromAddress);
            } else {
                contactPictureView.setImageResource(Icons.Outlined.AccountCircle);
            }
        } else {
            contactPictureView.setVisibility(View.GONE);
        }

//        listMailReceiverAdapter.submitAddress(messageDetail.getTo());
        CharSequence from = messageHelper.getSenderDisplayName(fromAddress);
        fromView.setText(from);
//        if (showStar) {
//            starView.setVisibility(View.VISIBLE);
//            starView.setSelected(message.isSet(Flag.FLAGGED));
//        } else {
//            starView.setVisibility(View.GONE);
//        }

        if (message.getSentDate() != null) {
            dateView.setText(relativeDateTimeFormatter.formatDate(message.getSentDate().getTime()));
        } else {
            dateView.setText("");
        }

        setRecipientNames(message, account);

        setReplyActions(message, account);

        setVisibility(View.VISIBLE);
    }

    private void setRecipientNames(Message message, Account account) {
        DisplayRecipientsExtractor displayRecipientsExtractor = new DisplayRecipientsExtractor(recipientFormatter,
                recipientNamesView.getMaxNumberOfRecipientNames());
        DisplayRecipients displayRecipients = displayRecipientsExtractor.extractDisplayRecipients(message, account);
        recipientNamesView.setRecipients(displayRecipients.getRecipientNames(),
                displayRecipients.getNumberOfRecipients());
    }

    private void setReplyActions(Message message, Account account) {
        ReplyActions replyActions = replyActionStrategy.getReplyActions(account, message);
        this.replyActions = replyActions;

//        setDefaultReplyAction(replyActions.getDefaultAction());
    }

//    private void setDefaultReplyAction(ReplyAction defaultAction) {
//        if (defaultAction == null) {
//            menuPrimaryActionView.setVisibility(View.GONE);
//        } else {
//            int replyIconResource = getReplyImageResource(defaultAction);
//            menuPrimaryActionView.setImageResource(replyIconResource);
//
//            String replyActionName = getReplyActionName(defaultAction);
//            TooltipCompat.setTooltipText(menuPrimaryActionView, replyActionName);
//
//            menuPrimaryActionView.setVisibility(View.VISIBLE);
//        }
//    }

    @DrawableRes
    private int getReplyImageResource(@NonNull ReplyAction replyAction) {
        switch (replyAction) {
            case REPLY: {
                return Icons.Outlined.Reply;
            }
            case REPLY_ALL: {
                return Icons.Outlined.ReplyAll;
            }
            default: {
                throw new IllegalStateException("Unknown reply action: " + replyAction);
            }
        }
    }

    @NonNull
    private String getReplyActionName(@NonNull ReplyAction replyAction) {
        Context context = getContext();
        switch (replyAction) {
            case REPLY: {
                return context.getString(R.string.reply_action);
            }
            case REPLY_ALL: {
                return context.getString(R.string.reply_all_action);
            }
            default: {
                throw new IllegalStateException("Unknown reply action: " + replyAction);
            }
        }
    }

    private void setAdditionalReplyActions(PopupMenu popupMenu) {
        List<ReplyAction> additionalActions = replyActions.getAdditionalActions();
        if (!additionalActions.contains(ReplyAction.REPLY)) {
            popupMenu.getMenu().removeItem(R.id.reply);
        }
        if (!additionalActions.contains(ReplyAction.REPLY_ALL)) {
            popupMenu.getMenu().removeItem(R.id.reply_all);
        }
    }

    public void setSubject(@NonNull String subject) {
        subjectView.setText(subject);
    }

    public void hideCryptoStatus() {
//        cryptoStatusIcon.setVisibility(View.GONE);
    }

    public void setCryptoStatusLoading() {
        setCryptoDisplayStatus(MessageCryptoDisplayStatus.LOADING);
    }

    public void setCryptoStatusDisabled() {
        setCryptoDisplayStatus(MessageCryptoDisplayStatus.DISABLED);
    }

    public void setCryptoStatus(MessageCryptoDisplayStatus displayStatus) {
        setCryptoDisplayStatus(displayStatus);
    }

    private void setCryptoDisplayStatus(MessageCryptoDisplayStatus displayStatus) {
//        int color = ThemeUtils.getStyledColor(getContext(), displayStatus.getColorAttr());
//        cryptoStatusIcon.setEnabled(displayStatus.isEnabled());
//        cryptoStatusIcon.setVisibility(View.VISIBLE);
//        cryptoStatusIcon.setImageResource(displayStatus.getStatusIconRes());
//        cryptoStatusIcon.setColorFilter(color);
    }

    public void setMessageHeaderClickListener(MessageHeaderClickListener messageHeaderClickListener) {
        this.messageHeaderClickListener = messageHeaderClickListener;
    }
}
