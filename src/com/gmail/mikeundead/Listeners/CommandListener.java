package com.gmail.mikeundead.Listeners;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.mikeundead.SimpleGifts;
import com.gmail.mikeundead.Handlers.ConfigHandler;
import com.gmail.mikeundead.Listeners.HandleCmds.MoneyGiftCmd;
import com.gmail.mikeundead.Listeners.HandleCmds.MultipleGiftsCmd;
import com.gmail.mikeundead.Listeners.HandleCmds.SingleGiftCmd;

public class CommandListener implements CommandExecutor
{
	private MultipleGiftsCmd handleMultipleGifts;
	private SingleGiftCmd handleSingleGift;

	private SimpleGifts simpleGifts;
	private MoneyGiftCmd handleMoneyGift;
	private HashMap<String, Integer> giftCooldowns;
	private ConfigHandler config;
	
	public CommandListener(SimpleGifts simpleGifts, ConfigHandler configHandler) 
	{
		this.config = configHandler;
		this.simpleGifts = simpleGifts;
		this.handleSingleGift = new SingleGiftCmd(this.simpleGifts);
		this.handleMultipleGifts = new MultipleGiftsCmd(this.simpleGifts);
		this.handleMoneyGift = new MoneyGiftCmd(this.simpleGifts);
		this.giftCooldowns = new HashMap<String, Integer>();

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
			
			if(this.giftCooldowns.containsKey(player.getName()) == false)
			{
				this.giftCooldowns.put(player.getName(), 0);
			}
			
			if(this.giftCooldowns.get(player.getName()) < this.config.getMaxGiftsPerHour())
			{
				if(args.length == 1 && this.simpleGifts.permissions.has(player, "gift.single"))
		        {
		        	this.handleSingleGift.HandleNormalGift(player, args, this.giftCooldowns);
		        }
		        
		        if(args.length == 2 && this.simpleGifts.permissions.has(player, "gift.multiple"))
		        {
		        	this.handleMultipleGifts.HandleNormalGift(player, args, this.giftCooldowns);
		        }
		        
		        if(args.length == 3 && this.simpleGifts.permissions.has(player, "gift.money"))
		        {
		        	this.handleMoneyGift.HandleMoney(player, args, this.giftCooldowns);
		        }
			}
	    }
		return true;
	}
}
