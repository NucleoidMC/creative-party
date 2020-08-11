package supercoder79.creativeparty;

import xyz.nucleoid.plasmid.game.GameType;

import net.minecraft.util.Identifier;

import net.fabricmc.api.ModInitializer;

public class CreativeParty implements ModInitializer {
	public static final String ID = "creative_party";

	@Override
	public void onInitialize() {
		GameType.register(
				new Identifier(ID, "creative_party"),
				CreativePartyWaiting::open,
				CreativePartyConfig.CODEC
		);
	}
}
