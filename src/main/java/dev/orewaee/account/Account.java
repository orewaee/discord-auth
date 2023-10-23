package dev.orewaee.account;

public class Account {
    private String name, discord;

    public Account(String name, String discord) {
        this.name = name;
        this.discord = discord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscord() {
        return discord;
    }

    public void setDiscord(String discord) {
        this.discord = discord;
    }

    /*
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Account account = (Account) object;

        return name.equals(account.name) && discord.equals(account.discord);
    }
    */
}
