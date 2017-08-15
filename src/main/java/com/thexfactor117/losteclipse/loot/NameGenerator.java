package com.thexfactor117.losteclipse.loot;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import com.google.common.collect.Lists;
import com.thexfactor117.losteclipse.api.Rarity;
import com.thexfactor117.losteclipse.stats.attributes.ArmorAttribute;
import com.thexfactor117.losteclipse.stats.attributes.JewelryAttribute;
import com.thexfactor117.losteclipse.stats.attributes.WeaponAttribute;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 * @author TheXFactor117
 *
 * Class used to randomly generate names for items given a variety of factors.S
 * 
 */
public class NameGenerator 
{
	/** 
	 * Generates a random name based on rarity and the current attributes the NBTTagCompound might have.
	 * 
	 * Common - if it has an attribute, generate an attribute prefix. If not, generate a common prefix.
	 * Uncommon - generate a prefix or a suffix based on attribute.
	 * Rare+ - generate a random prefix OR suffix OR both.
	 */
	public static void generateName(ItemStack stack, NBTTagCompound nbt)
	{
		if (Rarity.getRarity(nbt) == Rarity.COMMON)
		{
			String type = NameGenerator.getType(nbt.getString("Type") + "_type");
			
			if (WeaponAttribute.getRandomActiveAttribute(nbt) != null)
			{	
				String prefix = NameGenerator.getPrefix(getAttributeString(nbt) + "_prefix");
				stack.setStackDisplayName(Rarity.getRarity(nbt).getColor() + prefix + " " + type);
			}
			else
			{
				String prefix = NameGenerator.getPrefix("common_prefix");
				stack.setStackDisplayName(Rarity.getRarity(nbt).getColor() + prefix + " " + type);
			}
		}
		else if (Rarity.getRarity(nbt) == Rarity.UNCOMMON)
		{
			String type = NameGenerator.getType(nbt.getString("Type") + "_type");
			String prefix = NameGenerator.getPrefix(getAttributeString(nbt) + "_prefix");
			stack.setStackDisplayName(Rarity.getRarity(nbt).getColor() + prefix + " " + type);
		}
		else
		{
			String type = NameGenerator.getType(nbt.getString("Type") + "_type");
			int rand = (int) (Math.random() * 3);
			
			if (rand == 0)
			{
				String prefix = NameGenerator.getPrefix(getAttributeString(nbt) + "_prefix");
				stack.setStackDisplayName(Rarity.getRarity(nbt).getColor() + prefix + " " + type);
			}
			else if (rand == 1)
			{
				String suffix = NameGenerator.getSuffix(getAttributeString(nbt) + "_suffix");
				stack.setStackDisplayName(Rarity.getRarity(nbt).getColor() + type + " " + suffix);
			}
			else
			{
				String prefix = NameGenerator.getPrefix(getAttributeString(nbt) + "_prefix");
				String suffix = NameGenerator.getSuffix(getAttributeString(nbt) + "_suffix");
				stack.setStackDisplayName(Rarity.getRarity(nbt).getColor() + prefix + " " + type + " " + suffix);
			}
		}
	}
	
	/** Returns a randomized, localized name of a current attribute to lookup in name files. */
	private static String getAttributeString(NBTTagCompound nbt)
	{
		String type = nbt.getString("Type");
		
		if (type.equals("dagger") || type.equals("sword") || type.equals("mace") || type.equals("wand") || type.equals("staff"))
		{
			return WeaponAttribute.getRandomActiveAttribute(nbt).getLocalizedString();
		}
		else if (type.equals("helmet") || type.equals("chestplate") || type.equals("leggings") || type.equals("boots"))
		{
			return ArmorAttribute.getRandomActiveAttribute(nbt).getLocalizedString();
		}
		else if (type.equals("amulet") || type.equals("ring") || type.equals("belt"))
		{
			return JewelryAttribute.getRandomActiveAttribute(nbt).getLocalizedName();
		}
		
		return "";
	}
	
	public static String getPrefix(String property)
	{
		try
		{
			return NameGenerator.readNameFile("prefixes", property);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}
	
	public static String getType(String property)
	{
		try
		{
			return NameGenerator.readNameFile("types", property);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}
	
	public static String getSuffix(String property)
	{
		try
		{
			return NameGenerator.readNameFile("suffixes", property);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		return "";
	}
	
	/** Reads the given file. Searches the file for the property passed in, and stores all of those properties into a list. Randomly return one of the properties. */
	private static String readNameFile(String filePath, String property) throws IOException
	{
		InputStreamReader in = new InputStreamReader(NameGenerator.class.getClassLoader().getResourceAsStream("assets/losteclipse/names/" + filePath + ".txt"), "UTF-8");
		Properties props = new Properties();
		props.load(in);
		Enumeration<?> e = props.propertyNames();
		List<String> list = Lists.newArrayList();
		
		while(e.hasMoreElements())
		{
			String key = (String) e.nextElement();
			
			if (key != null && key.contains(property))
			{
				list.add(props.getProperty(key));
			}
		}
		
		in.close();
		
		return list.size() > 0 ? list.get((int) (Math.random() * list.size())) : "Error";
	}
}