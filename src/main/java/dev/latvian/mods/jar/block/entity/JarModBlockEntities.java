package dev.latvian.mods.jar.block.entity;

import dev.latvian.mods.jar.JarMod;
import dev.latvian.mods.jar.block.JarModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class JarModBlockEntities
{
	public static final DeferredRegister<TileEntityType<?>> REGISTRY = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, JarMod.MOD_ID);

	public static final RegistryObject<TileEntityType<JarBlockEntity>> JAR = REGISTRY.register("jar", () -> TileEntityType.Builder.create(JarBlockEntity::new, JarModBlocks.JAR.get()).build(null));
	public static final RegistryObject<TileEntityType<TemperedJarBlockEntity>> TEMPERED_JAR = REGISTRY.register("tempered_jar", () -> TileEntityType.Builder.create(TemperedJarBlockEntity::new, JarModBlocks.TEMPERED_JAR.get()).build(null));
}