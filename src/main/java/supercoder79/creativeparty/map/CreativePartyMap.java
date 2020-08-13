package supercoder79.creativeparty.map;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.gen.chunk.ChunkGenerator;

public class CreativePartyMap {
	public ChunkGenerator chunkGenerator(MinecraftServer server) {
		return new CreativePartyChunkGenerator(server);
	}
}
