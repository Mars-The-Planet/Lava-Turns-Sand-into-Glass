package com.mars.lavasand.mixin;

import com.mars.lavasand.LavaSandConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Block.class)
public abstract class MixinMinecraft extends BlockBehaviour implements Fallable {
    @Shadow private BlockState defaultBlockState;

    @Shadow protected abstract Block asBlock();

    public MixinMinecraft(Properties properties) {
        super(properties);
    }

    public void onLand(Level p_52068_, BlockPos p_52069_, BlockState p_52070_, BlockState p_52071_, FallingBlockEntity p_52072_) {
        if (shouldSolidify(p_52068_, p_52069_, p_52071_)) {
            p_52068_.setBlock(p_52069_, Blocks.GLASS.defaultBlockState(), 3);
        }
    }

    @Inject(at = @At("HEAD"), method = "getStateForPlacement", cancellable = true)
    public void getStateForPlacement(BlockPlaceContext context, CallbackInfoReturnable<BlockState> cir) {
        BlockGetter blockgetter = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = blockgetter.getBlockState(blockpos);
        if(shouldSolidify(blockgetter, blockpos, this.defaultBlockState) || shouldSolidify(blockgetter, blockpos, blockstate)){
            cir.setReturnValue(Blocks.GLASS.defaultBlockState());
        }
    }

    private static boolean isOnSandList(Block block){
        for (String sand : LavaSandConfig.blocks_to_glass){
            try {
                if(block.equals(BuiltInRegistries.BLOCK.getValue(Identifier.parse(sand)))){
                    return true;
                }
            }
            catch (Exception e) {
                System.out.println(e);
            }
        }
        return false;
    }

    private boolean shouldSolidify(BlockGetter p_52081_, BlockPos p_52082_, BlockState p_52083_) {
        return (canSolidify(p_52083_) || touchesLiquid(p_52081_, p_52082_)) && isOnSandList(this.asBlock());
    }

    private static boolean touchesLiquid(BlockGetter p_52065_, BlockPos p_52066_) {
        boolean flag = false;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = p_52066_.mutable();

        for(Direction direction : Direction.values()) {
            BlockState blockstate = p_52065_.getBlockState(blockpos$mutableblockpos);
            if (direction != Direction.DOWN || canSolidify(blockstate)) {
                blockpos$mutableblockpos.setWithOffset(p_52066_, direction);
                blockstate = p_52065_.getBlockState(blockpos$mutableblockpos);
                if (canSolidify(blockstate) && !blockstate.isFaceSturdy(p_52065_, p_52066_, direction.getOpposite())) {
                    flag = true;
                    break;
                }
            }
        }

        return flag;
    }

    private static boolean canSolidify(BlockState p_52089_) {
        return p_52089_.getFluidState().is(FluidTags.LAVA);
    }

    @Override
    public BlockState updateShape(BlockState p_52074_, LevelReader p_374245_, ScheduledTickAccess p_374286_, BlockPos p_52078_, Direction p_52075_, BlockPos p_52079_, BlockState p_52076_, RandomSource p_374119_) {
        return shouldSolidify(p_374245_, p_52078_, p_52074_) ? Blocks.GLASS.defaultBlockState() : p_52074_;
    }
}
