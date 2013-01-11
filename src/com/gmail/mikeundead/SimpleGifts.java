package com.gmail.mikeundead;

import java.util.logging.Logger;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class SimpleGifts extends JavaPlugin 
{
	public Logger log;
	private GiftCommand giftCommand;
	public Permission permissions;
    public Economy econ = null;
    
	public void onEnable()
	{
		this.log = this.getLogger();
		
		this.giftCommand = new GiftCommand(this);
		this.getCommand("gift").setExecutor(giftCommand);
		
		this.setupPermissions();
		this.setupEconomy();
		
		this.log.info("Simple Gifts has been enabled!");
	}
 
	public void onDisable()
	{
		this.log.info("Simple Gifts has been disabled.");
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
