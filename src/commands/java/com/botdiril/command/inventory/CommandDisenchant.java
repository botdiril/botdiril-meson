package com.botdiril.command.inventory;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.framework.command.invoke.ParType;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.IIdentifiable;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ShopEntries;

@Command(value = "disenchant", aliases = {
        "dust" }, category = CommandCategory.ITEMS, description = "Disenchant items or cards into " + Icons.DUST + ".", levelLock = 6)
public class CommandDisenchant
{
    @CmdInvoke
    public static void dust(CommandContext co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item)
    {
        dust(co, item, 1);
    }

    @CmdInvoke
    public static void dust(CommandContext co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item, @CmdPar(value = "amount", type = ParType.AMOUNT_ITEM_OR_CARD) long amount)
    {
        CommandAssert.numberMoreThanZeroL(amount, "You can't disenchant zero items / cards.");

        if (!ShopEntries.canBeDisenchanted(item))
            throw new CommandException("That item cannot be sold.");

        if (item instanceof Item)
        {
            CommandAssert.numberNotAboveL(amount, co.ui.howManyOf((Item) item), "You don't have that many items of that type.");
            co.ui.addItem((Item) item, -amount);
        }
        else if (item instanceof Card)
        {
            var card = (Card) item;
            CommandAssert.numberNotAboveL(amount, co.ui.howManyOf(card) - 1, "You don't have that many cards of that type. Keep in mind you need to keep at least one card of each type once you receive it.");
            co.ui.addCard(card, -amount);
        }

        var value = amount * ShopEntries.getDustForDisenchanting(item);
        co.ui.addDust(value);

        co.respond(String.format("You disenchanted **%d** %s for **%d** %s.", amount, item.inlineDescription(), value, Icons.DUST));
    }
}