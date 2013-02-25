package com.gmail.mikeundead;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.mikeundead.Handlers.ConfigHandler;
import com.gmail.mikeundead.Listeners.CommandListener;

public class SimpleGifts extends JavaPlugin
{
	public Logger log;
	private CommandListener giftCommand;
	public Permission permissions;
    public Economy econ = null;

	@Override
	public void onEnable()
	{
		ConfigHandler configHandler = new ConfigHandler(this);

		this.giftCommand = new CommandListener(this, configHandler);
		this.getCommand("gift").setExecutor(giftCommand);
		this.getCommand("giftinfo").setExecutor(giftCommand);

		this.setupPermissions();
		this.setupEconomy();

		this.getLogger().info("Simple Gifts has been enabled!");
	}

	@Override
	public void onDisable()
	{
		this.getLogger().info("Simple Gifts has been disabled.");
	}

    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        permissions = rsp.getProvider();
        return permissions != null;
    }

	 private boolean setupEconomy()
	 {
		 if(getServer().getPluginManager().getPlugin("Vault") == null)
		 {
			 return false;
		 }
		 RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		 if (rsp == null)
		 {
	        return false;
		 }
		 econ = rsp.getProvider();
		 return econ != null;
	 }
}
