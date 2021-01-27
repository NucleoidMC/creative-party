package supercoder79.creativeparty.map.type;

import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.chunk.Chunk;

public class LayeredMapType implements MapType {
	public static final Codec<LayeredMapType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.fieldOf("biome").forGetter(config -> config.biomeId),
			Layer.CODEC.listOf().fieldOf("layers").forGetter(config -> config.layers),
			Codec.INT.fieldOf("max_y").forGetter(config -> config.maxY)
	).apply(instance, LayeredMapType::new));

	private final Identifier biomeId;
	private final List<Layer> layers;
	private final int maxY;

	public LayeredMapType(Identifier biomeId, List<Layer> layers, int maxY) {
		this.biomeId = biomeId;
		this.layers = layers;
		this.maxY = maxY;
	}

	@Override
	public void populateNoise(ChunkRegion world, Chunk chunk) {
		BlockPos.Mutable mutable = new BlockPos.Mutable();

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				for (int y = 0; y <= this.maxY; y++) {
					mutable.set(x, y, z);

					for (Layer layer : this.layers) {
						if (layer.startY <= y) {
							chunk.setBlockState(mutable, layer.state, false);
						}
					}
				}
			}
		}
	}

	@Override
	public void buildSurface(ChunkRegion world, Chunk chunk) {

	}

	@Override
	public Identifier biomeId() {
		return this.biomeId;
	}

	@Override
	public Codec<? extends MapType> getCodec() {
		return CODEC;
	}

	private static final class Layer {
		private static final Codec<Layer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				BlockState.CODEC.fieldOf("state").forGetter(layer -> layer.state),
				Codec.INT.fieldOf("start_y").forGetter(layer -> layer.startY)
		).apply(instance, Layer::new));

		private final BlockState state;
		private final int startY;

		private Layer(BlockState state, int startY) {
			this.state = state;
			this.startY = startY;
		}
	}
}
