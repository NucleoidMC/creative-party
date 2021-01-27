package supercoder79.creativeparty.map;

import supercoder79.creativeparty.map.type.MapType;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;

public class CreativePartyMap {
	public ChunkGenerator chunkGenerator(MinecraftServer server, MapType mapType) {
		return new CreativePartyChunkGenerator(server, mapType);
	}
}
