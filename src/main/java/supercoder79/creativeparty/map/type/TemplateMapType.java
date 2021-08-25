package supercoder79.creativeparty.map.type;

import java.io.IOException;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.world.BlockView;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import xyz.nucleoid.plasmid.game.GameOpenException;
import xyz.nucleoid.plasmid.game.world.view.VoidBlockView;
import xyz.nucleoid.plasmid.map.template.MapTemplate;
import xyz.nucleoid.plasmid.map.template.MapTemplateSerializer;
import xyz.nucleoid.plasmid.map.template.TemplateChunkGenerator;

public class TemplateMapType implements MapType {
	public static final Codec<TemplateMapType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Identifier.CODEC.fieldOf("map_template").forGetter(config -> config.mapTemplateId)
	).apply(instance, TemplateMapType::new));

	private final Identifier mapTemplateId;
	private MapTemplate mapTemplate;
	private TemplateChunkGenerator chunkGenerator;

	public TemplateMapType(Identifier mapTemplateId) {
		this.mapTemplateId = mapTemplateId;
	}

	private void ensureMapTemplate() {
		if (this.mapTemplate == null) {
			try {
				this.mapTemplate = MapTemplateSerializer.INSTANCE.loadFromResource(this.mapTemplateId);
			} catch (IOException e) {
				throw new GameOpenException(new LiteralText("Failed to load map template"), e);
			}
		}
	}

	private void ensureChunkGenerator(ChunkRegion world) {
		if (this.chunkGenerator == null) {
			this.ensureMapTemplate();
			this.chunkGenerator = new TemplateChunkGenerator(world.toServerWorld().getServer(), this.mapTemplate);
		}
	}

	@Override
	public void populateNoise(ChunkRegion world, Chunk chunk) {
		this.ensureChunkGenerator(world);
		this.chunkGenerator.populateNoise(world, world.toServerWorld().getStructureAccessor(), chunk);
	}

	@Override
	public void populateEntities(ChunkRegion world) {
		this.ensureChunkGenerator(world);
		this.chunkGenerator.populateEntities(world);
	}

	@Override
	public int getHeight(int x, int z, Heightmap.Type heightmapType) {
		return this.chunkGenerator == null ? 0 : this.chunkGenerator.getHeight(x, z, heightmapType);
	}

	@Override
	public BlockView getColumnSample(int x, int z) {
		return this.chunkGenerator == null ? VoidBlockView.INSTANCE : this.chunkGenerator.getColumnSample(x, z);
	}
	
	@Override
	public double getSpawnY(World world) {
		this.ensureMapTemplate();
		return this.mapTemplate.getBounds().getMax().getY();
	}

	@Override
	public void buildSurface(ChunkRegion world, Chunk chunk) {
		return;
	}

	@Override
	public Identifier biomeId() {
		this.ensureMapTemplate();
		return this.mapTemplate.getBiome().getValue();
	}

	@Override
	public Codec<? extends MapType> getCodec() {
		return CODEC;
	}
}
