package supercoder79.creativeparty;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameMode;
import supercoder79.creativeparty.map.CreativePartyMap;
import xyz.nucleoid.plasmid.game.GameSpace;
import xyz.nucleoid.plasmid.game.event.GameOpenListener;
import xyz.nucleoid.plasmid.game.event.OfferPlayerListener;
import xyz.nucleoid.plasmid.game.event.PlayerAddListener;
import xyz.nucleoid.plasmid.game.player.JoinResult;
import xyz.nucleoid.plasmid.game.rule.GameRule;
import xyz.nucleoid.plasmid.game.rule.RuleResult;
import xyz.nucleoid.plasmid.util.PlayerRef;

import java.util.Set;
import java.util.stream.Collectors;

public class CreativePartyActive {

	private final GameSpace gameSpace;
	private final CreativePartyMap map;
	private final CreativePartyConfig config;
	private final Set<PlayerRef> participants;

	public CreativePartyActive(GameSpace gameSpace, CreativePartyMap map, CreativePartyConfig config, Set<PlayerRef> participants) {
		this.gameSpace = gameSpace;
		this.map = map;
		this.config = config;
		this.participants = participants;
	}

	public static void open(GameSpace gameSpace, CreativePartyMap map, CreativePartyConfig config) {
		gameSpace.openGame(game -> {
			Set<PlayerRef> participants = gameSpace.getPlayers().stream()
					.map(PlayerRef::of)
					.collect(Collectors.toSet());

			CreativePartyActive active = new CreativePartyActive(gameSpace, map, config, participants);
			game.setRule(GameRule.CRAFTING, RuleResult.DENY);
			game.setRule(GameRule.PORTALS, RuleResult.DENY);
			game.setRule(GameRule.PVP, RuleResult.DENY);
			game.setRule(GameRule.BLOCK_DROPS, RuleResult.DENY);
			game.setRule(GameRule.FALL_DAMAGE, RuleResult.DENY);
			game.setRule(GameRule.HUNGER, RuleResult.DENY);
			game.setRule(CreativeParty.DISABLE_EXPLOSION, RuleResult.ALLOW);
			game.setRule(CreativeParty.DISABLE_POTIONS, RuleResult.ALLOW);
			game.setRule(CreativeParty.DISABLE_FALLING_BLOCKS, RuleResult.ALLOW);
			game.setRule(CreativeParty.DISABLE_BOSSES, RuleResult.ALLOW);
			game.setRule(CreativeParty.DISABLE_EGGS, RuleResult.ALLOW);

			game.on(GameOpenListener.EVENT, active::onOpen);

			game.on(OfferPlayerListener.EVENT, player -> JoinResult.ok());
			game.on(PlayerAddListener.EVENT, active::addPlayer);
		});
	}

	private void onOpen() {
		ServerWorld world = this.gameSpace.getWorld();
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

		ServerWorld world = this.gameSpace.getWorld();
		player.teleport(world, 0, 62, 0, 0.0F, 0.0F);
	}
}
