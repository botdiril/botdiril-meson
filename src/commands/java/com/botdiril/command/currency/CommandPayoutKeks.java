package com.botdiril.command.currency;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.ParType;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.stat.EnumStat;
import com.botdiril.userdata.timers.EnumTimer;
import com.botdiril.userdata.timers.TimerUtil;
import com.botdiril.util.BotdirilFmt;

@Command(value = "payoutkeks", aliases = {
        "payout", "kekspayout", "kekspayout", "payoutkek" }, category = CommandCategory.CURRENCY, description = "Pay out your keks for some tokens.", levelLock = 7)
public class CommandPayoutKeks
{
    private static final long conversionRate = 125;

    @CmdInvoke
    public static void payout(CommandContext co, @CmdPar(value = "how many", type = ParType.AMOUNT_CLASSIC_KEKS) long keks)
    {
        CommandAssert.numberMoreThanZeroL(keks, "You can't pay out zero keks.");

        TimerUtil.require(co.ui, EnumTimer.PAYOUT, "You need to wait **$** before paying out again.");

        var tokens = keks / conversionRate;
        co.ui.addKeks(-keks);
        co.ui.addKekTokens(tokens);
        var xp = Math.round(Math.pow(keks / 5.0 + 1.0, 0.55));
        co.ui.addXP(co, xp);

        if (co.po.getStat(EnumStat.BIGGEST_PAYOUT) < keks)
        {
            co.po.setStat(EnumStat.BIGGEST_PAYOUT, keks);
        }

        co.respond(String.format("Paid out **%s** %s for **%s** %s at a conversion rate of **%d:1**. **[+%s XP]**",
            BotdirilFmt.format(keks), Icons.KEK, BotdirilFmt.format(tokens), Icons.TOKEN, conversionRate, BotdirilFmt.format(xp)));
    }
}