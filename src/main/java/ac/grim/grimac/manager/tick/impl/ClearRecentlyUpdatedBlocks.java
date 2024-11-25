package ac.grim.grimac.manager.tick.impl;

import ac.grim.grimac.GrimAPI;
import ac.grim.grimac.manager.TickManager;
import ac.grim.grimac.manager.tick.Tickable;
import ac.grim.grimac.player.GrimPlayer;

public class ClearRecentlyUpdatedBlocks implements Tickable {

    private static final TickManager tickManager = GrimAPI.INSTANCE.getTickManager();
    private static final int maxTickAge = 2;

    @Override
    public void tick() {
        for (GrimPlayer player : GrimAPI.INSTANCE.getPlayerDataManager().getEntries()) {
            player.blockHistory.cleanup(tickManager.currentTick - maxTickAge);
        }
    }
}
