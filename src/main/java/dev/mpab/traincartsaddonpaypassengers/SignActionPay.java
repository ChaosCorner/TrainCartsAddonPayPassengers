package dev.mpab.traincartsaddonpaypassengers;

import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import com.bergerkiller.bukkit.tc.events.SignActionEvent;
import com.bergerkiller.bukkit.tc.events.SignChangeActionEvent;
import com.bergerkiller.bukkit.tc.signactions.SignAction;
import com.bergerkiller.bukkit.tc.signactions.SignActionType;
import com.bergerkiller.bukkit.tc.utils.SignBuildOptions;
import org.bukkit.entity.Player;

public class SignActionPay extends SignAction {

    @Override
    public boolean match(SignActionEvent info) {
        return info.isType("pay");
    }

    @Override
    public void execute(SignActionEvent info) {
        // When a [train] sign is placed, activate when powered by redstone when the train
        // goes over the sign, or when redstone is activated.
        if (info.isTrainSign()
                && info.isAction(SignActionType.GROUP_ENTER, SignActionType.REDSTONE_ON)
                && info.isPowered() && info.hasGroup()
        ) {
            for (MinecartMember<?> member : info.getGroup()) {
                payPassengerPerCart(info, member);
            }
            return;
        }

        // When a [cart] sign is placed, activate when powered by redstone when each cart
        // goes over the sign, or when redstone is activated.
        if (info.isCartSign()
                && info.isAction(SignActionType.MEMBER_ENTER, SignActionType.REDSTONE_ON)
                && info.isPowered() && info.hasMember()
        ) {
            payPassengerPerCart(info, info.getMember());
            return;
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
        // third line should be the amount each passenger gets paid for adding
        String amount = info.getLine(2);

        for (Player passenger : member.getEntity().getPlayerPassengers()) {
            // TODO: add payment code here (will require either vault or essentialsx)
        }
    }
}
