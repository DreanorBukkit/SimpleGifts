package com.gmail.mikeundead.Listeners.HandleCmds;

import java.util.HashMap;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.gmail.mikeundead.SimpleGifts;

public class MoneyGiftCmd 
{
	private SimpleGifts simpleGifts;
    
	public MoneyGiftCmd(SimpleGifts simpleGifts) 
	{
		this.simpleGifts = simpleGifts;
	}

	public void HandleMoney(Player player, String[] args, HashMap<String,Integer> giftCooldowns) 
	{
		double money;
		
		try
		{
			money = Double.parseDouble(args[1]);
		}
		catch(Exception e)
		{
			player.sendMessage(ChatColor.RED + "Wrong usage. Type /gift money <amount> <Playername>");
			return;
		}

		Player otherPlayer = (this.simpleGifts.getServer().getPlayer(args[2]));
		
		if(otherPlayer == null)
		{
			player.sendMessage(ChatColor.RED + args[2] + " is not Online.");
			return;
		}
		
		if(otherPlayer.getName().equalsIgnoreCase(player.getName()))
		{
			player.sendMessage(ChatColor.RED + "You cant send yourself money.");
		}
		else
		{
			EconomyResponse response = this.simpleGifts.econ.withdrawPlayer(player.getName(), money);
			
			if(response.transactionSuccess()) 
			{
				otherPlayer.sendMessage(String.format("%s send you %s you now have %s", player.getName(), this.simpleGifts.econ.format(response.amount), this.simpleGifts.econ.getBalance(otherPlayer.getName())));
				player.sendMessage(String.format("You send %s to %s and have now %s", this.simpleGifts.econ.format(response.amount), otherPlayer.getName(), this.simpleGifts.econ.getBalance(player.getName())));
				
				int cd = giftCooldowns.get(player.getName()) +1;
				giftCooldowns.put(player.getName(), cd);
			} 
			else 
			{
				player.sendMessage(String.format(ChatColor.RED + "You dont have enough money"));
			}
		}
	}
}
