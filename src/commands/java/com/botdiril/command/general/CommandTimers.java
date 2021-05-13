package com.botdiril.command.general;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.userdata.timers.EnumTimer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.util.Arrays;

import com.botdiril.userdata.UserInventory;

import cz.tefek.pluto.chrono.MiniTime;

@Command(value = "timers", aliases = {
        "cooldowns", "cooldown", "timer" }, category = CommandCategory.GENERAL, description = "Check user's timers.")
public class CommandTimers
{
    @CmdInvoke
    public static void check(CommandContext co, @CmdPar("user") User user)
    {
        var eb = new EmbedBuilder();
        eb.setTitle("Timers");
        eb.setDescription(user.getAsMention() + "'s active timers / cooldowns.");

        final var ui = new UserInventory(co.db, user.getIdLong());

        Arrays.stream(EnumTimer.values()).forEach(t ->
        {
            var remaining = ui.checkTimer(t);

            if (remaining < 0)
            {
                return;
            }

            eb.addField(t.getLocalizedName(), MiniTime.formatDiff(remaining), false);
        });

        eb.setColor(0x008080);
        eb.setThumbnail(user.getEffectiveAvatarUrl());

        co.respond(eb);
    }

    @CmdInvoke
    public static void checkSelf(CommandContext co)
    {
        check(co, co.caller);
    }
}