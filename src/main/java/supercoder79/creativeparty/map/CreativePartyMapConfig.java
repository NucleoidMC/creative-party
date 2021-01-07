package supercoder79.creativeparty.map;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class CreativePartyMapConfig {
	public static final Codec<CreativePartyMapConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ConfiguredSurfaceBuilder.CODEC.optionalFieldOf("surface_builder", ConfiguredSurfaceBuilders.GRASS).forGetter(config -> config.surfaceBuilder)
	).apply(instance, CreativePartyMapConfig::new));

	public final ConfiguredSurfaceBuilder<?> surfaceBuilder;

	public CreativePartyMapConfig(ConfiguredSurfaceBuilder<?> surfaceBuilder) {
		this.surfaceBuilder = surfaceBuilder;
	}
}
