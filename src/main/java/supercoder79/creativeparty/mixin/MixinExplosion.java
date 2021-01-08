package supercoder79.creativeparty.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import supercoder79.creativeparty.CreativeParty;
import xyz.nucleoid.plasmid.game.ManagedGameSpace;
import xyz.nucleoid.plasmid.game.rule.RuleResult;

import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

@Mixin(Explosion.class)
public class MixinExplosion {
	@Shadow @Final private World world;

	@Inject(method = "affectWorld", at = @At("HEAD"), cancellable = true)
	private void disable(boolean bl, CallbackInfo ci) {
		ManagedGameSpace space = ManagedGameSpace.forWorld(this.world);
		if (space != null && space.testRule(CreativeParty.DISABLE_EXPLOSION) == RuleResult.ALLOW) {
			ci.cancel();
		}
	}
}
