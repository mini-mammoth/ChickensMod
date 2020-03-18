package com.setycz.chickens.handler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;

/**
 * Created by setyc on 12.02.2016.
 */
public class ChickensTab extends CreativeTabs {

    public ChickensTab() {
        super("chickens");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(Items.CHICKEN);
    }

}
