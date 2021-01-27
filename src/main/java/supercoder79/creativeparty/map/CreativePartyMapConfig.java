package supercoder79.creativeparty.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import supercoder79.creativeparty.map.type.MapType;

import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class CreativePartyMapConfig {
	public static final Codec<CreativePartyMapConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			MapType.CODEC.fieldOf("map_type").forGetter(config -> config.mapType)
	).apply(instance, CreativePartyMapConfig::new));

	public final MapType mapType;

	public CreativePartyMapConfig(MapType mapType) {
		this.mapType = mapType;
	}
}
