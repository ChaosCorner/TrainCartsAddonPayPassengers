package dev.mpab.traincartsaddonpaypassengers;

import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import com.bergerkiller.bukkit.tc.signactions.SignActionType;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SignActionPay extends SignAction {

    // essentially a copypasta from https://wiki.traincarts.net/p/TrainCarts/API/SignAction don't be proud of me lol

    @Override
    public boolean match(SignActionEvent info) {
        return info.isType("pay");
    }

    @Override
    public void execute(SignActionEvent info) {
        // When a [train] sign is placed, activate when powered by redstone when the train
        // goes over the sign, or when redstone is activated.
        if (info.isTrainSign()
                && info.isAction(SignActionType.MEMBER_ENTER, SignActionType.REDSTONE_ON)
                && info.isPowered() && info.hasGroup()
        ) {
            for (MinecartMember<?> member : info.getGroup()) {
                payPassengerPerCart(info, member);
            }
        }

        // When a [cart] sign is placed, activate when powered by redstone when each cart
        // goes over the sign, or when redstone is activated.
        if (info.isCartSign()
                && info.isAction(SignActionType.MEMBER_ENTER, SignActionType.REDSTONE_ON)
                && info.isPowered() && info.hasMember()
        ) {
            payPassengerPerCart(info, info.getMember());
        }
    }

    @Override
    public boolean build(SignChangeActionEvent event) {
        return SignBuildOptions.create()
                .setName(event.isCartSign() ? "cart pay passenger" : "train pay passengers")
                .setDescription("pays each passenger a specified amount when passing over the sign")
                .handle(event.getPlayer());
    }

    private void payPassengerPerCart(SignActionEvent info, MinecartMember<?> member) {

        if (TrainCartsAddonPayPassengers.eco == null) {
            for (Player passenger : member.getEntity().getPlayerPassengers()) {
                passenger.sendMessage("If you see this, then MB messed up real bad. :(");
            }
            return;
        }

        // third line should be the amount each passenger gets paid for passing over it
        double amount;
        try {
            amount = Double.parseDouble(info.getLine(2));
        } catch (Exception ignored) {
            // it just won't do anything from here
            return;
        }

        for (Player passenger : member.getEntity().getPlayerPassengers()) {
            if (amount > 0) {
                TrainCartsAddonPayPassengers.eco.depositPlayer(passenger, amount);
                passenger.sendMessage("&aThanks for using the train! You've been given &e" + amount + " &acurrency!");
            } else if (amount < 0) {
                TrainCartsAddonPayPassengers.eco.withdrawPlayer(passenger, Math.abs(amount));
                passenger.sendMessage("&aThanks for using the train! You've have given &e" + amount + " &acurrency back to the train company!");
            }
        }
    }
}
