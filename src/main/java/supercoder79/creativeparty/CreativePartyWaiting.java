package supercoder79.creativeparty;

import java.util.concurrent.CompletableFuture;

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
import xyz.nucleoid.plasmid.game.world.bubble.BubbleWorldConfig;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
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

	public static CompletableFuture<Void> open(MinecraftServer server, CreativePartyConfig config) {
		CreativePartyMapGenerator generator = new CreativePartyMapGenerator();

		return generator.create().thenAccept(map -> {
			BubbleWorldConfig worldConfig = new BubbleWorldConfig()
					.setGenerator(map.chunkGenerator())
					.setDefaultGameMode(GameMode.SPECTATOR);

			GameWorld gameWorld = GameWorld.open(server, worldConfig);

			CreativePartyWaiting waiting = new CreativePartyWaiting(gameWorld, map, config);

			gameWorld.newGame(game -> {
				game.setRule(GameRule.ALLOW_CRAFTING, RuleResult.DENY);
				game.setRule(GameRule.ALLOW_PORTALS, RuleResult.DENY);
				game.setRule(GameRule.ALLOW_PVP, RuleResult.DENY);
				game.setRule(GameRule.FALL_DAMAGE, RuleResult.DENY);
				game.setRule(GameRule.ENABLE_HUNGER, RuleResult.DENY);

				game.on(RequestStartListener.EVENT, waiting::requestStart);
				game.on(OfferPlayerListener.EVENT, waiting::offerPlayer);

				game.on(PlayerAddListener.EVENT, waiting::addPlayer);
				game.on(PlayerDeathListener.EVENT, waiting::onPlayerDeath);
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
			return StartResult.notEnoughPlayers();
		}

		CreativePartyActive.open(this.gameWorld, this.map, this.config);

		return StartResult.ok();
	}

	private void addPlayer(ServerPlayerEntity player) {
		this.spawnPlayer(player);
	}

	private boolean onPlayerDeath(ServerPlayerEntity player, DamageSource source) {
		this.spawnPlayer(player);
		return true;
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
