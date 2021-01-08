package supercoder79.creativeparty.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import supercoder79.creativeparty.CreativeParty;
import xyz.nucleoid.plasmid.game.ManagedGameSpace;
import xyz.nucleoid.plasmid.game.rule.RuleResult;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

@Mixin(ThrowablePotionItem.class)
public class MixinThrowablePotionItem {

	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void disable(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
		ManagedGameSpace space = ManagedGameSpace.forWorld(world);
		if (space != null && space.testRule(CreativeParty.DISABLE_POTIONS) == RuleResult.ALLOW) {
			cir.setReturnValue(TypedActionResult.success(user.getStackInHand(hand), world.isClient()));
		}
	}
}
