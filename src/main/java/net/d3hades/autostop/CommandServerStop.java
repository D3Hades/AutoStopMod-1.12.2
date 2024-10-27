package net.d3hades.autostop;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.time.Instant;

public class CommandServerStop extends CommandBase {
    @Override
    public String getName()
    {
        return "serverstop";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 4;
    }

    @Override
    public String getUsage( ICommandSender sender)
    {
        return "/serverstop";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if(args.length == 0) {
            ServerStoppingThread stop = new ServerStoppingThread();
            stop.start();
        }
        else
            throw new WrongUsageException(this.getUsage(sender));
    }
}

class ServerStoppingThread extends Thread{
    MinecraftServer mc = FMLCommonHandler.instance().getMinecraftServerInstance();
    public void run(){
        mc.getCommandManager().executeCommand(mc,"broadcast Рестарт сервера");
        AutoStop.instance.logger.info("Server is stopping...");
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
    }
}