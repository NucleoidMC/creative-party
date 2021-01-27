package supercoder79.creativeparty.map;

import java.util.Collections;
import java.util.Optional;

import supercoder79.creativeparty.map.type.MapType;
import xyz.nucleoid.plasmid.game.world.generator.GameChunkGenerator;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.StructuresConfig;

public class CreativePartyChunkGenerator extends GameChunkGenerator {

	private final MapType type;

	public CreativePartyChunkGenerator(MinecraftServer server, MapType type) {
		super(createBiomeSource(server, RegistryKey.of(Registry.BIOME_KEY, type.biomeId())), new StructuresConfig(Optional.empty(), Collections.emptyMap()));
		this.type = type;
	}

	protected static FixedBiomeSource createBiomeSource(MinecraftServer server, RegistryKey<Biome> biome) {
		DynamicRegistryManager registryManager = server.getRegistryManager();
		return new FixedBiomeSource((Biome)registryManager.get(Registry.BIOME_KEY).get(biome));
	}

	@Override
	public void populateNoise(WorldAccess world, StructureAccessor structures, Chunk chunk) {
		this.type.populateNoise((ChunkRegion) world, chunk);
	}

	@Override
	public void buildSurface(ChunkRegion region, Chunk chunk) {
		this.type.buildSurface(region, chunk);
	}

	@Override
	public void carve(long seed, BiomeAccess access, Chunk chunk, GenerationStep.Carver carver) {
		
	}
}
