package supercoder79.creativeparty.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import supercoder79.creativeparty.CreativeParty;
import xyz.nucleoid.plasmid.game.ManagedGameSpace;
import xyz.nucleoid.plasmid.game.rule.RuleResult;

import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

@Mixin(FallingBlock.class)
public class MixinFallingBlock {
	@Inject(method = "scheduledTick", at = @At("HEAD"), cancellable = true)
	private void disable(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
		ManagedGameSpace space = ManagedGameSpace.forWorld(world);
		if (space != null && space.testRule(CreativeParty.DISABLE_FALLING_BLOCKS) == RuleResult.ALLOW) {
			ci.cancel();
		}
	}
}
