package com.gmail.mikeundead;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiftCommand implements CommandExecutor
{
	private HandleMultipleGifts handleMultipleGifts;
	private HandleSingleGift handleSingleGift;
	private SimpleGifts simpleGifts;
	private HandleMoneyGift handleMoneyGift;
	
	public GiftCommand(SimpleGifts simpleGifts) 
	{
		this.simpleGifts = simpleGifts;
		this.handleSingleGift = new HandleSingleGift(this.simpleGifts);
		this.handleMultipleGifts = new HandleMultipleGifts(this.simpleGifts);
		this.handleMoneyGift = new HandleMoneyGift(this.simpleGifts);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) 
	{
		Player player = null;
		if (sender instanceof Player) 
		{
			player = (Player) sender;
	    }

		if (cmd.getName().equalsIgnoreCase("gift"))
		{
			if(args.length > 3)
			{
				sender.sendMessage(ChatColor.RED + "Too many Arguments.");
		        return true;
			}
	        if (args.length == 0) 
	        {
	        	return this.HandleHelpList(player);
	        }
	        
	        if(args.length == 1 && this.simpleGifts.permissions.has(player, "gift.single"))
	        {
	        	this.handleSingleGift.HandleNormalGift(player, args);
	        }
	        
	        if(args.length == 2 && this.simpleGifts.permissions.has(player, "gift.multiple"))
	        {
	        	this.handleMultipleGifts.HandleNormalGift(player, args);
	        }
	        
	        if(args.length == 3 && this.simpleGifts.permissions.has(player, "gift.money"))
	        {
	        	this.handleMoneyGift.HandleMoney(player, args);
	        }
	    }
		return true;
	}

	private boolean HandleHelpList(CommandSender sender)
	{
		sender.sendMessage(ChatColor.YELLOW + "Simple Gifts v1.1 - commands");
		sender.sendMessage(ChatColor.AQUA + "/gift <Playername>" + ChatColor.WHITE + "- Send the item that is currently in your Hand to the Player.");
		sender.sendMessage(ChatColor.AQUA + "/gift <amount> <Playername>" + ChatColor.WHITE + "- Send x items that are currently in your Hand to the Player.");
		sender.sendMessage(ChatColor.AQUA + "/gift money <amount> <Playername>" + ChatColor.WHITE + "- Send money to the Player.");
		return true;
	}
}
