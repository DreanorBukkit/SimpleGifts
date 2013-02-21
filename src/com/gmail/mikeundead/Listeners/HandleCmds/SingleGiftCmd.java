package com.gmail.mikeundead.Listeners.HandleCmds;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.gmail.mikeundead.SimpleGifts;

public class SingleGiftCmd 
{
	private SimpleGifts simpleGifts;
	
	public SingleGiftCmd(SimpleGifts simpleGifts) 
	{
		this.simpleGifts = simpleGifts;
	}

	public void HandleNormalGift(Player player, String[] args, HashMap<String,Integer> giftCooldowns) 
	{
		PlayerInventory senderInventory = player.getInventory();
		
		Material itemInHand = player.getItemInHand().getType();
		
		Map<Enchantment, Integer> itemInHandEnchant = player.getItemInHand().getEnchantments();	
		ItemStack itemInHandStack = new ItemStack(itemInHand);
		
		if(itemInHandStack.getType() == Material.AIR)
		{
			player.sendMessage(ChatColor.RED + "You dont have a Item in your Hand.");
			return;
		}
		
		if(itemInHandEnchant.size() > 0)
		{
			itemInHandStack.addEnchantments(itemInHandEnchant);
		}
		
		Player otherPlayer = (this.simpleGifts.getServer().getPlayer(args[0]));
		
		if(otherPlayer == null)
		{
			player.sendMessage(ChatColor.RED + args[0] + " is not Online.");
			return;
		}
		else
		{
			if(!otherPlayer.getName().equalsIgnoreCase(player.getName()))
			{
				PlayerInventory inventory = otherPlayer.getInventory();
				inventory.addItem(itemInHandStack);
				senderInventory.remove(itemInHand);
				
				otherPlayer.sendMessage(ChatColor.GREEN + player.getName() + " send you 1 " + itemInHand.name());
				player.sendMessage(ChatColor.GREEN + "You send " + otherPlayer.getName() + " 1 " + itemInHand.name());
				
				int cd = giftCooldowns.get(player.getName()) +1;
				giftCooldowns.put(player.getName(), cd);
			}
			else
			{		
				player.sendMessage(ChatColor.RED + "You cant send yourself a gift.");
			}
		}
	}
}
