package net.d3hades.autostop;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.time.Instant;

public class CommandUptime extends CommandBase {
    @Override
    public String getName()
    {
        return "uptime";
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "/uptime";
    }

    @Override
    public void execute( MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if(args.length == 0) {
            TextComponentString msg = new TextComponentString("Сервер работает:");
            msg.getStyle().setColor(TextFormatting.GREEN);
            sender.sendMessage(msg);
            long totalSecs = Instant.now().getEpochSecond() - AutoStop.instance.secondsAfterStart.getEpochSecond();

            int hours = (int) (totalSecs / 3600);
            int minutes = (int) (totalSecs % 3600) / 60;
            int seconds = (int) totalSecs % 60;
            String timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            msg = new TextComponentString(timeString);
            msg.getStyle().setColor(TextFormatting.GOLD);
            sender.sendMessage(msg);

            msg = new TextComponentString("До рестарта:");
            msg.getStyle().setColor(TextFormatting.GREEN);
            sender.sendMessage(msg);

            totalSecs = (AutoStop.instance.secondsAfterStart.getEpochSecond() + AutoStop.instance.config.minutes * 60) - Instant.now().getEpochSecond();
            hours = (int) (totalSecs / 3600);
            minutes = (int) (totalSecs % 3600) / 60;
            seconds = (int) totalSecs % 60;

            timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
            msg = new TextComponentString(timeString);
            msg.getStyle().setColor(TextFormatting.GOLD);
            sender.sendMessage(msg);
        }
        else
            throw new WrongUsageException(this.getUsage(sender));
    }
}
