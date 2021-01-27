package supercoder79.creativeparty.map.type;

import com.mojang.serialization.Codec;

import net.minecraft.util.Identifier;

public class MapTypes {
	public static void init() {
		register("default", DefaultMapType.CODEC);
		register("layered", LayeredMapType.CODEC);
	}

	private static void register(String identifier, Codec<? extends MapType> type) {
		MapType.REGISTRY.register(new Identifier("creative_party", identifier), type);
	}
}
