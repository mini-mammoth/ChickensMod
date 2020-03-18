package com.setycz.chickens.handler;

import javax.annotation.Nullable;

import com.setycz.chickens.registry.LiquidEggRegistry;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

/**
 * Created by setyc on 13.12.2016.
 */
public class LiquidEggFluidWrapper implements IFluidHandler, IFluidHandlerItem, ICapabilityProvider {

    private final ItemStack container;

    public LiquidEggFluidWrapper(ItemStack container) {
        this.container = container;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable Direction facing) {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY ||
               capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY; 
    }

    @Override
    @Nullable
    public <T> T getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
        }
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY) {
            return CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(this);
        }
        return null;
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new FluidTankProperties[]{new FluidTankProperties(getFluid(), Fluid.BUCKET_VOLUME)};
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        return 0;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        FluidStack fluidStack = getFluid();
        if (!resource.isFluidEqual(fluidStack)) {
            return null;
        }

        return drain(resource.amount, doDrain);
    }

    private FluidStack getFluid() {
        Fluid fluid = LiquidEggRegistry.findById(container.getMetadata()).getFluid();
        return new FluidStack(fluid, Fluid.BUCKET_VOLUME);
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (container.getCount() < 1 || maxDrain < Fluid.BUCKET_VOLUME) {
            return null;
        }

        FluidStack fluidStack = getFluid();
        if (doDrain) {
            container.shrink(1);
        }
        return fluidStack;
    }
    /**
     * @return empty stack - item is consumable
     */
    @Override
    public ItemStack getContainer() {
        return ItemStack.EMPTY;
    }
}
