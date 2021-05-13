package com.botdiril.command.currency;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.timers.EnumTimer;
import com.botdiril.userdata.timers.TimerUtil;
import com.botdiril.util.BotdirilFmt;

import java.util.function.Function;

@Command(value = "payoutmegakeks", aliases = { "payoutmega",
        "bigpayout" }, category = CommandCategory.CURRENCY, description = "Pay out your megakeks for some coins.", levelLock = 1)
public class CommandPayoutMegaKeks
{
    private static final Function<Long, Long> conversion = megaKeks -> megaKeks * 80 + megaKeks * megaKeks * 4;
    private static final Function<Long, Long> xpConversion = megaKeks -> megaKeks * 5;

    @CmdInvoke
    public static void payout(CommandContext co)
    {
        var has = co.ui.getMegaKeks();
        CommandAssert.numberMoreThanZeroL(has, String.format("You can't pay out zero %s.", Icons.MEGAKEK));

        var gets = conversion.apply(has);
        var xp = xpConversion.apply(has);

        co.respond(String.format("You would receive **%s** %s and **[+%s XP]** for your **%s** %s.\n" +
                                 "Type `%s%s confirm` to confirm this transaction.",
             BotdirilFmt.format(gets), Icons.COIN, BotdirilFmt.format(xp), BotdirilFmt.format(has), Icons.MEGAKEK,
            co.usedPrefix, co.usedAlias));
    }

    @CmdInvoke
    public static void payout(CommandContext co, @CmdPar("confirm") String confirmation)
    {
        var has = co.ui.getMegaKeks();
        CommandAssert.numberMoreThanZeroL(has, String.format("You can't pay out zero %s.", Icons.MEGAKEK));

        CommandAssert.assertEquals("confirm", confirmation, "Type `%s%s confirm` to confirm this transaction.".formatted(co.usedPrefix, co.usedAlias));

        TimerUtil.require(co.ui, EnumTimer.PAYOUT, "You need to wait **$** before paying out again.");

        var gets = conversion.apply(has);

        co.ui.setMegaKeks(0);
        co.ui.addCoins(gets);

        var xp = xpConversion.apply(has);
        co.ui.addXP(co, xp);

        co.respond(String.format("Paid out **%s** %s for **%s** %s. **[+%s XP]**",
            BotdirilFmt.format(has), Icons.MEGAKEK, BotdirilFmt.format(gets), Icons.COIN, BotdirilFmt.format(xp)));
    }
}