package supercoder79.creativeparty;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import supercoder79.creativeparty.map.CreativePartyMapConfig;
import xyz.nucleoid.plasmid.game.config.PlayerConfig;

public class CreativePartyConfig {
	public static final Codec<CreativePartyConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			PlayerConfig.CODEC.fieldOf("players").forGetter(config -> config.players),
			CreativePartyMapConfig.CODEC.fieldOf("map").forGetter(config -> config.map)
	).apply(instance, CreativePartyConfig::new));

	public final PlayerConfig players;
	public final CreativePartyMapConfig map;

	public CreativePartyConfig(PlayerConfig players, CreativePartyMapConfig map) {
		this.players = players;
		this.map = map;
	}
}
