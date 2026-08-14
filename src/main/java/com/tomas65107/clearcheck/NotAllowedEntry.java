package com.tomas65107.clearcheck;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class NotAllowedEntry {

    public enum Types {
        MOD  ("Mod         "),
        PACK ("Resource  ");

        public final String formatted;

        Types(String formatted) {
            this.formatted = formatted;
        }
    }

    public Types type;
    public String modName;

    public NotAllowedEntry(Types type, String modName) {
        this.type = type;
        this.modName = modName;
    }

//    public static final StreamCodec<ByteBuf, NotAllowedEntry> CODEC =
//            StreamCodec.composite(
//                    ByteBufCodecs.VAR_INT.map(
//                            i -> NotAllowedEntry.Types.values()[i],
//                            Enum::ordinal
//                    ),
//                    entry -> entry.type,
//
//                    ByteBufCodecs.STRING_UTF8,
//                    entry -> entry.modName,
//
//                    NotAllowedEntry::new
//            );
}
