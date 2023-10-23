package dev.orewaee.session;

import java.util.HashSet;
import java.util.Set;

public class SessionManager {
    private static final Set<Session> sessions = new HashSet<>();

    public static void addSession(String name, String ip) {
        if (sessionExists(name, ip)) return;

        sessions.add(new Session(name, ip));
    }

    public static void removeSession(Session session) {
        sessions.remove(session);
    }

    public static void removeSession(String name, String ip) {
        if (!sessionExists(name, ip)) return;

        for (Session session : sessions) {
            boolean nameEqual = name.equals(session.getName());
            boolean ipEqual = ip.equals(session.getIp());

            if (nameEqual && ipEqual) {
                session.stopTimer();
                sessions.remove(session);
                break;
            }
        }
    }

    public static boolean sessionExists(String name, String ip) {
        boolean sessionExistsByName = sessionExistsByName(name);
        boolean sessionExistsByIp = sessionExistsByName(ip);

        return  sessionExistsByName || sessionExistsByIp;
    }

    public static boolean sessionExistsByName(String name) {
        for (Session session : sessions)
            if (name.equals(session.getName()))
                return true;

        return false;
    }

    public static boolean sessionExistsByIp(String ip) {
        for (Session session : sessions)
            if (ip.equals(session.getIp()))
                return true;

        return false;
    }

    public static Set<Session> getSessions() {
        return sessions;
    }

    public static Session getSessionByName(String name) {
        for (Session session : sessions)
            if (name.equals(session.getName()))
                return session;

        return null;
    }

    public static Session getSessionByIp(String ip) {
        for (Session session : sessions)
            if (ip.equals(session.getIp()))
                return session;

        return null;
    }
}
