package com.android.internal.util.zap;

import android.app.Notification;
import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

public class SpamFilter {

    public static final String AUTHORITY = "com.zap.spam";
    public static final Uri NOTIFICATION_URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .build();

    public static final class SpamContract {

        public static final class PackageTable {
            public static final String TABLE_NAME = "packages";
            public static final String ID = "_id";
            public static final String PACKAGE_NAME = "package_name";
        }

        public static final class NotificationTable {
            public static final String TABLE_NAME = "notifications";
            public static final String ID = "_id";
            public static final String PACKAGE_ID = "package_id";
            public static final String MESSAGE_TEXT = "message_text";
            public static final String COUNT = "count";
            public static final String LAST_BLOCKED = "last_blocked";
            public static final String NORMALIZED_TEXT = "normalized_text";
        }

    }

    public static String getNormalizedContent(String msg) {
        return msg.toLowerCase().replaceAll("[^\\p{L}\\p{Nd}]+", "");
    }

    public static String getNotificationContent(Notification notification) {
        Bundle extras = notification.extras;
        String titleExtra = extras.containsKey(Notification.EXTRA_TITLE_BIG)
                ? Notification.EXTRA_TITLE_BIG : Notification.EXTRA_TITLE;
        CharSequence notificationTitle = extras.getCharSequence(titleExtra);
        CharSequence notificationMessage = extras.getCharSequence(Notification.EXTRA_TEXT);

        if (TextUtils.isEmpty(notificationMessage)) {
            CharSequence[] inboxLines = extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);
            if (inboxLines == null || inboxLines.length == 0) {
                notificationMessage = "";
            } else {
                notificationMessage = TextUtils.join("\n", inboxLines);
            }
        }
        return notificationTitle + "\n" + notificationMessage;
    }
}
