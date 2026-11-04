package net.atired.nethermore.blockentities;

import com.mojang.authlib.GameProfile;
import net.atired.nethermore.datagen.NMDatapackProvider;
import net.atired.nethermore.init.NMBlockEntityInit;
import net.atired.nethermore.init.NMItemComponentInit;
import net.atired.nethermore.init.NMItemInit;
import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

public class InfusingStandBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer {
    private NonNullList<ItemStack> items;
    private static final int INGREDIENT_SLOT = 1;
    private static final int FUEL_SLOT = 2;
    private static final int[] SLOTS_FOR_UP = new int[]{1};
    private static final int[] SLOTS_FOR_DOWN = new int[]{0};
    private static final int[] SLOTS_FOR_SIDES = new int[]{2};
    public int boilTime = 0;
    public int fuel = 0;
    public float aged = 0;
    protected final ContainerData dataAccess;
    public ItemStack usedToBe=ItemStack.EMPTY;
    public InfusingStandBlockEntity(BlockPos pos, BlockState blockState) {
        super(NMBlockEntityInit.INFUSING_STAND_ENTITY.get(), pos, blockState);
        this.items = NonNullList.withSize(3, ItemStack.EMPTY);
        this.dataAccess = new ContainerData() {
            public int get(int p_58431_) {
                switch (p_58431_) {
                    case 0:
                        return InfusingStandBlockEntity.this.boilTime;
                    case 1:
                        return InfusingStandBlockEntity.this.fuel;
                    default:
                        return 0;
                }
            }

            public void set(int p_58433_, int p_58434_) {
                switch (p_58433_) {
                    case 0:
                        InfusingStandBlockEntity.this.boilTime = p_58434_;
                        break;
                    case 1:
                        InfusingStandBlockEntity.this.fuel = p_58434_;
                        break;
                }

            }

            public int getCount() {
                return 2;
            }
        };
    }

    public static void tick(Level level, BlockPos pos, BlockState state, InfusingStandBlockEntity blockEntity) {
        if(blockEntity.usedToBe!=blockEntity.getItem(1)){
            blockEntity.setChanged();
            blockEntity.getLevel().sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
        }
        blockEntity.usedToBe=blockEntity.getItem(1);
        if(blockEntity.boilTime>0){
            blockEntity.boilTime-=5;
            if(blockEntity.boilTime%20==0){
                if(!(blockEntity.getItem(0).has(DataComponents.POTION_CONTENTS)
                        &&(blockEntity.getItem(1).has(DataComponents.FOOD)))){
                    blockEntity.boilTime=0;
                    //|| blockEntity.getItem(1).has(DataComponents.TOOL)
                    return;
                }
            }
            if(blockEntity.boilTime<=0){
                blockEntity.boilTime=0;
                if(level instanceof ServerLevel serverLevel){
                    ItemStack potion = blockEntity.getItem(0);
                    blockEntity.getItem(1).set(NMItemComponentInit.INFUSED,potion.getOrDefault(DataComponents.POTION_CONTENTS,new PotionContents(Potions.AWKWARD)));
                    if(blockEntity.getItem(1).has(DataComponents.FOOD)){
                        ItemStack stack = blockEntity.getItem(1);
                        FoodProperties properties = stack.get(DataComponents.FOOD);
                        ArrayList<FoodProperties.PossibleEffect> list = new ArrayList<>(properties.effects());
                        Iterator<MobEffectInstance> iterable = stack.get(NMItemComponentInit.INFUSED).getAllEffects().iterator();
                        if(iterable.hasNext()){
                            MobEffectInstance mobEffectInstance = iterable.next();
                            list.add(new FoodProperties.PossibleEffect(()->{return mobEffectInstance;},1.0f));
                        }
                        properties = new FoodProperties(properties.nutrition(),properties.saturation(),properties.canAlwaysEat(),properties.eatSeconds(),properties.usingConvertsTo(),list);
                        stack.set(DataComponents.FOOD,properties);
                    }
                    blockEntity.setChanged();
                    blockEntity.getLevel().sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
                    ItemStack finishedUsing = potion.finishUsingItem(level,new FakePlayer(serverLevel,new GameProfile(new UUID(-1,-1),"JohnPotionDrinker")));
                    if(potion.getCount()<=0){
                        blockEntity.setItem(0,finishedUsing);
                    }else{
                        Vec3 centered = pos.getCenter().add(-0.3,0.2,-0.3);
                        ItemEntity bottle = new ItemEntity(level,centered.x,centered.y,centered.z,finishedUsing);
                        level.addFreshEntity(bottle);
                    }

                }
            }
        }else if(level instanceof ServerLevel serverLevel){
            if(blockEntity.getItem(0).has(DataComponents.POTION_CONTENTS)&&!blockEntity.getItem(1).has(NMItemComponentInit.INFUSED)
                    &&(blockEntity.getItem(1).has(DataComponents.FOOD))&&
                    (blockEntity.getItem(2).is(NMItemInit.ASH_BALL)||blockEntity.fuel>0)){
                if(blockEntity.fuel>0){
                    blockEntity.fuel-=1;
                }else{
                    ItemStack ashBalls = blockEntity.getItem(2);
                    ashBalls.setCount(ashBalls.getCount()-1);
                    blockEntity.setItem(2,ashBalls);
                    blockEntity.fuel=4;
                }
                blockEntity.boilTime=400;
            }
        }
        blockEntity.aged+=0.36f;
    }

    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putShort("BrewTime", (short)this.boilTime);
        ContainerHelper.saveAllItems(tag, this.items, registries);
        tag.putByte("Fuel", (byte)this.fuel);
    }
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, this.items, registries);
        this.boilTime = tag.getShort("BrewTime");

        this.fuel = tag.getByte("Fuel");
    }
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag compoundtag = new CompoundTag();
        ContainerHelper.saveAllItems(compoundtag, this.items, true, registries);
        return compoundtag;
    }
    @Override
    public int[] getSlotsForFace(Direction direction) {
        if(direction==Direction.UP){return SLOTS_FOR_UP;}
        if(direction==Direction.DOWN){return SLOTS_FOR_DOWN;}
        return SLOTS_FOR_SIDES;
    }

    @Override
    public boolean canPlaceItemThroughFace(int i, ItemStack itemStack, @Nullable Direction direction) {
        return canPlaceItem(i,itemStack);
    }
    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return super.canPlaceItem(slot, stack);
    }
    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return index != 1 || stack.is(Items.GLASS_BOTTLE);
    }


    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("block.nethermore.infusing_stand");
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> nonNullList) {
        this.items=nonNullList;
    }


    public void setItem(int index, ItemStack stack) {
        ItemStack itemstack = (ItemStack)this.items.get(index);
        boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameComponents(itemstack, stack);
        this.items.set(index, stack);
        stack.limitSize(this.getMaxStackSize(stack));
        if (index == 0 && !flag) {
            this.setChanged();
        }

    }
    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {

        return new InfusingStandMenu(i,inventory,this,this.dataAccess);
    }
}
