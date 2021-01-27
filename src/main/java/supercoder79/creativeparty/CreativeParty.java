package supercoder79.creativeparty;

import supercoder79.creativeparty.map.type.MapTypes;
import xyz.nucleoid.plasmid.game.GameType;
import xyz.nucleoid.plasmid.game.rule.GameRule;

import net.minecraft.util.Identifier;

import net.fabricmc.api.ModInitializer;

public class CreativeParty implements ModInitializer {
	public static final String ID = "creative_party";

	public static final GameRule DISABLE_EXPLOSION = new GameRule();
	public static final GameRule DISABLE_POTIONS = new GameRule();
	public static final GameRule DISABLE_FALLING_BLOCKS = new GameRule();
	public static final GameRule DISABLE_BOSSES = new GameRule();
	public static final GameRule DISABLE_EGGS = new GameRule();

	@Override
	public void onInitialize() {
		MapTypes.init();

		GameType.register(
				new Identifier(ID, "creative_party"),
				CreativePartyWaiting::open,
				CreativePartyConfig.CODEC
		);
	}
}
