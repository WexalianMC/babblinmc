package com.wexalian.mods.babblinmc.block;

import com.wexalian.mods.babblinmc.block.entity.endertank.EnderTank;
import com.wexalian.mods.babblinmc.block.entity.endertank.EnderTankBlockEntity;
import com.wexalian.mods.babblinmc.block.entity.endertank.TankOwner;
import com.wexalian.mods.babblinmc.feature.ModFeatures;
import com.wexalian.mods.babblinmc.util.InventoryUtil;
import com.wexalian.mods.babblinmc.util.RaytraceUtil;
import com.wexalian.mods.babblinmc.util.hitbox.HitboxHelper;
import com.wexalian.nullability.annotations.Nullable;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.*;

public class EnderTankBlock extends Block implements BlockEntityProvider {
    public static final DirectionProperty FACING = Properties.FACING;
    public static final BooleanProperty POWERED = Properties.POWERED;
    public static final EnumProperty<EnumVariant> VARIANT = EnumProperty.of("variant", EnumVariant.class);
    public static final EnumProperty<FlowDirection> FLOW = EnumProperty.of("flow", FlowDirection.class);
    
    public static final HashMap<Direction, VoxelShape[]> SHAPES = Util.make(new HashMap<>(), m -> {
        Map<Direction, VoxelShape> tankBody = HitboxHelper.getBody();
        Map<Direction, VoxelShape> topRing = HitboxHelper.getRingTop();
        Map<Direction, VoxelShape> middleRing = HitboxHelper.getRingMiddle();
        Map<Direction, VoxelShape> bottomRing = HitboxHelper.getRingBottom();
        
        for (Direction direction : Direction.values()) {
            VoxelShape[] shapes = new VoxelShape[4];
            shapes[0] = tankBody.getOrDefault(direction, VoxelShapes.empty());
            shapes[1] = topRing.getOrDefault(direction, VoxelShapes.empty());
            shapes[2] = middleRing.getOrDefault(direction, VoxelShapes.empty());
            shapes[3] = bottomRing.getOrDefault(direction, VoxelShapes.empty());
            
            m.put(direction, shapes);
        }
    });
    
    public EnderTankBlock() {
        super(Settings.of(Material.STONE, MapColor.BLACK).strength(1.5F, 5.0F).sounds(BlockSoundGroup.STONE));
        
        this.setDefaultState(getStateManager().getDefaultState()
                                              .with(FACING, Direction.NORTH)
                                              .with(POWERED, false)
                                              .with(VARIANT, EnumVariant.PUBLIC)
                                              .with(FLOW, FlowDirection.NONE));
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, VARIANT, FLOW);
    }
    
    @Override
    public boolean isShapeFullCube(BlockState state, BlockView world, BlockPos pos) {
        return false;
    }
    
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new EnderTankBlockEntity(pos, state);
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);
        if (context instanceof EntityShapeContext entityContext && entityContext.getEntity() instanceof PlayerEntity player) {
            if (isDye(player.getMainHandStack())) {
                int subHit = RaytraceUtil.rayTraceShapes(player, pos, SHAPES.get(facing));
                if (subHit >= 1 && subHit <= 3) {
                    return SHAPES.get(facing)[subHit];
                }
            }
        }
        return SHAPES.get(facing)[0];
    }
    
    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        Direction facing = context.getSide().getOpposite();
        boolean powered = context.getWorld().isReceivingRedstonePower(context.getBlockPos());
        
        return getDefaultState().with(FACING, facing).with(POWERED, powered);
    }
    
    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        ItemStack stack = super.getPickStack(world, pos, state);
        if (world.getBlockEntity(pos) instanceof EnderTankBlockEntity tank) {
            tank.setStackNbt(stack);
        }
        return stack;
    }
    
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        
        if (!world.isClient && world.getBlockEntity(pos) instanceof EnderTankBlockEntity tank) {
            if (ModFeatures.ENDER_TANK.isEnabled()) {
                ItemStack held = player.getMainHandStack();
                
                if (held.isEmpty()) {
                    if (player.isSneaking()) {
                        if (tryToggleFlow(world, state, pos)) {
                            return ActionResult.SUCCESS;
                        }
                    }
                    else {
                        trySendMessage(player, tank);
                    }
                }
                if (isDye(held)) {
                    if (tryDyeRing(state, pos, player, tank)) {
                        if (!player.getAbilities().creativeMode) {
                            held.decrement(1);
                        }
                        return ActionResult.SUCCESS;
                    }
                }
                if (isPrivateUpgrade(held)) {
                    if (tryPrivateUpgrade(world, state, pos, player, tank)) {
                        return ActionResult.SUCCESS;
                    }
                }
                if (isFluidContainer(held)) {
                    if (tryFluidContainer(state, tank, player, hand)) {
                        if (!player.getAbilities().creativeMode) {
                            held.decrement(1);
                        }
                        return ActionResult.SUCCESS;
                    }
                }
            }
            else {
                player.sendSystemMessage(new TranslatableText("block.babblinmc.ender_tank.disabled"), player.getUuid());
                return ActionResult.FAIL;
            }
        }
        return ActionResult.SUCCESS;
    }
    
    private boolean isDye(ItemStack stack) {
        return stack.getItem() instanceof DyeItem;
    }
    
    private boolean isFluidContainer(ItemStack stack) {
        return ContainerItemContext.withInitial(stack).find(FluidStorage.ITEM) != null;
    }
    
    private boolean tryFluidContainer(BlockState state, EnderTankBlockEntity tank, PlayerEntity player, Hand hand) {
        Storage<FluidVariant> storage = ContainerItemContext.ofPlayerHand(player, hand).find(FluidStorage.ITEM);
        if (storage != null) {
            FlowDirection flow = state.get(FLOW);
            if (flow.isInput()) {
                try (Transaction outer = Transaction.openOuter()) {
                    for (StorageView<FluidVariant> view : storage.iterable(outer)) {
                        if (view.isResourceBlank()) continue;
                        FluidVariant variant = view.getResource();
                        
                        try (Transaction inner = Transaction.openNested(outer)) {
                            long extract = storage.extract(variant, FluidConstants.BUCKET, inner);
                            if (extract > 0) {
                                long insert = tank.getEnderTank().getFluidStorage().insert(variant, extract, inner);
                                if (extract == insert) {
                                    inner.commit();
                                }
                            }
                        }
                    }
                    outer.commit();
                }
            }
            else if (flow.isOutput()) {
                try (Transaction transaction = Transaction.openOuter()) {
                    FluidVariant variant = tank.getFluidVariant();
                    if (!variant.isBlank()) {
                        long extract = tank.getEnderTank().getFluidStorage().extract(variant, FluidConstants.BUCKET, transaction);
                        if (extract > 0) {
                            long insert = storage.insert(variant, extract, transaction);
                            if (extract == insert) {
                                transaction.commit();
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }
    
    private boolean tryDyeRing(BlockState state, BlockPos pos, PlayerEntity player, EnderTankBlockEntity tank) {
        int subHit = RaytraceUtil.rayTraceShapes(player, pos, SHAPES.get(state.get(FACING)));
        if (subHit >= 1 && subHit <= 3) {
            DyeColor color = getDyeColor(player.getMainHandStack());
            if (color != null) {
                return tank.setColor(subHit - 1, color);
            }
        }
        return false;
    }
    
    private boolean isPrivateUpgrade(ItemStack stack) {
        return ModFeatures.ENDER_TANK.getPrivateUpgrades().contains(stack.getItem());
    }
    
    private boolean tryPrivateUpgrade(World world, BlockState state, BlockPos pos, PlayerEntity player, EnderTankBlockEntity tank) {
        if (state.get(VARIANT) == EnumVariant.PUBLIC) {
            world.setBlockState(pos, state.cycle(VARIANT), Block.NOTIFY_LISTENERS);
            tank.setOwner(TankOwner.of(player));
            if (!player.getAbilities().creativeMode) {
                player.getMainHandStack().decrement(1);
                return true;
            }
        }
        else if (state.get(VARIANT) == EnumVariant.PRIVATE) {
            boolean newOwner = player.getAbilities().creativeMode || tryAddItems(player, ModFeatures.ENDER_TANK.getPrivateUpgrades());
            if (newOwner) {
                world.setBlockState(pos, state.cycle(VARIANT), Block.NOTIFY_LISTENERS);
                tank.setOwner(TankOwner.ALL);
                return true;
            }
        }
        return false;
    }
    
    public boolean tryAddItems(PlayerEntity player, Collection<Item> items) {
        ItemStack heldItem = player.getMainHandStack();
        if (heldItem.getCount() < heldItem.getMaxCount()) {
            heldItem.increment(1);
            return true;
        }
        else {
            Set<ItemStack> stacks = Util.make(new HashSet<>(), s -> items.forEach(item -> s.add(new ItemStack(item))));
            return InventoryUtil.putItems(player.getInventory(), stacks, Direction.DOWN, true);
        }
    }
    
    private boolean tryToggleFlow(World world, BlockState state, BlockPos pos) {
        return world.setBlockState(pos, state.cycle(FLOW), Block.NOTIFY_NEIGHBORS);
    }
    
    private static void trySendMessage(PlayerEntity player, EnderTankBlockEntity tank) {
        EnderTank enderTank = tank.getEnderTank();
        FluidVariant variant = enderTank.getFluidStorage().variant;
        
        Text text = new TranslatableText("block.babblinmc.ender_tank.empty");
        
        if (!variant.isBlank()) {
            Identifier id = Registry.FLUID.getId(variant.getFluid());
            long amount = enderTank.getFluidStorage().getAmount();
            // text = new LiteralText("Fluid: ");
            // text = text.append(new TranslatableText("block." + id.getNamespace() + "." + id.getPath()));
            // text = text.append(new LiteralText(", " + amount / (FluidConstants.BUCKET / FluidConstants.NUGGET) + " mB"));
            Text fluidText = new TranslatableText("block." + id.getNamespace() + "." + id.getPath());
            long milliBuckets = amount / (FluidConstants.BUCKET / FluidConstants.NUGGET);
            text = new TranslatableText("block.babblinmc.ender_tank.fluid", fluidText, milliBuckets);
        }
        player.sendMessage(text, true);
    }
    
    private DyeColor getDyeColor(ItemStack stack) {
        if (stack.getItem() instanceof DyeItem dye) {
            return dye.getColor();
        }
        return null;
    }
    
    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (ModFeatures.ENDER_TANK.isEnabled() && !world.isClient) {
            boolean powered = state.get(POWERED);
            if (powered != world.isReceivingRedstonePower(pos)) {
                world.setBlockState(pos, state.cycle(POWERED), Block.NOTIFY_LISTENERS);
            }
        }
    }
    
    public static int getRingColor(BlockState state, BlockView world, BlockPos pos, int tintIndex) {
        if (tintIndex >= 1 && tintIndex <= 3) {
            if (world.getBlockEntity(pos) instanceof EnderTankBlockEntity tank) {
                return tank.getCode().toHex(tintIndex - 1);
            }
        }
        
        return 0xFFFFFF;
    }
    
    public enum EnumVariant implements StringIdentifiable {
        PUBLIC("public"),
        PRIVATE("private");
        
        private final String name;
        
        EnumVariant(String name) {
            this.name = name;
        }
        
        public String asString() {
            return this.name;
        }
        
        public String toString() {
            return this.name;
        }
    }
    
    public enum FlowDirection implements StringIdentifiable {
        NONE("none", "None", false, false),
        INPUT("input", "Input", true, false),
        OUTPUT("output", "Output", false, true);
        
        private final String name;
        private final String formatName;
        private final boolean input;
        private final boolean output;
        
        FlowDirection(String name, String formatName, boolean input, boolean output) {
            this.name = name;
            this.formatName = formatName;
            this.input = input;
            this.output = output;
        }
        
        public String asString() {
            return this.name;
        }
        
        public String toString() {
            return this.name;
        }
        
        public boolean isInput() {
            return input;
        }
        
        public boolean isOutput() {
            return output;
        }
        
        public String getFormatName() {
            return formatName;
        }
    }
}
