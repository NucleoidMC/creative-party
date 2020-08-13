package supercoder79.creativeparty.map;

import xyz.nucleoid.plasmid.game.world.generator.GameChunkGenerator;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;

public class CreativePartyChunkGenerator extends GameChunkGenerator {

	public CreativePartyChunkGenerator(MinecraftServer server) {
		super(server);
	}

	private static final BlockState STONE = Blocks.STONE.getDefaultState();
	private static final BlockState WATER = Blocks.WATER.getDefaultState();

	@Override
	public void populateNoise(WorldAccess world, StructureAccessor structures, Chunk chunk) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();

		for (int x = 0; x < 16; x++) {
		    for (int z = 0; z < 16; z++) {
				for (int y = 0; y < 60; y++) {
					chunk.setBlockState(mutable.set(x, y, z), STONE, false);
				}
		    }
		}
	}

	@Override
	public void buildSurface(ChunkRegion region, Chunk chunk) {
		ChunkPos chunkPos = chunk.getPos();

		ChunkRandom chunkRandom = new ChunkRandom();
		chunkRandom.setTerrainSeed(chunkPos.x, chunkPos.z);
		long seed = region.getSeed();

		int minWorldX = chunkPos.getStartX();
		int minWorldZ = chunkPos.getStartZ();
		BlockPos.Mutable mutablePos = new BlockPos.Mutable();

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				int worldX = minWorldX + x;
				int worldZ = minWorldZ + z;
				int height = chunk.sampleHeightmap(Heightmap.Type.WORLD_SURFACE_WG, x, z) + 1;

				mutablePos.set(minWorldX + x, height, minWorldZ + z);
				Biomes.PLAINS.buildSurface(chunkRandom, chunk, worldX, worldZ, height, 0.0, STONE, WATER, 0, seed);
			}
		}
	}
}
