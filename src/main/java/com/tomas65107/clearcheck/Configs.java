package com.tomas65107.clearcheck;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Configs {
    private static final ModConfigSpec.Builder SERVER_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue PACKETS_TIMEOUT = SERVER_BUILDER.
            comment("How long to wait for packets from client before kicking player. High values may allow players who do not send packets to play unauthorized.")
            .defineInRange("packets_timeout", 40, 40, Integer.MAX_VALUE);

    public static final ModConfigSpec.BooleanValue PERFORM_AUTH = SERVER_BUILDER.
            comment("This is a switch for turning Clearcheck client token authentification off. Turning this off will NOT remove tokens from players that have joined this server when this was on. Server also keeps tokens.")
            .define("perform_auth", true);

    public static final ModConfigSpec.BooleanValue RECONSTRUCT_SIGNATURES = SERVER_BUILDER.
            comment("When server does not have a players signature or loses it (but the player send a token), if the server should save the new signature of the token. Warning: this may allow new players to pass their own token for their account! Turn this off for maximal protection!")
            .define("reconstruct_signatures", true);

    static final ModConfigSpec CLEARCHECK_CONFIG_SERVER = SERVER_BUILDER.build();

    private static final ModConfigSpec.Builder CLIENT_BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SHARE_USERNAME = CLIENT_BUILDER.
            comment("Manage, if you want to opt-out from sharing your set operating-system username with the server you're connecting to!")
            .define("share_username", true);

    public static final ModConfigSpec.BooleanValue HIDE_CONSENT_SCREEN = CLIENT_BUILDER.
            comment("Whether to show the consent screen again. This screen is normally shown only once after the first installation.")
            .define("hide_consent_screen", false);

    static final ModConfigSpec CLEARCHECK_CONFIG_CLIENT = CLIENT_BUILDER.build();
}
