package com.setycz.chickens.item;

import java.util.List;

import com.setycz.chickens.entity.EntityColoredEgg;
import com.setycz.chickens.handler.IColorSource;
import com.setycz.chickens.registry.ChickensRegistry;
import com.setycz.chickens.registry.ChickensRegistryItem;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.EggItem;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

/**
 * Created by setyc on 13.02.2016.
 */
public class ItemColoredEgg extends EggItem implements IColorSource {
    public ItemColoredEgg() {
        setHasSubtypes(true);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(new TranslationTextComponent("item.colored_egg.tooltip"));
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        DyeColor color = DyeColor.byDyeDamage(stack.getMetadata());
        String unlocalizedName = color.getTranslationKey();
        // hotfix for compatibility with MoreChickens
        if (unlocalizedName.equals("silver")) {
            unlocalizedName += "Dye";
        }
        return new TranslationTextComponent(getTranslationKey() + "." + unlocalizedName + ".name");
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
    	
        if (this.isInCreativeTab(tab))
        {
        	for (ChickensRegistryItem chicken : ChickensRegistry.getItems()) {
        		if (chicken.isDye()) {
        			subItems.add(new ItemStack(this, 1, chicken.getDyeMetadata()));
        		}
        	}
        }
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return DyeColor.byDyeDamage(stack.getMetadata()).getColorValue();
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);

        if (!playerIn.capabilities.isCreativeMode) {
            itemStackIn.shrink(1);
        }

        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_EGG_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote) {
            String chickenType = getChickenType(itemStackIn);
            if (chickenType != null) {
                EntityColoredEgg entityIn = new EntityColoredEgg(worldIn, playerIn);
                entityIn.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
                entityIn.setChickenType(chickenType);
                worldIn.spawnEntity(entityIn);
            }
        }

        //noinspection ConstantConditions
        playerIn.addStat(Stats.getObjectUseStats(this));
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, itemStackIn);
    }

    public String getChickenType(ItemStack itemStack) {
        ChickensRegistryItem chicken = ChickensRegistry.findDyeChicken(itemStack.getMetadata());
        if (chicken == null) {
            return null;
        }
        return chicken.getRegistryName().toString();
    }
}
