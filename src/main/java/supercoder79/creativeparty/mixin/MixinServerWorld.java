package supercoder79.creativeparty.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import supercoder79.creativeparty.CreativeParty;
import xyz.nucleoid.plasmid.game.ManagedGameSpace;
import xyz.nucleoid.plasmid.game.rule.RuleResult;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.explosion.Explosion;

@Mixin(ServerWorld.class)
public class MixinServerWorld {
	@Redirect(method = "createExplosion", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/explosion/Explosion;collectBlocksAndDamageEntities()V"))
	private void clearAffectedBlocks(Explosion explosion) {
		explosion.collectBlocksAndDamageEntities();

		ManagedGameSpace space = ManagedGameSpace.forWorld((ServerWorld) (Object) this);
		if (space != null && space.testRule(CreativeParty.DISABLE_EXPLOSION) == RuleResult.ALLOW) {
			explosion.clearAffectedBlocks();
		}
	}
}
