package net.skulluhc;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class LavaTickModifier extends JavaPlugin implements Listener {

    public static LavaTickModifier INSTANCE;

    @Getter @Setter
    private long value;
    private Map<Player, Cooldown> cooldowns;

    @Getter @Setter
    private boolean debug;

    @Override
    public void onEnable() {
        INSTANCE = this;

        this.cooldowns = new HashMap<>();
        this.value = 500L;

        getCommand("setlavatickmodifier").setExecutor(new ModifierCommand());
        getCommand("setdebugmode").setExecutor(new DebugCommand());
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onLavaTick(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) return;

        Player player = (Player) event.getEntity();
        if(event.getCause() != EntityDamageEvent.DamageCause.LAVA && !event.getCause().name().contains("FIRE")) return;
        if(!cooldowns.containsKey(player)) {
            cooldowns.put(player, new Cooldown(this.value));
            if(debug) player.sendMessage(this.value + " seconds cooldown is now active");
            return;
        }

        Cooldown cooldown = cooldowns.get(player);
        if(cooldown != null && !cooldown.hasExpired()) {
            event.setCancelled(true);
            if(debug) player.sendMessage("Player has tick cooldown " + cooldown.getTimeLeft() + " seconds");
            return;
        }

        cooldowns.remove(player);
        cooldowns.put(player, new Cooldown(this.value));
        if(debug) player.sendMessage("'" + this.value + "' seconds cooldown is now active");
    }
}
