package supercoder79.creativeparty.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import supercoder79.creativeparty.CreativeParty;
import xyz.nucleoid.plasmid.game.ManagedGameSpace;
import xyz.nucleoid.plasmid.game.rule.RuleResult;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.SummonCommand;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

@Mixin(SummonCommand.class)
public class MixinSummonCommand {
	@Inject(method = "execute", at = @At("HEAD"), cancellable = true)
	private static void disable(ServerCommandSource source, Identifier entity, Vec3d pos, CompoundTag nbt, boolean initialize, CallbackInfoReturnable<Integer> cir) {
		ManagedGameSpace space = ManagedGameSpace.forWorld(source.getWorld());
		if (space != null && space.testRule(CreativeParty.DISABLE_BOSSES) == RuleResult.ALLOW) {
			if (entity.getPath().equals("wither") || entity.getPath().equals("ender_dragon") || entity.getPath().equals("giant")) {
				source.sendFeedback(new LiteralText("Can't summon this entity!").formatted(Formatting.RED), false);
				cir.setReturnValue(1);
			}
		}
	}
}
