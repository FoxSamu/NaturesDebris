/*
 * Copyright (c) 2019 RedGalaxy & contributors
 * Licensed under the Apache Licence v2.0.
 * Do not redistribute.
 *
 * By  : RGSW
 * Date: 7 - 23 - 2019
 */

package modernity.client.gui.wiki.fakeworld;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.world.World;

import java.util.UUID;

public class FakePlayer extends EntityPlayerSP {
    public FakePlayer( World world ) {
        super( Minecraft.getInstance(), world, new NetHandlerPlayClient(
                Minecraft.getInstance(), null, null,
                new GameProfile( UUID.randomUUID(), "THISISAFAKEPLAYERTHATNEEDEDANAMESOIGAVEITALONGNAMEWITHOUTHSPACES" )
        ), null, null );
    }

    @Override
    public boolean isSpectator() {
        return true;
    }

    @Override
    public boolean isCreative() {
        return true;
    }
}
