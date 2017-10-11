package com.lsc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.lsc.init.ModCapabilities;
import com.lsc.init.ModEntities;
import com.lsc.init.ModEvents;
import com.lsc.init.ModLootFunctions;
import com.lsc.init.ModLootTables;
import com.lsc.init.ModPackets;
import com.lsc.proxies.ServerProxy;
import com.lsc.util.GuiHandler;
import com.lsc.util.Reference;
import com.lsc.worldgen.LSCWorldGenerator;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * 
 * @author TheXFactor117
 *
 * A Hack/Mine-like Minecraft mod.
 *
 */
@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION)
public class LootSlashConquer 
{
	@Instance(Reference.MODID)
	public static LootSlashConquer instance;
	@SidedProxy(clientSide = Reference.CLIENT_PROXY, serverSide = Reference.SERVER_PROXY)
	public static ServerProxy proxy;
	public static final Logger LOGGER = LogManager.getLogger(Reference.NAME);
	public static SimpleNetworkWrapper network;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{	
		ModLootTables.register();
		ModCapabilities.registerCapabilities();
		ModEvents.registerEvents();
		ModEntities.registerEntities();
		ModLootFunctions.registerFunctions();
		
		proxy.preInit(event);
		
		ModPackets.registerPackets();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		GameRegistry.registerWorldGenerator(new LSCWorldGenerator(), 100);
		proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit(event);
	}
}