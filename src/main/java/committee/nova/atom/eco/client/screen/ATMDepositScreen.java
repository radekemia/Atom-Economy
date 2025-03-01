package committee.nova.atom.eco.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import committee.nova.atom.eco.Eco;
import committee.nova.atom.eco.common.config.ConfigUtil;
import committee.nova.atom.eco.common.containers.ATMContainer;
import committee.nova.atom.eco.core.MoneyItemManager;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Description:存款
 * Author: cnlimiter
 * Date: 2022/2/5 11:34
 * Version: 1.0
 */
public class ATMDepositScreen extends AbstractScreenPage<ATMContainer>{


    private TextFieldWidget values;
    private ExtendedButton deposit;

    protected ATMDepositScreen(ATMContainer container, ContainerScreen<ATMContainer> parent, Consumer<Integer> changePage) {
        super(container, parent, changePage);
    }

    @Override
    public void init() {
        final int halfW = width / 2;
        final int halfH = height / 2;

        values = new TextFieldWidget(font, parent.getGuiLeft() + 10, parent.getGuiTop() + parent.getYSize() - 40,  100, 16, new TranslationTextComponent("gui.atomeco.deposit.deposit_value"));

        deposit = new ExtendedButton(halfW + 40,parent.getGuiTop()+parent.getYSize() - 60,60,20, new TranslationTextComponent("gui.atomeco.index.deposit"),
                t-> withdrawAct());


        values.setSuggestion(I18n.get("gui.atomeco.deposit.deposit_value"));

        this.addButton(new ExtendedButton(halfW + 40, parent.getGuiTop()+parent.getYSize() - 30, 60, 20, new TranslationTextComponent("gui.atomeco.back"),
                $ -> NavigateTo(0)
        ));

        this.children.add(values);
        this.children.add(deposit);
    }

    @Override
    public void renderInternal(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        values.render(matrix, mouseX, mouseY, partialTicks);
        deposit.render(matrix, mouseX, mouseY, partialTicks);

        StringTextComponent inPlayer = new StringTextComponent(ConfigUtil.getWorthAsString(MoneyItemManager.countInInventory(container.player)));

        matrix.pushPose();
        matrix.scale(1.3f,1.3f,1.3f);
        font.draw(matrix, new TranslationTextComponent("gui.atomeco.player_leave"), (int)((this.parent.getGuiLeft() + 8) / 1.3),parent.getGuiTop() + 4,0xFFFFF0);
        font.draw( matrix, inPlayer, (int)((this.parent.getGuiLeft() + 8) / 1.3),parent.getGuiTop() + 14,0xFFFFF0);
        matrix.popPose();
    }

    @Override
    public void drawGuiContainerForegroundLayer(MatrixStack matrix, int mouseX, int mouseY) {

    }

    private void withdrawAct(){
        long money;
        try {
            money  = Long.parseLong(values.getValue()) * 1000;
            if(money > 0 && container.processSelfAction(money, true)){
                Eco.LOGGER.info("deposit success");
            }
        }
        catch (Exception e){
            container.player.sendMessage(new StringTextComponent("不是有效的数字"), UUID.randomUUID());
        }

    }
}
