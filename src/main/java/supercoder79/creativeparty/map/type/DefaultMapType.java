package supercoder79.creativeparty.map.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

public class DefaultMapType implements MapType {
	public static final Codec<DefaultMapType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			ConfiguredSurfaceBuilder.CODEC.optionalFieldOf("surface_builder", ConfiguredSurfaceBuilders.GRASS).forGetter(config -> config.surfaceBuilder),
			Identifier.CODEC.fieldOf("biome").forGetter(config -> config.biomeId)
	).apply(instance, DefaultMapType::new));

	private static final BlockState STONE = Blocks.STONE.getDefaultState();
	private static final BlockState BEDROCK = Blocks.BEDROCK.getDefaultState();
	private static final BlockState WATER = Blocks.WATER.getDefaultState();

	private final ConfiguredSurfaceBuilder<?> surfaceBuilder;
	private final Identifier biomeId;

	public DefaultMapType(ConfiguredSurfaceBuilder<?> surfaceBuilder, Identifier biomeId) {
		this.surfaceBuilder = surfaceBuilder;
		this.biomeId = biomeId;
	}

	@Override
	public void populateNoise(ChunkRegion world, Chunk chunk) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 60; y++) {

					if (y == 0) {
						chunk.setBlockState(mutable.set(x, y, z), BEDROCK, false);
					} else {
						chunk.setBlockState(mutable.set(x, y, z), STONE, false);
					}
				}
			}
		}
	}

	@Override
	public void buildSurface(ChunkRegion world, Chunk chunk) {
		ChunkPos chunkPos = chunk.getPos();

		ChunkRandom chunkRandom = new ChunkRandom();
		chunkRandom.setTerrainSeed(chunkPos.x, chunkPos.z);
		long seed = world.getSeed();

		int minWorldX = chunkPos.getStartX();
		int minWorldZ = chunkPos.getStartZ();

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int worldX = minWorldX + x;
				int worldZ = minWorldZ + z;
				int height = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE_WG, x, z) + 1;

				this.surfaceBuilder.initSeed(seed);
				this.surfaceBuilder.generate(chunkRandom, chunk, BuiltinBiomes.PLAINS, worldX, worldZ, height, 0.0, STONE, WATER, 0, seed);
			}
		}
	}

	@Override
	public Identifier biomeId() {
		return this.biomeId;
	}

	@Override
	public Codec<? extends MapType> getCodec() {
		return CODEC;
	}
}
