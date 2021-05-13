package com.botdiril.command.superuser;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.permission.EnumPowerLevel;
import net.dv8tion.jda.api.entities.Member;

import com.botdiril.userdata.UserInventory;

@Command(value = "resetcooldowns", aliases = {
        "resetcds" }, category = CommandCategory.SUPERUSER, description = "Reset timers on everything for a user.", powerLevel = EnumPowerLevel.SUPERUSER_OVERRIDE)
public class CommandResetCooldowns
{
    @CmdInvoke
    public static void resetCooldowns(CommandContext co)
    {
        resetCooldowns(co, co.callerMember);
    }

    @CmdInvoke
    public static void resetCooldowns(CommandContext co, @CmdPar("user") Member user)
    {
        co.db.exec("DELETE FROM " + UserInventory.TABLE_TIMERS + " WHERE fk_us_id=?", stat ->
        {
            var res = stat.executeUpdate();

            if (res == 0)
            {
                co.respond("No timers were reset.");
                return 0;
            }

            co.respond(String.format("Reset **%d** timer(s) of **%s**.", res, user.getEffectiveName()));
            return res;

        }, new UserInventory(co.db, user.getUser().getIdLong()).getFID());
    }
}