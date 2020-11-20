package supercoder79.creativeparty;

import java.util.concurrent.CompletableFuture;

import xyz.nucleoid.plasmid.game.GameOpenContext;
import xyz.nucleoid.plasmid.game.GameWorld;
import xyz.nucleoid.plasmid.game.StartResult;
import xyz.nucleoid.plasmid.game.event.OfferPlayerListener;
import xyz.nucleoid.plasmid.game.event.PlayerAddListener;
import xyz.nucleoid.plasmid.game.event.PlayerDeathListener;
import xyz.nucleoid.plasmid.game.event.RequestStartListener;
import xyz.nucleoid.plasmid.game.player.JoinResult;
import xyz.nucleoid.plasmid.game.rule.GameRule;
import xyz.nucleoid.plasmid.game.rule.RuleResult;
import supercoder79.creativeparty.map.CreativePartyMap;
import supercoder79.creativeparty.map.CreativePartyMapGenerator;
import xyz.nucleoid.plasmid.world.bubble.BubbleWorldConfig;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.world.GameMode;
import net.minecraft.world.Heightmap;

public class CreativePartyWaiting {
	private final GameWorld gameWorld;
	private final CreativePartyMap map;
	private final CreativePartyConfig config;

	public CreativePartyWaiting(GameWorld gameWorld, CreativePartyMap map, CreativePartyConfig config) {
		this.gameWorld = gameWorld;
		this.map = map;
		this.config = config;
	}

	public static CompletableFuture<GameWorld> open(GameOpenContext<CreativePartyConfig> context) {
		CreativePartyMapGenerator generator = new CreativePartyMapGenerator();

		return generator.create().thenCompose(map -> {
			BubbleWorldConfig worldConfig = new BubbleWorldConfig()
					.setGenerator(map.chunkGenerator(context.getServer()))
					.setDefaultGameMode(GameMode.SPECTATOR);

			return context.openWorld(worldConfig).thenApply(gameWorld -> {
				CreativePartyWaiting waiting = new CreativePartyWaiting(gameWorld, map, context.getConfig());

				gameWorld.openGame(game -> {
					game.setRule(GameRule.CRAFTING, RuleResult.DENY);
					game.setRule(GameRule.PORTALS, RuleResult.DENY);
					game.setRule(GameRule.PVP, RuleResult.DENY);
					game.setRule(GameRule.FALL_DAMAGE, RuleResult.DENY);
					game.setRule(GameRule.HUNGER, RuleResult.DENY);

					game.on(RequestStartListener.EVENT, waiting::requestStart);
					game.on(OfferPlayerListener.EVENT, waiting::offerPlayer);

					game.on(PlayerAddListener.EVENT, waiting::addPlayer);
					game.on(PlayerDeathListener.EVENT, waiting::onPlayerDeath);
				});
				return gameWorld;
			});
		});
	}

	private JoinResult offerPlayer(ServerPlayerEntity player) {
		if (this.gameWorld.getPlayerCount() >= this.config.players.getMaxPlayers()) {
			return JoinResult.gameFull();
		}

		return JoinResult.ok();
	}

	private StartResult requestStart() {
		if (this.gameWorld.getPlayerCount() < this.config.players.getMinPlayers()) {
			return StartResult.NOT_ENOUGH_PLAYERS;
		}

		CreativePartyActive.open(this.gameWorld, this.map, this.config);

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

		ServerWorld world = this.gameWorld.getWorld();
		player.teleport(world, 0, 62, 0, 0.0F, 0.0F);
	}
}
