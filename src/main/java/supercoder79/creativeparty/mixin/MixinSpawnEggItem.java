package supercoder79.creativeparty.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import supercoder79.creativeparty.CreativeParty;
import xyz.nucleoid.plasmid.game.ManagedGameSpace;
import xyz.nucleoid.plasmid.game.rule.RuleResult;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

@Mixin(SpawnEggItem.class)
public abstract class MixinSpawnEggItem {
	@Shadow public abstract EntityType<?> getEntityType(CompoundTag tag);

	@Inject(method = "use", at = @At("HEAD"), cancellable = true)
	private void disable(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
		ManagedGameSpace space = ManagedGameSpace.forWorld(world);
		if (space != null && space.testRule(CreativeParty.DISABLE_EGGS) == RuleResult.ALLOW) {
			EntityType<?> type = this.getEntityType(user.getStackInHand(hand).getTag());
			if (type == EntityType.WITHER || type == EntityType.ENDER_DRAGON || type == EntityType.TNT || type == EntityType.CREEPER) {
				cir.setReturnValue(TypedActionResult.success(user.getStackInHand(hand), world.isClient()));
			}
		}
	}
}
