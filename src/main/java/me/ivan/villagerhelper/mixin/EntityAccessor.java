package me.ivan.villagerhelper.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor("x")
    public double getX();
    @Accessor("y")
    public double getY();
    @Accessor("z")
    public double getZ();
    @Accessor("dimension")
    public DimensionType getDimension();
}
