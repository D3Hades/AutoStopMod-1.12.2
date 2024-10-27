package net.d3hades.autostop;

import net.minecraftforge.common.config.Configuration;

public class Config {
    public int minutes;
    public String message;

    public void init(Configuration cfg){
        String c = Configuration.CATEGORY_GENERAL;
        this.minutes = cfg.getInt("RestartTimer", c, 60, 1, Integer.MAX_VALUE, "Таймер в минутах");
        this.message = cfg.getString("Message", c,"Рестарт через %time% минут.","Сообщение для игроков");
        cfg.save();
    }
}