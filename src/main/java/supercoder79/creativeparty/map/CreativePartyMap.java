package supercoder79.creativeparty.map;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;

public class CreativePartyMap {
	public ChunkGenerator chunkGenerator(MinecraftServer server, ConfiguredSurfaceBuilder<?> surfaceBuilder) {
		return new CreativePartyChunkGenerator(server, surfaceBuilder);
	}
}
