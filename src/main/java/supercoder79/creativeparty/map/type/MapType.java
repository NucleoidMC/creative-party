package supercoder79.creativeparty.map.type;

import java.util.function.Function;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

import xyz.nucleoid.plasmid.game.world.view.VoidBlockView;
import xyz.nucleoid.plasmid.registry.TinyRegistry;

import net.minecraft.util.Identifier;
import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.chunk.Chunk;

public interface MapType {
	TinyRegistry<Codec<? extends MapType>> REGISTRY = TinyRegistry.newStable();
	MapCodec<MapType> CODEC = REGISTRY.dispatchMap(MapType::getCodec, Function.identity());

	void populateNoise(ChunkRegion world, Chunk chunk);

	void buildSurface(ChunkRegion world, Chunk chunk);

	default void populateEntities(ChunkRegion world) {
		return;
	}

	default int getHeight(int x, int z, Heightmap.Type heightmapType) {
		return 0;
	}

	default BlockView getColumnSample(int x, int z) {
		return VoidBlockView.INSTANCE;
	}

	default double getSpawnY() {
		return 62;
	}

	Identifier biomeId();

	Codec<? extends MapType> getCodec();
}
