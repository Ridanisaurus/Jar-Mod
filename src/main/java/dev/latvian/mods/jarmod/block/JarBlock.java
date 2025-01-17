package dev.latvian.mods.jarmod.block;

import dev.latvian.mods.jarmod.block.entity.JarBlockEntity;
import dev.latvian.mods.jarmod.block.entity.TemperedJarBlockEntity;
import dev.latvian.mods.jarmod.item.JarModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class JarBlock extends Block implements TubeConnection {
	public static final VoxelShape SHAPE = Shapes.or(
			box(3, 0, 3, 13, 13, 13),
			box(6, 13, 6, 10, 14, 10),
			box(5, 14, 5, 11, 16, 11)
	);

	public JarBlock() {
		super(Properties.of(Material.GLASS).sound(SoundType.BONE_BLOCK).strength(0.6F).noOcclusion());
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new JarBlockEntity();
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
		return SHAPE;
	}

	@Override
	@Deprecated
	public boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	@Deprecated
	public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
		return 0;
	}

	@Override
	@Deprecated
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity entity = world.getBlockEntity(pos);

			if (entity instanceof JarBlockEntity) {
				world.updateNeighbourForOutputSignal(pos, this);
			}

			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Override
	@Deprecated
	public boolean isPathfindable(BlockState arg, BlockGetter arg2, BlockPos arg3, PathComputationType arg4) {
		return false;
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack item = player.getItemInHand(hand);

		if (item.getItem() == JarModItems.JAR.get() || item.getItem() == JarModItems.TEMPERED_JAR.get() || item.getItem() == JarModItems.TUBE.get()) {
			return InteractionResult.PASS;
		}

		BlockEntity tileEntity = worldIn.getBlockEntity(pos);

		if (tileEntity instanceof JarBlockEntity) {
			((JarBlockEntity) tileEntity).rightClick(player, hand, item);
		} else if (tileEntity instanceof TemperedJarBlockEntity) {
			((TemperedJarBlockEntity) tileEntity).rightClick(player, hand, item);
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public boolean canTubeConnect(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return face == Direction.UP;
	}
}
