package com.botdiril.command.general;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.util.CommandAssert;

@Command(value = "removealias", aliases = {
    "aliasdelete", "aliasremove", "deletealias"
}, category = CommandCategory.GENERAL, description = "Remove an alias you previously set.")
public class CommandRemoveAlias
{
    @CmdInvoke
    public static void unbind(CommandContext co, @CmdPar("alias number") int number)
    {
        CommandAssert.numberInBoundsInclusiveL(number, 0, Byte.SIZE - 1, "Alias number must be non-negative and less than " + Byte.SIZE + "!");

        var bound = co.po.getUsedAliases();

        var bit = (byte) (1 << number);

        if ((bound & bit) != 0)
        {
            co.po.setUsedAliases((byte) (bound & ~bit));
            co.respond(String.format("Alias with the number %d was removed.", number));
        }
        else
        {
            co.respond(String.format("Alias with the number %d does not exist and could not be removed.", number));
        }
    }
}