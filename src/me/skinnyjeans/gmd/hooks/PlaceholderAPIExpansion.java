package me.skinnyjeans.gmd.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.skinnyjeans.gmd.Affinity;
import me.skinnyjeans.gmd.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlaceholderAPIExpansion extends PlaceholderExpansion {

    private Main plugin;
    private Affinity affinity;
    private boolean usePrefix;

    public PlaceholderAPIExpansion(Main plugin, Affinity af, boolean pr){
        this.plugin = plugin;
        this.affinity = af;
        this.usePrefix = pr;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getIdentifier() {
        return "dd";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier){
        if(identifier.equals("text_difficulty")){
            if(usePrefix)
                return affinity.getPrefix(player.getUniqueId());
            return affinity.calcDifficulty(player.getUniqueId());
        }
        if(identifier.equals("affinity_points"))
            return affinity.getAffinity(player.getUniqueId()) + "";
        if(identifier.equals("world_text_difficulty")){
            if(usePrefix)
                return affinity.getPrefix(null);
            return affinity.calcDifficulty(null);
        }
        if(identifier.equals("world_affinity_points"))
            return affinity.getAffinity(null)+"";
        if(identifier.equals("max_affinity"))
            return affinity.getVariableMaxAffinity()+"";
        if(identifier.equals("min_affinity"))
            return affinity.getVariableMinAffinity()+"";

        return null;
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier){
        UUID uuid = null;
        if(player != null)
            uuid = player.getUniqueId();

        Bukkit.getConsoleSender().sendMessage(uuid.toString());

        if(identifier.equals("text_difficulty")){
            if(usePrefix)
                return affinity.getPrefix(uuid);
            return affinity.calcDifficulty(uuid);
        }
        if(identifier.equals("affinity_points"))
            return affinity.getAffinity(uuid) + "";
        if(identifier.equals("world_text_difficulty")){
            if(usePrefix)
                return affinity.getPrefix(null);
            return affinity.calcDifficulty(null);
        }
        if(identifier.equals("world_affinity_points"))
            return affinity.getAffinity(null)+"";
        if(identifier.equals("max_affinity"))
            return affinity.getVariableMaxAffinity()+"";
        if(identifier.equals("min_affinity"))
            return affinity.getVariableMinAffinity()+"";

        return null;
    }
}
