package mcvmcomputers.item;

import java.io.File;
import java.util.List;

import mcvmcomputers.MCVmComputersMod;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemHarddrive extends Item{

	public ItemHarddrive(Settings settings) {
		super(settings);
	}
	
	@Override
	public boolean shouldSyncTagToClient() {
		return true;
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if(!world.isClient) {
			ItemStack is = user.getStackInHand(hand);
			boolean contains = false;
			try {
				contains = is.getTag().contains("vhdfile");
			}catch(NullPointerException ex) {}
			if(!contains) {
				is.decrement(1);
				user.giveItemStack(new ItemStack(ItemList.ITEM_NEW_HARDDRIVE));
			}else if(!new File(MCVmComputersMod.vhdDirectory, is.getTag().getString("vhdfile")).exists()) {
				is.decrement(1);
				user.giveItemStack(new ItemStack(ItemList.ITEM_NEW_HARDDRIVE));
			}
		}
		return super.use(world, user, hand);
	}
	
	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		if(stack != null) {
			if(stack.getTag() != null) {
				if(stack.getTag().contains("vhdfile")) {
					if(new File(MCVmComputersMod.vhdDirectory, stack.getTag().getString("vhdfile")).exists()) {
						tooltip.add(new LiteralText("Hard drive file: " + stack.getTag().getString("vhdfile")).formatted(Formatting.GRAY));
						tooltip.add(new LiteralText("Used size: " + ((float)new File(MCVmComputersMod.vhdDirectory, stack.getTag().getString("vhdfile")).length())/1024f/1024f).formatted(Formatting.GRAY).append(" MB"));
						return;
					}else {
						stack.getTag().remove("vhdfile");
					}
				}
			}
		}
		tooltip.add(new LiteralText("Invalid item, right click with item").formatted(Formatting.RED));
		tooltip.add(new LiteralText("in hand to get new hard drive").formatted(Formatting.RED));
	}
	
	@Override
	public Text getName(ItemStack stack) {
		if(stack.getTag() != null) {
			if(stack.getTag().contains("vhdfile")) {
				if(new File(MCVmComputersMod.vhdDirectory, stack.getTag().getString("vhdfile")).exists()) {
					return new LiteralText("Hard Drive (").formatted(Formatting.WHITE).append(new LiteralText(stack.getTag().getString("vhdfile")).formatted(Formatting.GREEN).append(new LiteralText(")").formatted(Formatting.WHITE)));
				}else {
					stack.getTag().remove("vhdfile");
				}
			}
		}
		return new LiteralText("Hard Drive (").formatted(Formatting.WHITE).append(new LiteralText("Invalid").formatted(Formatting.RED).append(new LiteralText(")").formatted(Formatting.WHITE)));
	}
	
	public static ItemStack createHardDrive(String fileName) {
		ItemStack is = new ItemStack(ItemList.ITEM_HARDDRIVE);
		CompoundTag ct = is.getOrCreateTag();
		ct.putString("vhdfile", fileName);
		is.setTag(ct);
		return is;
	}

}
