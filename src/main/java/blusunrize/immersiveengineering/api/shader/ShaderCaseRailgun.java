package blusunrize.immersiveengineering.api.shader;

import blusunrize.immersiveengineering.api.ApiUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;

public class ShaderCaseRailgun extends ShaderCase
{
	public String additionalTexture = null;

	public ShaderCaseRailgun(String overlayType, int[] colourGrip, int[] colourPrimary, int[] colourSecondary, String additionalTexture)
	{
		super(overlayType, colourGrip,colourPrimary,colourSecondary, "immersiveengineering:items/shaders/railgun_");
		this.additionalTexture = additionalTexture;
	}

	@Override
	public String getShaderType()
	{
		return "railgun";
	}


	@Override
	public int getPasses(ItemStack shader, ItemStack item, String modelPart)
	{
		if(modelPart.equals("sled")||modelPart.equals("wires")||modelPart.equals("tubes"))
			return 1;
		boolean hasUncoloured = modelPart.equals("barrel")||modelPart.equals("frame")||modelPart.equals("upgrade_speed")||modelPart.equals("upgrade_scope");
		return 2+(additionalTexture!=null?1:0)+(hasUncoloured?1:0);
	}

	@Override
	public TextureAtlasSprite getReplacementSprite(ItemStack shader, ItemStack item, String modelPart, int pass)
	{
		int maxPass = getPasses(shader, item, modelPart);
		boolean hasUncoloured = modelPart.equals("sled")||modelPart.equals("wires")||modelPart.equals("tubes")||modelPart.equals("frame")||modelPart.equals("barrel")||modelPart.equals("upgrade_speed")||modelPart.equals("upgrade_scope");
		if(hasUncoloured && pass==maxPass-1)//uncoloured
			return i_railgunUncoloured;
		if(pass==maxPass-(hasUncoloured?2:1) && i_railgunAdditional!=null)
			return i_railgunAdditional;

		return pass==0?i_railgunBase: i_railgunOverlay;
	}

	@Override
	public int[] getRGBAColourModifier(ItemStack shader, ItemStack item, String modelPart, int pass)
	{
		int maxPass = getPasses(shader, item, modelPart);
		boolean hasUncoloured = modelPart.equals("sled")||modelPart.equals("wires")||modelPart.equals("tubes")||modelPart.equals("frame")||modelPart.equals("barrel")||modelPart.equals("upgrade_speed")||modelPart.equals("upgrade_scope");
		if(hasUncoloured&&pass==maxPass-1)
			return defaultWhite;
		if(pass==maxPass-(hasUncoloured?2:1) && i_railgunAdditional!=null)
			return colourOverlay;

		int i=getTextureType(modelPart,pass); //0 == grip, 1==main, 2==detail
		if(i==0)
			return colourUnderlying;
		if(i==1)
			return colourPrimary;
		if(i==2)
			return colourSecondary;
		return defaultWhite;
	}

	public int getTextureType(String modelPart, int pass)
	{
		//0 == grip, 1==main, 2==detail
		if(modelPart.equals("grip"))
			return pass==0?0:pass+1;
		return pass+1;
	}

	public TextureAtlasSprite i_railgunBase;
	public TextureAtlasSprite i_railgunOverlay;
	public TextureAtlasSprite i_railgunUncoloured;
	public TextureAtlasSprite i_railgunAdditional;
	@Override
	public void stichTextures(TextureMap map, int sheetID)
	{
		i_railgunBase = ApiUtils.getRegisterSprite(map, "immersiveengineering:items/shaders/railgun_0");
		i_railgunOverlay = ApiUtils.getRegisterSprite(map, this.baseTexturePath+"1_"+this.overlayType);
		i_railgunUncoloured = ApiUtils.getRegisterSprite(map, "immersiveengineering:items/shaders/railgun_uncoloured");
		if(this.additionalTexture!=null)
			i_railgunAdditional = ApiUtils.getRegisterSprite(map, this.baseTexturePath+additionalTexture);
	}

	@Override
	public void modifyRender(ItemStack shader, ItemStack item, String modelPart, int pass, boolean pre, boolean inventory)
	{
	}
}