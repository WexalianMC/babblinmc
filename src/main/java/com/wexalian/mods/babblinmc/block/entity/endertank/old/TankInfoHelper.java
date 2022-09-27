package com.wexalian.mods.babblinmc.block.entity.endertank.old;

public class TankInfoHelper {
    // private static final Style GOLD = Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GOLD));
    //
    // private static final Style GRAY = Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GRAY));
    //
    // private static final Style GREEN = Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GREEN));
    //
    // private static final Style AQUA = Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.AQUA));
    //
    // private static final Style BOLD_RED = Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.RED)).withBold(Boolean.TRUE);
    //
    // private static final MutableText DUMMY = new TranslatableText("");
    //
    // public static void sendTankInfo(PlayerEntity player, TankOwner owner, ColorCode code, boolean sendAll) {
    //     sendTankInfo(player,
    //                  TankHelper.getEnderTank(owner, code, player.getWorld().isClient).getContentsInfo(!sendAll),
    //                  new TranslatableText("").append(new TranslatableText("info.endertanks." + (sendAll ? "tank" : "bucket") + ".header").append(
    //                      ":").setStyle(GOLD)).append(infoTankOwner(owner, false, true)));
    // }
    //
    // public static void sendTankInfo(PlayerEntity player, EnderTankBlockEntity tile) {
    //     sendTankInfo(player,
    //                  tile.getEnderTank().getContentsInfo(false),
    //                  new TranslatableText("").append(new TranslatableText("info.endertanks.tank.header").append(":").setStyle(GOLD))
    //                                          .append(infoPump(tile.getPumpCapacity()))
    //                                          .append(infoTankOwner(tile.getOwner(), false, true)));
    // }
    //
    // private static void sendTankInfo(PlayerEntity player, List<MutableText> contents, MutableText info) {
    //     if (contents.size() > 1) {
    //         contents.add(0, info);
    //         sendInfo(player, contents.toArray(new MutableText[0]));
    //     }
    //     else if (contents.size() == 1) {
    //         sendInfo(player, info.append(" ").append(contents.get(0)));
    //     }
    // }
    //
    // public static void getHUDInfo(List<MutableText> tooltip, TankOwner owner, ColorCode code, int pump, List<MutableText> contents) {
    //     for (MutableText entry : contents)
    //         tooltip.add(entry.setStyle(GRAY));
    //     tooltip.add(infoPump(pump).setStyle(GRAY));
    //     tooltip.add(infoTankOwner(owner, true, false));
    //     tooltip.add(new TranslatableText(" ").append(new TranslatableText("info.endertanks.tank.code"))
    //                                          .append(new TranslatableText(" " + code)));
    // }
    //
    // public static void sendCapacityInfo(PlayerEntity player, EnderTankBlockEntity tile) {
    //     if (tile.getWorld() != null) {
    //         EnderTank tank = TankHelper.getEnderTank(tile.getOwner(), tile.getCode(), tile.getWorld().isClient());
    //         sendInfo(player, new TranslatableText("info.endertanks.tank.capacity").append(" " + tank.getCapacity()));
    //     }
    // }
    //
    // public static void sendPumpInfo(PlayerEntity player, EnderTankBlockEntity tile) {
    //     sendInfo(player, new TranslatableText("info.endertanks.tank.pump.upgrade").append(" " + tile.getPumpCapacity() + "mB"));
    // }
    //
    // public static void sendLinkError(PlayerEntity player, int error) {
    //     sendInfo(player, new TranslatableText("error.endertanks.bucket.nolink" + error).setStyle(BOLD_RED));
    // }
    //
    // public static void sendError(PlayerEntity player, String error) {
    //     sendInfo(player, new TranslatableText("error." + error).setStyle(BOLD_RED));
    // }
    //
    // private static MutableText infoPump(long capacity) {
    //     if (capacity >= FluidConstants.BUCKET) {
    //         return new TranslatableText(" " + capacity / FluidConstants.BUCKET + "K mB ").append(new TranslatableText(
    //             "info.endertanks.tank.pump"));
    //     }
    //     return new TranslatableText(" " + capacity / (FluidConstants.BUCKET / FluidConstants.NUGGET) + "mB ").append(new TranslatableText("info.endertanks.tank.pump"));
    // }
    //
    // private static MutableText infoTankOwner(TankOwner owner, boolean displayPublic, boolean comma) {
    //     if (owner.isAll()) {
    //         if (displayPublic) {
    //             return new TranslatableText(" [-").setStyle(GOLD)
    //                                               .append(new TranslatableText("info.endertanks.tank.public"))
    //                                               .append(new TranslatableText("-]"));
    //         }
    //         return DUMMY;
    //     }
    //     return new TranslatableText(comma ? "," : "").append(new TranslatableText(" [-" + owner + "-]").setStyle(AQUA));
    // }
    //
    // @Nullable
    // public static MutableText formatTankOwner(TankOwner owner) {
    //     MutableText component = infoTankOwner(owner, false, false);
    //     return component != DUMMY ? component : null;
    // }
    //
    // public static void sendInfo(PlayerEntity player, MutableText... message) {
    //     if (player instanceof ServerPlayerEntity) {
    //         // NetworkHandler.sendToPlayer((PacketBase) new PacketTankInfo((Component[]) message), (ServerPlayer) player);
    //         //TODO networking sendToPlayer server
    //         MutableText text = Stream.of(message).reduce(new TranslatableText("(server) "), MutableText::append);
    //         player.sendSystemMessage(text, player.getUuid());
    //     }
    //     else {
    //         MutableText text = Stream.of(message).reduce(new TranslatableText("(client) "), MutableText::append);
    //         player.sendSystemMessage(text, player.getUuid());
    //         // EventHandlerClient.setDelayedMessage((Component[]) message);
    //         //TODO networking sendToPlayer client
    //     }
    // }
}
