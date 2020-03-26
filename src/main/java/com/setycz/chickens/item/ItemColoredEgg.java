package com.setycz.chickens.item;

import java.util.List;
import java.util.Random;

import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.entity.EntityColoredEgg;
import com.setycz.chickens.handler.IColorSource;
import com.setycz.chickens.registry.ChickensRegistry;
import com.setycz.chickens.registry.ChickensRegistryItem;

import init.ModItemGroups;
import init.ModItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.EggItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * Created by setyc on 13.02.2016.
 */
public class ItemColoredEgg extends EggItem implements IColorSource {
    public ItemColoredEgg() {
        super(new Properties().group(ModItemGroups.CHICKENS_TAB));
        setRegistryName(new ResourceLocation(ChickensMod.MODID, "colored_egg"));
    }

    @Override
    public String getTranslationKey() {
        return "colored_egg";
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(TextComponentUtils.toTextComponent(() -> I18n.format("item.colored_egg.tooltip")));
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        DyeColor color = DyeColor.byDyeDamage(stack.getMetadata());

        String unlocalizedName = color.getTranslationKey();

        String finalUnlocalizedName = unlocalizedName;

        return TextComponentUtils.toTextComponent(() ->I18n.format(getTranslationKey() + "." + finalUnlocalizedName + ".name"));
    }

    @Override
    public void fillItemGroup(ItemGroup itemGroup, NonNullList<ItemStack> itemStacks) {
        super.fillItemGroup(itemGroup, itemStacks);

        for (ChickensRegistryItem chicken : ChickensRegistry.getItems()) {
            if (chicken.isDye()) {
                ItemStack itemStack = new ItemStack(ModItems.COLOREDEGG);
                applyEntityIdToItemStack(itemStack, chicken.getRegistryName());
                itemStacks.add(itemStack);
            }
        }
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return DyeColor.byDyeDamage(stack.getMetadata()).getColorValue();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
        Random itemRand = new Random();
        ItemStack itemStackIn = playerIn.getHeldItem(hand);

        if (!playerIn.isCreative()) {
            itemStackIn.shrink(1);
        }

        worldIn.playSound(playerIn, playerIn.getPosition(), SoundEvents.ENTITY_EGG_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote) {
            String chickenType = getChickenType(itemStackIn);
            if (chickenType != null) {
                EntityColoredEgg entityIn = new EntityColoredEgg(worldIn, playerIn);
                entityIn.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
                entityIn.setChickenType(chickenType);
                entityIn.getType().spawn(worldIn, null, null, entityIn.getPosition(), SpawnReason.MOB_SUMMONED, true, false);
            }
        }

        return new ActionResult<>(ActionResultType.SUCCESS, itemStackIn);
    }

    public String getChickenType(ItemStack itemStack) {
        ChickensRegistryItem chicken = ChickensRegistry.findDyeChicken(itemStack.getMetadata());
        if (chicken == null) {
            return null;
        }
        return chicken.getRegistryName().toString();
    }

    public static void applyEntityIdToItemStack(ItemStack stack, ResourceLocation entityId)
    {
        CompoundNBT nbttagcompound = stack.hasTag() ? stack.getTag() : new CompoundNBT();
        CompoundNBT nbttagcompound1 = new CompoundNBT();
        nbttagcompound1.putString("id", entityId.toString());
        nbttagcompound.put("ChickenType", nbttagcompound1);
        stack.setTag(nbttagcompound);
    }
}
