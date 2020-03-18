package com.setycz.chickens.client.gui;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by setyc on 06.03.2016.
 */
public class TileEntityGuiHandler implements IGuiHandler {

    @Override
    @Nullable
    public Object getServerGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof IInventoryGui) {
            return ((IInventoryGui) te).createContainer(player.inventory);
        }
        return null;
    }

    @Override
    @Nullable
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        if (te instanceof IInventoryGui) {
            return ((IInventoryGui) te).createGui(player.inventory);
        }
        return null;
    }
}
