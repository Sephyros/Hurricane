package haven.automated;

import haven.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static haven.OCache.posres;

public class AUtils {

    public final static HashSet<String> potentialAggroTargets = new HashSet<String>() {{ // ND: Probably still missing dungeon ants, dungeon bees, dungeon beavers, dungeon bats?
        add("gfx/borka/body");
        add("gfx/kritter/adder/adder");
        add("gfx/kritter/ants/ants");
//        add("gfx/kritter/cattle/cattle"); // ND: Aurochs are handled differently in the method below!
        add("gfx/kritter/badger/badger");
        add("gfx/kritter/bat/bat");
        add("gfx/kritter/bear/bear");
        add("gfx/kritter/beaver/beaver");
        add("gfx/kritter/boar/boar");
        add("gfx/kritter/boreworm/boreworm");
        add("gfx/kritter/caveangler/caveangler");
        add("gfx/kritter/cavelouse/cavelouse");
        add("gfx/kritter/chasmconch/chasmconch"); // ND: I even added this one
        add("gfx/kritter/eagleowl/eagleowl");
        add("gfx/kritter/fox/fox");
        add("gfx/kritter/goat/wildgoat");
        add("gfx/kritter/goldeneagle/goldeneagle");
        add("gfx/kritter/greyseal/greyseal");
        add("gfx/kritter/horse/horse");
        add("gfx/kritter/lynx/lynx");
        add("gfx/kritter/mammoth/mammoth");
        add("gfx/kritter/moose/moose");
//        add("gfx/kritter/sheep/sheep"); // ND: Mouflons are handled differently in the method below!
        add("gfx/kritter/nidbane/nidbane");
        add("gfx/kritter/ooze/greenooze");
        add("gfx/kritter/orca/orca");
        add("gfx/kritter/otter/otter");
        add("gfx/kritter/pelican/pelican");
        add("gfx/kritter/rat/caverat");
        add("gfx/kritter/reddeer/reddeer");
        add("gfx/kritter/reindeer/reindeer");
        add("gfx/kritter/roedeer/roedeer");
        add("gfx/kritter/spermwhale/spermwhale");
        add("gfx/kritter/stoat/stoat");
        add("gfx/kritter/swan/swan");
        add("gfx/kritter/troll/troll");
        add("gfx/kritter/walrus/walrus");
        add("gfx/kritter/wolf/wolf");
        add("gfx/kritter/wolverine/wolverine");
        add("gfx/kritter/woodgrouse/woodgrouse-m");

        add("gfx/kritter/ants/queenant");
        add("gfx/kritter/ants/royalguardant");
        add("gfx/kritter/ants/warriorant");
        add("gfx/kritter/ants/redants");

        add("gfx/kritter/beaver/beaverking");
        add("gfx/kritter/beaver/oldbeaver");
        add("gfx/kritter/beaver/grizzlybeaver");

        add("gfx/kritter/bees/warriordrone");
        add("gfx/kritter/bees/queenbee");
        add("gfx/kritter/bees/sentinelbee");
        add("gfx/kritter/bees/vulturebee");
        add("gfx/kritter/bees/honeybee");
        add("gfx/kritter/bees/beelarva");
        add("gfx/kritter/wildbees/beeswarm");

        add("gfx/kritter/bat/nightqueen");
        add("gfx/kritter/bat/vampire");
        add("gfx/kritter/bat/bloodstalker");
        add("gfx/kritter/bat/denmother");
        add("gfx/kritter/bat/fatbat");



    }
    };

    public static HashMap<Long, Gob> getAllAttackableMap(GameUI gui) {
        HashMap<Long, Gob> gobs = new HashMap<>();
        if (gui.map.plgob == -1) {
            return gobs;
        }
        synchronized (gui.map.glob.oc) {
            for (Gob gob : gui.map.glob.oc) {
                if (gob.getres() != null && gob.getres().name != null){
                    if (gob.id != gui.map.plgob) {
                        if (potentialAggroTargets.contains(gob.getres().name)){
                            gobs.put(gob.id, gob);
                        } else if (gob.getres().name.equals("gfx/kritter/cattle/cattle")) { // ND: Special case for Aurochs
                            for (GAttrib g : gob.attr.values()) {
                                if (g instanceof Drawable) {
                                    if (g instanceof Composite) {
                                        Composite c = (Composite) g;
                                        if (c.comp.cmod.size() > 0) {
                                            for (Composited.MD item : c.comp.cmod) {
                                                if (item.mod.get().basename().equals("aurochs")){
                                                    gobs.put(gob.id, gob);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (gob.getres().name.equals("gfx/kritter/sheep/sheep")) { // ND: Special case for Mouflon
                            for (GAttrib g : gob.attr.values()) {
                                if (g instanceof Drawable) {
                                    if (g instanceof Composite) {
                                        Composite c = (Composite) g;
                                        if (c.comp.cmod.size() > 0) {
                                            for (Composited.MD item : c.comp.cmod) {
                                                if (item.mod.get().basename().equals("mouflon")){
                                                    gobs.put(gob.id, gob);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }
        return gobs;
    }

    public static void attackGob(GameUI gui, Gob gob) {
        if (gob != null && gui != null && gui.map != null) {
            gui.act("aggro");
            gui.map.wdgmsg("click", Coord.z, gob.rc.floor(posres), 1, 0, 0, (int) gob.id, gob.rc.floor(posres), 0, -1);
            rightClick(gui);
        }
    }

    public static void rightClick(GameUI gui) {
        gui.map.wdgmsg("click", Coord.z, gui.map.player().rc.floor(posres), 3, 0);
    }

    public static HashMap<Long, Gob> getAllAttackablePlayersMap(GameUI gui) {
        HashMap<Long, Gob> gobs = new HashMap<>();
        if (gui.map.plgob == -1) {
            return gobs;
        }
        synchronized (gui.map.glob.oc) {
            for (Gob gob : gui.map.glob.oc) {
                if (gob.getres() != null && gob.getres().name != null){
                    if (gob.id != gui.map.plgob) {
                        if (gob.getres().name.equals("gfx/borka/body")){
                            gobs.put(gob.id, gob);
                        }
                    }
                }
            }
        }
        return gobs;
    }

    public static WItem findItemByPrefixInAllInventories(GameUI gui, final String resNamePrefix) {
        for(Inventory inventory : gui.getAllInventories()){
            for (Widget wdg = inventory.child; wdg != null; wdg = wdg.next) {
                if (wdg instanceof WItem) {
                    final WItem witm = (WItem)wdg;
                    try {
                        if (witm.item.getres().name.startsWith(resNamePrefix)) {
                            return witm;
                        }
                    }
                    catch (Loading ignored) {}
                }
            }
        }
        return null;
    }

    public static WItem findItemInInv(final Inventory inv, final String resName) {
        for (Widget wdg = inv.child; wdg != null; wdg = wdg.next) {
            if (wdg instanceof WItem) {
                final WItem witm = (WItem)wdg;
                try {
                    if (witm.item.getres().name.equals(resName)) {
                        return witm;
                    }
                }
                catch (Loading ignored) {}
            }
        }
        return null;
    }

    public static boolean waitForEmptyHand(final GameUI gui, final int timeout, final String error) throws InterruptedException {
        int t = 0;
        while (gui.vhand != null) {
            t += 5;
            if (t >= timeout) {
                gui.error(error);
                return false;
            }
            try {
                Thread.sleep(5L);
            }
            catch (InterruptedException ie) {
                throw ie;
            }
        }
        return true;
    }

    public static boolean waitForOccupiedHand(final GameUI gui, final int timeout, final String error) throws InterruptedException {
        int t = 0;
        while (gui.vhand == null) {
            t += 5;
            if (t >= timeout) {
                gui.error(error);
                return false;
            }
            try {
                Thread.sleep(5L);
            }
            catch (InterruptedException ie) {
                throw ie;
            }
        }
        return true;
    }

    public static boolean waitPf(GameUI gui) throws InterruptedException {
        if(gui.map.pfthread == null){
            return false;
        }
        int time = 0;
        boolean moved = false;
        Thread.sleep(300);
        while (gui.map.pfthread.isAlive()) {
            time += 70;
            Thread.sleep(70);
            if (gui.map.player().getv() > 0) {
                time = 0;
            }
            if (time > 2000 && moved == false) {
                System.out.println("TRYING UNSTUCK");
                return false;
            } else if (time > 20000) {
                return false;
            }
        }
        return true;
    }

    public static void waitProgBar(GameUI gui) throws InterruptedException {
        while (gui.prog != null && gui.prog.prog >= 0) {
            Thread.sleep(40);
        }
    }

    public static void rightClickShiftCtrl(GameUI gui, Gob gob) {
        gui.map.wdgmsg("click", Coord.z, gob.rc.floor(posres), 3, 3, 0, (int) gob.id, gob.rc.floor(posres), 0, -1);
    }

    public static ArrayList<Gob> getGobs(String name, GameUI gui) {
        ArrayList<Gob> gobs = new ArrayList<>();
        synchronized (gui.map.glob.oc) {
            for (Gob gob : gui.map.glob.oc) {
                try {
                    Resource res = gob.getres();
                    if (res != null && res.name.equals(name)) {
                        gobs.add(gob);
                    }
                } catch (Loading l) {
                }
            }
        }
        return gobs;
    }
}
