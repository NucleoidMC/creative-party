package supercoder79.creativeparty;

import java.util.Set;
import java.util.stream.Collectors;

import net.gegy1000.plasmid.game.GameWorld;
import net.gegy1000.plasmid.game.event.GameCloseListener;
import net.gegy1000.plasmid.game.event.GameOpenListener;
import net.gegy1000.plasmid.game.event.GameTickListener;
import net.gegy1000.plasmid.game.event.OfferPlayerListener;
import net.gegy1000.plasmid.game.event.PlayerAddListener;
import net.gegy1000.plasmid.game.event.PlayerDamageListener;
import net.gegy1000.plasmid.game.event.PlayerDeathListener;
import net.gegy1000.plasmid.game.player.JoinResult;
import net.gegy1000.plasmid.game.rule.GameRule;
import net.gegy1000.plasmid.game.rule.RuleResult;
import net.gegy1000.plasmid.util.PlayerRef;
import supercoder79.creativeparty.map.CreativePartyMap;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameMode;
import net.minecraft.world.Heightmap;

public class CreativePartyActive {

	private final GameWorld gameWorld;
	private final CreativePartyMap map;
	private final CreativePartyConfig config;
	private final Set<PlayerRef> participants;

	public CreativePartyActive(GameWorld gameWorld, CreativePartyMap map, CreativePartyConfig config, Set<PlayerRef> participants) {
		this.gameWorld = gameWorld;
		this.map = map;
		this.config = config;
		this.participants = participants;
	}

	public static void open(GameWorld gameWorld, CreativePartyMap map, CreativePartyConfig config) {
		Set<PlayerRef> participants = gameWorld.getPlayers().stream()
				.map(PlayerRef::of)
				.collect(Collectors.toSet());

		CreativePartyActive active = new CreativePartyActive(gameWorld, map, config, participants);

		gameWorld.newGame(game -> {
			game.setRule(GameRule.ALLOW_CRAFTING, RuleResult.DENY);
			game.setRule(GameRule.ALLOW_PORTALS, RuleResult.DENY);
			game.setRule(GameRule.ALLOW_PVP, RuleResult.DENY);
			game.setRule(GameRule.BLOCK_DROPS, RuleResult.DENY);
			game.setRule(GameRule.FALL_DAMAGE, RuleResult.DENY);
			game.setRule(GameRule.ENABLE_HUNGER, RuleResult.DENY);

			game.on(GameOpenListener.EVENT, active::onOpen);

			game.on(OfferPlayerListener.EVENT, player -> JoinResult.ok());
			game.on(PlayerAddListener.EVENT, active::addPlayer);
		});
	}

	private void onOpen() {
		ServerWorld world = this.gameWorld.getWorld();
		for (PlayerRef ref : this.participants) {
			ref.ifOnline(world, this::addPlayer);
		}
	}

	private void addPlayer(ServerPlayerEntity player) {
		player.inventory.clear();
		player.getEnderChestInventory().clear();
		player.clearStatusEffects();
		player.setHealth(20.0F);
		player.getHungerManager().setFoodLevel(20);
		player.fallDistance = 0.0F;
		player.setGameMode(GameMode.CREATIVE);

		ServerWorld world = this.gameWorld.getWorld();
		player.teleport(world, 0, 62, 0, 0.0F, 0.0F);
	}
}
