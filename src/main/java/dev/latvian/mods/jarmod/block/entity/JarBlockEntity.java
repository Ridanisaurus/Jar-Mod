package dev.latvian.mods.jarmod.block.entity;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class JarBlockEntity extends BlockEntity {
	public final FluidTank tank;
	public final LazyOptional<IFluidHandler> tankOptional;

	public JarBlockEntity() {
		super(JarModBlockEntities.JAR.get());
		tank = new FluidTank(8000);
		tankOptional = LazyOptional.of(() -> tank);
	}

	public void rightClick(Player player, InteractionHand hand, ItemStack stack) {
		if (FluidUtil.interactWithFluidHandler(player, hand, tank)) {
			// return;
		}

		if (!level.isClientSide()) {
			if (tank.isEmpty()) {
				player.displayClientMessage(new TranslatableComponent("block.jarmod.jar.empty"), true);
			} else {
				player.displayClientMessage(new TranslatableComponent("block.jarmod.jar.mb", tank.getFluidAmount(), tank.getFluid().getDisplayName()), true);
			}
		}
	}

	@Override
	public CompoundTag getUpdateTag() {
		return save(new CompoundTag());
	}

	@Override
	public void handleUpdateTag(BlockState state, CompoundTag tag) {
		load(state, tag);
	}

	@Nullable
	@Override
	public ClientboundBlockEntityDataPacket getUpdatePacket() {
		return new ClientboundBlockEntityDataPacket(worldPosition, 0, tank.writeToNBT(new CompoundTag()));
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		tank.readFromNBT(pkt.getTag());
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && (side == null || side == Direction.UP) ? tankOptional.cast() : super.getCapability(cap, side);
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		compound.put("Tank", tank.writeToNBT(new CompoundTag()));
		return super.save(compound);
	}

	@Override
	public void load(BlockState state, CompoundTag compound) {
		super.load(state, compound);
		tank.readFromNBT(compound.getCompound("Tank"));
	}
}
