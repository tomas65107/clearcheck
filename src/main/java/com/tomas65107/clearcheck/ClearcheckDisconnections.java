package com.tomas65107.clearcheck;

import java.net.URL;

public enum ClearcheckDisconnections {
    INCORRECT_TOKEN("incorrect_token", "https://docs.tomas65107.dev/clearcheck/common_disconnects#incorrect-token"),
    NO_TOKEN("no_token", "https://docs.tomas65107.dev/clearcheck/common_disconnects#no-token"),
    DISALLOWED_ASSETS("disallowed_assets", "https://docs.tomas65107.dev/clearcheck/common_disconnects#disallowed-assets"),
    PACKETS_NOT_SENT("packets_not_sent", "https://docs.tomas65107.dev/clearcheck/common_disconnects#packets-not-sent");

    public static final String START_MARKER = "clearcheck_disconnect";

    public final String id;
    public final String website;

    ClearcheckDisconnections(String id, String website) {
        this.id = id;
        this.website = website;
    }

    public String getFullId() {
        return START_MARKER+"_"+id;
    }

    public String getFullIdAndSuffix(String suffix) {
        return START_MARKER+"_"+id+"|#|"+suffix;
    }

    public static ClearcheckDisconnections getMarkerType(String reason) {
        if (reason.startsWith(START_MARKER)) {
            for (var v : ClearcheckDisconnections.values()) if (reason.contains(v.id)) return v;
        }
        return null;
    }
}
