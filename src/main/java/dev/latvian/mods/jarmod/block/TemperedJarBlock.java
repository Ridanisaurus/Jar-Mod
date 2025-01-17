package dev.latvian.mods.jarmod.block;


import dev.latvian.mods.jarmod.block.entity.TemperedJarBlockEntity;
import dev.latvian.mods.jarmod.heat.Temperature;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.Nullable;

/**
 * @author LatvianModder
 */
public class TemperedJarBlock extends JarBlock {
	public static final EnumProperty<Temperature> TEMPERATURE = EnumProperty.create("temperature", Temperature.class);

	public TemperedJarBlock() {
		registerDefaultState(getStateDefinition().any().setValue(TEMPERATURE, Temperature.NONE));
	}

	@Override
	public BlockEntity createTileEntity(BlockState state, BlockGetter world) {
		return new TemperedJarBlockEntity();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(TEMPERATURE);
	}

	@Override
	@Deprecated
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() != newState.getBlock()) {
			/*
			BlockEntity entity = world.getBlockEntity(pos);

			if (entity instanceof TemperedJarBlockEntity) {
			}
			 */

			world.updateNeighbourForOutputSignal(pos, this);
			super.onRemove(state, world, pos, newState, isMoving);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(TEMPERATURE, Temperature.fromWorld(context.getLevel(), context.getClickedPos().below()));
	}

	@Override
	@Deprecated
	public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
		return facing == Direction.DOWN && worldIn instanceof Level ? stateIn.setValue(TEMPERATURE, Temperature.fromWorld((Level) worldIn, facingPos)) : stateIn;
	}

	@Override
	public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, @Nullable Direction side) {
		return true;
	}

	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);

		BlockEntity entity = worldIn.getBlockEntity(pos);

		if (entity instanceof TemperedJarBlockEntity) {
			((TemperedJarBlockEntity) entity).neighborChanged();
		}
	}
}
