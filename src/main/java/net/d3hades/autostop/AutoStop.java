package net.d3hades.autostop;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

import java.time.Instant;

@Mod(modid = "autostop", name = "AutoStop", version = "1.0", acceptableRemoteVersions = "*")
public class AutoStop {
    public final Config config = new Config();
    public Logger logger;
    @Mod.Instance
    public static AutoStop instance;
    public Instant startInstant;
    public long startTime;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        this.logger = event.getModLog();
        this.config.init(new Configuration(event.getSuggestedConfigurationFile()));
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        startInstant = Instant.now();
        startTime = startInstant.getEpochSecond();
        event.registerServerCommand(new CommandUptime());
        event.registerServerCommand(new CommandServerStop());
        Timer timer = new Timer();
        timer.start();
    }
}

class Timer extends Thread{
    private void sendMessage(String time, boolean replaceWord) {
        StringBuilder message = new StringBuilder(AutoStop.instance.config.message);
        if(message.indexOf("%time%") != -1){
            message.replace(message.indexOf("%time%"),message.indexOf("%time%")+"%time%".length(), time);
        }
        if(replaceWord && message.indexOf("минут") != -1) {
            message.replace(message.indexOf("минут"),message.indexOf("минут")+"минут".length(), "минуту");
        }
        mc.getCommandManager().executeCommand(mc,"broadcast " + message.toString());
    }

    public void run(){
        int minutes = AutoStop.instance.config.minutes;
        long startTime = AutoStop.instance.startTime;
        boolean say10Minutes = false;
        boolean say5Minutes = false;
        boolean say1Minutes = false;

        MinecraftServer mc = FMLCommonHandler.instance().getMinecraftServerInstance();

        while (true){
            long curretTime = Instant.now().getEpochSecond();
            
            if(curretTime > (startTime + minutes * 60)){
                mc.getCommandManager().executeCommand(mc,"broadcast Рестарт сервера");
                AutoStop.instance.logger.info("Server is going to restart...");
                mc.getCommandManager().executeCommand(mc,"cmi kick all Рестарт сервера...");
                try {
                    Timer.sleep(3500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mc.getCommandManager().executeCommand(mc,"save-all");
                try {
                    Timer.sleep(3500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mc.getCommandManager().executeCommand(mc,"stop");
            } else {
                if(minutes > 10 && curretTime > (startTime + (minutes - 10) * 60) && !say10Minutes){
                    sendMessage("10", false);
                    say10Minutes = true;
                }
                if(minutes > 5 && curretTime > (startTime + (minutes - 5) * 60) && !say5Minutes){
                    sendMessage("5", false);
                    say5Minutes = true;
                }
                if(minutes > 1 && curretTime > (startTime + (minutes - 1) * 60) && !say1Minutes){
                    sendMessage("1", true);
                    say1Minutes = true;
                }
            }

            try {
                Timer.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}