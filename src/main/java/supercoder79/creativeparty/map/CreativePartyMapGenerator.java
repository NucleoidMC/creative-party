package supercoder79.creativeparty.map;

import java.util.concurrent.CompletableFuture;

import net.minecraft.util.Util;

public class CreativePartyMapGenerator {

	public CompletableFuture<CreativePartyMap> create() {
		return CompletableFuture.supplyAsync(this::build, Util.getServerWorkerExecutor());
	}

	public CreativePartyMap build() {
		return new CreativePartyMap();
	}
}
