package com.gmail.mikeundead.Handlers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.gmail.mikeundead.SimpleGifts;

public class ConfigHandler
{
	private SimpleGifts plugin;
	private File configFile;
    private FileConfiguration config;
	private int maxGiftsPerHour;

    public ConfigHandler(SimpleGifts plugin)
    {
    	this.plugin = plugin;
    	this.configFile = new File(this.plugin.getDataFolder(), "config.yml");
    	
	    try 
	    {
	    	this.FirstRun();
	    } 
	    catch (Exception e) 
	    {
	        e.printStackTrace();
	    }
	    
	    this.config = new YamlConfiguration();
	    this.LoadYamls();
    }
	
	public int getMaxGiftsPerHour()
	{
		return this.maxGiftsPerHour;
	}
	
	private void FirstRun() throws Exception
	{
	    if(!this.configFile.exists())
	    {
	        this.configFile.getParentFile().mkdirs();
	        
	        this.config = new YamlConfiguration();
	        
	        this.SaveYamls();
	        
	        this.Copy(this.plugin.getResource("config.yml"), configFile);
	    }
	}

	private void Copy(InputStream in, File file) 
	{
	    try 
	    {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        
	        while((len=in.read(buf)) > 0)
	        {
	            out.write(buf, 0, len);
	        }
	        
	        out.close();
	        in.close();
	    } 
	    catch (Exception e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	private void SaveYamls() 
	{
	    try 
	    {
	        this.config.save(configFile);
	    } 
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	private void LoadYamls() 
	{
	    try 
	    {
	        this.config.load(configFile);
	        this.LoadValues();
	    } 
	    catch (Exception e) 
	    {
	        e.printStackTrace();
	    }
	}
	
	private void LoadValues() 
	{
		this.setMaxGiftsPerHour();
	}	
		
	private void setMaxGiftsPerHour()
	{
		this.maxGiftsPerHour = this.config.getInt("MaxGiftsPerHour");
	}
}