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

public class MultipleGiftsCmd 
{	
	private SimpleGifts simpleGifts;
	
	public MultipleGiftsCmd(SimpleGifts simpleGifts) 
	{
		this.simpleGifts = simpleGifts;
	}

	public void HandleNormalGift(Player player, String[] args, HashMap<String, Integer> giftCooldowns)
	{
		int numberOfItems = 0;
		
		try
		{
			numberOfItems = Integer.parseInt(args[0]);
		}
		catch(Exception e)
		{
			player.sendMessage(ChatColor.RED + "Wrong usage. Type /gift <amount> <Playername>");
			return;
		}
		
		ItemStack itemStack = player.getItemInHand().clone();
		
		if(itemStack.getType() == Material.AIR)
		{
			player.sendMessage(ChatColor.RED + "You dont have a Item in your Hand.");
		}
		else
		{
			this.HandleSendItem(itemStack, player, numberOfItems, args, giftCooldowns);
		}
	}

	private void HandleSendItem(ItemStack itemStack, Player player, int numberOfItems, String[] args, HashMap<String, Integer> giftCooldowns)
	{
		if(args[0].equalsIgnoreCase("0"))
		{
			player.sendMessage(ChatColor.RED + "You can`t send 0 items.");
			return;
		}
		
		Map<Enchantment, Integer> itemInHandEnchant = player.getItemInHand().getEnchantments();	
		
		itemStack.addEnchantments(itemInHandEnchant);
		
		PlayerInventory senderInventory = player.getInventory();
		
		Player otherPlayer = (this.simpleGifts.getServer().getPlayer(args[1]));
		
		if(otherPlayer == null)
		{
			player.sendMessage(ChatColor.RED + args[1] + " is not Online.");
		}
		else
		{			
			if(!otherPlayer.getName().equalsIgnoreCase(player.getName()))
			{
				PlayerInventory inventory = otherPlayer.getInventory();
				
				Material itemInHand = itemStack.getType();
				
				ItemStack is;
				int realItemNumber = itemStack.getAmount();
				
				if(inventory.getSize() > 1)
				{
					if(numberOfItems >= itemStack.getAmount())
					{
						is = new ItemStack(itemStack.getType(), itemStack.getAmount());
						realItemNumber = itemStack.getAmount();
					}
					else
					{
						is = new ItemStack(itemStack.getType(), numberOfItems);	
						realItemNumber = numberOfItems;
					}
					
					inventory.addItem(is);
					senderInventory.remove(is);
			
					otherPlayer.sendMessage(ChatColor.GREEN + player.getName() + " send you " + realItemNumber + " " + itemInHand.name());
					player.sendMessage(ChatColor.GREEN + "You send " + otherPlayer.getName() + " " + realItemNumber + " " + itemInHand.name());
					
					int cd = giftCooldowns.get(player.getName()) +1;
					giftCooldowns.put(player.getName(), cd);
				}
				else
				{
					otherPlayer.sendMessage(ChatColor.RED + player.getName() + " tried to send you " + realItemNumber + " " + itemInHand.name() + " but your inventory is full.");
					player.sendMessage(ChatColor.RED + otherPlayer.getName() + " inventory is full!");
				}
			}
			else
			{
				player.sendMessage(ChatColor.RED + "You cant send yourself a gift.");
			}
		}
	}
}