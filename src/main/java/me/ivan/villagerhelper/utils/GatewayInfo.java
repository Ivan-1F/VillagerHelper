package me.ivan.villagerhelper.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

public class GatewayInfo {
    private BlockPos pos;
    private World world;

    public GatewayInfo(BlockPos pos, World world) {
        this.pos = pos;
        this.world = world;
    }

    public GatewayInfo() {
        disable();
    }

    public BlockPos getPos() {
        return pos;
    }

    public World getWorld() {
        return world;
    }

    public boolean isEnabled() {
        return this.pos != null;
    }

    public void disable() {
        this.pos = null;
        this.world = null;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public BlockState getBlockState() {
        return this.isEnabled() ? world.getBlockState(this.pos) : null;
    }

    public Block getBlock() {
        if (!this.isEnabled()) return null;
        return this.getBlockState().getBlock();
    }

    public boolean hasBlockEntity() {
        if (!this.isEnabled()) return false;
        return this.getBlock().hasBlockEntity();
    }

    // =========== getExitPortalPos ==========

    private WorldChunk getChunk(World world, Vec3d pos) {
        System.out.println("[getChunk] " + world.getChunk(MathHelper.floor(pos.x / 16.0D), MathHelper.floor(pos.z / 16.0D)).toString());
        return world.getChunk(MathHelper.floor(pos.x / 16.0D), MathHelper.floor(pos.z / 16.0D));
    }

    @Nullable
    private static BlockPos findPortalPosition(WorldChunk worldChunk) {
        System.out.println("[findPortalPosition] " + worldChunk.toString());
        ChunkPos chunkPos = worldChunk.getPos();
        BlockPos blockPos = new BlockPos(chunkPos.getStartX(), 30, chunkPos.getStartZ());
        int i = worldChunk.getHighestNonEmptySectionYOffset() + 16 - 1;
        BlockPos blockPos2 = new BlockPos(chunkPos.getEndX(), i, chunkPos.getEndZ());
        BlockPos blockPos3 = null;
        double d = 0.0D;
        Iterator var8 = BlockPos.iterate(blockPos, blockPos2).iterator();

        while(true) {
            BlockPos blockPos4;
            double e;
            do {
                BlockPos blockPos5;
                BlockPos blockPos6;
                do {
                    BlockState blockState;
                    do {
                        do {
                            if (!var8.hasNext()) {
                                return blockPos3;
                            }

                            blockPos4 = (BlockPos)var8.next();
                            blockState = worldChunk.getBlockState(blockPos4);
                            blockPos5 = blockPos4.up();
                            blockPos6 = blockPos4.up(2);
                        } while(blockState.getBlock() != Blocks.END_STONE);
                    } while(worldChunk.getBlockState(blockPos5).isFullCube(worldChunk, blockPos5));
                } while(worldChunk.getBlockState(blockPos6).isFullCube(worldChunk, blockPos6));

                e = blockPos4.getSquaredDistance(0.0D, 0.0D, 0.0D, true);
            } while(blockPos3 != null && e >= d);

            blockPos3 = blockPos4;
            d = e;
        }
    }

    private static BlockPos findExitPortalPos(BlockView world, BlockPos pos, int searchRadius, boolean bl) {
        System.out.println("[findExitPortalPos] " + world.toString());
        BlockPos blockPos = null;

        for(int i = -searchRadius; i <= searchRadius; ++i) {
            for(int j = -searchRadius; j <= searchRadius; ++j) {
                if (i != 0 || j != 0 || bl) {
                    for(int k = 255; k > (blockPos == null ? 0 : blockPos.getY()); --k) {
                        BlockPos blockPos2 = new BlockPos(pos.getX() + i, k, pos.getZ() + j);
                        BlockState blockState = world.getBlockState(blockPos2);
                        if (blockState.isFullCube(world, blockPos2) && (bl || blockState.getBlock() != Blocks.BEDROCK)) {
                            blockPos = blockPos2;
                            break;
                        }
                    }
                }
            }
        }

        return blockPos == null ? pos : blockPos;
    }

    public BlockPos getExitPortalPos() {
        System.out.println("[getExitPortalPos] " + this.world.toString());
        System.out.println(this.world.dimension.toString());
        System.out.println(this.world.isClient);

        BlockPos exitPortalPos = null;

        Vec3d vec3d = (new Vec3d((double)this.getPos().getX(), 0.0D, (double)this.getPos().getZ())).normalize();
        Vec3d vec3d2 = vec3d.multiply(1024.0D);

        int var4;

        for(var4 = 16; getChunk(this.world, vec3d2).getHighestNonEmptySectionYOffset() > 0 && var4-- > 0; vec3d2 = vec3d2.add(vec3d.multiply(-16.0D))) {
            System.out.println("Skipping backwards past nonempty chunk at " + vec3d2);
        }

        for(var4 = 16; getChunk(this.world, vec3d2).getHighestNonEmptySectionYOffset() == 0 && var4-- > 0; vec3d2 = vec3d2.add(vec3d.multiply(16.0D))) {
            System.out.println("Skipping forward past empty chunk at " + vec3d2);
        }

        System.out.println("Found chunk at " + vec3d2);
        WorldChunk worldChunk = getChunk(this.world, vec3d2);
        exitPortalPos = findPortalPosition(worldChunk);
        if (exitPortalPos == null) {
            exitPortalPos = new BlockPos(vec3d2.x + 0.5D, 75.0D, vec3d2.z + 0.5D);
            System.out.println("Failed to find suitable block, settling on " + exitPortalPos);
        } else {
            System.out.println("Found block at " + exitPortalPos);
        }

        exitPortalPos = findExitPortalPos(this.world, exitPortalPos, 16, true);
        exitPortalPos = exitPortalPos.up(10);

        return exitPortalPos;
    }

    // ====== END ======

    public EndGatewayBlockEntity getBlockEntity() {
        if (!this.isEnabled() || !this.hasBlockEntity()) return null;
        BlockEntity blockEntity = this.world.getBlockEntity(this.pos);
        EndGatewayBlockEntity gateway = (EndGatewayBlockEntity) blockEntity;
        return gateway;
    }

    public BlockPos getEntityExitPos() {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        for (int i = 255; i >= 0; i--) {
            for (int j = x - 5; j <= x + 5; j++) {
                for (int k = z - 5; k <= z + 5; k++) {
                    BlockPos pos = new BlockPos(j, i, k);
                    BlockState blockState = world.getBlockState(pos);
                    Block block = blockState.getBlock();
                    if (blockState.isFullCube(world, pos) && block != Blocks.BEDROCK) {
                        return pos;
                    }
                }
            }
        }

        return this.pos;
    }

}
