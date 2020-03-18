package com.setycz.chickens.common;

import com.setycz.chickens.ChickensMod;
import com.setycz.chickens.entity.EntityColoredEgg;
import com.setycz.chickens.item.ItemColoredEgg;
import com.setycz.chickens.item.ItemLiquidEgg;
import com.setycz.chickens.registry.LiquidEggRegistry;
import com.setycz.chickens.registry.LiquidEggRegistryItem;

import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by setyc on 18.02.2016.
 */
public class CommonProxy {
	
	public void preInit() {

	}
	
	public void init() {

	}
/*
	public void registerChicken(ChickensRegistryItem chicken) {
		if (chicken.isDye() && chicken.isEnabled()) {
			GameRegistry.addShapelessRecipe(new ItemStack(ChickensMod.coloredEgg, 1, chicken.getDyeMetadata()),
					new ItemStack(Items.EGG), new ItemStack(Items.DYE, 1, chicken.getDyeMetadata()));
		}
	}
*/

	public void registerLiquidEgg(LiquidEggRegistryItem liquidEgg) {
		DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.putObject(ChickensMod.liquidEgg, new DispenseLiquidEgg());
		DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.putObject(ChickensMod.coloredEgg, new DispenseColorEgg());
	}

	class DispenseColorEgg extends ProjectileDispenseBehavior {
		@Override
		protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
			EntityColoredEgg entityColoredEgg = new EntityColoredEgg(worldIn, position.getX(), position.getY(),
					position.getZ());
			entityColoredEgg.setChickenType(((ItemColoredEgg) stackIn.getItem()).getChickenType(stackIn));
			return entityColoredEgg;
		}
	}

	class DispenseLiquidEgg extends DefaultDispenseItemBehavior {
		@Override
		protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
			ItemLiquidEgg itemLiquidEgg = (ItemLiquidEgg) stack.getItem();
			BlockPos blockpos = source.getBlockPos().offset(source.getBlockState().getValue(DispenserBlock.FACING));
			Block liquid = LiquidEggRegistry.findById(stack.getMetadata()).getLiquid();
			if (!itemLiquidEgg.tryPlaceContainedLiquid(null, source.getWorld(), blockpos, liquid)) {
				return super.dispenseStack(source, stack);
			}
			stack.shrink(1);
			return stack;
		}
	}
}
