package cz.dat.oots.console;

import cz.dat.oots.Game;

public class CommandTele extends Command {

    public CommandTele(Console console) {
        super(console);
    }

    @Override
    public String getName() {
        return "tele";
    }

    @Override
    public String getUsage() {
        return "tele [x] [y] [z]";
    }

    @Override
    public boolean execute(String[] args) {
        if (args.length >= 3) {

            try {
                float x = Float.parseFloat(args[0]);
                float y = Float.parseFloat(args[1]);
                float z = Float.parseFloat(args[2]);

                Game game = super.console.getGame();

                if (!game.getWorldsManager().isInGame()) {
                    super.console.println("You must be ingame to use command "
                            + getName());
                    return false;
                }

                game.getCurrentWorld().getPlayer().setPosition(x, y, z);

                return true;
            } catch (NumberFormatException e) {
                super.console.println("Invalid values!");
                e.printStackTrace();
                return false;
            }
        } else {
            super.console.println("Not enough arguments, correct usage: ");
            super.console.println(getUsage());
        }

        return false;
    }

}
