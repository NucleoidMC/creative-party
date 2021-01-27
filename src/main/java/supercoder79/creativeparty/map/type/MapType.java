package supercoder79.creativeparty.map.type;

import java.util.function.Function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import xyz.nucleoid.plasmid.registry.TinyRegistry;

import net.minecraft.util.Identifier;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;

public interface MapType {
	TinyRegistry<Codec<? extends MapType>> REGISTRY = TinyRegistry.newStable();
	MapCodec<MapType> CODEC = REGISTRY.dispatchMap(MapType::getCodec, Function.identity());

	void populateNoise(ChunkRegion world, Chunk chunk);

	void buildSurface(ChunkRegion world, Chunk chunk);

	Identifier biomeId();

	Codec<? extends MapType> getCodec();
}
