package supercoder79.creativeparty.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import supercoder79.creativeparty.CreativeParty;
import xyz.nucleoid.plasmid.game.ManagedGameSpace;
import xyz.nucleoid.plasmid.game.rule.RuleResult;

import net.minecraft.block.WitherSkullBlock;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(WitherSkullBlock.class)
public class MixinWitherSkullBlock {
	@Inject(method = "onPlaced(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/entity/SkullBlockEntity;)V", at = @At("HEAD"), cancellable = true)
	private static void disable(World world, BlockPos pos, SkullBlockEntity blockEntity, CallbackInfo ci) {
		ManagedGameSpace space = ManagedGameSpace.forWorld(world);
		if (space != null && space.testRule(CreativeParty.DISABLE_BOSSES) == RuleResult.ALLOW) {
			ci.cancel();
		}
	}
}
