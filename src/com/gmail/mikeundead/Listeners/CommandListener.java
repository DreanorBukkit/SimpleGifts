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
	private HashMap<String, Integer> sendedGiftsByPlayer;
	private ConfigHandler config;
	private HashMap<String, Long> giftCooldown;

	public CommandListener(SimpleGifts simpleGifts, ConfigHandler configHandler)
	{
		this.config = configHandler;
		this.simpleGifts = simpleGifts;
		this.handleSingleGift = new SingleGiftCmd(this.simpleGifts);
		this.handleMultipleGifts = new MultipleGiftsCmd(this.simpleGifts);
		this.handleMoneyGift = new MoneyGiftCmd(this.simpleGifts);
		this.sendedGiftsByPlayer = new HashMap<String, Integer>();
		this.giftCooldown = new HashMap<String, Long>();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		Player player = null;
		if (sender instanceof Player)
		{
			player = (Player) sender;
	    }

		if (cmd.getName().equalsIgnoreCase("gift"))
		{
		    if (args.length == 0)
		    {
		    	this.HandleHelpList(player);
		    	return true;
		    }
			if(args.length > 3)
			{
				sender.sendMessage(ChatColor.RED + "Too many Arguments.");
		        return true;
			}

			if(!this.sendedGiftsByPlayer.containsKey(player.getName()))
			{
				this.sendedGiftsByPlayer.put(player.getName(), 0);
			}

			this.CheckCooldown(player);

			if(this.sendedGiftsByPlayer.get(player.getName()) < this.config.getMaxGiftsPerHour())
			{
				if(args.length == 1 && this.simpleGifts.permissions.has(player, "gift.single"))
		        {
		        	this.handleSingleGift.HandleNormalGift(player, args, this.sendedGiftsByPlayer);
		        }

		        if(args.length == 2 && this.simpleGifts.permissions.has(player, "gift.multiple"))
		        {
		        	this.handleMultipleGifts.HandleNormalGift(player, args, this.sendedGiftsByPlayer);
		        }

		        if(args.length == 3 && this.simpleGifts.permissions.has(player, "gift.money"))
		        {
		        	this.handleMoneyGift.HandleMoney(player, args, this.sendedGiftsByPlayer);
		        }
			}
			else
			{
				int giftPerHour = this.config.getMaxGiftsPerHour();
				long cooldownTime = this.giftCooldown.get(player.getName());
				long timeLeft = (cooldownTime - System.currentTimeMillis()) / 1000 / 60;

				player.sendMessage(String.format(ChatColor.RED + "You have reached the limit of %s gifts per hour. You must wait %s minutes.", giftPerHour, timeLeft));
			}
	    }

		if(cmd.getName().equalsIgnoreCase("giftinfo"))
		{
			if(args.length == 0)
			{
				this.HandleInfoCmd(player);
				return true;
			}
			else
			{
				player.sendMessage(ChatColor.RED + "Wrong usage. Use /giftinfo");
			}
		}
		return true;
	}

	private void HandleInfoCmd(Player player)
	{

		if(this.giftCooldown.containsKey(player.getName()))
		{
			Long cooldown = this.giftCooldown.get(player.getName());
			int sendedGifts = this.sendedGiftsByPlayer.get(player.getName());
			if(sendedGifts != this.config.getMaxGiftsPerHour())
			{
				player.sendMessage(String.format(ChatColor.GREEN + "You have sended %s gift(s) in the last 60min.", sendedGifts));
			}
			else
			{
				player.sendMessage(String.format(ChatColor.GREEN + "You have sended %s gifts. You have to wait: %s min", sendedGifts, (cooldown - System.currentTimeMillis()) / 1000 / 60));
			}
		}
		else
		{
			player.sendMessage(String.format(ChatColor.GREEN + "You haven't send any gifts yet."));
		}
	}

	private void HandleHelpList(CommandSender sender)
	{
		sender.sendMessage(ChatColor.YELLOW + "Simple Gifts - commands");
		sender.sendMessage(ChatColor.AQUA + "/gift <Playername>" + ChatColor.WHITE + " - Sends the item that is currently held in your Hand to a Player.");
		sender.sendMessage(ChatColor.AQUA + "/gift <amount> <Playername>" + ChatColor.WHITE + " - Sends a specific amount of items that are currently held in your Hand to a Player.");
		sender.sendMessage(ChatColor.AQUA + "/gift money <amount> <Playername>" + ChatColor.WHITE + " - Sends a specific amount of money to a Player");
		sender.sendMessage(ChatColor.AQUA + "/giftinfo" + ChatColor.WHITE + " -  Displays your current cooldown time for gifting or the amount of gifts that you are able to send until you hit the cooldown");
	}

	private void CheckCooldown(Player player)
	{
		if(!this.giftCooldown.containsKey(player.getName()))
		{
			this.giftCooldown.put(player.getName(), System.currentTimeMillis() + 3600000);
		}
		else
		{
			long cooldown = this.giftCooldown.get(player.getName());
			long currentTime = System.currentTimeMillis();
			if(currentTime >= cooldown)
			{
				this.giftCooldown.remove(player.getName());
				this.sendedGiftsByPlayer.put(player.getName(), 0);
			}
		}
	}
}
