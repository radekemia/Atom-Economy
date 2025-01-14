package committee.nova.atom.eco.init.events;

import committee.nova.atom.eco.api.account.Account;
import committee.nova.atom.eco.common.config.ConfigUtil;
import committee.nova.atom.eco.common.config.ModConfig;
import committee.nova.atom.eco.core.AccountDataManager;
import committee.nova.atom.eco.core.MoneyItemManager;
import committee.nova.atom.eco.utils.text.FormatUtil;
import committee.nova.atom.eco.utils.text.LogUtil;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/1/20 11:50
 * Version: 1.0
 */
@Mod.EventBusSubscriber
public class PlayerEventHandler {
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {

        if(event.getPlayer().getCommandSenderWorld().isClientSide){ return; }
        LogUtil.debug("加载 " + event.getPlayer().getName() + " 的账户|| " + event.getPlayer().getGameProfile().getId().toString());
        Account account = AccountDataManager.getAccount("player", event.getPlayer().getStringUUID(), true);
        if (ModConfig.COMMON.notifyBalanceOnJoin.get()) {
            event.getPlayer().sendMessage(new StringTextComponent("银行余额: " + ConfigUtil.getWorthAsString(account.getBalance())).withStyle(TextFormatting.BLUE), UUID.randomUUID());
            event.getPlayer().sendMessage(new StringTextComponent("身上余额: " + ConfigUtil.getWorthAsString(MoneyItemManager.countInInventory(event.getPlayer()))).withStyle(TextFormatting.DARK_GREEN), UUID.randomUUID());

        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event){
        LogUtil.debug("保存 " + event.getPlayer().getName() + "的账户|| " + event.getPlayer().getGameProfile().getId().toString());
        AccountDataManager.unloadAccount("player", event.getPlayer().getGameProfile().getId().toString());
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event){
        if (!ModConfig.COMMON.showItemWorthInTooltip.get()) return;
        long worth = ConfigUtil.getItemStackWorth(event.getItemStack());
        if(worth <= 0) return;
        String str = "&9" + ConfigUtil.getWorthAsString(worth, true, worth < 10);
        if(event.getItemStack().getCount() > 1){
            str += " &8(&7" + ConfigUtil.getWorthAsString(worth * event.getItemStack().getCount(), true, worth < 10) + "&8)";
        }
        event.getToolTip().add(new StringTextComponent(FormatUtil.format(str)));
    }
}
