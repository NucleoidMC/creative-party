package supercoder79.creativeparty;

import net.gegy1000.plasmid.game.GameType;

import net.minecraft.util.Identifier;

import net.fabricmc.api.ModInitializer;

public class CreativeParty implements ModInitializer {
	public static final String ID = "creative_party";

	public static final GameType<CreativePartyConfig> TYPE = GameType.register(
			new Identifier(ID, "creative_party"),
			CreativePartyWaiting::open,
			CreativePartyConfig.CODEC
	);

	@Override
	public void onInitialize() {

	}
}
