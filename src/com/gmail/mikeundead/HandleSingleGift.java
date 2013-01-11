package com.gmail.mikeundead;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class HandleSingleGift 
{
	private SimpleGifts simpleGifts;
	
	public HandleSingleGift(SimpleGifts simpleGifts) 
	{
		this.simpleGifts = simpleGifts;
	}

	public void HandleNormalGift(Player player, String[] args) 
	{
		Material itemInHand = player.getItemInHand().getType();
		
		Map<Enchantment, Integer> itemInHandEnchant = player.getItemInHand().getEnchantments();	
		ItemStack itemStack = new ItemStack(itemInHand);
		
		if(itemStack.getType() == Material.AIR)
		{
			player.sendMessage(ChatColor.RED + "You dont have a Item in your Hand.");
		}
		else
		{
			itemStack.addEnchantments(itemInHandEnchant);
			PlayerInventory senderInventory = player.getInventory();
			
			Player otherPlayer = (this.simpleGifts.getServer().getPlayer(args[0]));
			
			if(otherPlayer == null)
			{
				player.sendMessage(ChatColor.RED + args[0] + " is not Online.");
			}
			else
			{
				if(!otherPlayer.getName().equalsIgnoreCase(player.getName()))
				{
					PlayerInventory inventory = otherPlayer.getInventory();
					inventory.addItem(itemStack);
					senderInventory.remove(itemInHand);
					
					otherPlayer.sendMessage(ChatColor.GREEN + player.getName() + " send you 1 " + itemInHand.name());
					player.sendMessage(ChatColor.GREEN + "You send " + otherPlayer.getName() + " 1 " + itemInHand.name());
				}
				else
				{		
					player.sendMessage(ChatColor.RED + "You cant send yourself a gift.");
				}
			}
		}
	}
}
