package com.botdiril.command.superuser;

import com.botdiril.serverdata.ChannelPreferences;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.Instant;

import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.permission.EnumPowerLevel;

@Command(value = "toggledisable", aliases = {
        "disablechannel" }, category = CommandCategory.SUPERUSER, powerLevel = EnumPowerLevel.SUPERUSER, description = "Disable commands for non-elevated users in a text channel.")
public class CommandDisableChannel
{
    @CmdInvoke
    public static void toggleDisable(CommandContext co)
    {
        toggleDisable(co, co.textChannel);
    }

    @CmdInvoke
    public static void toggleDisable(CommandContext co, @CmdPar("channel") TextChannel channel)
    {
        var channelId = co.textChannel.getIdLong();
        var on = !ChannelPreferences.checkBit(co.db, channelId, ChannelPreferences.BIT_DISABLED);
        var lc = co.guild.getTextChannelById(co.sc.getLoggingChannel());

        if (on)
        {
            ChannelPreferences.setBit(co.db, channelId, ChannelPreferences.BIT_DISABLED);
            co.respond("Channel " + channel.getAsMention() + " is now disabled for non-elevated users.");
        }
        else
        {
            ChannelPreferences.clearBit(co.db, channelId, ChannelPreferences.BIT_DISABLED);
            co.respond("Channel " + channel.getAsMention() + " is now enabled for non-elevated users.");
        }

        if (lc != null)
        {
            var eb = new EmbedBuilder();
            eb.setTitle("Botdiril SuperUser");
            eb.setColor(0x008080);

            if (on)
            {
                eb.setDescription("Channel interaction has been disabled for non-elevated users.");
            }
            else
            {
                eb.setDescription("Channel interaction has been enabled for non-elevated users.");
            }

            eb.addField("User", co.caller.getAsMention(), false);
            eb.addField("Channel", co.textChannel.getAsMention(), false);
            eb.setFooter("Message ID: " + co.message.getIdLong(), null);
            eb.setTimestamp(Instant.now());

            co.send(lc, eb);
        }
    }
}