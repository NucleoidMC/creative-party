package supercoder79.creativeparty;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.world.GameMode;
import supercoder79.creativeparty.map.CreativePartyMap;
import supercoder79.creativeparty.map.CreativePartyMapGenerator;
import xyz.nucleoid.fantasy.BubbleWorldConfig;
import xyz.nucleoid.plasmid.game.GameOpenContext;
import xyz.nucleoid.plasmid.game.GameOpenProcedure;
import xyz.nucleoid.plasmid.game.GameSpace;
import xyz.nucleoid.plasmid.game.StartResult;
import xyz.nucleoid.plasmid.game.event.OfferPlayerListener;
import xyz.nucleoid.plasmid.game.event.PlayerAddListener;
import xyz.nucleoid.plasmid.game.event.PlayerDeathListener;
import xyz.nucleoid.plasmid.game.event.RequestStartListener;
import xyz.nucleoid.plasmid.game.player.JoinResult;
import xyz.nucleoid.plasmid.game.rule.GameRule;

public class CreativePartyWaiting {
	private final GameSpace gameSpace;
	private final CreativePartyMap map;
	private final CreativePartyConfig config;

	public CreativePartyWaiting(GameSpace gameSpace, CreativePartyMap map, CreativePartyConfig config) {
		this.gameSpace = gameSpace;
		this.map = map;
		this.config = config;
	}

	public static GameOpenProcedure open(GameOpenContext<CreativePartyConfig> context) {
		CreativePartyMapGenerator generator = new CreativePartyMapGenerator();

		CreativePartyMap map = generator.build();
		BubbleWorldConfig worldConfig = new BubbleWorldConfig()
				.setGenerator(map.chunkGenerator(context.getServer(), context.getConfig().map.mapType))
				.setDefaultGameMode(GameMode.SPECTATOR);

		return context.createOpenProcedure(worldConfig, game -> {
			CreativePartyWaiting waiting = new CreativePartyWaiting(game.getGameSpace(), map, context.getConfig());

			game.deny(GameRule.CRAFTING);
			game.deny(GameRule.PORTALS);
			game.deny(GameRule.PVP);
			game.deny(GameRule.FALL_DAMAGE);
			game.deny(GameRule.HUNGER);

			game.listen(RequestStartListener.EVENT, waiting::requestStart);
			game.listen(OfferPlayerListener.EVENT, waiting::offerPlayer);

			game.listen(PlayerAddListener.EVENT, waiting::addPlayer);
			game.listen(PlayerDeathListener.EVENT, waiting::onPlayerDeath);
		});
	}

	private JoinResult offerPlayer(ServerPlayerEntity player) {
		if (this.gameSpace.getPlayerCount() >= this.config.players.getMaxPlayers()) {
			return JoinResult.gameFull();
		}

		return JoinResult.ok();
	}

	private StartResult requestStart() {
		if (this.gameSpace.getPlayerCount() < this.config.players.getMinPlayers()) {
			return StartResult.NOT_ENOUGH_PLAYERS;
		}

		CreativePartyActive.open(this.gameSpace, this.map, this.config);

		return StartResult.OK;
	}

	private void addPlayer(ServerPlayerEntity player) {
		this.spawnPlayer(player);
	}

	private ActionResult onPlayerDeath(ServerPlayerEntity player, DamageSource source) {
		this.spawnPlayer(player);
		return ActionResult.FAIL;
	}

	private void spawnPlayer(ServerPlayerEntity player) {
		player.inventory.clear();
		player.getEnderChestInventory().clear();
		player.clearStatusEffects();
		player.setHealth(20.0F);
		player.getHungerManager().setFoodLevel(20);
		player.fallDistance = 0.0F;
		player.setGameMode(GameMode.SPECTATOR);

		ServerWorld world = this.gameSpace.getWorld();
		player.teleport(world, 0, this.config.map.mapType.getSpawnY(world), 0, 0.0F, 0.0F);
	}
}
