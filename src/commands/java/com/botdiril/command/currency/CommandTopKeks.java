package com.botdiril.command.currency;

import com.botdiril.Botdiril;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.util.BotdirilFmt;
import net.dv8tion.jda.api.EmbedBuilder;

import java.text.MessageFormat;
import java.util.Locale;

import com.botdiril.userdata.UserInventory;

@Command(value = "richkeks", aliases = { "topkeks",
        "topkek" }, category = CommandCategory.CURRENCY, description = "Shows the top 10 users with the most keks.")
public class CommandTopKeks
{
    public static final int LIMIT = 10;

    @CmdInvoke
    public static void show(CommandContext co)
    {
        var eb = new EmbedBuilder();
        eb.setAuthor("Kekest users");
        eb.setDescription(MessageFormat.format("Showing max {0} users.", LIMIT));
        eb.setColor(0x008080);
        eb.setThumbnail(co.jda.getSelfUser().getEffectiveAvatarUrl());

        co.db.setAutocommit(true);
        co.db.exec("SELECT us_userid, us_keks FROM " + UserInventory.TABLE_USER + " WHERE us_userid<>? ORDER BY us_keks DESC LIMIT " + LIMIT, stat ->
        {
            var rs = stat.executeQuery();

            int i = 1;

            while (rs.next())
            {
                var usn = String.format(Locale.ROOT, "**%d.** <@%d>", i, rs.getLong("us_userid"));
                var row = String.format("%s with **%s** %s", usn, BotdirilFmt.format(rs.getLong("us_keks")), Icons.KEK);

                eb.addField("", row, false);

                i++;
            }

            return 0;
        }, Botdiril.AUTHOR_ID);
        co.db.setAutocommit(false);

        co.respond(eb);
    }
}