package supercoder79.creativeparty;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.gegy1000.plasmid.game.config.GameConfig;
import net.gegy1000.plasmid.game.config.PlayerConfig;

public class CreativePartyConfig implements GameConfig {
	public static final Codec<CreativePartyConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			PlayerConfig.CODEC.fieldOf("players").forGetter(config -> config.players)
	).apply(instance, CreativePartyConfig::new));

	public final PlayerConfig players;

	public CreativePartyConfig(PlayerConfig config) {
		players = config;
	}
}
