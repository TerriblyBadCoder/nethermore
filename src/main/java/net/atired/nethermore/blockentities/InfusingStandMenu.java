package net.atired.nethermore.blockentities;

import net.atired.nethermore.init.NMBlockEntityInit;
import net.atired.nethermore.items.AshBallItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import org.jetbrains.annotations.Nullable;

public class InfusingStandMenu extends AbstractContainerMenu {
    public final Container infusingStand;
    public final ContainerData infusingStandData;
    protected InfusingStandMenu(int containerId, Inventory player, InfusingStandBlockEntity infusingStand, ContainerData infusingData) {
        super(NMBlockEntityInit.INFUSING_MENU.get(), containerId);
        checkContainerSize(infusingStand, 3);
        checkContainerDataCount(infusingData, 2);
        this.infusingStand=infusingStand;
        this.infusingStandData=infusingData;
        initStuff(NMBlockEntityInit.INFUSING_MENU.get(),player,containerId);
    }

    public InfusingStandMenu(int i, Inventory inventory) {
        super(NMBlockEntityInit.INFUSING_MENU.get(), i);
        this.infusingStand=new SimpleContainer(3);
        this.infusingStandData=new SimpleContainerData(2);
        initStuff(NMBlockEntityInit.INFUSING_MENU.get(),inventory,containerId);
    }
    public int getBrewingTicks(){
        return this.infusingStandData.get(0);
    }
    public int getFuel(){
        return this.infusingStandData.get(1);
    }

    public int getGridWidth() {
        return 1;
    }

    public int getGridHeight() {
        return 1;
    }

    public int getSize() {
        return 3;
    }

    private void initStuff(@Nullable MenuType<?> menuType,Inventory playerInventory, int containerId){
        if(this.infusingStand!=null){
            this.addSlot(new PotionSlot((Container) this.infusingStand,0,79, 17));
            this.addSlot(new ToolSlot((Container) this.infusingStand,1,79+18, 17+41));
            this.addSlot(new FuelSlot((Container) this.infusingStand,2,79-34, 17+7));
            this.addDataSlots(this.infusingStandData);
        }
        int k;
        for(k = 0; k < 3; ++k) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
            }
        }

        for(k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index <3) {
                if (!this.moveItemStackTo(itemstack1, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(itemstack1, itemstack);
            } else{
                    if (itemstack1.getItem() instanceof PotionItem item && !this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                    if (itemstack1.getItem() instanceof AshBallItem item &&!this.moveItemStackTo(itemstack1, 2, 3, false)) {
                        return ItemStack.EMPTY;
                    }
                    if ((itemstack1.has(DataComponents.FOOD))&&!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemstack1);
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.infusingStand.stillValid(player);
    }
    static class FuelSlot extends Slot {
        public FuelSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            return mayPlaceItem(stack);
        }

        public static boolean mayPlaceItem(ItemStack itemStack) {
            return itemStack.getItem() instanceof AshBallItem item;
        }
    }
    static class PotionSlot extends Slot {
        public PotionSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            return mayPlaceItem(stack);
        }

        public static boolean mayPlaceItem(ItemStack itemStack) {
            return itemStack.getItem() instanceof PotionItem item;
        }
    }
    static class ToolSlot extends Slot {
        public ToolSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        public boolean mayPlace(ItemStack stack) {
            return mayPlaceItem(stack);
        }

        public static boolean mayPlaceItem(ItemStack itemStack) {
            return itemStack.has(DataComponents.FOOD);
        }
    }
}
