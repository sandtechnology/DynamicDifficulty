package me.skinnyjeans.gmd.events;

import me.skinnyjeans.gmd.managers.MainManager;
import me.skinnyjeans.gmd.models.BaseListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class BlockMinedListener extends BaseListener {

    private final MainManager MAIN_MANAGER;
    private final HashMap<Material, Integer> BLOCKS = new HashMap<Material, Integer>();
    private final HashMap<Material, Material> ORE_BLOCKS = new HashMap<Material, Material>() {{
        put(Material.COAL_ORE, Material.COAL);
        put(Material.LAPIS_ORE, Material.LAPIS_LAZULI);
        put(Material.DIAMOND_ORE, Material.DIAMOND);
        put(Material.EMERALD_ORE, Material.EMERALD);
        put(Material.REDSTONE_ORE, Material.REDSTONE);
        put(Material.NETHER_QUARTZ_ORE, Material.QUARTZ);
    }};

    private boolean silkTouchAllowed;

    public BlockMinedListener(MainManager mainManager) { MAIN_MANAGER = mainManager; }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMined(BlockBreakEvent e) {
        if(!MAIN_MANAGER.getPlayerManager().isPlayerValid(e.getPlayer())) return;

        Bukkit.getScheduler().runTaskAsynchronously(MAIN_MANAGER.getPlugin(), () -> {
            if(BLOCKS.size() == 0) return;

            if(BLOCKS.containsKey(e.getBlock().getType()))
                if(silkTouchAllowed || ! e.getPlayer().getItemOnCursor().containsEnchantment(Enchantment.SILK_TOUCH))
                    MAIN_MANAGER.getPlayerManager().addAffinity(e.getPlayer().getUniqueId(), BLOCKS.get(e.getBlock().getType()));
        });

        if (ORE_BLOCKS.containsKey(e.getBlock().getType())) {
            if (! e.getPlayer().getItemOnCursor().containsEnchantment(Enchantment.SILK_TOUCH))
                e.getPlayer().getWorld().dropItemNaturally(e.getBlock().getLocation(),
                        new ItemStack(ORE_BLOCKS.get(e.getBlock().getType())));
        }
    }

    @Override
    public void reloadConfig() {
        BLOCKS.clear();
        silkTouchAllowed = MAIN_MANAGER.getDataManager().getConfig().getBoolean("silk-touch-allowed", false);
        ConfigurationSection config = MAIN_MANAGER.getDataManager().getConfig();

        int blockMined = config.getInt("block-mined", 2);

        try {
            // Add items from newer updates
            ORE_BLOCKS.put(Material.NETHER_GOLD_ORE, Material.GOLD_NUGGET);
            ORE_BLOCKS.put(Material.DEEPSLATE_COAL_ORE, Material.COAL);
            ORE_BLOCKS.put(Material.DEEPSLATE_LAPIS_ORE, Material.LAPIS_LAZULI);
            ORE_BLOCKS.put(Material.DEEPSLATE_DIAMOND_ORE, Material.DIAMOND);
            ORE_BLOCKS.put(Material.DEEPSLATE_EMERALD_ORE, Material.EMERALD);
            ORE_BLOCKS.put(Material.DEEPSLATE_REDSTONE_ORE, Material.REDSTONE);
            ORE_BLOCKS.put(Material.COPPER_ORE, Material.RAW_COPPER);
        } catch (Exception ignored) { }

        for(Object key : config.getList("blocks").toArray())
            try {
                String[] sep = key.toString().replaceAll("[{|}]","").split("=");
                int value = (sep.length > 1) ? Integer.parseInt(sep[1]) : blockMined;
                BLOCKS.put(Material.valueOf(sep[0]), value);
            } catch (Exception ignored) { }
    }
}
