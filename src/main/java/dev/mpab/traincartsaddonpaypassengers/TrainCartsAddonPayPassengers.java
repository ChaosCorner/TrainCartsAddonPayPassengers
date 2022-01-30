package dev.mpab.traincartsaddonpaypassengers;

import com.bergerkiller.bukkit.tc.signactions.SignAction;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class TrainCartsAddonPayPassengers extends JavaPlugin {

    public static Economy eco = null;
    public static final SignActionPay signActionPay = new SignActionPay();

    @Override
    public void onEnable() {
        getLogger().info("Starting up TC Eco SignLink Addon. Checking for Vault...");

        // Plugin startup logic
        if (!setupEconomy()) {
            getLogger().severe("Vault not found! This addon needs Vault! Download it and restart the server.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        SignAction.register(signActionPay);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SignAction.unregister(signActionPay);
    }

    private boolean setupEconomy() {

        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);

        if (rsp == null) {
            return false;
        }

        eco = rsp.getProvider();
        getLogger().info("Found economy provider! Name: " + rsp.getPlugin().getName());
        return true;
    }
}

